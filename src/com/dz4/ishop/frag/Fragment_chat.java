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
 * ���ﳵ��fragment
 * @author MZone
 *
 */
public class Fragment_chat extends  BaseFragment{
	private View rootview;
	private  Bundle savedInstanceState;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		return rootview;
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootview =getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_chat, null);
	};
	@Override
	public void processHandlerMessage(Message msg) {
		// TODO �Զ����ɵķ������
		
	}

	public void initView() {
		// TODO �Զ����ɵķ������
		
	}

	public void initData() {
		// TODO �Զ����ɵķ������
		
	}
	@Override
	public void initEvent() {
		// TODO �Զ����ɵķ������
		
	}
}
