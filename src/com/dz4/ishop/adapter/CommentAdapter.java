package com.dz4.ishop.adapter;
import java.util.ArrayList;
import com.dz4.ishop.domain.Comment;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishopping.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class CommentAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<Comment> datalist;
	public CommentAdapter(Context mContext, ArrayList<Comment> datalist) {
		// TODO Auto-generated constructor stub
		this.mContext=mContext;
		this.datalist=datalist;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_item, null);
			viewHolder.userName = (TextView)convertView.findViewById(R.id.userName_comment);
			viewHolder.commentContent = (TextView)convertView.findViewById(R.id.content_comment);
			viewHolder.index = (TextView)convertView.findViewById(R.id.index_comment);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		final Comment comment = datalist.get(position);
		if(comment.getUser()!=null){
			viewHolder.userName.setText(comment.getUser().getUsername());
			LogUtils.i("CommentActivity","NAME:"+comment.getUser().getUsername());
		}else{
			viewHolder.userName.setText("墙友");
		}
		viewHolder.index.setText((position+1)+"楼");
		viewHolder.commentContent.setText(comment.getCommentContent());
		
		return convertView;
	}
	public class ViewHolder{
		public TextView userName;
		public TextView commentContent;
		public TextView index;
	}
}
