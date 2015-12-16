package com.dz4.ishop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
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
import com.dz4.ishop.view.BottomTagView;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.FragmentBaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * 
 * @author MZone
 *
 */
public class MainActivity extends FragmentBaseActivity implements TitlechangeListener,TopBar.onTopBarbtnclickListener{
	
	private BottomTagView homeBottomTag;
	private BottomTagView chatBottomTag;
	private BottomTagView meBottomTag;
	
	private HomeFragment homeFragment;
	private NewsFragment chatFragment;
	private MeFragment meFragment;
	
	private FragmentManager  mFragmentManager ;
	private FragmentTransaction mTransaction;
	
	private TopBar mTopBar;
	
	private User user;
	
	private boolean meflag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		setSelected(0);
		homeBottomTag.setIconAlpha(1);
		
	}

	@Override
	public void processHandlerMessage(Message msg) {
		
	}

	public void initView() {
		homeBottomTag= (BottomTagView) findViewById(R.id.BottomTag_home);
		chatBottomTag= (BottomTagView) findViewById(R.id.BottomTag_chat);
		meBottomTag= (BottomTagView) findViewById(R.id.BottomTag_me);
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setTopBarbtnclickListener(this);
		mTopBar.setRightButtonImage(getResources().getDrawable(R.drawable.ic_action_edit));
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.avatar_default_circle));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		mTopBar.setRightButtonVisible(View.VISIBLE);
	}
	public void initData() {
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		user=((IshopApplication)getApplication()).getCurrentUser();
		//showToast("resume");
		islogin(user);
	}
	private void islogin(User user) {
		if(user!=null){
			if(user.getAvatar()!=null){
				ImageLoader.getInstance().displayImage(user.getAvatar().getFileUrl(getApplicationContext()), mTopBar.getLeftButton(),ImageUtils.getOptions(R.drawable.user_icon_default),new SimpleImageLoadingListener(){
					public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
						//mTopBar.setLeftButtonImage();
					};
				} );
			}
			
		}else{
			mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.avatar_default_circle));
		}
	}

	public void onTagClick(View v) {
		resetTag();
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.BottomTag_home:
			homeBottomTag.setIconAlpha(1);
			setSelected(0);
			break;
		case R.id.BottomTag_chat:
			chatBottomTag.setIconAlpha(1);
			setSelected(1);
			break;
		case R.id.BottomTag_me:
			meBottomTag.setIconAlpha(1);
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
			if(homeFragment==null){
				homeFragment =new HomeFragment(this);
				mTransaction.add(R.id.fragment_zone, homeFragment);
			}else{
				mTransaction.show(homeFragment);
			}
			changePage(homeFragment.getCurrentPage());
			switch(homeFragment.getCurrentPage()){
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
			meflag=false;
			break;
		case 1:
			if(chatFragment==null){
				chatFragment =new NewsFragment();
				mTransaction.add(R.id.fragment_zone, chatFragment);
			}else{
				mTransaction.show(chatFragment);
			}
			changeTitle(R.string.title_hot);
			mTopBar.setLeftButtonVisible(View.VISIBLE);
			mTopBar.setRightButtonVisible(View.VISIBLE);
			mTopBar.setRightButtonImage(getResources().getDrawable(R.drawable.ic_action_edit));
			meflag=false;
			break;
		case 2:
			if(meFragment==null){
				meFragment =new MeFragment();
				mTransaction.add(R.id.fragment_zone, meFragment);
			}else{
				mTransaction.show(meFragment);
			}
			changeTitle(R.string.title_me);
			mTopBar.setLeftButtonVisible(View.GONE);
			mTopBar.setRightButtonVisible(View.VISIBLE);
			mTopBar.setRightButtonImage(getResources().getDrawable(R.drawable.ic_action_setting));
			meflag=true;
			break;
			
		default:
			break;
		}
		mTransaction.commit();
	}

	private void hideFragment(FragmentTransaction mTransaction) {
		changePage(100);
		if(homeFragment!=null){
			mTransaction.hide(homeFragment);
		}
		if(chatFragment!=null){
			mTransaction.hide(chatFragment);
		}
		if(meFragment!=null){
			mTransaction.hide(meFragment);
		}
		
	}

	protected void resetTag(){
		homeBottomTag.setIconAlpha(0);
		meBottomTag.setIconAlpha(0);
		chatBottomTag.setIconAlpha(0);
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
		// TODO 自动生成的方法存根
		if(meflag!=true){
			if(user!=null){
				Intent intent = new Intent(this,EditQiangActivity.class);
				startActivity(intent);
			}
			else{
				Intent intent = new Intent(this,LoginActivity.class);
				startActivity(intent);
			}
		}else{
			showToast("setting");
		}
	}

	@Override
	public void leftbtnclick(View v) {
		// TODO 自动生成的方法存根
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
