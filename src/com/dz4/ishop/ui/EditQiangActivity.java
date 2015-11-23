package com.dz4.ishop.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.dz4.ImageUpload_9_zss.utils.ZssMyAdapter;
import com.dz4.ishop.domain.Goods;
import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.utils.CacheUtils;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;
import com.dz4.support.app.IApplication;

public class EditQiangActivity extends BaseUIActivity implements TopBar.onTopBarbtnclickListener,OnClickListener{
	
	private TopBar mTopBar;
	private TextView qiang_content;
	private LinearLayout openLayout;
	private LinearLayout takeLayout;
	private String dateTime;
	private ImageView albumPic;
	private ImageView takePic;
	private Context mContext;
	private	EditText goods_name_edit;
	private	EditText goods_category_edit;
	private	EditText goods_price_edit;
	
	private ZssMyAdapter zssMyAadapter; 
	private GridView gridView;
	private Goods goods;
	private EditText sailer_phone_edit;
	public static ArrayList<String>  imgItem = new ArrayList<String>();
	public static ArrayList<String>  imgDirPath = new ArrayList<String>();
	public static ArrayList<BmobFile>  BmobFileList = new ArrayList<BmobFile>();
	
	
	private static final int REQUEST_CODE_ALBUM = 1;
	private static final int REQUEST_CODE_CAMERA = 2;
	@Override
	public void initView() {
		// TODO �Զ����ɵķ������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit);
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setTitleText("������Ʒ");
		mTopBar.setRightButtonImage(getResources().getDrawable(R.drawable.btn_qiang_publish));
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.back));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		mTopBar.setRightButtonVisible(View.VISIBLE);
		
		openLayout = (LinearLayout) findViewById(R.id.open_layout);
		takeLayout = (LinearLayout) findViewById(R.id.take_layout);
		qiang_content =(TextView) findViewById(R.id.edit_content);
		albumPic = (ImageView) findViewById(R.id.open_pic);
		takePic = (ImageView) findViewById(R.id.take_pic);
		
		goods_name_edit=(EditText)findViewById(R.id.goods_name_edit);
		goods_category_edit=(EditText)findViewById(R.id.goods_category_edit);
		goods_price_edit=(EditText)findViewById(R.id.goods_price_edit);
		sailer_phone_edit=(EditText)findViewById(R.id.sailer_phone_edit);
		
		
		mContext = getApplicationContext();
		gridView = (GridView)findViewById(R.id.edit_activity_gridView);
	}

	@Override
	public void initData() {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void initEvent() {
		// TODO �Զ����ɵķ������
		mTopBar.setTopBarbtnclickListener(this);
		openLayout.setOnClickListener(this);
		takeLayout.setOnClickListener(this);
		
	}

	@Override
	public void rightbtnclick(View v) {
		// TODO �Զ����ɵķ������
		String commitContent = qiang_content.getText().toString().trim();
		String goods_name = goods_name_edit.getText().toString().trim();
		String goods_category = goods_category_edit.getText().toString().trim();
		String goods_price = goods_price_edit.getText().toString().trim();
		String sailer_phone = sailer_phone_edit.getText().toString().trim();
		
		
 		if (TextUtils.isEmpty(goods_name)) {
			showToast("��Ʒ������Ϊ��");
			return;
		}
 		if (TextUtils.isEmpty(goods_category)) {
			showToast("��Ʒ���಻��Ϊ��");
			return;
		}
 		if (TextUtils.isEmpty(goods_price)) {
			showToast("��Ʒ�۸���Ϊ��");
			return;
		}
 		if (TextUtils.isEmpty(sailer_phone)) {
			showToast("��ϵ�绰����Ϊ��");
			return;
		}
 		goods = new Goods();
 		goods.setCategory(goods_category);
 		goods.setName(goods_name);
 		goods.setPrice(Float.valueOf(goods_price));
 		goods.setDetails(commitContent);
 		goods.setCellphone(sailer_phone);
		if (TextUtils.isEmpty(commitContent)) {
			showToast("���ݲ���Ϊ��");
			return;
		}
		if (sourcepathlist == null) {
			publishWithoutFigure(commitContent,null, goods);
		} 
		else {
			publish(commitContent,goods);
		}
	}

	@Override
	public void leftbtnclick(View v) {
		// TODO �Զ����ɵķ������
		showMsgDialog("��ʾ", "         �Ƿ��˳��༭�� ", "ȷ��", "ȡ��", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				finish();
			}
		}, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		switch (v.getId()) {
		case R.id.open_layout:
//			Date date = new Date(System.currentTimeMillis());
//			dateTime = date.getTime() + "";
//			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//			intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
//					"image/*");
			Intent intent  = new Intent(getApplicationContext(),ShowImageActivity.class);
			startActivityForResult(intent, REQUEST_CODE_ALBUM);
			break;
		case R.id.take_layout:
			Date date1 = new Date(System.currentTimeMillis());
			dateTime = date1.getTime() + "";
			File f = new File(CacheUtils.getCacheDirectory(getApplicationContext(), true,
					"pic") + dateTime);
			if (f.exists()) {
				f.delete();
			}
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Uri uri = Uri.fromFile(f);

			Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(camera, REQUEST_CODE_CAMERA);
			break;
		default:
			break;
		}
	}
	/*
	 * �����ͼƬ
	 */
	private void publish(final String commitContent, final Goods goods) {
			
			showProgressDialog("�����ϴ�");
			targeturls = new String[sourcepathlist.size()];
				int i =0;
				//ѹ��
				for(String path :sourcepathlist){
					Bitmap bitmap = compressImageFromFile(path);
		 			targeturls[i] =saveToSdCard(bitmap);
		 			i++;
				}

	 		BmobProFile.getInstance(mContext).uploadBatch(targeturls, new UploadBatchListener() {

	          
	            public void onSuccess(boolean isFinish,String[] fileNames,String[] urls,BmobFile[] files) {
	                // isFinish �������ϴ��Ƿ����
	                // fileNames���ļ�������
	                // urls        : url���ļ���ַ����
	                // files     : BmobFile�ļ����飬`V3.4.1�汾`��ʼ�ṩ�����ڼ����¾��ļ�����
	                //ע�����ϴ�����ͼƬ��url(s)������ֱ����������鿴�������404���󣩣���Ҫ����`URLǩ��`�õ������Ŀɷ��ʵ�URL��ַ,��Ȼ��`V3.4.1`�汾��ֱ�Ӵ�BmobFile�л�ÿɷ��ʵ��ļ���ַ��
	        	    
	        	    if(isFinish) {
	            		publishWithoutFigure(commitContent,files,goods);
	            	}
	            	cancelProgressDialog();
	            }

	            
	            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
	                // curIndex    :��ʾ��ǰ�ڼ����ļ������ϴ�
	                // curPercent  :��ʾ��ǰ�ϴ��ļ��Ľ���ֵ���ٷֱȣ�
	                // total       :��ʾ�ܵ��ϴ��ļ���
	                // totalPercent:��ʾ�ܵ��ϴ����ȣ��ٷֱȣ�
	        	    showProgressDialog("ͼƬ�ϴ���");
	            	 
	            }

	          
	            public void onError(int statuscode, String errormsg) {
	                // TODO Auto-generated method stub
	        	    cancelProgressDialog();
	        	    showToast("ͼƬ�ϴ�ʧ��");
	            }
	        });
	 		}
	private void publishWithoutFigure(final String commitContent,
			final BmobFile[] BmobFileList, final Goods goods) {
		User user = BmobUser.getCurrentUser(mContext, User.class);

		// JavaBean  ������set �� JavaBean ����
		final QiangItem qiangitem = new QiangItem();
		qiangitem.setAuthor(user);
		qiangitem.setContent(commitContent);
		 
		if (BmobFileList != null) {
			int flag=0;
			for(BmobFile bf:BmobFileList){
				qiangitem.setBmobFileList(bf,flag);
				flag++;
				Log.i("BmobFileList", bf.getFileUrl(mContext)+".....");
			}
		}
		qiangitem.setLove(0);
		qiangitem.setHate(0);
		qiangitem.setShare(0);
		qiangitem.setComment(0);
		qiangitem.setPass(true);
		qiangitem.setFocus(false);
		
		goods.save(mContext,new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO �Զ����ɵķ������
				qiangitem.setGoods(goods);
				// Bmob ���  JavaBean ��  ����  ������
				qiangitem.save(mContext, new SaveListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						showToast("����ɹ�");
						setResult(RESULT_OK);
						finish();
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						showToast("����ʧ��");
					}
				});
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO �Զ����ɵķ������
				
			}
		});
		
	}

	String targeturl = null;
	private String[] targeturls=null;
	private ArrayList<String> sourcepathlist;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_ALBUM:
				imgItem = data.getStringArrayListExtra("imgItem");
				imgDirPath = data.getStringArrayListExtra("imgDirPath");
				//ͼƬ̫������ʾԭͼƬ������ʱѹ������
				sourcepathlist =new ArrayList<String>();
		 		for(int i=0;i<imgItem.size();i++){
		 			sourcepathlist.add(imgDirPath.get(i)+"/"+imgItem.get(i));
		 		}
				if(sourcepathlist != null){ 
					gridView.setVisibility(View.VISIBLE);
					zssMyAadapter = new ZssMyAdapter(getApplicationContext(),sourcepathlist,
							R.layout.zss_show_image);
					gridView.setAdapter(zssMyAadapter); 
				}else{
					gridView.setVisibility(View.GONE);
				}
				break;
			case REQUEST_CODE_CAMERA:
				String filename = CacheUtils.getCacheDirectory(mContext, true,
						"pic") + dateTime;
				File file = new File(filename);
				if (file.exists()) {
					Bitmap bitmap = compressImageFromFile(filename);
					targeturl = saveToSdCard(bitmap);
					takePic.setBackgroundDrawable(new BitmapDrawable(bitmap));
					openLayout.setVisibility(View.GONE);
				} else {

				}
				break;
			default:
				break;
			}
		}
	}

	private Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// ֻ����,��������
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// ���ò�����

		newOpts.inPreferredConfig = Config.ARGB_8888;// ��ģʽ��Ĭ�ϵ�,�ɲ���
		newOpts.inPurgeable = true;// ͬʱ���òŻ���Ч
		newOpts.inInputShareable = true;// ����ϵͳ�ڴ治��ʱ��ͼƬ�Զ�������

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressBmpFromBmp(bitmap);//ԭ���ķ������������������ͼ���ж���ѹ��
		// ��ʵ����Ч��,��Ҿ��ܳ���
		return bitmap;
	}

	public String saveToSdCard(Bitmap bitmap) {
		Date date = new Date(System.currentTimeMillis());
		dateTime = date.getTime() + "";
		String files = CacheUtils.getCacheDirectory(mContext, true, "pic")
				+ dateTime + "_11.jpg";
		File file = new File(files);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	@Override
	public void onBackPressed() {
		// TODO �Զ����ɵķ������
		showMsgDialog("��ʾ", "         �Ƿ��˳��༭�� ", "ȷ��", "ȡ��", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				finish();
			}
		}, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
			}
		});
		
	}
}
