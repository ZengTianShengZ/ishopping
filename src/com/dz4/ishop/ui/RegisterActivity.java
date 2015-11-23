package com.dz4.ishop.ui;

import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.Bmob;

import com.dz4.ishop.proxy.UserProxy;
import com.dz4.ishop.proxy.UserProxy.onSignUpListener;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishop.view.TopBar.onTopBarbtnclickListener;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;
import com.dz4.support.utils.UtilsTools;

public class RegisterActivity extends BaseUIActivity implements onTopBarbtnclickListener,OnClickListener
,onSignUpListener{
	private TopBar mTopBar;
	private EditText input_username;
	private EditText input_password;
	private EditText input_repassword;
	private EditText input_email;
	
	private Button regiest_btn;
	
	private String username;
	private String password;
	private String repassword;
	private String email;
	
	private UserProxy mUserProxy;
	@Override
	public void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regiest);
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setTitleText(getString(R.string.regiest));
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.back));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		
		input_username = (EditText) findViewById(R.id.input_username);
		input_password = (EditText) findViewById(R.id.input_password);
		input_repassword = (EditText) findViewById(R.id.input_repassword);
		input_email = (EditText) findViewById(R.id.input_email);
		regiest_btn = (Button)findViewById(R.id.regiest_btn);
	}

	@Override
	public void initData() {
		mUserProxy=new UserProxy(this);
	}

	@Override
	public void initEvent() {
		mTopBar.setTopBarbtnclickListener(this);
		regiest_btn.setOnClickListener(this);
		mUserProxy.setOnSignUpListener(this);
	}

	@Override
	public void rightbtnclick(View v) {
		
	}

	@Override
	public void leftbtnclick(View v) {
		finish();
	}

	@Override
	public void onClick(View v) {
		username = input_username.getEditableText().toString();
		password = input_password.getEditableText().toString();
		email = input_email.getEditableText().toString();
		repassword =input_repassword.getEditableText().toString(); 
		showProgressDialog();
		if(isValid(username,password,email,repassword)){
			mUserProxy.signUp(username, password, email);
		}else{
			cancelProgressDialog();
			showToast(R.string.regiest_faile);
		}
	}

	private boolean isValid(String username, String password, String email,String repassword) {
		if(UtilsTools.isStringInvalid(username)){
			showToast("username Invalid");
			return false;
		}
		if(UtilsTools.isStringInvalid(password)){
			showToast("password Invalid");
			return false;
		}
		if(UtilsTools.isStringInvalid(repassword)){
			showToast("repassword Invalid");
			return false;
		}
		if(UtilsTools.isStringInvalid(email)){
			showToast("email Invalid");
			return false;
		}
		if(!password.equals(repassword)){
			showToast(" Invalid");
			return false;
		}
		
		return true;
	}

	@Override
	public void onSignUpSuccess() {
		
		cancelProgressDialog();
		showToast(R.string.regiest_success);
		finish();
	}

	@Override
	public void onSignUpFailure(String msg) {
		
		cancelProgressDialog();
		showToast(msg);
	}
	
}
