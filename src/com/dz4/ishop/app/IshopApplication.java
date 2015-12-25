package com.dz4.ishop.app;


import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobUser;

import com.dz4.ishop.domain.User;
import com.dz4.ishop.listener.OnUserInfoChangeListener;
import com.dz4.ishop.utils.Constant;
import com.dz4.support.app.IApplication;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
/**
 * 
 * 
 * @author MZone
 *
 */

public class IshopApplication extends IApplication {
	private boolean mIsFirstLaunch=true;
	private SharedPreferences pre;
	private OnUserInfoChangeListener monUserInfoChangeListener;
	@Override
	public void onCreate() {
		// TODO �Զ����ɵķ������
		initImageLoader(getApplicationContext());
		Bmob.initialize(getApplicationContext(), Constant.BMOB_APP_ID);
		pre=getSharedPreferences(Constant.PRE_NAME, Context.MODE_WORLD_WRITEABLE);
		super.onCreate();
		
	}
	public User getCurrentUser() {
		User user = BmobUser.getCurrentUser(this, User.class);
		if(user!=null){
			return user;
		}
		return null;
	}
	private Handler mhandler;
	public Handler getHandler(){
		return mhandler;
	}
	public void setHandler(Handler mhandler){
		this.mhandler = mhandler;
	}
	/**
	 * ��¼���
	 */
	public void setLogin(){
		pre.edit().putBoolean(Constant.SHARE_KEY_LOGINFLAG, true).commit();
	}
	
	/**
	 * ע�����
	 */
	public void removeLogin(){
		pre.edit().putBoolean(Constant.SHARE_KEY_LOGINFLAG, false).commit();
	}
	/**
	 * 
	 * @return
	 */
	public boolean getloginState(){
		return pre.getBoolean(Constant.SHARE_KEY_LOGINFLAG, false);
	}
	/**
	 * ��ʼ��imageLoader
	 * ȷ���ڴ滺���С����������λ��   
	 */
	public void initImageLoader(Context context){
		File cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.memoryCache(new LruMemoryCache(5*1024*1024))
			.memoryCacheSize(10*1024*1024)
			.discCache(new UnlimitedDiscCache(cacheDir))
			.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
			.build();
		ImageLoader.getInstance().init(config);
	}
	/**
	 * �Ƿ��һ�ε���
	 * 
	 * @return
	 */
	public boolean isFirstLaunch() {
		return mIsFirstLaunch;
	}

	public void setFirstLaunch(boolean isFirstLaunch) {
		mIsFirstLaunch = isFirstLaunch;
	}
	public void setOnUserInfoChangeListener(OnUserInfoChangeListener monUserInfoChangeListener){
		this.monUserInfoChangeListener = monUserInfoChangeListener;
	}
	public void notifyDataChange(){
		if(monUserInfoChangeListener!=null){
			monUserInfoChangeListener.OnUserInfoChange();
		}
	}
	
}
