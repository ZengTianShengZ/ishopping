package com.dz4.ishop.adapter;

import java.util.ArrayList;

import com.dz4.ishop.adapter.QiangListAdapter.ViewHolder;
import com.dz4.ishop.ui.ShowImageActivity;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishopping.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class GridViewAdapter extends BaseAdapter {
	private ArrayList<String> paths;
	private Context mContext;

	public GridViewAdapter(Context mContext,ArrayList<String> paths){
		this.paths = paths;
		this.mContext=mContext;
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return paths.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return paths.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.zss_show_image, null);
			viewHolder.ImageView = (ImageView) convertView.findViewById(R.id.id_show_image);
			convertView.setTag(viewHolder);
		}else{
			viewHolder= (ViewHolder)convertView.getTag();
		}
		String itempath=paths.get(position);
		ImageLoader.getInstance().displayImage(
				itempath==null ?"":itempath,
				viewHolder.ImageView, ImageUtils.getOptions(R.drawable.bg_pic_loading),new SimpleImageLoadingListener(){
					public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
						
					};
				} );
		
		return convertView;
	}
	public class ViewHolder{
		private ImageView ImageView;
	}

}
