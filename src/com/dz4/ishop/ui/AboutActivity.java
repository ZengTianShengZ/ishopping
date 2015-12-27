package com.dz4.ishop.ui;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseActivity;
/**
 * 
 * 关于我们
 * @author MZone
 *
 */
public class AboutActivity extends  BaseActivity implements TopBar.onTopBarbtnclickListener{
	private TopBar mTopBar;
	private TextView versionView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);
		
		versionView=(TextView)findViewById(R.id.version_name);
		versionView.setText("version "+getAppVersionName());
		mTopBar=(TopBar)findViewById(R.id.topbar);
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		mTopBar.setTitleText("关于");
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.back));
		mTopBar.setTopBarbtnclickListener(this);
		
	}
	public String getAppVersionName() {  
		    String versionName = "";  
		    try {  
		        // ---get the package info---  
		        PackageManager pm = getPackageManager();  
		        PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);  
		        versionName = pi.versionName;  
		        if (versionName == null || versionName.length() <= 0) {  
		            return "";  
		        }  
		    } catch (Exception e) {  
		        LogUtils.e("VersionInfo", "Exception:"+e);  
		    }  
		    return versionName;  
		}  
	@Override
	public void rightbtnclick(View v) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void leftbtnclick(View v) {
		// TODO 自动生成的方法存根
		finish();
	}


}
