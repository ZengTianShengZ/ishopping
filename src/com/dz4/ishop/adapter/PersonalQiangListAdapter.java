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
import com.dz4.ishop.ui.EditQiangActivity;
import com.dz4.ishop.ui.GoodsDetailActivity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
public class PersonalQiangListAdapter extends BaseAdapter {
	private final String TAG = "QiangListAdapter";
	private ArrayList<QiangItem> datalist;
	private Context mContext;
	private BmobRelation focusRelation;
	private GridViewAdapter mGridViewAdapter;
	private User user;
	public PersonalQiangListAdapter(Context mContext,ArrayList datalist,User user){
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_qiang, null);
			viewHolder.nickName = (TextView) convertView
					.findViewById(R.id.user_name);
			viewHolder.userLogo = (ImageView) convertView
					.findViewById(R.id.user_logo);
			viewHolder.qiangtime = (TextView) convertView
					.findViewById(R.id.qiang_time);
			viewHolder.contentText = (TextView) convertView
					.findViewById(R.id.content_text);
			viewHolder.contentImage = (innerGridView) convertView
					.findViewById(R.id.content_image_gridView);
			viewHolder.love = (TextView) convertView
					.findViewById(R.id.item_action_love);
			viewHolder.hate = (TextView) convertView
					.findViewById(R.id.item_action_hate);
			viewHolder.share = (TextView) convertView
					.findViewById(R.id.item_action_share);
			convertView.setTag(viewHolder);
		}else{
			viewHolder =(ViewHolder)convertView.getTag();
		}
		//TODO
		final QiangItem mQiangItem= (QiangItem) datalist.get(position);
		viewHolder.contentText.setText(mQiangItem.getContent()+"");
		viewHolder.hate.setText(mQiangItem.getHate()+"");
		viewHolder.love.setText(mQiangItem.getLove()+"");
		viewHolder.nickName.setText(mQiangItem.getAuthor().getNickname());
		viewHolder.qiangtime.setText(mQiangItem.getCreatedAt());
		
		//当点击头像
		viewHolder.userLogo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if( mQiangItem.getAuthor()==null) return;
				if( user==null) {
					Intent intent = new Intent(mContext,PersonalActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(Constant.BUNDLE_KEY_AUTHOR, mQiangItem.getAuthor());
					mContext.startActivity(intent);
				}else{
					if(user.getObjectId().equalsIgnoreCase(mQiangItem.getAuthor().getObjectId()))
						return;
					Intent intent = new Intent(mContext,PersonalActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(Constant.BUNDLE_KEY_AUTHOR, mQiangItem.getAuthor());
					mContext.startActivity(intent);
				}
			}
			
		});
		viewHolder.contentImage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(mContext,GoodsDetailActivity.class);
				intent.putExtra(Constant.BUNDLE_KEY_QIANGITEM, mQiangItem);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,GoodsDetailActivity.class);
				intent.putExtra(Constant.BUNDLE_KEY_QIANGITEM, mQiangItem);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
			
		});
		if (null == mQiangItem.getContentfigureurl()) {
			viewHolder.contentImage.setVisibility(View.GONE);
		} else {
			viewHolder.contentImage.setVisibility(View.VISIBLE);
 
			ArrayList<String> paths = new ArrayList<String>();
			if(mQiangItem.getContentfigureurl()!=null)
				paths.add(mQiangItem.getContentfigureurl().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl1()!=null)
				paths.add(mQiangItem.getContentfigureurl1().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl2()!=null)
				paths.add(mQiangItem.getContentfigureurl2().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl3()!=null)
				paths.add(mQiangItem.getContentfigureurl3().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl4()!=null)
				paths.add(mQiangItem.getContentfigureurl4().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl5()!=null)
				paths.add(mQiangItem.getContentfigureurl5().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl6()!=null)
				paths.add(mQiangItem.getContentfigureurl6().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl7()!=null)
				paths.add(mQiangItem.getContentfigureurl7().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl8()!=null)
				paths.add(mQiangItem.getContentfigureurl8().getFileUrl(mContext));
			mGridViewAdapter=new GridViewAdapter(mContext, paths);
			viewHolder.contentImage.setAdapter(mGridViewAdapter);
		}
		
		
		String Imageurl = null;
		if (mQiangItem.getAuthor().getAvatar()!=null) {
			Imageurl = mQiangItem.getAuthor().getAvatar().getFileUrl(mContext);
		}
		ImageLoader.getInstance().displayImage(Imageurl,viewHolder.userLogo, ImageUtils.getOptions(R.drawable.user_icon_default_main),new SimpleImageLoadingListener(){
					public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
						
					};
				} );
	 
		return convertView;
	}
	public  class ViewHolder {
		public ImageView userLogo;
		public TextView nickName;
		public TextView qiangtime;
		public TextView contentText;
		public innerGridView contentImage;

		public TextView love;
		public TextView hate;
		public TextView share;
		public TextView comment;
	}
}
