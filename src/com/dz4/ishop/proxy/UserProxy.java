package com.dz4.ishop.proxy;

import com.dz4.ishop.domain.User;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.LogUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


import android.content.Context;

public class UserProxy {

	public static final String TAG = "UserProxy";
	
	private Context mContext;
	
	public UserProxy(Context context){
		this.mContext = context;
	}
	/**
	 * 
	 * 注册
	 * 
	 * @param userName
	 * @param password
	 * @param email
	 */
	public void signUp(String userName,String password,String email){
		User user = new User();
		user.setUsername(userName);
		user.setPassword(password);
		user.setEmail(email);
		user.setSex(Constant.SEX_FEMALE);
		user.setSignature("签名");
		user.signUp(mContext, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(signUpLister != null){
					signUpLister.onSignUpSuccess();
				}else{
					LogUtils.i(TAG,"signup listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				if(signUpLister != null){
					signUpLister.onSignUpFailure(msg);
				}else{
					LogUtils.i(TAG,"signup listener is null,you must set one!");
				}
			}
		});
	}
	
	public interface onSignUpListener{
		void onSignUpSuccess();
		void onSignUpFailure(String msg);
	}
	private onSignUpListener signUpLister;
	public void setOnSignUpListener(onSignUpListener signUpLister){
		this.signUpLister = signUpLister;
	}
	
	/**
	 * 当前用户
	 * 
	 * 
	 * @param userName
	 * @param password
	 * @param email
	 */
	public User getCurrentUser(){
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if(user != null){
			LogUtils.i(TAG,"鏈湴鐢ㄦ埛淇℃伅" + user.getObjectId() + "-"
					+ user.getUsername() + "-"
					+ user.getSessionToken() + "-"
					+ user.getCreatedAt() + "-"
					+ user.getUpdatedAt() + "-"
					+ user.getSignature() + "-"
					+ user.getSex());
			return user;
		}else{
		}
		return null;
	}
	/**
	 * 
	 * 
	 * 登录
	 * @param userName
	 * @param password
	 * @param email
	 */
	public void login(String userName,String password){
		final BmobUser user = new BmobUser();
		user.setUsername(userName);
		user.setPassword(password);
		user.login(mContext, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(loginListener != null){
					loginListener.onLoginSuccess();
				}else{
					LogUtils.i(TAG, "login listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				if(loginListener != null){
					loginListener.onLoginFailure(msg);
				}else{
					LogUtils.i(TAG, "login listener is null,you must set one!");
				}
			}
		});
	}
	
	public interface onLoginListener{
		void onLoginSuccess();
		void onLoginFailure(String msg);
	}
	private onLoginListener loginListener;
	public void setOnLoginListener(onLoginListener onloginListener){
		this.loginListener  = onloginListener;
	}
	/**
	 * 
	 * logout
	 * 
	 * @param userName
	 * @param password
	 * @param email
	 */
	public void logout(){
		BmobUser.logOut(mContext);
		LogUtils.i(TAG, "logout result:"+(null == getCurrentUser()));
	}
	/**
	 * 
	 * update
	 * 
	 * @param userName
	 * @param password
	 * @param email
	 */
	public void update(String... args){
		User user = getCurrentUser();
		user.setUsername(args[0]);
		user.setEmail(args[1]);
		user.setPassword(args[2]);
		user.setSex(args[3]);
		user.setSignature(args[4]);
		//...
		user.update(mContext, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(updateListener != null){
					updateListener.onUpdateSuccess();
				}else{
					LogUtils.i(TAG,"update listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				if(updateListener != null){
					updateListener.onUpdateFailure(msg);
				}else{
					LogUtils.i(TAG,"update listener is null,you must set one!");
				}
			}
		});
	}
	
	public interface onUpdateListener{
		void onUpdateSuccess();
		void onUpdateFailure(String msg);
	}
	private onUpdateListener updateListener;
	public void setOnUpdateListener(onUpdateListener updateListener){
		this.updateListener = updateListener;
	}
	/**
	 * 
	 * resetPassword
	 * 
	 * @param userName
	 * @param password
	 * @param email
	 */
	public void resetPassword(String email){
		BmobUser.resetPasswordByEmail(mContext, email, new ResetPasswordByEmailListener() {
			
			@Override
			public void onSuccess() {
				// TODO 自动生成的方法存根
				if(resetPasswordListener != null){
					resetPasswordListener.onResetSuccess();
				}else{
					LogUtils.i(TAG,"reset listener is null,you must set one!");
				}
			}
			
			@Override
			public void onFailure(int arg0, String msg) {
				// TODO 自动生成的方法存根
				if(resetPasswordListener != null){
					resetPasswordListener.onResetFailure(msg);
				}else{
					LogUtils.i(TAG,"reset listener is null,you must set one!");
				}
			}
		});
	}
	public interface onResetPasswordListener{
		void onResetSuccess();
		void onResetFailure(String msg);
	}
	
	private onResetPasswordListener resetPasswordListener;
	public void setOnResetPasswordListener(onResetPasswordListener resetPasswordListener){
		this.resetPasswordListener = resetPasswordListener;
	}

}
