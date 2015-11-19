package com.dz4.ishop.frag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

import com.dz4.ImageUpload_9_zss.utils.Fragment_chat_List_Head_Adapter;
import com.dz4.ishop.adapter.FragmentChatLiatAdapter;
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
public class Fragment_chat extends BaseFragment {
	private View rootview;
	private Bundle savedInstanceState;
	private View fragment_chat_list;

	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合

	private String[] titles; // 图片标题
	private int[] imageResId; // 图片ID
	private List<View> dots; // 图片标题正文的那些点

	private TextView tv_title;
 
	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;

	private Context context;

	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	
	private List<PushNews> mPushNews;
	private List<PushNews> mPushNewsTitle;
	private FragmentChatLiatAdapter fragmentChatLiatAdapter;
	private int pageNum,flagPageNum=0;
	final int NUMBERS_PER_PAGE = 8;
	final int mPushNewsTitleSize = 5;
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
				viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
				break;
			case 2:
				Log.i("upDataViewPager", "upDataViewPager");
				if(mPushNewsTitle.size() >( mPushNewsTitleSize-1)){
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
		Log.i("viewPager", "viewPager5555");
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
		fetchData();
		initViewPager();
	}

	@Override
	public void initData() {
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
		// 只有在  mListView.setAdapter 的情况下  addHeaderView 才有用 !!
		mListView.setDividerHeight(40);
		mListView.addHeaderView(fragment_chat_list);
		mPushNewsTitle =  new ArrayList<PushNews>();
		mPushNews = new ArrayList<PushNews>();
		fragmentChatLiatAdapter = new FragmentChatLiatAdapter(context,mPushNews,R.layout.fragment_chat_list);
		mListView.setAdapter(fragmentChatLiatAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
 				Intent intent = new Intent(context,NewsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("NewsUrl", mPushNews.get(position-2).getNewsUrl());
				Log.i("getNewsUrl", mPushNews.get(position-2).getNewsUrl()+"////");
				intent.putExtras(bundle);
				startActivity(intent);   
				Log.i("position",position+"////..///");
			}			 
	     });

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
		  
		query.setSkip(NUMBERS_PER_PAGE* (flagPageNum));//跳过几条数据，如第二次刷新就跳过第一次加载完的数据
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		query.include("newsTitle");
		Log.i("pageNum", pageNum+"...."+flagPageNum);
 
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
					}

					if (data.size() < NUMBERS_PER_PAGE) {
						//showToast("已加载完所有数据~");
						Log.i("findObjects", "已加载完所有数据~"); 
					}
					Log.i("ddddddd","ddddddddd"); 
					//data 的前5条数据是 viewpager 的 
					 for(int i=0;i<mPushNewsTitleSize;i++){
						mPushNewsTitle.add(data.get(i));
					}
					Log.i("mPushNewsTitle", mPushNewsTitle.size()+"..."); 
				 
					Message message = new Message();
				    message.what = 2;
				    handler.sendMessage(message);
				    Log.i("mPushNewsTitle", "message...");  
		
				    for(int i=0;i<5;i++){
						data.remove(0);
					}
					mPushNews.addAll(data);
					Log.i("mPushNewsTitle", mPushNews.size()+ "...onRefreshComplete...");  
					//更新数据
					personalQiangListAdapter.notifyDataSetChanged();
					//刷新完成
					mPullToRefreshListView.onRefreshComplete();
					 
				} else {
					//showToast("暂无更多数据~");
					Log.i("findObjects", "暂无更多数据~");
					flagPageNum--;
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
				Log.i("findObjects", "onError");
			}
		});
	}
	public void initViewPager(){
		
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
		viewPager = (ViewPager) fragment_chat_list.findViewById(R.id.fragment_chat_list_vp); 
		// 设置一个监听器，当ViewPager中的页面改变时调用
        viewPager.setOnPageChangeListener(new MyPageChangeListener()); 
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
			imageViews.add(imageView);
				}
		}
	     
		viewPager.setAdapter(new Fragment_chat_List_Head_Adapter(imageViews));// 设置填充ViewPager页面的适配器
		 
		 
	}
	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			tv_title.setText(titles[position]);
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % imageViews.size();
			    Message message = new Message();
			    message.what = 1;
			    handler.sendMessage(message);
				//handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}

	/*
	 * (non-Javadoc) 当 当前的 fragment 不可见时 hidden 为 true ，可见为 false 在 fragment 和
	 * viewpager 结合是 fragment 的 onPause（） 方法 onReause() 都不可用了 ！！！！！
	 */
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (hidden) {
			Log.i("ffffff", "hhhhh1");
			scheduledExecutorService.shutdown();
		} else {
			Log.i("ffffff", "hhhh22");
			scheduledExecutorService = Executors
					.newSingleThreadScheduledExecutor();
			scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 3,
					5, TimeUnit.SECONDS);
		}
		super.onHiddenChanged(hidden);
	}

	
	
	@Override
	public void initEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		Log.i("ffffff", "ffffff00000");
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

		/*
		 * 当Activity显示出来后，每两秒钟切换一次图片显示 command：执行线程 initialDelay：初始化延时
		 * period：前一次执行结束到下一次执行开始的间隔时间（间隔执行延迟时间） unit：计时单位 //TimeUnit.SECONDS 秒
		 */
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 3, 5,TimeUnit.SECONDS);
	    
		super.onStart();
	}
	
	@Override
	public void onStop() {
		scheduledExecutorService.shutdown();
		super.onStop();
	}

}
