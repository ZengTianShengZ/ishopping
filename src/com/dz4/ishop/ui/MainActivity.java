package com.dz4.ishop.ui;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import cn.bmob.v3.Bmob;

import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.frag.NewsFragment;
import com.dz4.ishop.frag.HomeFragment;
import com.dz4.ishop.frag.MeFragment;
import com.dz4.ishop.listener.TitlechangeListener;
import com.dz4.ishop.proxy.UserProxy;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishop.view.BottomTagView;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.FragmentBaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * 
 * @author MZone
 *
 */
public class MainActivity extends FragmentBaseActivity implements TitlechangeListener,TopBar.onTopBarbtnclickListener{
	private final String TAG = "MainActivity";
	
	private BottomTagView mHomeBottomTag;
	private BottomTagView mChatBottomTag;
	private BottomTagView mMeBottomTag;
	
	private HomeFragment mHomeFragment;
	private NewsFragment mNewsFragment;
	private MeFragment mMeFragment;
	
	private FragmentManager  mFragmentManager ;
	private FragmentTransaction mTransaction;
	
	private TopBar mTopBar;
	
	private User mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		setSelected(0);
		mHomeBottomTag.setIconAlpha(1);
		UmengUpdateAgent.update(this);
		initEvent();
	}

	@Override
	public void processHandlerMessage(Message msg) {
		
	}

	public void initView() {
		mHomeBottomTag= (BottomTagView) findViewById(R.id.BottomTag_home);
		mChatBottomTag= (BottomTagView) findViewById(R.id.BottomTag_chat);
		mMeBottomTag= (BottomTagView) findViewById(R.id.BottomTag_me);
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setTopBarbtnclickListener(this);
		mTopBar.setRightButtonImage(getResources().getDrawable(R.drawable.ic_action_edit));
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.avatar_default_circle));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		mTopBar.setRightButtonVisible(View.VISIBLE);
	}
	public void initEvent() {
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
			@Override
			    public void onClick(int status) {
			        switch (status) {
			        case UpdateStatus.Update:
			        	LogUtils.i(TAG,"user choose update");
			            break;
			        case UpdateStatus.Ignore:
			        	LogUtils.i(TAG,"user choose ignore");
			            break;
			        case UpdateStatus.NotNow:
			        	LogUtils.i(TAG,"user choose NotNow");
			            break;
			        }
			    }
			});
	}
	@Override
	protected void onResume() {
		super.onResume();
		mUser=((IshopApplication)getApplication()).getCurrentUser();
		islogin(mUser);
	}
	private void islogin(User user) {
		if(user!=null){
			if(user.getAvatar()!=null){
				ImageLoader.getInstance().displayImage(user.getAvatar().getFileUrl(getApplicationContext()), mTopBar.getLeftButton(),ImageUtils.getOptions(R.drawable.user_icon_default),new SimpleImageLoadingListener(){
					public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
					};
				} );
			}
			
		}else{
			mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.avatar_default_circle));
		}
	}

	public void onTagClick(View v) {
		resetTag();
		switch (v.getId()) {
		case R.id.BottomTag_home:
			mHomeBottomTag.setIconAlpha(1);
			setSelected(0);
			break;
		case R.id.BottomTag_chat:
			mChatBottomTag.setIconAlpha(1);
			setSelected(1);
			break;
		case R.id.BottomTag_me:
			mMeBottomTag.setIconAlpha(1);
			setSelected(2);	
				break;
		default:
			break;
		}
	}
	private void setSelected(int index) {
		mFragmentManager =getSupportFragmentManager();
		mTransaction = mFragmentManager.beginTransaction();
		hideFragment(mTransaction);
		switch(index){
		case 0:
			if(mHomeFragment==null){
				mHomeFragment =new HomeFragment(this);
				mTransaction.add(R.id.fragment_zone, mHomeFragment);
			}else{
				mTransaction.show(mHomeFragment);
			}
			changePage(mHomeFragment.getCurrentPage());
			switch(mHomeFragment.getCurrentPage()){
				case 0:
					changeTitle(R.string.title_qiang_all);
					break;
				case 1:
					changeTitle(R.string.title_qiang_focus);
					break;
				case 2:
					changeTitle(R.string.title_qiang_fav);
					break;
			
			}
			mTopBar.setLeftButtonVisible(View.VISIBLE);
			mTopBar.setRightButtonVisible(View.VISIBLE);
			mTopBar.setRightButtonImage(getResources().getDrawable(R.drawable.ic_action_edit));
			break;
		case 1:
			if(mNewsFragment==null){
				mNewsFragment =new NewsFragment();
				mTransaction.add(R.id.fragment_zone, mNewsFragment);
			}else{
				mTransaction.show(mNewsFragment);
			}
			changeTitle(R.string.title_hot);
			mTopBar.setLeftButtonVisible(View.VISIBLE);
			mTopBar.setRightButtonVisible(View.VISIBLE);
			mTopBar.setRightButtonImage(getResources().getDrawable(R.drawable.ic_action_edit));
			break;
		case 2:
			if(mMeFragment==null){
				mMeFragment =new MeFragment();
				mTransaction.add(R.id.fragment_zone, mMeFragment);
			}else{
				mTransaction.show(mMeFragment);
			}
			changeTitle(R.string.title_me);
			mTopBar.setLeftButtonVisible(View.GONE);
			mTopBar.setRightButtonVisible(View.GONE);
			break;
			
		default:
			break;
		}
		mTransaction.commit();
	}

	private void hideFragment(FragmentTransaction mTransaction) {
		changePage(100);
		if(mHomeFragment!=null){
			mTransaction.hide(mHomeFragment);
		}
		if(mNewsFragment!=null){
			mTransaction.hide(mNewsFragment);
		}
		if(mMeFragment!=null){
			mTransaction.hide(mMeFragment);
		}
		
	}

	protected void resetTag(){
		mHomeBottomTag.setIconAlpha(0);
		mMeBottomTag.setIconAlpha(0);
		mChatBottomTag.setIconAlpha(0);
	}

	@Override
	public void changeTitle(String title) {
		mTopBar.setTitleText(title);
	}
	@Override
	public void changeTitle(int titleRes) {
		mTopBar.setTitleText(getString(titleRes));
	}

	@Override
	public void changePage(int page) {
		
	}

	@Override
	public void rightbtnclick(View v) {
		if(mUser!=null){
			Intent intent = new Intent(this,EditQiangActivity.class);
			startActivity(intent);
		}
		else{
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void leftbtnclick(View v) {
		User user=((IshopApplication)getApplication()).getCurrentUser();
		if(user==null){
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent(this,UserInfoActivity.class);
			startActivity(intent);
		}
	}
}
