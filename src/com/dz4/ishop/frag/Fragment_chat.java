package com.dz4.ishop.frag;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dz4.ishop.listener.TitlechangeListener;
import com.dz4.ishopping.R;
import com.dz4.support.widget.BaseFragment;
/**
 * 
 * 购物车的fragment
 * @author MZone
 *
 */
public class Fragment_chat extends  BaseFragment{
	private View rootview;
	private  Bundle savedInstanceState;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return rootview;
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootview =getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_chat, null);
	};
	@Override
	public void processHandlerMessage(Message msg) {
		// TODO 自动生成的方法存根
		
	}

	public void initView() {
		// TODO 自动生成的方法存根
		
	}

	public void initData() {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void initEvent() {
		// TODO 自动生成的方法存根
		
	}
}
