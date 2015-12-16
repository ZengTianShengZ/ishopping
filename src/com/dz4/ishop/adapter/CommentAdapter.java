package com.dz4.ishop.adapter;
import java.util.ArrayList;

import com.dz4.ishop.domain.Comment;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishopping.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class CommentAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<Comment> datalist;
	private int commentCount;
	public CommentAdapter(Context mContext, ArrayList<Comment> datalist) {
		// TODO Auto-generated constructor stub
		this.mContext=mContext;
		this.datalist=datalist;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_comment, null);
			viewHolder.userNick = (TextView)convertView.findViewById(R.id.userName_comment);
			viewHolder.commentContent = (TextView)convertView.findViewById(R.id.content_comment);
			viewHolder.index = (TextView)convertView.findViewById(R.id.index_comment);
			viewHolder.userIcon = (ImageView)convertView.findViewById(R.id.userIcon_comment);
			viewHolder.time = (TextView)convertView.findViewById(R.id.time_comment);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		final Comment comment = datalist.get(position);
		if(comment.getUser()!=null){
			viewHolder.userNick.setText(comment.getUser().getNickname());
			LogUtils.i("CommentActivity","NAME:"+comment.getUser().getNickname());
			viewHolder.index.setText((commentCount-position)+"楼");
			viewHolder.commentContent.setText(comment.getCommentContent());
			viewHolder.time.setText(comment.getCreatedAt());
			ImageLoader.getInstance().displayImage(comment.getUser().getAvatar().getFileUrl(mContext), viewHolder.userIcon,ImageUtils.getOptions(R.drawable.user_icon_default_main));
			
		}else{
			viewHolder.userNick.setText("墙友");
		}
		return convertView;
	}
	public class ViewHolder{
		public TextView userNick;
		public TextView time;
		public ImageView userIcon;
		public TextView commentContent;
		public TextView index;
	}
}
