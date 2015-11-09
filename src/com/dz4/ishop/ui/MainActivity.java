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
import com.dz4.ishop.frag.Fragment_chat;
import com.dz4.ishop.frag.Fragment_home;
import com.dz4.ishop.frag.Fragment_me;
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
	
	private BottomTagView BottomTag_home;
	private BottomTagView BottomTag_chat;
	private BottomTagView BottomTag_me;
	
	private Fragment_home mFragment_home;
	private Fragment_chat mFragment_chat;
	private Fragment_me mFragment_me;
	
	private FragmentManager  mFragmentManager ;
	private FragmentTransaction mTransaction;
	
	private TopBar mTopBar;
	private View page1;
	private View page2;
	
	private User user;
	
	private boolean me_flag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		setSelected(0);
		BottomTag_home.setIconAlpha(1);
		
	}

	@Override
	public void processHandlerMessage(Message msg) {
		// TODO 自动生成的方法存根
		
	}

	public void initView() {
		// TODO 自动生成的方法存根
		BottomTag_home= (BottomTagView) findViewById(R.id.BottomTag_home);
		BottomTag_chat= (BottomTagView) findViewById(R.id.BottomTag_chat);
		BottomTag_me= (BottomTagView) findViewById(R.id.BottomTag_me);
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setTopBarbtnclickListener(this);
		page1=this.findViewById(R.id.page1);
		page2=this.findViewById(R.id.page2);
		mTopBar.setRightButtonImage(getResources().getDrawable(R.drawable.ic_action_edit));
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.avatar_default_circle));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		mTopBar.setRightButtonVisible(View.VISIBLE);
		
	}
	public void initData() {
		// TODO 自动生成的方法存根
		
	}
	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		user=((IshopApplication)getApplication()).getCurrentUser();
		//showToast("resume");
		islogin(user);
	}
	private void islogin(User user) {
		// TODO 自动生成的方法存根
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
			BottomTag_home.setIconAlpha(1);
			setSelected(0);
			break;
		case R.id.BottomTag_chat:
			BottomTag_chat.setIconAlpha(1);
			setSelected(1);
			break;
		case R.id.BottomTag_me:
			BottomTag_me.setIconAlpha(1);
			setSelected(2);	
				break;
		default:
			break;
		}
	}
	private void setSelected(int index) {
		// TODO 自动生成的方法存根
		mFragmentManager =getSupportFragmentManager();
		mTransaction = mFragmentManager.beginTransaction();
		hideFragment(mTransaction);
		switch(index){
		case 0:
			if(mFragment_home==null){
				mFragment_home =new Fragment_home(this);
				mTransaction.add(R.id.fragment_zone, mFragment_home);
			}else{
				mTransaction.show(mFragment_home);
			}
			changePage(mFragment_home.getCurrentPage());
			switch(mFragment_home.getCurrentPage()){
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
			me_flag=false;
			break;
		case 1:
			if(mFragment_chat==null){
				mFragment_chat =new Fragment_chat();
				mTransaction.add(R.id.fragment_zone, mFragment_chat);
			}else{
				mTransaction.show(mFragment_chat);
			}
			changeTitle(R.string.title_chat);
			mTopBar.setLeftButtonVisible(View.VISIBLE);
			mTopBar.setRightButtonVisible(View.VISIBLE);
			mTopBar.setRightButtonImage(getResources().getDrawable(R.drawable.ic_action_edit));
			me_flag=false;
			break;
		case 2:
			if(mFragment_me==null){
				mFragment_me =new Fragment_me();
				mTransaction.add(R.id.fragment_zone, mFragment_me);
			}else{
				mTransaction.show(mFragment_me);
			}
			changeTitle(R.string.title_me);
			mTopBar.setLeftButtonVisible(View.GONE);
			mTopBar.setRightButtonVisible(View.VISIBLE);
			mTopBar.setRightButtonImage(getResources().getDrawable(R.drawable.ic_action_setting));
			me_flag=true;
			break;
			
		default:
			break;
		}
		mTransaction.commit();
	}

	private void hideFragment(FragmentTransaction mTransaction) {
		// TODO 自动生成的方法存根
		changePage(100);
		if(mFragment_home!=null){
			mTransaction.hide(mFragment_home);
		}
		if(mFragment_chat!=null){
			mTransaction.hide(mFragment_chat);
		}
		if(mFragment_me!=null){
			mTransaction.hide(mFragment_me);
		}
		
	}

	protected void resetTag(){
		BottomTag_home.setIconAlpha(0);
		BottomTag_me.setIconAlpha(0);
		BottomTag_chat.setIconAlpha(0);
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
		// TODO 自动生成的方法存根
		switch(page){
		case 0:
			page1.setVisibility(View.VISIBLE);
			page2.setVisibility(View.INVISIBLE);
			break;
		case 1:
			page1.setVisibility(View.INVISIBLE);
			page2.setVisibility(View.VISIBLE);
			break;
		default:
			page1.setVisibility(View.INVISIBLE);
			page2.setVisibility(View.INVISIBLE);
			break;
		}
		
	}

	@Override
	public void rightbtnclick(View v) {
		// TODO 自动生成的方法存根
		if(me_flag!=true){
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
