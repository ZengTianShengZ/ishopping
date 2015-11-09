package com.dz4.ishop.frag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import android.os.Bundle;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

import com.dz4.ishop.adapter.QiangListAdapter;
import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.listener.TitlechangeListener;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishopping.R;
import com.dz4.support.widget.BaseFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 
 *  墙语
 * @author MZone
 *
 */
public class Fragment_Qiang extends BaseFragment {
	
	
	private View contentView ;
	private PullToRefreshListView mPullRefreshListView;
	private ListView actualListView;
	private TextView networkTips;
	private ProgressBar progressbar;
	
	private ArrayList<QiangItem> mListItems;
	private QiangListAdapter mQiangListAdapter;
	private enum RefreshType {Refresh,Loadmore};
	private RefreshType mRefreshType=RefreshType.Loadmore;
	private int pageNum;
	
	private User user;
	public static BaseFragment newInstance(int position) {
		// TODO 自动生成的方法存根
		BaseFragment fragment = new Fragment_Qiang();
		Bundle bundle = new Bundle();
		bundle.putInt("page", position);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		contentView = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_qiang, null, false);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//contentView = inflater.inflate(R.layout.fragment_qiang, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return contentView;
	}
	
	@Override
	public void processHandlerMessage(Message msg) {
		// TODO 自动生成的方法存根
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onViewCreated(view, savedInstanceState);
	}
	@Override
	public void initView() {
		// TODO 自动生成的方法存根
		mPullRefreshListView = (PullToRefreshListView)contentView
				.findViewById(R.id.pull_refresh_list);
		networkTips = (TextView)contentView.findViewById(R.id.networkTips);
		progressbar = (ProgressBar)contentView.findViewById(R.id.progressBar);
		actualListView = mPullRefreshListView.getRefreshableView();
	}

	@Override
	public void initData() {
		// TODO 自动生成的方法存根
		user =((IshopApplication)getActivity().getApplication()).getCurrentUser();
		mPullRefreshListView.setMode(Mode.BOTH);
		mListItems =new ArrayList<QiangItem>();
		mQiangListAdapter = new QiangListAdapter(getContext(),mListItems,((IshopApplication)getActivity().getApplication()));
		actualListView.setAdapter(mQiangListAdapter);
		if(mListItems.size() == 0){
			mRefreshType=RefreshType.Refresh;
			pageNum=0;
			loadData();
		}	
	}
	
	@Override
	public void initEvent() {
		// TODO 自动生成的方法存根
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase refreshView) {
				// TODO 自动生成的方法存根
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				mRefreshType=RefreshType.Refresh;
				pageNum=0;
				loadData();
			}
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase refreshView) {
				// TODO 自动生成的方法存根
				mRefreshType=RefreshType.Loadmore;
				loadData();
				
			}
		});
	}
	
	protected void loadData() {
		// TODO 自动生成的方法存根
		setLoadingState(LOADING);
		BmobQuery<QiangItem> query = new BmobQuery<QiangItem>();
		query.order("-createdAt");
//		query.setCachePolicy(CachePolicy.NETWORK_ONLY);
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		query.include("author");
		query.findObjects(getActivity(), new FindListener<QiangItem>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO 自动生成的方法存根
				pageNum--;
				setLoadingState(LOADING_FAILED);
				mPullRefreshListView.onRefreshComplete();
			}

			@Override
			public void onSuccess(List<QiangItem> list) {
				// TODO 自动生成的方法存根
				if(list.size()!=0&&list.get(list.size()-1)!=null){
					if(mRefreshType==RefreshType.Refresh){
						mListItems.clear();
					}
					
					if(list.size()<Constant.NUMBERS_PER_PAGE){
						
					}
					
					List<QiangItem> Targetlist = list;
					Targetlist = setfocus(Targetlist);
					mListItems.addAll(Targetlist);
					mQiangListAdapter.notifyDataSetChanged();
					
					setLoadingState(LOADING_COMPLETED);
					mPullRefreshListView.onRefreshComplete();
				}else{
					pageNum--;
					setLoadingState(LOADING_COMPLETED);
					mPullRefreshListView.onRefreshComplete();
				}
			}

			private List<QiangItem> setfocus(List<QiangItem> list) {
				// TODO 自动生成的方法存根
				if(list!=null && user!=null){
					for(final QiangItem qi:list){
						BmobQuery<User> query = new BmobQuery<User>();
						query.addWhereContains("focus", qi.getAuthor().getObjectId());
						query.findObjects(getContext(), new FindListener<User>() {
							@Override
							public void onSuccess(List<User> focuslist) {
								// TODO 自动生成的方法存根
								if(!focuslist.isEmpty()){
									qi.setFocus(true);
								}
								else{
									qi.setFocus(false);
								}
								mQiangListAdapter.notifyDataSetChanged();
							}
							
							@Override
							public void onError(int arg0, String arg1) {
								// TODO 自动生成的方法存根
								
							}
						});
					}
				}
				return list;
			}
		});
		
	}
	
	private static final int LOADING = 1;
	private static final int LOADING_COMPLETED = 2;
	private static final int LOADING_FAILED =3;
	private static final int NORMAL = 4;
	public void setLoadingState(int state){
		switch (state) {
		case LOADING:
			if(mListItems.size() == 0){
				mPullRefreshListView.setVisibility(View.GONE);
				progressbar.setVisibility(View.VISIBLE);
			}
			networkTips.setVisibility(View.GONE);
			
			break;
		case LOADING_COMPLETED:
			networkTips.setVisibility(View.GONE);
			progressbar.setVisibility(View.GONE);
			
		    mPullRefreshListView.setVisibility(View.VISIBLE);
		    mPullRefreshListView.setMode(Mode.BOTH);

			
			break;
		case LOADING_FAILED:
			if(mListItems.size()==0){
				mPullRefreshListView.setVisibility(View.VISIBLE);
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);
				networkTips.setVisibility(View.VISIBLE);
			}
			progressbar.setVisibility(View.GONE);
			break;
		case NORMAL:
			
			break;
		default:
			break;
		}
	}
}
