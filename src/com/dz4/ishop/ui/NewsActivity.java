package com.dz4.ishop.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;

public class NewsActivity  extends BaseUIActivity implements TopBar.onTopBarbtnclickListener{

	protected Context mContext;
	private TopBar mTopBar;
	private WebView webView;
	
	@Override
	public void rightbtnclick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leftbtnclick(View v) {
		finish();
		
	}

	@Override
	public void initView() {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_webview);
		this.mContext = getApplicationContext();
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.back));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		
		webView = (WebView) findViewById(R.id.news_webView);
		
	}

	@Override
	public void initData() {
		
        Bundle bundle  = getIntent().getExtras();
        String newsUrl = bundle.getString("NewsUrl");
        Log.i("NewsUrl", newsUrl);
        webView.loadUrl(newsUrl);
        
      //����Ӧ��Ļ
        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        
      //����֧��javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        
        webView.setWebViewClient(new WebViewClient(){
        	
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                   //����ֵ��true��ʱ�����ȥWebView�򿪣�Ϊfalse����ϵͳ�����������������
                 view.loadUrl(url);
                return true;
            }
        });
	}

	@Override
	public void initEvent() {
		mTopBar.setTopBarbtnclickListener(this);
	}

}