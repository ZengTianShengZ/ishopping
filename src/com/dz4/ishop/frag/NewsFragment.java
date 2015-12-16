package com.dz4.ishop.frag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.ViewFlipper;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

import com.dz4.ishop.adapter.FragmentChatListAdapter;
import com.dz4.ishop.adapter.PersonalQiangListAdapter;
import com.dz4.ishop.domain.PushNews;
import com.dz4.ishop.ui.NewsActivity;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishopping.R;
import com.dz4.support.widget.BaseFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * 
 * 购物车的fragment
 * 
 * @author MZone
 *
 * 
 */
public class NewsFragment extends BaseFragment {
	private View rootview;
	private View fragment_chat_list;
 
	private List<ImageView> imageViews; // 滑动的图片集合

	private String[] titles; // 图片标题
	private int[] imageResId; // 图片ID
	private List<View> dots; // 图片标题正文的那些点

	private TextView tv_title;
 
	private int currentItem = 0; // 当前图片的索引号
 
	// 这里的颜色值跟 fragment_char.xml 的背景颜色值一样
	private int intColor = 0xEBEBEB;
	private Context context;

	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private ViewFlipper viewFlipper;
	private List<PushNews> mPushNews;
	private List<PushNews> mPushNewsTitle;
	private FragmentChatListAdapter fragmentChatListAdapter;
	private int pageNum,flagPageNum=0;
	final int NUMBERS_PER_PAGE = 8;
	final int mPushNewsTitleSize = 5;
	private int position = 0;
	private int oldPosition = 0;
	
	public enum RefreshType {
		REFRESH, LOAD_MORE
	}

	private RefreshType mRefreshType = RefreshType.LOAD_MORE;
	private PersonalQiangListAdapter personalQiangListAdapter;

	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 1:
				 //....
				break;
			case 2:
                
