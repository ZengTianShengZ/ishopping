package com.dz4.ishop.app;


import java.io.File;

import android.content.Context;
import cn.bmob.v3.BmobUser;

import com.dz4.ishop.domain.User;
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
	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		initImageLoader(getApplicationContext());
		super.onCreate();
		
	}
	public User getCurrentUser() {
		User user = BmobUser.getCurrentUser(this, User.class);
		if(user!=null){
			return user;
		}
		return null;
	}
	
	/**
	 * 初始化imageLoader
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
	 * 是否第一次登入
	 * 
	 * @return
	 */
	public boolean isFirstLaunch() {
		return mIsFirstLaunch;
	}

	public void setFirstLaunch(boolean isFirstLaunch) {
		mIsFirstLaunch = isFirstLaunch;
	}
}
