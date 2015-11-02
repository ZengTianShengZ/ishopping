package com.dz4.ishop.adapter;

import java.util.ArrayList;

import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishopping.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
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
public class QiangListAdapter extends BaseAdapter{
	private ArrayList<QiangItem> Datalist;
	private Context mContext;
	public QiangListAdapter(Context mContext,ArrayList Datalist){
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_qiang, null);
			viewHolder.userName = (TextView) convertView
					.findViewById(R.id.user_name);
			viewHolder.userLogo = (ImageView) convertView
					.findViewById(R.id.user_logo);
			viewHolder.qiangtime = (TextView) convertView
					.findViewById(R.id.qiang_time);
			viewHolder.focus = (CheckBox) convertView
					.findViewById(R.id.item_action_focus);
			viewHolder.contentText = (TextView) convertView
					.findViewById(R.id.content_text);
			viewHolder.contentImage = (ImageView) convertView
					.findViewById(R.id.content_image);
			viewHolder.love = (TextView) convertView
					.findViewById(R.id.item_action_love);
			viewHolder.hate = (TextView) convertView
					.findViewById(R.id.item_action_hate);
			viewHolder.share = (TextView) convertView
					.findViewById(R.id.item_action_share);
			viewHolder.comment = (TextView) convertView
					.findViewById(R.id.item_action_comment);
			convertView.setTag(viewHolder);
		}else{
			viewHolder =(ViewHolder)convertView.getTag();
		}
		//TODO
		final QiangItem mQiangItem= (QiangItem) Datalist.get(position);
		viewHolder.contentText.setText(mQiangItem.getContent()+"");
		viewHolder.hate.setText(mQiangItem.getHate()+"");
		viewHolder.love.setText(mQiangItem.getLove()+"");
		viewHolder.userName.setText(mQiangItem.getAuthor().getUsername());
		viewHolder.qiangtime.setText(mQiangItem.getCreatedAt());
		
		if (null == mQiangItem.getContentfigureurl()) {
			viewHolder.contentImage.setVisibility(View.GONE);
		} else {
			viewHolder.contentImage.setVisibility(View.VISIBLE);
		ImageLoader.getInstance().displayImage(
				mQiangItem.getContentfigureurl().getFileUrl(mContext)==null ?"":mQiangItem.getContentfigureurl().getFileUrl(mContext),
				viewHolder.contentImage, ImageUtils.getOptions(R.drawable.bg_pic_loading),new SimpleImageLoadingListener(){
					public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
						
					};
				} );
		}
		
		
		String Imageurl = null;
		if (mQiangItem.getAuthor().getAvatar()!=null) {
			Imageurl = mQiangItem.getAuthor().getAvatar().getFileUrl(mContext);
		}
		ImageLoader.getInstance().displayImage(Imageurl,viewHolder.userLogo, ImageUtils.getOptions(R.drawable.user_icon_default_main),new SimpleImageLoadingListener(){
					public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
						
					};
				} );
		if (mQiangItem.isFocus()) {
			viewHolder.focus.setChecked(true);
		} else {
			viewHolder.focus.setChecked(false);
		}
		viewHolder.focus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mQiangItem.setFocus(!mQiangItem.isFocus());
				viewHolder.focus.setChecked(mQiangItem.isFocus());
			}
		});
		
		return convertView;
	}
	public  class ViewHolder {
		public ImageView userLogo;
		public TextView userName;
		public TextView qiangtime;
		public TextView contentText;
		public ImageView contentImage;

		public CheckBox focus;
		public TextView love;
		public TextView hate;
		public TextView share;
		public TextView comment;
	}
}
