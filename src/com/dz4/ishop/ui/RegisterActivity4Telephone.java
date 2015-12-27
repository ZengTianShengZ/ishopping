package com.dz4.ishop.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobSmsState;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QuerySMSStateListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

import com.dz4.ishop.proxy.UserProxy;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishop.utils.RegularExpression;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;

public class RegisterActivity4Telephone extends BaseUIActivity implements TopBar.onTopBarbtnclickListener,
OnClickListener,UserProxy.onQueryListener{

	private TopBar mTopBar;
	private EditText telephone_num;
	private EditText SMS_num;
	private TextView SMS_code;
	private TextView tv_Time;
	private RelativeLayout next_btn;
	private Chronometer Chro_Time;
	
	private String telephoneum;
	private String SmsNum;
	private String FindPassword;
	private int Time_number = 59;
	
	private Context context;
	private Integer SmsId = null;
	
	private UserProxy mUserProxy;
	
	private boolean CodeCheak;


	@Override
	public void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regiest_telephone);
		
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.back));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		
		telephone_num = (EditText) findViewById(R.id.regiest_telephone_edit_telephone_num);
		SMS_num = (EditText) findViewById(R.id.regiest_telephone_edit_SMS_num);
		SMS_code = (TextView) findViewById(R.id.regiest_telephone_SMS_Code);
		next_btn = (RelativeLayout) findViewById(R.id.regiest_telephone_next_btn);

		tv_Time = (TextView) findViewById(R.id.regiest_telephone_tv_Time);
		Chro_Time = (Chronometer) findViewById(R.id.regiest_telephone_Chr_Time);
 
	}

	@Override
	public void initData() {
		context = getApplicationContext();
		Chro_Time.setVisibility(View.GONE);
		tv_Time.setVisibility(View.GONE);
		mUserProxy=new UserProxy(this);
		//Chronometer 计时器 ， 该组件继承 TextView
		//Chro_Time = new Chronometer(this);
 
	}

	@Override
	public void initEvent() {
		SMS_code.setOnClickListener(this);
		next_btn.setOnClickListener(this);
		mTopBar.setTopBarbtnclickListener(this);
		mUserProxy.setOnQueryListener(this);
		
		Chro_Time.setOnChronometerTickListener(new OnChronometerTickListener(){
			
			@Override
			public void onChronometerTick(Chronometer ch){
				if(SystemClock.elapsedRealtime() - ch.getBase() > 1000){
					
					Chro_Time.setBase(SystemClock.elapsedRealtime());// 当1S 时间到时重新获取系统时间
					Chro_Time.start();
					Time_number--;
				 	tv_Time.setText(Time_number+" S");
					if(Time_number <= 0){ // 59 秒 计时完
						ch.stop(); // 停止计时 
						Time_number = 59; 
						tv_Time.setVisibility(View.GONE);
					 	SMS_code.setVisibility(View.VISIBLE);
				    	 
					} 
				}
			}
		});
		
	}

	@Override
	public void rightbtnclick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leftbtnclick(View v) {
		finish();
	}
	@Override
	public void onClick(View v) {
		telephoneum = telephone_num.getEditableText().toString();
	    SmsNum = SMS_num.getEditableText().toString();

		if(v == SMS_code){
			if(TextUtils.isEmpty(telephoneum)){//telephoneum 为空或 null 返回 true
				showToast(R.string.regiest_telephone_null); 
				 
			}else{
				 			    
			    if(RegularExpression.RegExp_telephoneNumber(telephoneum)){ // 自定义工具类，用正则表达式判断手机号是否符合    boolean			    	

			    	CodeCheak = true;
			    	mUserProxy.query(telephoneum); // 查询号码是否已被注册
			    	 
			    }else{
			    	showToast(R.string.telephone_is_wrong); 
			    }
				 
			}
		}
		if(v == next_btn){
			if(TextUtils.isEmpty(SmsNum)|| TextUtils.isEmpty(telephoneum) ){
				showToast(R.string.regiest_telephone_or_code_null); 
			}else{
				if(RegularExpression.RegExp_is_SixNumber(SmsNum)&& 
						RegularExpression.RegExp_telephoneNumber(telephoneum))// 正则表达式判断 
				{
					CodeCheak = false;
					mUserProxy.query(telephoneum); // 查询号码是否已被注册
  			
				}else{
					showToast(R.string.telephone_or_SMScode_is_wrong); 
				}
 
			}
		}
	}

	/**
	 * 查找到手机号说明   手机号已经被注册
	 */
	@Override
	public void onQuerySuccess(String MobilePhoneNumber) {

			showToast(R.string.telephone_is_exist);

	}
    /**
     *  手机号没被 注册
     */
	@Override
	public void onQueryFailure(String msg) {
		 
		if(CodeCheak){

			requestSMSCode();
			 
		}else{
			 
			 showProgressDialog();
			 verifySmsCode();
		}
        
	}
	/**
	 * 验证验证码
	 */
	private void verifySmsCode(){
		BmobSMS.verifySmsCode(context, telephoneum, SmsNum, new VerifySMSCodeListener() {
			
			@Override
			public void done(BmobException ex) {
				if(ex == null){// 验证码验证成功
					cancelProgressDialog();
					Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
					intent.putExtra("telephoneum", telephoneum);
					startActivity(intent);
		 
				}else{
					cancelProgressDialog();
					showToast(R.string.SMScode_FALL); 
				}
			}
		}); 
	}
	/**
	 * 发送验证码
	 */
	private void requestSMSCode(){
		
		showToast(R.string.wait_SMS_code); 
    	SMS_code.setVisibility(View.GONE);
    	tv_Time.setVisibility(View.VISIBLE);
    	tv_Time.setText("59 S");
    	Chro_Time.setBase(SystemClock.elapsedRealtime());
    	Chro_Time.start();// 开始计时			    	 
    	
		BmobSMS.requestSMSCode(context, telephoneum, "短信验证码", new RequestSMSCodeListener() {
			
			@Override
			public void done(Integer smsId, BmobException ex) {
				SmsId = smsId;
				if(ex == null){// 验证码发送成功
					 
					LogUtils.i("smsSendSuccekc", "短信ID"+smsId);
				}
			}
		});
	}
	
}
