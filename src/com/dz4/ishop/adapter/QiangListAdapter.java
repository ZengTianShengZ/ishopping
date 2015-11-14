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
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishop.view.innerGridView;
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
	private IshopApplication app;
	private GridViewAdapter mGridViewAdapter;
	public QiangListAdapter(Context mContext,ArrayList Datalist, IshopApplication app){
		this.Datalist=Datalist;
		this.mContext=mContext;
		this.app=app;
		
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
			viewHolder.contentImage = (innerGridView) convertView
					.findViewById(R.id.content_image_gridView);
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
			//viewHolder.contentImage.setAdapter(adapter);
//		ImageLoader.getInstance().displayImage(
//				mQiangItem.getContentfigureurl().getFileUrl(mContext)==null ?"":mQiangItem.getContentfigureurl().getFileUrl(mContext),
//				viewHolder.contentImage, ImageUtils.getOptions(R.drawable.bg_pic_loading),new SimpleImageLoadingListener(){
//					public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
//						
//					};
//				} );
		//TODO
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
			viewHolder.contentImage.setFocusable(false);
			viewHolder.contentImage.setClickable(false);
			viewHolder.contentImage.setPressed(false);
			viewHolder.contentImage.setEnabled(false);
		}
		
		
		String Imageurl = null;
		if (mQiangItem.getAuthor().getAvatar()!=null) {
			Imageurl = mQiangItem.getAuthor().getAvatar().getFileUrl(mContext);
		}
		ImageLoader.getInstance().displayImage(Imageurl,viewHolder.userLogo, ImageUtils.getOptions(R.drawable.user_icon_default_main),new SimpleImageLoadingListener(){
					public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
						
					};
				} );
		if(mQiangItem.isFocus()){
			viewHolder.focus.setChecked(true);
		}else{
			viewHolder.focus.setChecked(false);
		}
		viewHolder.focus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				User user =app.getCurrentUser();
				if(user==null){
					return ;
				}
				User targetUser = mQiangItem.getAuthor();
				BmobRelation relation =null;
				if(!mQiangItem.isFocus()){
					if(user.getFocus()==null){
						user.setFocus(new BmobRelation());
					}
					user.getFocus().add(targetUser);
					//user.setFocus(relation);
					user.update(mContext, new UpdateListener() {
						@Override
						public void onSuccess() {
							mQiangItem.setFocus(true);
							Toast.makeText(mContext, "已关注", Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onFailure(int arg0, String arg1) {
							Toast.makeText(mContext, "关注失败"+arg1, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		});
		return convertView;
	}
	public  class ViewHolder {
		public ImageView userLogo;
		public TextView userName;
		public TextView qiangtime;
		public TextView contentText;
		public innerGridView contentImage;

		public CheckBox focus;
		public TextView love;
		public TextView hate;
		public TextView share;
		public TextView comment;
	}
}
