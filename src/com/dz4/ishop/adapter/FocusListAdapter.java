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
public class FocusListAdapter extends BaseAdapter{
	private ArrayList<User> datalist;
	private Context mContext;
	private User user;
	public FocusListAdapter(Context mContext,ArrayList<User> datalist, User user){
		this.datalist=datalist;
		this.mContext=mContext;
		this.user = user;
		
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return datalist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return datalist.get(position);
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
			
			viewHolder.userNick = (TextView) convertView
					.findViewById(R.id.user_name);
			viewHolder.userLogo = (ImageView) convertView
					.findViewById(R.id.user_logo);
			viewHolder.cancelBtn = (TextView)convertView.findViewById(R.id.focus_cancel);
			viewHolder.userSignature = (TextView) convertView.findViewById(R.id.user_signature);
			convertView.setTag(viewHolder);
		}else{
			viewHolder =(ViewHolder)convertView.getTag();
		}
		//TODO
		final User focusUser= (User) datalist.get(position);
		viewHolder.userNick.setText(focusUser.getNickname());
		viewHolder.userSignature.setText(focusUser.getSignature());
		viewHolder.cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				BmobRelation relation = new BmobRelation();
				relation.remove(focusUser);
				user.setFocus(relation);
				user.update(mContext, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						Toast.makeText(mContext, "取消关注", Toast.LENGTH_SHORT).show();
						datalist.remove(focusUser);
						notifyDataSetChanged();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(mContext, "取消关注失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		viewHolder.userLogo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,PersonalActivity.class);
				intent.putExtra(Constant.BUNDLE_KEY_AUTHOR, focusUser);
				mContext.startActivity(intent);
			}
		});
		
		String Imageurl = null;
		if (focusUser.getAvatar()!=null) {
			Imageurl = focusUser.getAvatar().getFileUrl(mContext);
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
		public TextView userNick;
		private TextView cancelBtn;
	}
}
