package com.dz4.ishop.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.ui.LoginActivity;
import com.dz4.ishop.ui.PersonalActivity;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishop.view.innerGridView;
import com.dz4.ishopping.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 *  墙的ListView的适配器
 *  
 * @author MZone
 *
 */
public class ContactListAdapter extends BaseAdapter{
	private ArrayList<User> Datalist;
	private Context mContext;
	public ContactListAdapter(Context mContext,ArrayList<User> Datalist){
		this.Datalist=Datalist;
		this.mContext=mContext;
		
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return Datalist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return Datalist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		final ViewHolder viewHolder;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_focus, null);
			
			viewHolder.userName = (TextView) convertView
					.findViewById(R.id.user_name);
			viewHolder.userLogo = (ImageView) convertView
					.findViewById(R.id.user_logo);
			viewHolder.userSignature = (TextView) convertView
			.findViewById(R.id.user_signature);
			convertView.setTag(viewHolder);
		}else{
			viewHolder =(ViewHolder)convertView.getTag();
		}
		//TODO
		final User focus_User= (User) Datalist.get(position);
		viewHolder.userName.setText(focus_User.getUsername());
		viewHolder.userSignature.setText(focus_User.getSignature());
		viewHolder.userLogo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,PersonalActivity.class);
				intent.putExtra(Constant.BUNDLE_KEY_AUTHOR, focus_User);
				mContext.startActivity(intent);
			}
		});
		
		String Imageurl = null;
		if (focus_User.getAvatar()!=null) {
			Imageurl = focus_User.getAvatar().getFileUrl(mContext);
		}
		ImageLoader.getInstance().displayImage(Imageurl,viewHolder.userLogo, ImageUtils.getOptions(R.drawable.user_icon_default_main),new SimpleImageLoadingListener(){
					public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
						
					};
				} );
		return convertView;
	}
	public  class ViewHolder {
		public TextView userSignature;
		public ImageView userLogo;
		public TextView userName;
	}
}
