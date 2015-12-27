package com.dz4.ishop.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.proxy.UserProxy;
import com.dz4.ishop.utils.CacheUtils;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;
import com.dz4.support.utils.UtilsTools;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class UserInfoActivity extends BaseUIActivity implements
TopBar.onTopBarbtnclickListener,OnClickListener,OnCheckedChangeListener,OnFocusChangeListener{
	private final String TAG ="UserInfoActivity";
	
	private Context mContext;
	private TopBar mTopBar;
	private ImageView usericon;
	private EditText userNickNameText;
	private CheckBox sexCheckBox;
	private CheckBox pushCheckBox;
	private TextView userSignText;
	private View userSign;
	private View iconitem;
	private View clCacheBtn;
	private View userNick;
	
	private String iconurl;
	private String nickname;
	private String sex;
	private String sign;
	
	private User user; 

	private IshopApplication app;
	
	private AlertDialog albumDialog;
	private String dateTime;


	private String iconUrl;

	private View UpdateBtn;

	@Override
	public void initView() {
		// TODO 自动生成的方法存根
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_userinfo);
		mTopBar = (TopBar)findViewById(R.id.topbar);
		mTopBar.setTitleText(getString(R.string.userinfo));
		mTopBar.setTopBarbtnclickListener(this);
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.login_back));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		
		usericon =(ImageView) findViewById(R.id.user_icon_image);
		iconitem =(View) findViewById(R.id.user_icon);
		userNickNameText =(EditText)findViewById(R.id.user_nick_text);
		sexCheckBox =(CheckBox) findViewById(R.id.sex_choice_switch);
		pushCheckBox =(CheckBox) findViewById(R.id.settings_push_switch);
		userSignText = (TextView) findViewById(R.id.user_sign_text);
		userNick = (View)findViewById(R.id.user_nick);
		userSign=(View)findViewById(R.id.user_sign);
		clCacheBtn = (View)findViewById(R.id.settings_cache);
		UpdateBtn = (View)findViewById(R.id.settings_update);
		
	}
	public void logout(View v){
		showMsgDialog("提示", "     是否确定退出登录？    ", "确定", "取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				new UserProxy(getApplicationContext()).logout();
				app.removeLogin();//清楚登录标记
				app.notifyDataChange();
				finish();
			}
		}, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				cancelMsgDialog();
			}
		});
		
	}
	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		initData();
		super.onResume();
	}
	@Override
	public void initData() {
		mContext=getApplicationContext();
		// TODO 自动生成的方法存根
		app = (IshopApplication)getApplication();
		user = ((IshopApplication)getApplication()).getCurrentUser();
		nickname=user.getNickname();
		if(!UtilsTools.isStringInvalid(nickname)){
			userNickNameText.setText(nickname);
		}else{
			userNickNameText.setText(user.getUsername());
		}
		sex=user.getSex();
		if(!UtilsTools.isStringInvalid(sex)){
			
			if(sex.trim().equals(Constant.SEX_MALE)){
				sexCheckBox.setChecked(false);
			}
			else if(sex.trim().equals(Constant.SEX_FEMALE))
			{
				sexCheckBox.setChecked(true);
			}
		}
		sign = user.getSignature();
		if(!UtilsTools.isStringInvalid(sign)){
			userSignText.setText(sign);
		}
		BmobFile icon;
		if((icon= user.getAvatar())!=null){
			iconurl = icon.getFileUrl(getApplicationContext());
		}
		ImageLoader.getInstance().displayImage(iconurl,usericon, ImageUtils.getOptions(R.drawable.user_icon_default_main),new SimpleImageLoadingListener(){
			public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
				
			};
		} );
	}
	@Override
	public void initEvent() {
		// TODO 自动生成的方法存根
		iconitem.setOnClickListener(this);
		userSign.setOnClickListener(this);
		userNick.setOnClickListener(this);
		userNickNameText.setOnFocusChangeListener(this);
		
		sexCheckBox.setOnCheckedChangeListener(this);
		pushCheckBox.setOnCheckedChangeListener(this);
		clCacheBtn.setOnClickListener(this);
		UpdateBtn.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.user_nick:
			
			break;
		case R.id.user_sign:
			LogUtils.i(TAG, "user_sign click");
			Intent intent = new Intent(UserInfoActivity.this,EditSigntureActivity.class);
			intent.putExtra(Constant.BUNDLE_KEY_SIGNATURE, sign);
			intent.putExtra(Constant.BUNDLE_KEY_USER, user);
			startActivity(intent);
			break;
		case R.id.user_icon:
			LogUtils.i(TAG, "user_icon click");
			showAlbumDialog();
			break;
		case R.id.settings_cache:
			ImageLoader.getInstance().clearMemoryCache();
			ImageLoader.getInstance().clearDiscCache();
			showToast("清除图片缓存");
			break;
		case R.id.settings_update:
			UmengUpdateAgent.forceUpdate(mContext);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				
				@Override
				public void onUpdateReturned(int arg0, UpdateResponse arg1) {
					// TODO 自动生成的方法存根
					switch (arg0) {
					case UpdateStatus.Yes:
						LogUtils.i(TAG,"need update");
						break;
					case UpdateStatus.No:
						LogUtils.i(TAG,"don't need update");
						showToast("已经是最新版本了！");
						break;
					default:
						break;
					}
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView,
			boolean isChecked) {
		// TODO 自动生成的方法存根
		switch(buttonView.getId()){
		case R.id.sex_choice_switch:
			if(isChecked){
				user.setSex(Constant.SEX_FEMALE);
			}else{
				user.setSex(Constant.SEX_MALE);
			}
			user.update(getApplicationContext(), new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO 自动生成的方法存根
					app.notifyDataChange();
					showToast("数据更新成功");
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO 自动生成的方法存根
					showToast("数据更新失败");
				}
			});
			break;
		case R.id.settings_push_switch:
			if(isChecked){
				//user.
				showToast("接受推送消息");
				LogUtils.i(TAG, "push is allow!");
			}else{
				showToast("不接受推送消息");
				LogUtils.i(TAG, "push is not allow!");
			}
			break;
		}
	}
	@Override
	public void rightbtnclick(View v) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void leftbtnclick(View v) {
		// TODO 自动生成的方法存根
		finish();
	}
	public void showAlbumDialog() {
		View v = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_usericon, null);
		AlertDialog.Builder builder =new Builder(UserInfoActivity.this);
		builder.setView(v);
		albumDialog=builder.create();
		albumDialog.show();
		TextView albumPic = (TextView) v.findViewById(R.id.album_pic);
		TextView cameraPic = (TextView) v.findViewById(R.id.camera_pic);
		albumPic.setOnClickListener(new OnClickListener() {



			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				albumDialog.dismiss();
				Date date1 = new Date(System.currentTimeMillis());
				dateTime = date1.getTime() + "";
				getAvataFromAlbum();
			}
		});
		cameraPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				albumDialog.dismiss();
				Date date = new Date(System.currentTimeMillis());
				dateTime = date.getTime() + "";
				getAvataFromCamera();
			}
		});
	}
	/**
	 * 相簿取得图片，
	 * 结果通过onActivityReslut返回
	 */
	private void getAvataFromAlbum() {
		Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
		intent2.setType("image/*");
		startActivityForResult(intent2, 2);
	}
	/**
	 * 相机取得图片
	 * 结果通过onActivityReslut返回
	 */
	private void getAvataFromCamera() {
		File f = new File(CacheUtils.getCacheDirectory(mContext, true, "icon")
				+ dateTime);
		if (f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri uri = Uri.fromFile(f);
		Log.e("uri", uri + "");

		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(camera, 1);
	}
	/**
	 * 裁剪头像图片
	 * 结果通过onActivityReslut返回
	 * @param uri
	 * 
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("aspectX", 1);//X方向上的比例
		intent.putExtra("aspectY", 1);//Y方向上的比例
		intent.putExtra("outputX", 120);//裁剪区的宽
		intent.putExtra("outputY", 120);//裁剪区的高
		intent.putExtra("crop", "true");//发送裁剪信号
		intent.putExtra("scale", true);//是否保留比例
		intent.putExtra("scaleUpIfNeeded", true);
		// intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);//是否返回数据
		startActivityForResult(intent, 3);

	}
	public String saveToSdCard(Bitmap bitmap) {
		String files = CacheUtils.getCacheDirectory(mContext, true, "icon")
				+ dateTime + "_12.jpg";
		File file = new File(files);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
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
		LogUtils.i(TAG, file.getAbsolutePath());
		return file.getAbsolutePath();
	}
	private void setAvata(String avataPath) {
		if (avataPath != null) {
			final BmobFile f = new BmobFile(new File(avataPath));
			f.upload(mContext, new UploadFileListener() {
				@Override
				public void onSuccess() {
					user.setAvatar(f);
					user.update(mContext, new UpdateListener() {
						
						@Override
						public void onSuccess() {
							// TODO 自动生成的方法存根
							LogUtils.i(TAG, "上传头像成功");
							showToast("上传头像成功");
							initData();
							app.notifyDataChange();
						}
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO 自动生成的方法存根
							LogUtils.i(TAG, "上传头像失败");
							showToast("上传头像失败");
						}
					});
					LogUtils.i(TAG, "加载头像到Bmob成功");
				}
				@Override
				public void onProgress(Integer value) {
					super.onProgress(value);
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					LogUtils.i(TAG, arg1);
					showToast(arg1);
				}
				});
			}
		}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == this.RESULT_OK) {
			switch (requestCode) {
			case 1:
				String files = CacheUtils.getCacheDirectory(mContext, true,
						"icon") + dateTime;
				File file = new File(files);
				if (file.exists() && file.length() > 0) {
					Uri uri = Uri.fromFile(file);
					startPhotoZoom(uri);
				} else {

				}
				break;
			case 2:
				if (data == null) {
					return;
				}
				startPhotoZoom(data.getData());
				break;
			case 3:
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bitmap = extras.getParcelable("data");
						// 锟斤拷锟斤拷图片
						iconUrl = saveToSdCard(bitmap);
						setAvata(iconUrl);
						usericon.setImageBitmap(bitmap);
					}
				}
				break;
			default:
				break;
			}
			
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO 自动生成的方法存根
		LogUtils.i(TAG, "onFocusChange value："+hasFocus);
		if(!hasFocus){
			String ninkname =((EditText)v).getEditableText().toString();
			user.setNickname(ninkname);
			user.update(mContext, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					showToast(R.string.update_success);
					app.notifyDataChange();
					LogUtils.i(TAG, "nickname update success!!");
					
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO 自动生成的方法存根
					LogUtils.i(TAG, "nickname update failure!!");
					showToast(R.string.update_faile);
				}
			});
		}
	}
	
	
}
