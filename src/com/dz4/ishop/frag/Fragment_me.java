package com.dz4.ishop.frag;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.push.lib.util.LogUtil;
import cn.bmob.v3.datatype.BmobFile;

import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.ui.LoginActivity;
import com.dz4.ishop.ui.PersonalActivity;
import com.dz4.ishop.ui.UserInfoActivity;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishopping.R;
import com.dz4.support.domain.Result;
import com.dz4.support.widget.BaseFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 
 * 我的信息的fragment
 * @author MZone
 *
 */
public class Fragment_me extends  BaseFragment implements OnClickListener{
	private final String TAG ="Fragment_me";
	private View rootview;
	
	private ImageView mIconView;
	private TextView mUsername;
	private TextView mSignature;
	private View mUser_info; 
	
	private User mUser;
	private String username;
	private String signature;
	private String iconurl;
	
	private IshopApplication appdata;
	private View myPublish;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return rootview;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootview =getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_me, null);
	}
	@Override
	public void processHandlerMessage(Message msg) {
		// TODO 自动生成的方法存根
		switch(msg.what){
		case Constant.MSG_LOGIN_CHANGE:
			initData();
			break;
		}
	}

	@Override
	public void initView() {
		// TODO 自动生成的方法存根
		mIconView =(ImageView) rootview.findViewById(R.id.me_usericon);
		mUsername =(TextView) rootview.findViewById(R.id.me_username);
		mSignature =(TextView) rootview.findViewById(R.id.me_signature);
		mUser_info =(View) rootview.findViewById(R.id.me_userinfo);
		myPublish=(View)rootview.findViewById(R.id.publish_content);
		
	}

	@Override
	public void initData() {
		// TODO 自动生成的方法存根
		appdata= ((IshopApplication)getActivity().getApplication());
		appdata.setHandler(getHandler());
		mUser = appdata.getCurrentUser();
		if(mUser!=null){
			BmobFile file =mUser.getAvatar();
			if(file!=null){
				iconurl = file.getFileUrl(getContext());	
			}
			username = mUser.getUsername();
			signature = mUser.getSignature();
			
			mUsername.setText(username);
			mSignature.setText(signature);
			
			ImageLoader.getInstance().displayImage(iconurl, mIconView, ImageUtils.getOptions(R.drawable.user_icon_default_main));
		}else{

			mUsername.setText("未登录");
			mSignature.setText("他很懒，什么都没有留下!");
			mIconView.setImageDrawable(getResources().getDrawable(R.drawable.user_icon_default_main));
		}
	}
	
	@Override
	public void initEvent() {
		// TODO 自动生成的方法存根
		mUser_info.setOnClickListener(this);
		myPublish.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		LogUtils.i(TAG, "onClick");
		switch(v.getId()){
		case R.id.me_userinfo:
			if(appdata.getloginState()){
				Intent intent = new Intent(getContext(),UserInfoActivity.class);
				//Bundle bundle =new Bundle();
				//bundle携带信息。。
				//intent.putExtra("userinfo", bundle);
				startActivity(intent);
			}else{
				Intent intent = new Intent(getContext(),LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.publish_content:
			if(appdata.getloginState()){
				Intent intent = new Intent(getContext(),PersonalActivity.class);
				intent.putExtra(Constant.BUNDLE_KEY_AUTHOR, mUser);
				startActivity(intent);
			}else{
				Intent intent = new Intent(getContext(),LoginActivity.class);
				startActivity(intent);
			}
			break;
		}
		
	}
}
