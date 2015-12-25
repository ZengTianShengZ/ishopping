package com.dz4.ishop.sns;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishop.utils.NetworkUtil;
import com.dz4.ishop.utils.Sputil;
import com.dz4.ishopping.R;
import com.tencent.connect.common.Constants;
import com.tencent.open.SocialConstants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.utils.HttpUtils.HttpStatusException;
import com.tencent.utils.HttpUtils.NetworkUnavailableException;

/**
 * @author kingofglory email: kingofglory@yeah.net blog: http:www.google.com
 * @date 2014-3-3 TODO 瀹炵幇QQ濂藉弸浠ュ強Qzone鍒嗕韩涓�閿畬鎴� usage:1.鎸塷pensdk璇存槑鏂囨。閰嶇疆濂紸ndroidManifest.xml 2.鍦ˊsee{TencentShareConstants}涓～鍐欑浉搴斿垎浜唴瀹�
 *       ,褰撹皟鐢ㄦ棤鍙傛暟鍒嗕韩鏂规硶鏃讹紝鍒氶噰鐢ㄨ鎺ュ彛涓粯璁ゅ～鍐欏唴瀹癸紝褰撹皟鐢ㄦ湁鍙傛暟鍒嗕韩鏃讹紝鍒氶噰鐢ㄤ紶鍏ュ弬鏁� 3.璋冪敤绀轰緥锛� TencentShare ts = new TencentShare(Activity,TencentShareEntity);
 *       绗簩涓弬鏁拌嫢涓虹┖锛屽垰閲囩敤绗簩姝ヤ腑濉啓榛樿鍙傛暟 ts.shareToQQ();//鏃犲弬鏁板垎浜埌QQ濂藉弸 ts.shareToQZone();//鍒嗕韩鍒癚Q绌洪棿
 */

public class TencentShare implements TencentShareConstants {

    public static final String TAG="TencentShare";

    public static final String SCOPE="get_simple_userinfo";

    private Activity mContext;

    private Tencent tencent;

    private TencentShareEntity shareEntity;

    private Sputil sputil;

    public TencentShare(Activity context, TencentShareEntity entity) {
        mContext=context;
        initTencent();
        shareEntity=entity;
        if(shareEntity == null) {
            shareEntity=
                new TencentShareEntity(TencentShareConstants.TITLE, TencentShareConstants.IMG_URL,
                    TencentShareConstants.TARGET_URL, TencentShareConstants.SUMMARY, TencentShareConstants.COMMENT);
        }
        sputil=new Sputil(mContext, Constant.PRE_NAME);
    }

    /**
     * 鍒濆鍖杢encent瀹炰緥
     */
    private void initTencent() {
        if(tencent == null) {
            tencent=Tencent.createInstance(getAppId(), mContext);
        }

    }

    /**
     * 浠嶢dminifest.xml閲岃鍙朼pp_id
     * @return
     */
    private String getAppId() {
        String appId="";
        try {
            ApplicationInfo appInfo=
                mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            appId=appInfo.metaData.getString("TA_APPKEY");
            // LogUtil.i(TAG,appId.substring(3));
        } catch(NameNotFoundException e) {
            e.printStackTrace();
        }
        return appId.substring(3);
    }

    /**
     * 妫�鏌ョ綉缁滃苟寮�濮嬪垎浜�
     */
    public void shareToQQ() {
        shareToQQ(shareEntity);
    }

