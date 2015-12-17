package com.dz4.ishop.frag;

import java.util.ArrayList;
import java.util.zip.Inflater;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.dz4.ishop.adapter.HomeContentAdapter;
import com.dz4.ishop.listener.TitlechangeListener;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishopping.R;
import com.dz4.support.widget.BaseFragment;
/**
 * 
 * 主页的fragment
 * @author MZone
 *
 */
public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
	private final String TAG ="HomeFragment";
	private View rootview;
	private ArrayList<BaseFragment> fragments;
	private ViewPager viewPager;
	private TitlechangeListener mlistener;
	private View pointView;
	private int FragramPage = 0;
	private float screenWidth;
	
	public HomeFragment(TitlechangeListener mlistener) {
		this.mlistener = mlistener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		rootview = getLayoutInflater(savedInstanceState).inflate(
				R.layout.fragment_home, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return rootview;
	}

	@Override
	public void processHandlerMessage(Message msg) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		// TODO 自动生成的方法存根
		viewPager = (ViewPager) rootview.findViewById(R.id.viewpager);
		pointView = rootview.findViewById(R.id.point_view);
		fragments = new ArrayList<BaseFragment>();

		fragments.add(QiangFragment.newInstance(0));
		fragments.add(FocusFragment.newInstance(1));

		HomeContentAdapter adapter = new HomeContentAdapter(
				getFragmentManager(), fragments);
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(this);
		
		WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
	        DisplayMetrics outMetrics = new DisplayMetrics();
	        wm.getDefaultDisplay().getMetrics(outMetrics);
	        screenWidth = outMetrics.widthPixels;
		pointView.getLayoutParams().width=(int) (screenWidth/fragments.size());
	}

	@Override
	public void initData() {
		
		// TODO 自动生成的方法存根
	}
	public int getCurrentPage(){
		return FragramPage;
	}
	@Override
	public void initEvent() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO 自动生成的方法存根
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO 自动生成的方法存根
		LogUtils.i(TAG, arg0+":"+arg1+":"+arg2);
		if(arg0<0.9999f)
		pointView.setTranslationX(arg2/fragments.size());
	}

	@Override
	public void onPageSelected(int page) {
		// TODO 自动生成的方法存根
		mlistener.changePage(page);
		switch (page) {
		case 0:
			mlistener.changeTitle(R.string.title_qiang_all);
			FragramPage=0;
			break;
		case 1:
			mlistener.changeTitle(R.string.title_qiang_focus);
			FragramPage=1;
			break;
		}
	}
	

}
