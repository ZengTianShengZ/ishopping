package com.dz4.ishop.ui;


import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

import com.dz4.ishop.adapter.CommentAdapter;
import com.dz4.ishop.adapter.ImagePagerAdapter;
import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.domain.Comment;
import com.dz4.ishop.domain.Goods;
import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GoodsDetailActivity extends BaseUIActivity implements
		OnClickListener {

	protected static final String TAG = "GoodsDetailActivity";
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
	private User author;
	private String uploadtime;
	private String nickname;
	private String QiangItemId;
	private ArrayList<View> dots;
	private TextView goodNname;
	private TextView goodsCategory;
	private TextView goodsPrice;
	private Goods goods;
	private TextView contact_btn;
	private String sailerPhone;
	private ImageView backBtn;
	private TextView comment_btn;
	private User mUser;
	private ListView mCommentView;
	private ArrayList<Comment> datalist;
	private int commentCount;
	private TextView moreComment;
	private int pageNum;
	private CommentAdapter mAdapter;
	private TextView focusView;

	private TextView dotZoneLeft, dotZoneRight;

	@Override
	public void initView() {
		setContentView(R.layout.activity_goodsdetails);
		rootview = getLayoutInflater().inflate(
				R.layout.item_goodsdetails, null);
		imagePager = (ViewPager) rootview.findViewById(R.id.ImagePager);
		refreshview = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_view);
		refreshview.addView(rootview);
		mtipsView = (TextView) findViewById(R.id.networkTips);
		mtipsView.setVisibility(View.GONE);
		mIconView = (ImageView) findViewById(R.id.user_logo);
		mNameView = (TextView) findViewById(R.id.user_name);
		mTimeView = (TextView) findViewById(R.id.qiang_time);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		mContentView = (TextView) findViewById(R.id.content_text);
		contact_btn = (TextView) findViewById(R.id.contact_btn);
		comment_btn = (TextView) findViewById(R.id.comment_btn);
		goodNname = (TextView) rootview
				.findViewById(R.id.goods_name_text);
		goodsCategory = (TextView) rootview
				.findViewById(R.id.goods_category_text);
		goodsPrice = (TextView) findViewById(R.id.goods_price_text);
		backBtn = (ImageView) findViewById(R.id.back_btn);
		moreComment = (TextView) findViewById(R.id.more_comment);
		mCommentView = (ListView) findViewById(R.id.comment_list);
		focusView = (TextView) findViewById(R.id.focus_view);

		dotZoneLeft = (TextView) findViewById(R.id.tv_dotzone_L);
		dotZoneRight = (TextView) findViewById(R.id.tv_dotzone_R);

	}

	@Override
	public void initData() {

		mContext = getApplicationContext();
		mUser = ((IshopApplication) getApplication()).getCurrentUser();
		mQiangItem = (QiangItem) getIntent().getSerializableExtra(
				Constant.BUNDLE_KEY_QIANGITEM);
		author = mQiangItem.getAuthor();
		urls = geturlsArray();
		mImagePagerAdapter = new ImagePagerAdapter(mContext, urls);
		if (imagePager != null) {
			imagePager.setAdapter(mImagePagerAdapter);
		}
		mQiangItem.getAuthor();
		if (author.getAvatar() != null) {
			String url = author.getAvatar().getFileUrl(mContext);
			ImageLoader.getInstance()
					.displayImage(url,
							mIconView,
							ImageUtils.getOptions(R.drawable.user_icon_default));
		}
		nickname = author.getNickname();
		uploadtime = mQiangItem.getUpdatedAt();
		QiangItemId = mQiangItem.getObjectId();
		loadData();
		mQiangItem.setAuthor(author);
		goods = mQiangItem.getGoods();
		mNameView.setText(nickname);
		mTimeView.setText(uploadtime);
		mContentView.setText(mQiangItem.getContent());
		if (goods != null) {
			sailerPhone = goods.getCellphone();
			LogUtils.i("TAG", "phone:" + sailerPhone);
			goodNname.setText(goods.getName());
			goodsCategory.setText(goods.getCategory());
			goodsPrice.setText("" + goods.getPrice());
		}
		refreshview.setMode(Mode.BOTH);
		if (mUser == null || mUser.getObjectId().equals(
				mQiangItem.getAuthor().getObjectId())) {
			focusView.setVisibility(View.GONE);
		} else {
			focusView.setVisibility(View.VISIBLE);
		}
		initdot();
		/**
		 * 评论区
		 * **/
		loadCommentData();
		datalist = new ArrayList<Comment>();
		mAdapter = new CommentAdapter(mContext, datalist);
		mCommentView.setAdapter(mAdapter);

	}

	private void initdot() {

		if (urls.isEmpty())
			return;
		dotZoneLeft.setText(1 + "");
		dotZoneRight.setText(geturlsArray().size() + "");

		imagePager.setOnPageChangeListener(new OnPageChangeListener() {
			int last = 0;

			@Override
			public void onPageSelected(int arg0) {
				dotZoneLeft.setText(arg0 + 1 + "");
				last = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1,
					int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO 自动生成的方法存根
			}
		});
	}

	private void loadData() {
		BmobQuery<QiangItem> query = new BmobQuery<QiangItem>();
		query.include("goods");
		query.getObject(mContext, QiangItemId,
				new GetListener<QiangItem>() {

					@Override
					public void onFailure(int arg0,
							String arg1) {
						// TODO 自动生成的方法存根
						setLoadingState(LOADING_FAILED);
						refreshview.onRefreshComplete();
					}

					@Override
					public void onSuccess(
							QiangItem qiangitem) {
						// TODO 自动生成的方法存根
						setLoadingState(LOADING_COMPLETED);
						mQiangItem = qiangitem;
						goods = mQiangItem.getGoods();
						mNameView.setText(nickname);
						mTimeView.setText(uploadtime);
						mContentView.setText(mQiangItem
								.getContent());
						if (goods != null) {
							sailerPhone = goods
									.getCellphone();
							LogUtils.i("TAG",
									"phone:"
											+ sailerPhone);
							goodNname.setText(goods
									.getName());
							goodsCategory.setText(goods
									.getCategory());
							goodsPrice.setText(String.valueOf(goods.getPrice()));
						}
						refreshview.onRefreshComplete();
					}
				});
		if (mUser != null) {
			BmobQuery<User> query1 = new BmobQuery<User>();
			query1.addWhereRelatedTo("focus",
					new BmobPointer(mUser));
			final String id = mQiangItem.getAuthor().getObjectId();
			query1.findObjects(mContext, new FindListener<User>() {

				@Override
				public void onError(int arg0, String arg1) {
					// TODO 自动生成的方法存根
				}

				@Override
				public void onSuccess(List<User> arg0) {
					// TODO 自动生成的方法存根
					if (arg0 != null) {
						StringBuffer info = new StringBuffer();
						for (User user : arg0) {
							info.append(user.getObjectId()
									+ "::"
									+ id
									+ ",");
							if (user.getObjectId()
									.equals(id)) {
								focus_flag = true;
								focusView.setText("已关注");
								focusView.setTextColor(Color
										.parseColor("#777777"));
								return;
							} else {
								focus_flag = false;
								focusView.setText("关注Ta");
								focusView.setTextColor(Color
										.parseColor("#000000"));
							}
						}
						LogUtils.i(TAG, info.toString());
					}
				}
			});
		}
	}

	private boolean focus_flag = false;

	private ArrayList<String> geturlsArray() {
		urls = new ArrayList<String>();
		if (mQiangItem.getContentfigureurl() != null)
			urls.add(mQiangItem.getContentfigureurl().getFileUrl(
					mContext));
		if (mQiangItem.getContentfigureurl1() != null)
			urls.add(mQiangItem.getContentfigureurl1().getFileUrl(
					mContext));
		if (mQiangItem.getContentfigureurl2() != null)
			urls.add(mQiangItem.getContentfigureurl2().getFileUrl(
					mContext));
		if (mQiangItem.getContentfigureurl3() != null)
			urls.add(mQiangItem.getContentfigureurl3().getFileUrl(
					mContext));
		if (mQiangItem.getContentfigureurl4() != null)
			urls.add(mQiangItem.getContentfigureurl4().getFileUrl(
					mContext));
		if (mQiangItem.getContentfigureurl5() != null)
			urls.add(mQiangItem.getContentfigureurl5().getFileUrl(
					mContext));
		if (mQiangItem.getContentfigureurl6() != null)
			urls.add(mQiangItem.getContentfigureurl6().getFileUrl(
					mContext));
		if (mQiangItem.getContentfigureurl7() != null)
			urls.add(mQiangItem.getContentfigureurl7().getFileUrl(
					mContext));
		if (mQiangItem.getContentfigureurl8() != null)
			urls.add(mQiangItem.getContentfigureurl8().getFileUrl(
					mContext));
		return urls;
	}

	@Override
	public void initEvent() {
		refreshview.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				// TODO 自动生成的方法存根
				String label = DateUtils.formatDateTime(
						mContext,
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(label);
				pageNum = 0;
				datalist.clear();
				loadData();
				loadCommentData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				loadCommentData();
			}

		});
		backBtn.setOnClickListener(this);
		contact_btn.setOnClickListener(this);
		comment_btn.setOnClickListener(this);
		moreComment.setOnClickListener(this);
		focusView.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
	}

	private static final int LOADING = 1;
	private static final int LOADING_COMPLETED = 2;
	private static final int LOADING_FAILED = 3;
	private static final int NORMAL = 4;

	public void setLoadingState(int state) {
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
			showToast("请检查网络并下拉刷新");
			break;
		case NORMAL:

			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.contact_btn:
			Uri uri = Uri.parse("tel:" + sailerPhone);
			LogUtils.i("TAG", uri.toString());
			Intent intent = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(intent);
			break;
		case R.id.comment_btn:
			Intent intent1 = new Intent(GoodsDetailActivity.this,
					EditCommentActivity.class);
			intent1.putExtra(Constant.BUNDLE_KEY_USER, mUser);
			intent1.putExtra(Constant.BUNDLE_KEY_QIANGITEM,
					mQiangItem);
			startActivityForResult(intent1,
					Constant.CHANGER_COMMENT);
			break;
		case R.id.more_comment:
			break;
		case R.id.back_btn:
			finish();
			break;
		case R.id.focus_view:
			if (mUser == null)
				break;
			if (!focus_flag) {
				BmobRelation focus = new BmobRelation();
				focus.add(mQiangItem.getAuthor());
				mUser.setFocus(focus);
				mUser.update(mContext, new UpdateListener() {
					@Override
					public void onSuccess() {
						focus_flag = true;
						focusView.setText("已关注");
						focusView.setTextColor(Color
								.parseColor("#777777"));
						showToast("已关注");
					}

					@Override
					public void onFailure(int arg0,
							String arg1) {
						showToast("关注失败");
					}
				});
			} else {
				BmobRelation focus = new BmobRelation();
				focus.remove(mQiangItem.getAuthor());
				mUser.setFocus(focus);
				mUser.update(mContext, new UpdateListener() {

					@Override
					public void onSuccess() {
						focus_flag = false;
						focusView.setText("关注Ta");
						focusView.setTextColor(Color
								.parseColor("#000000"));

						showToast("取消关注");

					}

					@Override
					public void onFailure(int arg0,
							String arg1) {
						showToast("取消关注失败请，查看网络");
					}
				});
			}
			break;
		}
	}

	private void loadCommentData() {
		// TODO 自动生成的方法存根
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereEqualTo("qiang", mQiangItem);
		query.count(mContext, Comment.class, new CountListener() {

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void onSuccess(int arg0) {
				// TODO 自动生成的方法存根
				mAdapter.setCommentCount(arg0);
			}
		});
		query.include("user");
		query.order("-createdAt");
		query.setLimit(Constant.COMMENT_NUMBERS_PER_PAGE);
		query.setSkip(Constant.COMMENT_NUMBERS_PER_PAGE * (pageNum++));
		query.findObjects(this, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> data) {
				// TODO Auto-generated method stub
				if (data.size() != 0
						&& data.get(data.size() - 1) != null) {

					if (data.size() < Constant.COMMENT_NUMBERS_PER_PAGE) {

						showToast("已加载完所有评论~");
					}
					datalist.addAll(data);
					mAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(mCommentView);
				} else {
					pageNum--;

				}
				cancelProgressDialog();
				refreshview.onRefreshComplete();
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				showToast("获取评论失败。请检查网络~");
				cancelProgressDialog();
				pageNum--;
			}
		});
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter
						.getCount() - 1)) + 15;
		listView.setLayoutParams(params);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_CANCELED) {
			// 登录完成
			mAdapter.notifyDataSetChanged();
			refreshview.requestLayout();
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		LogUtils.i(TAG, "onResume");
		super.onResume();
	}

}
