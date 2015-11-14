package com.dz4.ishop.frag;

import java.util.ArrayList;
import java.util.zip.Inflater;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dz4.ishop.adapter.QiangContentAdapter;
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
public class Fragment_home extends BaseFragment implements
		ViewPager.OnPageChangeListener {
	private View rootview;
	private ArrayList<BaseFragment> fragments;
	private ViewPager viewPager;
	private TitlechangeListener mlistener;
	private int FragramPage = 0;
	
	public Fragment_home(TitlechangeListener mlistener) {
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
	}

	@Override
	public void initData() {
		fragments = new ArrayList<BaseFragment>();

		fragments.add(Fragment_Qiang.newInstance(0));
		fragments.add(Fragment_Qiang_focus.newInstance(1));

		QiangContentAdapter adapter = new QiangContentAdapter(
				getFragmentManager(), fragments);
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(this);
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
		case 2:
			mlistener.changeTitle(R.string.title_qiang_fav);
			FragramPage=2;
			break;
		}
	}
	

}
