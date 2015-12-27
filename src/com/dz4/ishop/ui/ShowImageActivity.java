package com.dz4.ishop.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dz4.imageupload9.base.ImageFloder;
import com.dz4.imageupload9.ui.ListImageDirPopupWindow;
import com.dz4.imageupload9.ui.ListImageDirPopupWindow.OnImageDirSelected;
import com.dz4.imageupload9.utils.MyAdapter;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
 

public class ShowImageActivity extends Activity implements OnImageDirSelected,TopBar.onTopBarbtnclickListener
{
	private ProgressDialog mProgressDialog;
	private TopBar mTopBar;

	List<String>  SelectedImageList = new LinkedList<String>();
	public static ArrayList<String>  imgItem = new ArrayList<String>();
	public static ArrayList<String>  imgDirPath = new ArrayList<String>();
	private int mPicsSize;
	private File mImgDir;
	private List<String> mImgs;
	private GridView mGirdView;
	private MyAdapter mAdapter;
	private HashSet<String> mDirPaths = new HashSet<String>();
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	private RelativeLayout mBottomLy;

	private TextView mChooseDir;
	private TextView mImageCount;
	private TextView  texttitle;
 
	int totalCount = 0;

	private int mScreenHeight;

	private ListImageDirPopupWindow mListImageDirPopupWindow;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			mProgressDialog.dismiss();
			//为View绑定数据  
			//为View绑定数据  
			initListDirPopupWindw();
			data2View();
		}
	};
 

	/** 
     * 为View绑定数据 
     */  
	private void data2View()
	{
		if (mImgDir == null)
		{
			Toast.makeText(getApplicationContext(), "没扫描到任何图片",
					Toast.LENGTH_SHORT).show();
			return;
		}

		//Flie.list() 返回�?��字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录�? 
//		mImgs = Arrays.asList(mImgDir.list());
//		/** 
//         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消�?�?
//         */  
//		// mImgs 图片路径
//		//mImgDir.getAbsolutePath() 图片文件夹路�?	
//		Log.i("TAG", mImgDir.list().toString());
//		Log.i("TAG", mImgs.get(0));
//		Log.i("TAG", mImgDir.getAbsolutePath());
//		mAdapter = new MyAdapter(getApplicationContext(), mImgs,
//				R.layout.zss_grid_item, mImgDir.getAbsolutePath());
//		mGirdView.setAdapter(mAdapter);
//		mImageCount.setText(totalCount + "");
		selected(mImageFloders.get(0));
	};

	/** 
     * 初始化展示文件夹的popupWindw 
     */ 
	private void initListDirPopupWindw()
	{
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
				LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
				mImageFloders, LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.zss_list_dir, null));

		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				// 设置背景颜色变暗 
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// 设置选择文件夹的回调   
		mListImageDirPopupWindow.setOnImageDirSelected(this);
	}
 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zss_activity_select_image);

		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;
 
		Log.i("oncreat", "onceat");
		initView();
		getImages();
		initEvent();
		Log.i("oncreat", "finsh");
	}

	
	@Override
	protected void onDestroy() {
		 
		super.onDestroy();
		if(mProgressDialog.isShowing())
		mProgressDialog.dismiss();
	}

	/** 
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，�?��获得jpg�?��的那个文件夹 
     */  
	private ArrayList<String> AllImagepaths;
	private void getImages()
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(this, "û���ⲿ�洢��", Toast.LENGTH_SHORT).show();
			return;
		}
		mProgressDialog = ProgressDialog.show(this, null, "ͼƬ���ڼ�����..");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				String firstImage = null;
		 
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				
				ContentResolver mContentResolver = ShowImageActivity.this
						.getContentResolver();

				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				Log.e("TAG", mCursor.getCount() + "");
				if(AllImagepaths==null){
					AllImagepaths=new ArrayList<String>();
				}else{
					AllImagepaths.clear();
				}
				while (mCursor.moveToNext())
				{
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					Log.e("TAG", path);
					AllImagepaths.add(path);
					if (firstImage == null)
						firstImage = path;
					File parentFile = new File(path).getParentFile();
					  
					if (parentFile == null)
						continue;
					
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					if (mDirPaths.contains(dirPath))
					{
						continue;
					} else
					{
						mDirPaths.add(dirPath);
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}

					int picSize = parentFile.list(new FilenameFilter()
					{
						@Override
						public boolean accept(File dir, String filename)
						{
							if (filename.endsWith(".jpg")|| filename.endsWith(".png")|| filename.endsWith(".jpeg"))
								return true;
							return false;
						}
					}).length;
					totalCount += picSize;

					imageFloder.setCount(picSize);
					//�? Bean  加到  list 里面
					mImageFloders.add(imageFloder);

					if (picSize > mPicsSize)
					{
						mPicsSize = picSize;
						mImgDir = parentFile;
					}
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存�? 
				mDirPaths = null;

				// 通知Handler扫描图片完成 
				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}

	/**
	 * 鍒濆鍖朧i
	 */
	private void initView()
	{
		
		mGirdView = (GridView) findViewById(R.id.id_gridView);
		mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
		mImageCount = (TextView) findViewById(R.id.id_total_count);
 
		//texttitle = (TextView) this.findViewById(R.id.titleText);
		
		mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
		
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setTitleText("ѡ��ͼƬ");
		mTopBar.setRightButtonImage(getResources().getDrawable(R.drawable.btn_qiang_publish));
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.back));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		mTopBar.setRightButtonVisible(View.VISIBLE);

	}

	private void initEvent()
	{
		/** 
         * 为底部的布局设置点击事件，弹出popupWindow 
         */  
		mBottomLy.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mListImageDirPopupWindow
						.setAnimationStyle(R.style.anim_popup_dir);
				mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

				// 设置背景颜色变暗 
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = .3f;
				getWindow().setAttributes(lp);
			}
		});
		
		
		mTopBar.setTopBarbtnclickListener(this);
		
	}
	@Override
	public void selected(ImageFloder floder)
	{

		mImgDir = new File(floder.getDir());
		mImgs = Arrays.asList(mImgDir.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String filename)
			{
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
						|| filename.endsWith(".jpeg"))
					return true;
				return false;
			}
		}));
		/** 
	     * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消�?�?
	     */   
		mAdapter = new MyAdapter(getApplicationContext(), mImgs,
				R.layout.zss_grid_item, mImgDir.getAbsolutePath());
		mGirdView.setAdapter(mAdapter);
		
		// mAdapter.notifyDataSetChanged();
		mImageCount.setText(floder.getCount() + "");
		mChooseDir.setText(floder.getName());
		mListImageDirPopupWindow.dismiss();

	}

	@Override
	public void rightbtnclick(View v) {
		// TODO Auto-generated method stub
		Intent intent  = new Intent();
	    Bundle bundle = new Bundle();
	     
	     imgItem =  mAdapter.returnImgItem();
	     imgDirPath = mAdapter.returnImgDirPath();
	     
	     bundle.putStringArrayList("imgItem", imgItem);
	     bundle.putStringArrayList("imgDirPath", imgDirPath);
	     bundle.putString("IntentData","ShowImageActivity");
		 intent.putExtras(bundle);
		  
		 setResult(RESULT_OK, intent);
		 finish();//鍏抽棴褰撳墠 activity
	}

	@Override
	public void leftbtnclick(View v) {
		// TODO Auto-generated method stub
		finish();//鍏抽棴褰撳墠 activity
	}
 

	
}
