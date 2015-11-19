package com.dz4.ishop.ui;


import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

import com.dz4.ishop.adapter.ImagePagerAdapter;
import com.dz4.ishop.domain.Goods;
import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GoodsDetailActivity extends BaseUIActivity {

	private ViewPager imagePager;
	private QiangItem mQiangItem;
	private Context mContext;
	private PullToRefreshScrollView refreshview;
	private View rootview;
	private ImagePagerAdapter mImagePagerAdapter;
	private ArrayList<String> urls = null;
	private TextView mtipsView;
	private TextView mNameView;
	private ImageView mIconView;
	private TextView mTimeView;
	private TextView mContentView;
	private ProgressBar mProgressBar;
	private LinearLayout dot_layout;
	private User author;
	private String uploadtime;
	private String username;
	private String QiangItemId;
	private ArrayList<View> dots;
	private TextView goods_name;
	private TextView goods_category;
	private TextView goods_price;
	private Goods goods;

	@Override
	public void initView() {
		setContentView(R.layout.activity_goodsdetails);
		rootview = getLayoutInflater().inflate(R.layout.item_goodsdetails, null);
		imagePager = (ViewPager)rootview.findViewById(R.id.ImagePager);
		refreshview = (PullToRefreshScrollView)findViewById(R.id.pull_refresh_view);
		refreshview.addView(rootview);
		mtipsView = (TextView) findViewById(R.id.networkTips);
		mtipsView.setVisibility(View.GONE);
		mIconView = (ImageView)findViewById(R.id.user_logo);
		mNameView = (TextView)findViewById(R.id.user_name);
		mTimeView = (TextView)findViewById(R.id.qiang_time);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		mContentView = (TextView)findViewById(R.id.content_text);
		
		goods_name = (TextView)rootview.findViewById(R.id.goods_name_text);
		goods_category= (TextView)rootview.findViewById(R.id.goods_category_text);
		goods_price = (TextView)findViewById(R.id.goods_price_text);
		dot_layout = (LinearLayout)findViewById(R.id.layout_dotzone);
		
		
	}
		
	@Override
	public void initData() {
		
		mContext = getApplicationContext();
		mQiangItem=(QiangItem)getIntent().getSerializableExtra(Constant.BUNDLE_KEY_QIANGITEM);
		author = mQiangItem.getAuthor();
		urls = geturlsArray();
		mImagePagerAdapter =  new ImagePagerAdapter(mContext,urls);
		if(imagePager!=null){
			imagePager.setAdapter(mImagePagerAdapter);
		}
		mQiangItem.getAuthor();
		if(author.getAvatar()!=null){
			String url = author.getAvatar().getFileUrl(mContext);
			ImageLoader.getInstance().displayImage(url, mIconView,ImageUtils.getOptions(R.drawable.user_icon_default));
		}
		username = author.getUsername();
		uploadtime = mQiangItem.getUpdatedAt();
		QiangItemId = mQiangItem.getObjectId();
		goods = mQiangItem.getGoods();
		mNameView.setText(username); 
		mTimeView.setText(uploadtime);
		mContentView.setText(mQiangItem.getContent());
		if(goods!=null){
			goods_name.setText(goods.getName());
			goods_category.setText(goods.getCategory());
			goods_price.setText(""+goods.getPrice());
			refreshview.setMode(Mode.PULL_FROM_START);
		}
		initdot();
		
	}
	private void initdot(){
		dot_layout.removeAllViews();
		if(urls.isEmpty()) return;
		for(String url:geturlsArray()){
			
			ImageView view =new ImageView(mContext);
			view.setBackgroundResource(R.drawable.dot_normal);
			MarginLayoutParams layoutParams = new MarginLayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()),
					(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10, getResources().getDisplayMetrics()));
			layoutParams.setMargins(10, 10, 10, 10);
			view.setLayoutParams(layoutParams);
			view.setAlpha(0.5f);
			dot_layout.addView(view);
		}
		dot_layout.getChildAt(0).setBackgroundResource(R.drawable.dot_focused);
		imagePager.setOnPageChangeListener(new OnPageChangeListener() {
			int last = 0;
			@Override
			public void onPageSelected(int arg0) {
				// TODO �Զ����ɵķ������
				dot_layout.getChildAt(arg0).setBackgroundResource(R.drawable.dot_focused);
				dot_layout.getChildAt(last).setBackgroundResource(R.drawable.dot_normal);
				last = arg0;
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO �Զ����ɵķ������
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO �Զ����ɵķ������
			}
		});
	}
	private void loadData(){
		BmobQuery<QiangItem> query = new BmobQuery<QiangItem>();
		query.getObject(mContext, QiangItemId, new GetListener<QiangItem>() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO �Զ����ɵķ������
				setLoadingState(LOADING_FAILED);
				refreshview.onRefreshComplete();
			}
			
			@Override
			public void onSuccess(QiangItem qiangitem) {
				// TODO �Զ����ɵķ������
				setLoadingState(LOADING_COMPLETED);
				mQiangItem= qiangitem;
				initData();
				refreshview.onRefreshComplete();
			}
		});
	}
	private ArrayList<String> geturlsArray(){
		urls = new ArrayList<String>();
		if(mQiangItem.getContentfigureurl()!=null)
			urls.add(mQiangItem.getContentfigureurl().getFileUrl(mContext));
		if(mQiangItem.getContentfigureurl1()!=null)
			urls.add(mQiangItem.getContentfigureurl1().getFileUrl(mContext));
		if(mQiangItem.getContentfigureurl2()!=null)
			urls.add(mQiangItem.getContentfigureurl2().getFileUrl(mContext));
		if(mQiangItem.getContentfigureurl3()!=null)
			urls.add(mQiangItem.getContentfigureurl3().getFileUrl(mContext));
		if(mQiangItem.getContentfigureurl4()!=null)
			urls.add(mQiangItem.getContentfigureurl4().getFileUrl(mContext));
		if(mQiangItem.getContentfigureurl5()!=null)
			urls.add(mQiangItem.getContentfigureurl5().getFileUrl(mContext));
		if(mQiangItem.getContentfigureurl6()!=null)
			urls.add(mQiangItem.getContentfigureurl6().getFileUrl(mContext));
		if(mQiangItem.getContentfigureurl7()!=null)
			urls.add(mQiangItem.getContentfigureurl7().getFileUrl(mContext));
		if(mQiangItem.getContentfigureurl8()!=null)
			urls.add(mQiangItem.getContentfigureurl8().getFileUrl(mContext));
		return urls;
	}
	@Override
	public void initEvent() {
		refreshview.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				// TODO �Զ����ɵķ������
				String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				//setLoadingState(LOADING);
				loadData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				// TODO �Զ����ɵķ������
			}
			
		});
	}
	@Override
	protected void onDestroy() {
		// TODO �Զ����ɵķ������
		super.onDestroy();
	}
	private static final int LOADING = 1;
	private static final int LOADING_COMPLETED = 2;
	private static final int LOADING_FAILED =3;
	private static final int NORMAL = 4;
	public void setLoadingState(int state){
		switch (state) {
		case LOADING:
			refreshview.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
			mtipsView.setVisibility(View.GONE);
			
			break;
		case LOADING_COMPLETED:
			mtipsView.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.GONE);
			
			refreshview.setVisibility(View.VISIBLE);
			break;
		case LOADING_FAILED:
			refreshview.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			showToast("�������粢����ˢ��");
			break;
		case NORMAL:
			
			break;
		default:
			break;
		}
	}
}
