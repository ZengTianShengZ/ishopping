package com.dz4.ishop.frag;

import java.util.List;

import android.app.Application;
import android.app.DownloadManager.Query;
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
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.domain.Comment;
import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.listener.OnUserInfoChangeListener;
import com.dz4.ishop.ui.AboutActivity;
import com.dz4.ishop.ui.LoginActivity;
import com.dz4.ishop.ui.NewsActivity;
import com.dz4.ishop.ui.PersonalActivity;
import com.dz4.ishop.ui.PersonalCommentActivity;
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
public class MeFragment extends  BaseFragment implements OnClickListener,OnUserInfoChangeListener{
	private final String TAG ="MeFragment";
	private View rootView;
	
	private ImageView mIconView;
	private TextView mNickname;
	private TextView mSignature;
	private View mUserInfo; 
	
	private User mUser;
	private String nickname;
	private String signature;
	private String iconUrl;
	
	private IshopApplication appData;
	private View myPublish;
	private TextView publishCount;
	private TextView commentCount;
	private TextView focusCount;
	private View commentContent;
	private View publishView;
	private View commentView;
	private View focusView;
	private View aboutUsView;
	private View fouctUs;
	private String weiboUrl = "http://weibo.com/u/5815804076/home?wvr=5&uut=fin&from=reg";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return rootView;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView =getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_me, null);
	}
	@Override
	public void initView() {
		mIconView =(ImageView) rootView.findViewById(R.id.me_usericon);
		mNickname =(TextView) rootView.findViewById(R.id.me_username);
		mSignature =(TextView) rootView.findViewById(R.id.me_signature);
		mUserInfo =(View) rootView.findViewById(R.id.me_userinfo);
		myPublish=(View)rootView.findViewById(R.id.publish_content);
		commentContent=(View)rootView.findViewById(R.id.comment_content);
		
		publishCount=(TextView)rootView.findViewById(R.id.publish_count);
		commentCount=(TextView)rootView.findViewById(R.id.comment_count);
		focusCount=(TextView)rootView.findViewById(R.id.focus_count);
		
		publishView=(View)rootView.findViewById(R.id.publish);
		commentView=(View)rootView.findViewById(R.id.comment);
		focusView=(View)rootView.findViewById(R.id.focus);
		aboutUsView=(View)rootView.findViewById(R.id.about_us);
		fouctUs = rootView.findViewById(R.id.fouct_us);
		
	}

	@Override
	public void initData() {
		appData= ((IshopApplication)getActivity().getApplication());
		mUser = appData.getCurrentUser();
		if(mUser!=null){
			loadNetData();
			BmobFile file =mUser.getAvatar();
			if(file!=null){
				iconUrl = file.getFileUrl(getContext());	
			}
			nickname = mUser.getNickname();
			signature = mUser.getSignature();
			
			mNickname.setText(nickname);
			mSignature.setText(signature);
			
			ImageLoader.getInstance().displayImage(iconUrl, mIconView, ImageUtils.getOptions(R.drawable.user_icon_default_main));
		}else{

			mNickname.setText("未登录");
			mSignature.setText("您还没有登录！");
			mIconView.setImageDrawable(getResources().getDrawable(R.drawable.user_icon_default_main));
			commentCount.setText("0");
			publishCount.setText("0");
			focusCount.setText("0");
		}
		
	}
	
	private void loadNetData() {
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereEqualTo("user", mUser);
		query.count(getContext(), Comment.class, new CountListener() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				commentCount.setText("0");
			}
			
			@Override
			public void onSuccess(int arg0) {
				
				commentCount.setText(arg0+"");
				LogUtils.i(TAG,"focus_count"+arg0);
			}
		});
		
		BmobQuery<QiangItem> query1 = new BmobQuery<QiangItem>();
		query1.addWhereEqualTo("author", mUser);
		query1.count(getContext(), QiangItem.class, new CountListener() {
			@Override
			public void onFailure(int arg0, String arg1) {
				
				publishCount.setText("0");
			}
			@Override
			public void onSuccess(int arg0) {
				
				publishCount.setText(arg0+"");
				LogUtils.i(TAG,"qiang_count"+arg0);
			}
		});
		BmobQuery<User> query2 = new BmobQuery<User>();
		query2.addWhereRelatedTo("focus", new BmobPointer(mUser));
		query2.count(getContext(), User.class, new CountListener() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				focusCount.setText("0");
			}
			
			@Override
			public void onSuccess(int arg0) {
				
				focusCount.setText(arg0+"");
				LogUtils.i(TAG,"focus_count"+arg0);
			}
		});
	}
	@Override
	public void initEvent() {
		appData.setOnUserInfoChangeListener(this);
		mUserInfo.setOnClickListener(this);
		myPublish.setOnClickListener(this);
		commentContent.setOnClickListener(this);
		
		publishView.setOnClickListener(this);
		commentView.setOnClickListener(this);
		focusView.setOnClickListener(this);
		aboutUsView.setOnClickListener(this);
		fouctUs.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		LogUtils.i(TAG, "onClick");
		switch(v.getId()){
		case R.id.me_userinfo:
			if(appData.getloginState()){
				Intent intent = new Intent(getContext(),UserInfoActivity.class);
				startActivity(intent);
			}else{
				Intent intent = new Intent(getContext(),LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.publish_content:
			if(appData.getloginState()){
				Intent intent = new Intent(getContext(),PersonalActivity.class);
				intent.putExtra(Constant.BUNDLE_KEY_AUTHOR, mUser);
				startActivity(intent);
			}else{
				Intent intent = new Intent(getContext(),LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.comment_content:
			if(appData.getloginState()){
				Intent intent = new Intent(getContext(),PersonalCommentActivity.class);
				intent.putExtra(Constant.BUNDLE_KEY_AUTHOR, mUser);
				startActivity(intent);
			}else{
				Intent intent = new Intent(getContext(),LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.comment:
			if(mUser == null) return;
			Intent intent = new  Intent(getContext(),PersonalCommentActivity.class);
			intent.putExtra(Constant.BUNDLE_KEY_AUTHOR, mUser);
			startActivity(intent);
			break;
		case R.id.publish:
			if(mUser == null) return;
			Intent intent1 = new  Intent(getContext(),PersonalActivity.class);
			intent1.putExtra(Constant.BUNDLE_KEY_AUTHOR, mUser);
			startActivity(intent1);
			break;
		case R.id.focus:
			break;
		
		case R.id.about_us:
			Intent intent2 = new  Intent(getContext(),AboutActivity.class);
			startActivity(intent2);
			break;
		case R.id.fouct_us:
			Intent intent3 = new Intent(getContext(),NewsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("NewsUrl",weiboUrl);
			 
			intent3.putExtras(bundle);
			startActivity(intent3); 
			break;
		}
	}
		
	@Override
	public void OnUserInfoChange() {
		initData();
		LogUtils.i(TAG, "OnUserInfoChange");
	}
	@Override
	public void processHandlerMessage(Message msg) {
		
	}
}
