package com.dz4.ishop.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.dz4.ishop.adapter.CommentAdapter;
import com.dz4.ishop.adapter.PersonalCommentAdapter;
import com.dz4.ishop.adapter.PersonalQiangListAdapter;
import com.dz4.ishop.adapter.QiangListAdapter;
import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.domain.Comment;
import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishop.view.TopBar;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class PersonalCommentActivity extends BaseUIActivity implements TopBar.onTopBarbtnclickListener{
 
	private View mPersonInfoView;
	
	private TopBar mTopBar;
	private ImageView personalIcon;
	private TextView personalName;
	private TextView personalSign;

	private ImageView goSettings;
	private TextView personalTitle;
	private PullToRefreshListView mPullToRefreshListView;
 
	private ListView mListView;
	private ArrayList<Comment> mComments;
	private PersonalCommentAdapter personalCommentAdapter;
	
	private User mUser;
	protected Context mContext;
	 
	public enum RefreshType {
		REFRESH, LOAD_MORE
	}

	private RefreshType mRefreshType = RefreshType.LOAD_MORE;
	private int pageNum;
	final int NUMBERS_PER_PAGE = 15;
	@Override
	public void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personal);
		this.mContext = getApplicationContext();
		mTopBar = (TopBar) this.findViewById(R.id.topbar);
		mTopBar.setLeftButtonImage(getResources().getDrawable(R.drawable.back));
		mTopBar.setLeftButtonVisible(View.VISIBLE);
		
		 
		mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list_personal);
		mListView = mPullToRefreshListView.getRefreshableView();
		
		mPersonInfoView = getLayoutInflater().inflate(R.layout.personal_info, mListView, false);
	
		personalIcon = (ImageView) mPersonInfoView.findViewById(R.id.personal_icon);
		personalName = (TextView) mPersonInfoView.findViewById(R.id.personl_name);
		personalSign = (TextView) mPersonInfoView.findViewById(R.id.personl_signature);
		goSettings = (ImageView) mPersonInfoView.findViewById(R.id.go_settings);
		personalTitle = (TextView) mPersonInfoView.findViewById(R.id.personl_title);
	}
 
	@Override
	public void initData() {
		mUser = (User)getIntent().getSerializableExtra(Constant.BUNDLE_KEY_AUTHOR);
		//更新个人信息
		updatePersonalInfo(mUser);
		//初始化 标题
		initPersonalTitle();
		// 初始化 个人 发表过的信息
		fetchData();
	}
	
	private void initPersonalTitle() {
		
		if (isCurrentUser(mUser)) {
			personalTitle.setText("我评论过的");
			goSettings.setVisibility(View.VISIBLE);
			User user = BmobUser.getCurrentUser(mContext, User.class);
			updatePersonalInfo(user);
		} else {
			goSettings.setVisibility(View.GONE);
			personalTitle.setText("Ta 发表过的");
		}
		
		mPullToRefreshListView.setMode(Mode.BOTH);
		mPullToRefreshListView.setOnScrollListener(new AbsListView.OnScrollListener(){
			 @Override
	            public void onScrollStateChanged(AbsListView view, int scrollState) {
	            }

	            @Override
	            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	            	 
	            	//这样是得不到  ScrollY 的 得到 1.1.1.1.1.1
	            	Log.i("getScrollY",mPullToRefreshListView.getScrollY()+"..."); 
	            	//自己定义得到 ScrollY
	            	Log.i("getScrollY",MygetScrollY()+"..."+mPersonInfoView.getHeight()); 
	              
	            	if(MygetScrollY()>=810){
	            		mTopBar.setTitleText(mUser.getUsername());
	            		mTopBar.setTitleSize(20);
	            	}else{
	            		mTopBar.setTitleText("");
	            	}
	            }
	          
		});
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						// 得到当前时间  format 格式化的
						String label = DateUtils.formatDateTime(mContext,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						mRefreshType = RefreshType.REFRESH;
						pageNum = 0;
						fetchData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						mRefreshType = RefreshType.LOAD_MORE;
						fetchData();
					}
				});
		//mListView 点击用户发表过的 Item
		 
		//mListView.addHeaderView(mPlaceHolderView);
		//mListView = mPullToRefreshListView.getRefreshableView();
		mListView.addHeaderView(mPersonInfoView);
		mComments = new ArrayList<Comment>();
		personalCommentAdapter =new PersonalCommentAdapter(this,mComments,mUser);
		mListView.setAdapter(personalCommentAdapter);
	}
	private void updatePersonalInfo(User mUser2) {
		
		personalName.setText(mUser2.getNickname());
		personalSign.setText(mUser2.getSignature());
		if (mUser2.getAvatar() != null) {
			// 设置 个人 头像 ，采用开源的 图片加载程序
			ImageLoader.getInstance().displayImage(
					mUser2.getAvatar().getFileUrl(mContext),
					personalIcon,
					ImageUtils.getOptions(R.drawable.user_icon_default_main),
					new SimpleImageLoadingListener() {

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
						  
						}

					});
		}		 
		
	}
	private boolean isCurrentUser(User mUser2) {
		if (null != mUser2) {
			// 得到 当前用户 ： “我”
			User cUser = BmobUser.getCurrentUser(mContext, User.class);
			// 判断 当前点击的 Item 是否是 当前用户 “我”
			if (cUser != null && cUser.getObjectId().equals(mUser2.getObjectId())) {
				return true;
			}
		}
		return false;
	}
	
	protected void fetchData() {
		getPublishion();
		
	}
	private void getPublishion() {
		//mIProgressControllor.showActionBarProgress();
		//筛选 用户信息、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、//
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.setLimit(NUMBERS_PER_PAGE); // 限制 15条 消息
		query.setSkip(NUMBERS_PER_PAGE* (pageNum++));
		query.order("-createdAt");
		query.addWhereEqualTo("user", mUser);
		query.include("qiang.author");
		query.findObjects(mContext, new FindListener<Comment>() {
			@Override
			public void onSuccess(List<Comment> data) {
				// TODO Auto-generated method stub
				//mIProgressControllor.hideActionBarProgress();
				if (data.size() != 0 && data.get(data.size() - 1) != null) {
					if (mRefreshType == RefreshType.REFRESH) {
						// 如果是向上刷新，则先清除掉当前数据
						mComments.clear();
					}
					if (data.size() < NUMBERS_PER_PAGE) {
						showToast("已加载完所有数据~");
					}

					mComments.addAll(data);
					//更新数据
					personalCommentAdapter.notifyDataSetChanged();
					//刷新完成
					mPullToRefreshListView.onRefreshComplete();
					
				} else {
					showToast("暂无更多数据~");
					pageNum--;
					mPullToRefreshListView.onRefreshComplete();
				}
			}

			@Override
			public void onError(int arg0, String msg) {
				// TODO Auto-generated method stub
				//mIProgressControllor.hideActionBarProgress();
				//LogUtils.i(TAG, "find failed." + msg);
				pageNum--;
				mPullToRefreshListView.onRefreshComplete();
			}
		});
	}
	@Override
	public void initEvent() {
		mTopBar.setTopBarbtnclickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
//				Intent intent = new Intent(mContext,GoodsDetailActivity.class);
//				intent.putExtra(Constant.BUNDLE_KEY_QIANGITEM, mComments.get(position-2));
//				startActivity(intent);
			}
		});
	}
	
	@Override
	public void rightbtnclick(View v) {
		 
	}
	@Override
	public void leftbtnclick(View v) {
		Log.i("rightbtnclick"," rightbtnclick");
		finish();
	}
 
	public int MygetScrollY() {
	    View c = mListView.getChildAt(0);
	    if (c == null) {
	        return 0;
	    }
	    int firstVisiblePosition = mListView.getFirstVisiblePosition();
	    int top = c.getTop();  // 得到移除去的 高度
	    Log.i("getTop",top+"..."+firstVisiblePosition+"..."+ c.getHeight());
	   // Log.i("firstVisiblePosition",firstVisiblePosition+"...");
	    // top  5..4..2..0..-1..-3..-5..-10..-9..-4..-1..1..4..5
	    return -top + firstVisiblePosition * c.getHeight() ;
	}

 
 
}
