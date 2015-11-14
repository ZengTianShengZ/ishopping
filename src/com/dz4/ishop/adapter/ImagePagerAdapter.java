package com.dz4.ishop.adapter;

import java.util.ArrayList;

import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishopping.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;


public class ImagePagerAdapter extends PagerAdapter {
	private ArrayList<String> urls;
	private Context mContext;
	private ImageView imageview;
	public ImagePagerAdapter(Context mContext,ArrayList<String> urls){
		this.urls= urls;
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		return urls.size();
	}
	
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view==obj;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		imageview = null;
		imageview=new ImageView(mContext);
		imageview.setScaleType(ScaleType.CENTER_CROP);
		ImageLoader.getInstance().displayImage(urls.get(position),imageview,ImageUtils.getOptions(R.drawable.open_picture));
		container.addView(imageview);
		return imageview;
	}
	public void destroyItem(View arg0, int position, Object arg2) {
		
                ((ViewPager) arg0).removeView((View)arg2);  
            }
	

}
