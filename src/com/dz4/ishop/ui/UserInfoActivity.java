package com.dz4.ishop.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobFile;

import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.proxy.UserProxy;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;
import com.dz4.support.utils.UtilsTools;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class UserInfoActivity extends BaseUIActivity implements TopBar.onTopBarbtnclickListener {
	private TopBar mTopBar;
	private ImageView usericon;
	private TextView usernickname_Text;
	private CheckBox sex_checkbox;
	private TextView usersign_Text;
	
	private String iconurl;
	private String username;
	private String sex;
	private String sign;
	
	
	private User user; 
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
		usernickname_Text =(TextView)findViewById(R.id.user_nick_text);
		sex_checkbox =(CheckBox) findViewById(R.id.sex_choice_switch);
		usersign_Text = (TextView) findViewById(R.id.user_sign_text);
		
		user = ((IshopApplication)getApplication()).getCurrentUser();
		username=user.getUsername();
		if(!UtilsTools.isStringInvalid(username)){
			usernickname_Text.setText(username);
		}
		sex=user.getSex();
		if(!UtilsTools.isStringInvalid(sex)){
			if(sex.equals("male")){
				sex_checkbox.setChecked(false);
			}else if(sex.equals("female"))
			{
				sex_checkbox.setChecked(true);
			}
		}
		sign = user.getSignature();
		if(!UtilsTools.isStringInvalid(sign)){
			usersign_Text.setText(sign);
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
	public void logout(View v){
		showMsgDialog("提示", "     是否确定退出登录？    ", "确定", "取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				new UserProxy(getApplicationContext()).logout();
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
	public void initData() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void initEvent() {
		// TODO 自动生成的方法存根

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

}
