package com.dz4.ishop.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;

public class NewsActivity  extends BaseUIActivity implements TopBar.onTopBarbtnclickListener{

	protected Context mContext;
	private TopBar mTopBar;
	private WebView mWebView;
	private ProgressBar pb;
	
	@Override
	public void rightbtnclick(View v) {
	}

	@Override
	public void leftbtnclick(View v) {
		if(mWebView.canGoBack()){
			mWebView.goBack();
		}else{
			finish();
		}
		 
	}

	@Override
	public void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_webview);
		mContext = getApplicationContext();
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.back));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		mTopBar.setBackgroundColor(Color.WHITE);
		
		pb = (ProgressBar) findViewById(R.id.webViewProgressBar);
		mWebView = (WebView) findViewById(R.id.news_webView);
	}

	@Override
	public void initData() {
		
        Bundle bundle  = getIntent().getExtras();
        String newsUrl = bundle.getString("NewsUrl");
        LogUtils.i("NewsUrl", newsUrl);
        mWebView.loadUrl(newsUrl);
        
      //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        
      //启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        
        mWebView.setWebViewClient(new WebViewClient(){
        	
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                   //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                 view.loadUrl(url);
                return true;
            }
        });
        
        mWebView.setWebChromeClient(new WebChromeClient(){
        	
        	 @Override
             public void onProgressChanged(WebView view, int newProgress) {
                 if (newProgress == 100) {
                     pb.setVisibility(View.INVISIBLE);
                 } else {
                     if (View.INVISIBLE == pb.getVisibility()) {
                         pb.setVisibility(View.VISIBLE);
                     }
                     pb.setProgress(newProgress);
                 }
                 super.onProgressChanged(view, newProgress);
             }
        });
	}

	@Override
	public void initEvent() {
		mTopBar.setTopBarbtnclickListener(this);
		       
/*		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if ((keyCode == KEYCODE_BACK) && mWebView.canGoBack()) { 
				mWebView.goBack();
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}*/
	}
	 

}
