package com.dz4.ishop.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.utils.CacheUtils;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;

public class EditQiangActivity extends BaseUIActivity implements TopBar.onTopBarbtnclickListener,OnClickListener{
	private TopBar mTopBar;
	private TextView qiang_content;
	private LinearLayout openLayout;
	private LinearLayout takeLayout;
	private String dateTime;
	private ImageView albumPic;
	private ImageView takePic;
	private Context mContext;
	
	private static final int REQUEST_CODE_ALBUM = 1;
	private static final int REQUEST_CODE_CAMERA = 2;
	@Override
	public void initView() {
		// TODO 自动生成的方法存根
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit);
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setTitleText("发布商品");
		mTopBar.setRightButtonImage(getResources().getDrawable(R.drawable.btn_qiang_publish));
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.back));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		mTopBar.setRightButtonVisible(View.VISIBLE);
		
		openLayout = (LinearLayout) findViewById(R.id.open_layout);
		takeLayout = (LinearLayout) findViewById(R.id.take_layout);
		qiang_content =(TextView) findViewById(R.id.edit_content);
		albumPic = (ImageView) findViewById(R.id.open_pic);
		takePic = (ImageView) findViewById(R.id.take_pic);
		
		mContext = getApplicationContext();
	}

	@Override
	public void initData() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void initEvent() {
		// TODO 自动生成的方法存根
		mTopBar.setTopBarbtnclickListener(this);
		openLayout.setOnClickListener(this);
		takeLayout.setOnClickListener(this);
		
	}

	@Override
	public void rightbtnclick(View v) {
		// TODO 自动生成的方法存根
		String commitContent = qiang_content.getText().toString().trim();
		if (TextUtils.isEmpty(commitContent)) {
			showToast("内容不能为空");
			return;
		}
		if (targeturl == null) {
			publishWithoutFigure(commitContent, null);
		} else {
			publish(commitContent);
		}
	}

	@Override
	public void leftbtnclick(View v) {
		// TODO 自动生成的方法存根
		showMsgDialog("提示", "         是否退出编辑？ ", "确定", "取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		}, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.open_layout:
			Date date = new Date(System.currentTimeMillis());
			dateTime = date.getTime() + "";
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
					"image/*");
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
	 * 发表带图片
	 */
	private void publish(final String commitContent) {

		// final BmobFile figureFile = new BmobFile(QiangYu.class, new
		// File(targeturl));

		final BmobFile figureFile = new BmobFile(new File(targeturl));

		figureFile.upload(mContext, new UploadFileListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				publishWithoutFigure(commitContent, figureFile);

			}

			@Override
			public void onProgress(Integer arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
			}
		});

	}
	private void publishWithoutFigure(final String commitContent,
			final BmobFile figureFile) {
		User user = BmobUser.getCurrentUser(mContext, User.class);

		final QiangItem qiangitem = new QiangItem();
		qiangitem.setAuthor(user);
		qiangitem.setContent(commitContent);
		if (figureFile != null) {
			qiangitem.setContentfigureurl(figureFile);
		}
		qiangitem.setLove(0);
		qiangitem.setHate(0);
		qiangitem.setShare(0);
		qiangitem.setComment(0);
		qiangitem.setPass(true);
		qiangitem.save(mContext, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				showToast("发表成功");
				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				showToast("发表失败");
			}
		});
	}

	String targeturl = null;

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_ALBUM:
				String fileName = null;
				if (data != null) {
					Uri originalUri = data.getData();
					ContentResolver cr = getContentResolver();
					Cursor cursor = cr.query(originalUri, null, null, null,
							null);
					if (cursor.moveToFirst()) {
						do {
							fileName = cursor.getString(cursor
									.getColumnIndex("_data"));
						} while (cursor.moveToNext());
					}
					Bitmap bitmap = compressImageFromFile(fileName);
					targeturl = saveToSdCard(bitmap);
					albumPic.setBackgroundDrawable(new BitmapDrawable(bitmap));
					takeLayout.setVisibility(View.GONE);
				}
				break;
			case REQUEST_CODE_CAMERA:
				String files = CacheUtils.getCacheDirectory(mContext, true,
						"pic") + dateTime;
				File file = new File(files);
				if (file.exists()) {
					Bitmap bitmap = compressImageFromFile(files);
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
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
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
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
		// 其实是无效的,大家尽管尝试
		return bitmap;
	}

	public String saveToSdCard(Bitmap bitmap) {
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
		// TODO 自动生成的方法存根
		showMsgDialog("提示", "         是否退出编辑？ ", "确定", "取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		}, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
			}
		});
		
	}
}
