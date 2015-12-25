package com.dz4.ishop.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

/**
 * 棣栭�夐」宸ュ叿绫�
 * 
 * @author adison
 * 
 */
public class Sputil {
	private Context context;
	private SharedPreferences sp = null;
	private Editor edit = null;

	/**
	 * 鍒涘缓榛樿sp
	 * 
	 * @param context
	 */
	public Sputil(Context context) {
		this(context, PreferenceManager.getDefaultSharedPreferences(context));
	}

	/**
	 * 閫氳繃鏂囦欢鍚嶅垱寤簊p
	 * 
	 * @param context
	 * @param filename
	 */
	public Sputil(Context context, String filename) {
		this(context, context.getSharedPreferences(filename,
				Context.MODE_WORLD_WRITEABLE));
	}

	/**
	 * 閫氳繃sp鍒涘缓sp
	 * 
	 * @param context
	 * @param sp
	 */
	public Sputil(Context context, SharedPreferences sp) {
		this.context = context;
		this.sp = sp;
		edit = sp.edit();
	}

	public SharedPreferences getInstance() {
		return sp;
	}

	// Set

	// Boolean
	public void setValue(String key, boolean value) {
		edit.putBoolean(key, value);
		edit.commit();
	}

	public void setValue(int resKey, boolean value) {
		setValue(this.context.getString(resKey), value);
	}

	// Float
	public void setValue(String key, float value) {
		edit.putFloat(key, value);
		edit.commit();
	}

	public void setValue(int resKey, float value) {
		setValue(this.context.getString(resKey), value);
	}

	// Integer
	public void setValue(String key, int value) {
		edit.putInt(key, value);
		edit.commit();
	}

	public void setValue(int resKey, int value) {
		setValue(this.context.getString(resKey), value);
	}

	// Long
	public void setValue(String key, long value) {
		edit.putLong(key, value);
		edit.commit();
	}

	public void setValue(int resKey, long value) {
		setValue(this.context.getString(resKey), value);
	}

	// String
	public void setValue(String key, String value) {
		edit.putString(key, value);
		edit.commit();
	}

	public void setValue(int resKey, String value) {
		setValue(this.context.getString(resKey), value);
	}

	// Get

	// Boolean
	public boolean getValue(String key, boolean defaultValue) {
		return sp.getBoolean(key, defaultValue);
	}

	public boolean getValue(int resKey, boolean defaultValue) {
		return getValue(this.context.getString(resKey), defaultValue);
	}

	// Float
	public float getValue(String key, float defaultValue) {
		return sp.getFloat(key, defaultValue);
	}

	public float getValue(int resKey, float defaultValue) {
		return getValue(this.context.getString(resKey), defaultValue);
	}

	// Integer
	public int getValue(String key, int defaultValue) {
		return sp.getInt(key, defaultValue);
	}

	public int getValue(int resKey, int defaultValue) {
		return getValue(this.context.getString(resKey), defaultValue);
	}

	// Long
	public long getValue(String key, long defaultValue) {
		return sp.getLong(key, defaultValue);
	}

	public long getValue(int resKey, long defaultValue) {
		return getValue(this.context.getString(resKey), defaultValue);
	}

	// String
	public String getValue(String key, String defaultValue) {
		return sp.getString(key, defaultValue);
	}

	public String getValue(int resKey, String defaultValue) {
		return getValue(this.context.getString(resKey), defaultValue);
	}

	// Delete
	public void remove(String key) {
		edit.remove(key);
		edit.commit();
	}

	public void clear() {
		edit.clear();
		edit.commit();
	}

	/**
	 * 鏄惁绗竴娆″惎鍔ㄥ簲鐢�
	 * 
	 * @param context
	 * @return
	 */
	public boolean isFirstStart(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			int curVersion = info.versionCode;
			int lastVersion = sp.getInt("version", 0);
			if (curVersion > lastVersion) {
				// 濡傛灉褰撳墠鐗堟湰澶т簬涓婃鐗堟湰锛岃鐗堟湰灞炰簬绗竴娆″惎鍔�
				// 灏嗗綋鍓嶇増鏈啓鍏reference涓紝鍒欎笅娆″惎鍔ㄧ殑鏃跺�欙紝鎹鍒ゆ柇锛屼笉鍐嶄负棣栨鍚姩
				return true;
			} else {
				return false;
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 鏄惁绗竴娆″畨瑁呭簲鐢�
	 * 
	 * @param context
	 * @return
	 */
	public boolean isFirstInstall(Context context) {
		int install = sp.getInt("first_install", 0);
		if (install == 0)
			return true;

		return false;
	}

	/**
	 * 搴旂敤宸插惎鍔�
	 * 
	 * @param context
	 */
	public void setStarted(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			int curVersion = info.versionCode;
			sp.edit().putInt("version", curVersion).commit();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 搴旂敤宸插畨瑁呭苟鍚姩
	 * 
	 * @param context
	 */
	public void setInstalled(Context context) {
		sp.edit().putInt("first_install", 1).commit();
	}

	

	/**
	 * 鏄惁闇�瑕佹敼鍙樻暟鎹�
	 * 
	 * @param context
	 * @param typeID
	 * @return
	 */
	public  boolean needChangeIndexContent(Context context, String openID) {
		
		String save = sp.getString(openID, "");
		String cur = getDateByNumber();
		if (save.equals(cur)) {
			// be the last statement in the method
			return false;
		}
		return true;
	}

	/**
	 * 淇濆瓨鏇存柊鏃ユ湡
	 * 
	 * @param context
	 * @param typeID
	 */
	public void saveChangeIndexContent(Context context, String openID) {
		
		String cur = getDateByNumber();
		sp.edit().putString(openID, cur).commit();
	}
	
	/**
	 * 璁板綍鏃ユ湡锛屽喅瀹氭槸鍚︽暟鎹槸鍚﹂渶瑕佹敼鍔�
	 * 
	 * @return
	 */
	public static String getDateByNumber() {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd",
				new Locale("zh"));
		String cur = s.format(new Date());
		return cur;
	}
}