    /**
     * 妫�鏌ョ綉缁滃苟寮�濮嬪垎浜�,鏀寔鍔ㄦ�佹敼鍙樺垎浜弬鏁�
     */
    private void shareToQQ(TencentShareEntity entity) {
        if(NetworkUtil.isAvailable(mContext)) {
            doShareToQQ(entity);
        } else {
           Toast.makeText(mContext, "网络无连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * QQ鍒嗕韩瀹為檯鎿嶄綔
     * @param title
     * @param imgUrl
     * @param targetUrl
     * @param summary
     */
    private void doShareToQQ(TencentShareEntity entity) {
        System.out.println(entity);
        Bundle params=new Bundle();
        params.putString(SocialConstants.PARAM_TITLE, entity.getTitle());
        params.putString(SocialConstants.PARAM_IMAGE_URL, entity.getImgUrl());
        params.putString(SocialConstants.PARAM_TARGET_URL, entity.getTargetUrl());
        params.putString(SocialConstants.PARAM_SUMMARY, entity.getSummary());
        params.putString(SocialConstants.PARAM_COMMENT, entity.getComment());
        params.putString(SocialConstants.PARAM_APPNAME, mContext.getResources().getString(R.string.app_name));
        initTencent();
        tencent.shareToQQ(mContext, params, new BaseUiListener(0));
    }

    /**
     * 妫�鏌ョ綉缁滅姸鎬佸苟寮�濮婹zone鍒嗕韩
     */
    public void shareToQZone() {
	Toast.makeText(mContext, "网络无连接", Toast.LENGTH_SHORT).show();
    }

    /**
     * 妫�鏌ョ綉缁滅姸鎬佸苟寮�濮婹zone鍒嗕韩锛屾敮鎸佸姩鎬佹敼鍙樺垎浜弬鏁�
     */
    private void shareToQZone(TencentShareEntity entity) {
        if(NetworkUtil.isAvailable(mContext)) {
            doShareToQZone(entity);
        } else {
        	Toast.makeText(mContext, "网络无连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 鍒嗕韩鍒癚Q绌洪棿锛屽疄闄呭垎浜搷浣�
     */
    private void doShareToQZone(TencentShareEntity entity) {
        if(ready()) {
            // send story
            sendStoryToQZone(entity);
        } else {
            // go to login
            tencent.login(mContext, SCOPE, new BaseUiListener(2));
        }
    }

    /**
     * 浠呬粎鏄粦瀹歈Q
     */
    public void bindQQ() {
        tencent.login(mContext, SCOPE, (IUiListener) new BaseUiListener(3));
    }

    public void unBindQQ() {
        sputil.remove("nick");
        loginOut();
    }

    /**
     * 鐧诲嚭
     */
    private void loginOut() {
        tencent.logout(mContext);
    }

    /**
     * 鏄惁缁戝畾QQ
     * @return
     */
    public boolean isBindQQ() {
        if(!sputil.getValue("nick", "").equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 妫�楠孮Q鏄惁鍦ㄧ櫥褰曠姸鎬�
     * @return
     */
    private boolean ready() {
        boolean ready=tencent.isSessionValid() && tencent.getOpenId() != null;
        return ready;
    }

    /**
     * 杩涘叆QZone鍒嗕韩锛屽疄闄呭垎浜搷浣�
     */
    private void sendStoryToQZone(TencentShareEntity entity) {
        Bundle params=new Bundle();

        params.putString(SocialConstants.PARAM_TITLE, entity.getTitle());
        params.putString(SocialConstants.PARAM_IMAGE, entity.getImgUrl());
        params.putString(SocialConstants.PARAM_SUMMARY, entity.getSummary());
        params.putString(SocialConstants.PARAM_TARGET_URL, entity.getTargetUrl());
        params.putString(SocialConstants.PARAM_COMMENT, entity.getComment());
        params.putString(SocialConstants.PARAM_ACT, "杩涘叆搴旂敤");
        tencent.story(mContext, params, new BaseUiListener(1));
    }

    private class BaseUiListener implements IUiListener {

        private int flag=-1;

        public BaseUiListener(int flag) {
            this.flag=flag;
        }

        @Override
        public void onError(UiError e) {

            LogUtils.i("QQ", "onError----" + "code:" + e.errorCode + ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            switch(flag) {
                case 0:
                    LogUtils.i(TAG, "share to qq complete!");
                    onShareToQQComplete();
                    break;
                case 1:
                    LogUtils.i(TAG, "share to qzone complete!");
                    onShareToQZoneComplete();
                    break;
                case 2:
                    LogUtils.i(TAG, "login complete and begin to story!");
                    doShareToQZone(shareEntity);
                    onQQLoginComplete();
                    break;
                case 3:
                    onQQLoginComplete();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 鐧婚寗瀹孮Q浠ュ悗鎯冲仛鐨勬搷浣滐紝姣斿鐛插彇QQ淇℃伅绛�
     */
    public void onQQLoginComplete() {
        if(ready()) {
            BaseApiListener requestListener=new BaseApiListener("get_simple_userinfo", false);
            Bundle params=new Bundle();
            if(tencent != null && tencent.isSessionValid()) {
                params.putString(Constants.PARAM_ACCESS_TOKEN, tencent.getAccessToken());
                params.putString(Constants.PARAM_CONSUMER_KEY, tencent.getAppId());
                params.putString(Constants.PARAM_OPEN_ID, tencent.getOpenId());
                params.putString("format", "json");
            }
            tencent.requestAsync("user/get_simple_userinfo", params, Constants.HTTP_GET, (IRequestListener) requestListener, null);
        }
    }

    /**
     * 濡傛灉QQ鍒嗕韩瀹屾垚鎯宠繕鏈夊叾浠栨搷浣滐紝璇烽噸鍐欒鏂规硶瀹炵幇
     */
    public void onShareToQQComplete() {

    }

    /**
     * 濡傛灉QZone鍒嗕韩瀹屾垚鎯宠繕鏈夊叾浠栨搷浣滐紝璇烽噸鍐欒鏂规硶瀹炵幇
     */
    public void onShareToQZoneComplete() {

    }

    private class BaseApiListener implements IRequestListener {

        private String mScope="all";

        private Boolean mNeedReAuth=false;

        public BaseApiListener(String scope, boolean needReAuth) {
            mScope=scope;
            mNeedReAuth=needReAuth;
        }

        @Override
        public void onComplete(JSONObject response) {
            // TODO Auto-generated method stub
            LogUtils.i("onComplete:", response.toString());
            doComplete(response);
        }

        protected void doComplete(JSONObject response) {
            try {
                int ret=response.getInt("ret");
                if(ret == 100030) {
                    if(mNeedReAuth) {
                        Runnable r=new Runnable() {

                            public void run() {
                                tencent.reAuth(mContext, mScope, new BaseUiListener(-1));
                            }
                        };
                        mContext.runOnUiThread(r);
                    }
                } else if(ret == 0) {
                    String nick=response.getString("nickname");
                    sputil.setValue("nick", nick);
                }
            } catch(JSONException e) {
                e.printStackTrace();
                Log.e("toddtest", response.toString());
            }

        }

        @Override
        public void onIOException(final IOException e) {
            LogUtils.i("IRequestListener.onIOException:", e.getMessage());
        }

        @Override
        public void onMalformedURLException(final MalformedURLException e) {
        	LogUtils.i("IRequestListener.onMalformedURLException", e.toString());
        }

        @Override
        public void onJSONException(final JSONException e) {
        	LogUtils.i("IRequestListener.onJSONException:", e.getMessage());
        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException arg0) {
        	LogUtils.i("IRequestListener.onConnectTimeoutException:", arg0.getMessage());

        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException arg0) {
        	LogUtils.i("IRequestListener.SocketTimeoutException:", arg0.getMessage());
        }

        @Override
        public void onUnknowException(Exception arg0) {
        	LogUtils.i("IRequestListener.onUnknowException:", arg0.getMessage());
        }

		@Override
		public void onHttpStatusException(HttpStatusException arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onNetworkUnavailableException(
				NetworkUnavailableException arg0) {
			// TODO Auto-generated method stub
			
		}

       

    }

}
