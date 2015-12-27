package com.dz4.ishop.ui;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.Bmob;

import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.proxy.UserProxy;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishop.utils.RegularExpression;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;

public class LoginActivity extends BaseUIActivity implements TopBar.onTopBarbtnclickListener,OnClickListener
,UserProxy.onLoginListener,UserProxy.onQueryListener{
	private TopBar mTopBar;
	private EditText input_account;
	private EditText input_password;
	private Button login_btn;
	private TextView more; 
	
	private String account;
	private String password;
	
	private UserProxy mUserProxy;

	@Override
	public void initView() {
		// TODO �Զ����ɵķ������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setTitleText(getString(R.string.login));
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.login_back));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		
		input_account = (EditText) findViewById(R.id.input_account);
		input_password = (EditText) findViewById(R.id.input_password);
		login_btn = (Button)findViewById(R.id.login_btn);
		more=(TextView)findViewById(R.id.more);
		
	}

	@Override
	public void initData() {
		// TODO �Զ����ɵķ������
		Bmob.initialize(getApplicationContext(), Constant.BMOB_APP_ID);
		mUserProxy=new UserProxy(this);
	}

	@Override
	public void initEvent() {
		// TODO �Զ����ɵķ������
		mTopBar.setTopBarbtnclickListener(this);
		login_btn.setOnClickListener(this);
		more.setOnClickListener(this);
		mUserProxy.setOnLoginListener(this);
		mUserProxy.setOnQueryListener(this);
	}

	@Override
	public void rightbtnclick(View v) {
		// TODO �Զ����ɵķ������
	}

	@Override
	public void leftbtnclick(View v) {
		// TODO �Զ����ɵķ������
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		switch(v.getId()){
			case R.id.login_btn:
				login();
				break;
			case R.id.more:
				showMsgDialog(getString(R.string.more), null, getString(R.string.regiest), getString(R.string.findpsw), new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						 
						//Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
						Intent intent = new Intent(getApplicationContext(),RegisterActivity4Telephone.class);
						startActivity(intent);
					}
				}, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO �Զ����ɵķ������
						//showToast(getString(R.string.findpsw));
						
						Intent intent = new Intent(getApplicationContext(),ResetPasswordActivity.class);
						startActivity(intent);
					}
				});
				break;
		}
	}

	private void login() {
		// TODO �Զ����ɵķ������
		account=input_account.getEditableText().toString();
		password=input_password.getEditableText().toString();
		
		 
		
  	    if(RegularExpression.RegExp_is_allNumber(account)){  // ����� ����
 	    	LogUtils.i("query","......sion.RegExp_is_allNu  ");
			if(RegularExpression.RegExp_telephoneNumber(account)){
				mUserProxy.query(account);
			}else{
				showToast("�ֻ���������");
				return; //  return ���������� ����
			}
 	    } 
 	    else{
 	    	LogUtils.i("query","...... login(account, password);  ");
	 		mUserProxy.login(account, password);  //  ���� �û�����¼
		}
 
		if(((IshopApplication)getApplication()).getHandler()!=null){
			((IshopApplication)getApplication()).getHandler().sendEmptyMessage(Constant.MSG_LOGIN_CHANGE);
		}
		showProgressDialog();
				
	}

	/**
	 * ��¼�ص�
	 */
	@Override
	public void onLoginSuccess() {
		// TODO �Զ����ɵķ������
		
		cancelProgressDialog();
		showToast(R.string.login_success);
		IshopApplication appdata=((IshopApplication)getApplication());
		appdata.setLogin();//��ǵ�¼
		
		Intent intent = new Intent(getApplicationContext(),MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onLoginFailure(String msg) {
		// TODO �Զ����ɵķ������
		cancelProgressDialog();
		showToast(R.string.login_faile);
	}

	/**
	 * ��ѯ�ص�
	 */
	@Override
	public void onQuerySuccess(String Username) {
		mUserProxy.login(Username, password);  // ������ʽͨ���������ֻ��ŵ�¼������ true
	}

	@Override
	public void onQueryFailure(String msg) {
		LogUtils.i("query","......query....sibsisisib");
		cancelProgressDialog();
		showToast(R.string.telephone_no_exist);
	}
	
}
