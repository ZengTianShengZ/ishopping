package com.dz4.ishop.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.dz4.ishop.frag.QiangFragment;
import com.dz4.ishop.frag.HomeFragment;
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
public class HomeContentAdapter extends FragmentStatePagerAdapter {

	private ArrayList<BaseFragment> fragments;
	public HomeContentAdapter(FragmentManager fragmentManager, ArrayList<BaseFragment> fragments) {
		super(fragmentManager);
		this.fragments =fragments;
	}
	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
		
	}
	
	@Override
	public int getCount() {
		return fragments.size();
	}
	

}
