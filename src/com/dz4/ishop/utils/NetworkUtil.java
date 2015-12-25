package com.dz4.ishop.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * @author  缃戠粶宸ュ叿绫�
 */
public class NetworkUtil{

	/**
	 * 杩斿洖缃戠粶鏄惁鍙敤銆傞渶瑕佹潈闄愶細
	 * <p>
	 * <b> < uses-permission
	 * android:name="android.permission.ACCESS_NETWORK_STATE" /> </b>
	 * </p>
	 * 
	 * @param context
	 *            涓婁笅鏂�
	 * @return 缃戠粶鍙敤鍒欒繑鍥瀟rue锛屽惁鍒欒繑鍥瀎alse
	 */
	public static boolean isAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null && info.isAvailable();
	}

	/**
	 * 鍒ゆ柇缃戠粶杩炴帴鐘舵��
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetType(Context context) {

		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {

				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {

					if (info.getState() == NetworkInfo.State.CONNECTED) {
						if (info.getType() == ConnectivityManager.TYPE_WIFI) {
							// wifi
							return Constant.NETWORK_TYPE_WIFI;
						} else {
							// 鎵嬫満缃戠粶
							return Constant.NETWORK_TYPE_MOBILE;
						}
					}
				}
			}
		} catch (Exception e) {
			// 缃戠粶閿欒
			return Constant.NETWORK_TYPE_ERROR;
		}
		// 缃戠粶閿欒
		return Constant.NETWORK_TYPE_ERROR;

	}

	/**
	 * 杩斿洖Wifi鏄惁鍚敤
	 * 
	 * @param context
	 *            涓婁笅鏂�
	 * @return Wifi缃戠粶鍙敤鍒欒繑鍥瀟rue锛屽惁鍒欒繑鍥瀎alse
	 */
	public static boolean isWIFIActivate(Context context) {
		return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
				.isWifiEnabled();
	}

	/**
	 * 淇敼Wifi鐘舵��
	 * 
	 * @param context
	 *            涓婁笅鏂�
	 * @param status
	 *            true涓哄紑鍚疻ifi锛宖alse涓哄叧闂璚ifi
	 */
	public static void changeWIFIStatus(Context context, boolean status) {
		((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
				.setWifiEnabled(status);
	}
}