				if(mPushNewsTitle.size() >( mPushNewsTitleSize-1)){
					currentItem = 0;
					upDataViewPager();
				}			 
				break;

			}
			 
		};
	};
 

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreateView(inflater, container, savedInstanceState);
		return rootview;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		rootview = getLayoutInflater(savedInstanceState).inflate(
				R.layout.fragment_chat, null);

	};

	@Override
	public void processHandlerMessage(Message msg) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void initView() {
		this.context = getContext();
		mPullToRefreshListView = (PullToRefreshListView) rootview
				.findViewById(R.id.fragment_char_list);
		mListView = mPullToRefreshListView.getRefreshableView();

		LayoutInflater inflater = LayoutInflater.from(context);  
		fragment_chat_list = inflater.inflate(R.layout.fragment_chat_list_hard, mListView, false);
		 
		viewFlipper = (ViewFlipper) fragment_chat_list.findViewById(R.id.fragment_chat_list_vf);
		
		 
		initViewDots();
	}

	@Override
	public void initData() {
		
		pageNum=0;
		
		mPullToRefreshListView.setMode(Mode.BOTH);
		mPullToRefreshListView
				.setOnScrollListener(new AbsListView.OnScrollListener() {
					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
					}

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {

					}

				});
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {

						// 得到当前时间  format 格式化的
						String label = DateUtils.formatDateTime(context,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						mRefreshType = RefreshType.REFRESH;
						flagPageNum = 0;
						pageNum = 0;
						fetchData();					
						
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {

						mRefreshType = RefreshType.LOAD_MORE;
						flagPageNum++;
						fetchData();
					}
				});
		// mListView 点击用户发表过的 Item
		mPullToRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
					}
				});
		// 注意 setDivider要放在 setDividerHeight前，setDividerHeight才有效！！！
		mListView.setDivider(new ColorDrawable(intColor));  
		// 只有在  mListView.setAdapter 的情况下  addHeaderView 才有用 !!
		mListView.setDividerHeight(40);
		mListView.addHeaderView(fragment_chat_list);
		mPushNewsTitle =  new ArrayList<PushNews>();
		mPushNews = new ArrayList<PushNews>();
		fragmentChatListAdapter = new FragmentChatListAdapter(context,mPushNews,R.layout.fragment_chat_list);
		mListView.setAdapter(fragmentChatListAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
 				Intent intent = new Intent(context,NewsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("NewsUrl", mPushNews.get(position-2).getNewsUrl());
				 
				intent.putExtras(bundle);
				startActivity(intent);   
				 
			}			 
	     });

	     
	}

	
	
	@Override
	public void initEvent() {

		viewFlipperSrart();
	    viewFlipper.setFlipInterval(5000);
	     

	   // viewFlipper.setOnTouchListener(new ViewFlipperOnTouchListener());
 
	    mRefreshType = RefreshType.REFRESH;
		flagPageNum = 0;
		pageNum = 0;
		fetchData();
	}
 
	protected void fetchData() {
		getPublishion();
		
	}
	private void getPublishion() {
		//mIProgressControllor.showActionBarProgress();
		//筛选 用户信息、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、//
		BmobQuery<PushNews> query = new BmobQuery<PushNews>();
		query.order("-createdAt");
		query.setLimit(NUMBERS_PER_PAGE); // 限制 6条 消息
		  
		 
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		query.setSkip(NUMBERS_PER_PAGE* (pageNum++));//跳过几条数据，如第二次刷新就跳过第一次加载完的数据
		query.include("newsTitle");
		Log.i("pageNum", pageNum+"....");
 
		query.findObjects(context, new FindListener<PushNews>() {

			@Override
			public void onSuccess(List<PushNews> data) {
				// TODO Auto-generated method stub
				//mIProgressControllor.hideActionBarProgress();
				if (data.size() != 0 && data.get(data.size() - 1) != null) {
					if (mRefreshType == RefreshType.REFRESH) {
						// 如果是向上刷新，则先清除掉当前数据
						mPushNews.clear();
						mPushNewsTitle.clear();
						Log.i("xfindObjects", "xxxxxx"+mPushNewsTitle.size());

						//data 的前5条数据是 viewpager 的 
						 for(int i=0;i<mPushNewsTitleSize;i++){
							mPushNewsTitle.add(data.get(i));
						}
						 Log.i("xfindObjects", "vvvvvvv"+mPushNewsTitle.size());
						 
						Message message = new Message();
					    message.what = 2;
					    handler.sendMessage(message);
			
			
					    for(int i=0;i<5;i++){
							data.remove(0);
						}
					}
					Log.i("xfindObjects", ".....REFRESH");
					if (data.size() < NUMBERS_PER_PAGE) {
						showToast("已加载完所有数据~");
			
					}
				  
					mPushNews.addAll(data);
					Log.i("xfindObjects", ".....addAll");
					 
					//更新数据
					//刷新完成
					mPullToRefreshListView.onRefreshComplete();
					 
				} else {
					//showToast("暂无更多数据~");
					showToast("暂无更多数据~");
					Log.i("xfindObjects", "暂无更多数据~");
					flagPageNum--;
					pageNum--;
					mPullToRefreshListView.onRefreshComplete();
				}
			}

			@Override
			public void onError(int arg0, String msg) {
				// TODO Auto-generated method stub
				//mIProgressControllor.hideActionBarProgress();
				//LogUtils.i(TAG, "find failed." + msg);
				//pageNum--;
				mPullToRefreshListView.onRefreshComplete();
				Log.i("xfindObjects", "xx..onError ; "+msg+"  "+arg0);
			}
		});
	}
	public void initViewDots(){
		
		dots = new ArrayList<View>();
		dots.add(fragment_chat_list
				.findViewById(R.id.fragment_chat_list_v_dot0));
		dots.add(fragment_chat_list
				.findViewById(R.id.fragment_chat_list_v_dot1));
		dots.add(fragment_chat_list
				.findViewById(R.id.fragment_chat_list_v_dot2));
		dots.add(fragment_chat_list
				.findViewById(R.id.fragment_chat_list_v_dot3));
		dots.add(fragment_chat_list
				.findViewById(R.id.fragment_chat_list_v_dot4));

		 
		titles = new String[mPushNewsTitleSize]; 
		tv_title = (TextView) fragment_chat_list.findViewById(R.id.fragment_chat_list_tv_title);
		imageViews = new ArrayList<ImageView>(); 
		//....
	}
	private void upDataViewPager(){
		
		 
		for(int i=0;i<mPushNewsTitleSize;i++){
			titles[i]= mPushNewsTitle.get(i).getNewsTitle();
		}		 
		 
		tv_title.setText(titles[0]);//

		String Imageurl = null;
		 
 	// 初始化图片资源
	    if(mPushNewsTitle.get(0).getNewsImage() != null){
				
				for (int i = 0; i < mPushNewsTitle.size(); i++) {
					
					Imageurl = mPushNewsTitle.get(i).getNewsImage().getFileUrl(context);
					ImageView imageView = new ImageView(context);

					ImageLoader.getInstance().displayImage(Imageurl,
					imageView, 
					ImageUtils.getOptions(R.drawable.user_icon_default_main),new SimpleImageLoadingListener(){
				    public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
					
				    	};
			    	} );
		 				     
					// 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
					imageView.setScaleType(ScaleType.CENTER_CROP);
					//imageView.setOnClickListener(new ImageViewOnClickListener( mPushNewsTitle.get(i).getNewsUrl()));
					imageView.setOnTouchListener(new ViewFlipperOnTouchListener(mPushNewsTitle.get(i).getNewsUrl()));
				
					//imageViews.add(imageView);
					viewFlipper.addView(imageView);
				 
				}
		}
	    
	}
 

	private class ViewFlipperAnimationListener implements AnimationListener{
 
		@Override
		public void onAnimationStart(Animation animation) {
			position++;
			if(position>4)
				position = 0;
			setTitle();
			Log.i("Animation", "...."+position);
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			setTitle();
			Log.i("Animation", "....2.."+position);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class ViewFlipperOnTouchListener implements OnTouchListener{

		private String NewsUrl;
		private int downX = 0;
		private int upX = 0;
		public ViewFlipperOnTouchListener(String newsUrl) {
			 this.NewsUrl = newsUrl;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				viewFlipper.stopFlipping();
				downX = (int) event.getX();
				break;
			case MotionEvent.ACTION_UP:
				upX = (int) event.getX();
				
				Log.i("getAction", "...."+downX+"...."+upX);
				
				if((upX - downX) >50){
 
					viewFlipperShowPrevious();

					position--;
					if(position<0)
						position = 4;
					setTitle();
 
					viewFlipperSrart();
				}
				else if((downX - upX) >50){
	 
					viewFlipperShowNext();
					
					position++;
					if(position>4)
						position = 0;
					setTitle();
					
					viewFlipperSrart();//需要重新启动viewflipper动画
   
				}
				else{
					Intent intent = new Intent(context,NewsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("NewsUrl",NewsUrl);		 
					intent.putExtras(bundle);
					startActivity(intent);   
				}
				//viewFlipperSrart();
				break;
			}
			 
			return true;
		}
		
	}
	
	/*
	 * (non-Javadoc) 当 当前的 fragment 不可见时 hidden 为 true ，可见为 false 在 fragment 和
	 * viewpager 结合是 fragment 的 onPause（） 方法 onReause() 都不可用了 ！！！！！
	 */
	@Override
	public void onHiddenChanged(boolean hidden) {
 
		super.onHiddenChanged(hidden);
		if(!hidden){
			viewFlipperSrart();
		}
	}

 
	private void viewFlipperShowNext() {
		viewFlipper.setInAnimation(context,R.anim.viewflipper_in_right); 
	    viewFlipper.setOutAnimation(context,R.anim.viewflipper_out_lift);
	    viewFlipper.showNext();
	}
	private void viewFlipperShowPrevious() {
		viewFlipper.setInAnimation(context,R.anim.viewflipper_in_lift); 
	    viewFlipper.setOutAnimation(context,R.anim.viewflipper_out_right);
	    viewFlipper.showPrevious();
	}
	private void viewFlipperSrart(){
		viewFlipper.setInAnimation(context,R.anim.viewflipper_in_right); 
	    viewFlipper.setOutAnimation(context,R.anim.viewflipper_out_lift);
	    viewFlipper.getOutAnimation().setAnimationListener(new ViewFlipperAnimationListener());
		viewFlipper.startFlipping();
	}
	private void setTitle(){
		tv_title.setText(titles[position]);  //ArrayIndexOutOfBoundsException: length=5; index=5
		dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
		dots.get(position).setBackgroundResource(R.drawable.dot_focused);
		oldPosition = position;
	}

	@Override
	public void onResume() {
		 
		super.onResume();
		viewFlipperSrart();
	}
	

}
