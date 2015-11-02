package com.dz4.ishop.ui;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.Bmob;

import com.dz4.ishop.proxy.UserProxy;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;

public class LoginActivity extends BaseUIActivity implements TopBar.onTopBarbtnclickListener,OnClickListener
,UserProxy.onLoginListener{
	private TopBar mTopBar;
	private EditText input_username;
	private EditText input_password;
	private Button login_btn;
	private TextView more;
	
	private String username;
	private String password;
	
	private UserProxy mUserProxy;

	@Override
	public void initView() {
		// TODO 自动生成的方法存根
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setTitleText(getString(R.string.login));
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.login_back));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		
		input_username = (EditText) findViewById(R.id.input_username);
		input_password = (EditText) findViewById(R.id.input_password);
		login_btn = (Button)findViewById(R.id.login_btn);
		more=(TextView)findViewById(R.id.more);
		
	}

	@Override
	public void initData() {
		// TODO 自动生成的方法存根
		Bmob.initialize(getApplicationContext(), Constant.BMOB_APP_ID);
		mUserProxy=new UserProxy(this);
	}

	@Override
	public void initEvent() {
		// TODO 自动生成的方法存根
		mTopBar.setTopBarbtnclickListener(this);
		login_btn.setOnClickListener(this);
		more.setOnClickListener(this);
		mUserProxy.setOnLoginListener(this);
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

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch(v.getId()){
			case R.id.login_btn:
				login();
				break;
			case R.id.more:
				showMsgDialog(getString(R.string.more), null, getString(R.string.regiest), getString(R.string.findpsw), new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
						startActivity(intent);
					}
				}, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						showToast(getString(R.string.findpsw));
					}
				});
				break;
		}
	}

	private void login() {
		// TODO 自动生成的方法存根
		username=input_username.getEditableText().toString();
		password=input_password.getEditableText().toString();
		mUserProxy.login(username, password);
		showProgressDialog();
				
	}

	@Override
	public void onLoginSuccess() {
		// TODO 自动生成的方法存根
		
		cancelProgressDialog();
		showToast(R.string.login_success);
		finish();
	}

	@Override
	public void onLoginFailure(String msg) {
		// TODO 自动生成的方法存根
		cancelProgressDialog();
		showToast(R.string.login_faile);
	}
	
}
