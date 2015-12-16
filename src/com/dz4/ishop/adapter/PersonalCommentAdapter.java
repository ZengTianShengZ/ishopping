package com.dz4.ishop.adapter;
import java.util.ArrayList;

import com.dz4.ishop.domain.Comment;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.ui.GoodsDetailActivity;
import com.dz4.ishop.ui.PersonalCommentActivity;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishopping.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class PersonalCommentAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<Comment> datalist;
	private User mUser;
	public PersonalCommentAdapter(Context mContext, ArrayList<Comment> datalist,User mUser) {
		// TODO Auto-generated constructor stub
		this.mContext=mContext;
		this.datalist=datalist;
		this.mUser = mUser;
		
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
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_personal_comment, null);
			viewHolder.commentContent = (TextView)convertView.findViewById(R.id.content_comment);
			viewHolder.commentFor = (TextView)convertView.findViewById(R.id.comment_for);
			viewHolder.userIcon = (ImageView)convertView.findViewById(R.id.userIcon_comment);
			viewHolder.time = (TextView)convertView.findViewById(R.id.time_comment);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		final Comment comment = datalist.get(position);
		if(mUser!=null){
			LogUtils.i("CommentActivity","NAME:"+comment.getUser().getNickname());
			viewHolder.commentContent.setText(comment.getCommentContent());
			viewHolder.time.setText(comment.getCreatedAt());
			viewHolder.commentFor.setText("评论至"+" ~"+comment.getQiang().getAuthor().getNickname());
			viewHolder.commentFor.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent(mContext,GoodsDetailActivity.class);
					intent.putExtra(Constant.BUNDLE_KEY_QIANGITEM, comment.getQiang());
					mContext.startActivity(intent);
				}
			});
			ImageLoader.getInstance().displayImage(mUser.getAvatar().getFileUrl(mContext), viewHolder.userIcon,ImageUtils.getOptions(R.drawable.user_icon_default_main));
			
		}
		return convertView;
	}
	public class ViewHolder{
		public TextView time;
		public ImageView userIcon;
		public TextView commentContent;
		public TextView commentFor;
	}
}
