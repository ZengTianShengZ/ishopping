package com.dz4.ishop.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.dz4.ishop.frag.Fragment_Qiang;
import com.dz4.ishop.frag.Fragment_home;
import com.dz4.ishop.listener.TitlechangeListener;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishopping.R;
import com.dz4.support.widget.BaseFragment;
/**
 * viepagerµƒ  ≈‰∆˜
 * 
 * @author MZone
 * 
 *
 */
public class QiangContentAdapter extends FragmentStatePagerAdapter {

	private ArrayList<BaseFragment> fragments;
	public QiangContentAdapter(FragmentManager fragmentManager, ArrayList<BaseFragment> fragments) {
		super(fragmentManager);
		// TODO Auto-generated constructor stub
		this.fragments =fragments;
	}
	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}
	

}
