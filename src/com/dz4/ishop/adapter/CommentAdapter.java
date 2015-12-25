package com.dz4.ishop.adapter;
import java.util.ArrayList;

import cn.bmob.v3.BmobUser;

import com.dz4.ishop.domain.Comment;
import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.ui.EditCommentActivity;
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



public class CommentAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<Comment> datalist;
	private QiangItem mQiangItem;
	public CommentAdapter(Context mContext, ArrayList<Comment> datalist,QiangItem mQiangItem) {
		// TODO Auto-generated constructor stub
		this.mContext=mContext;
		this.datalist=datalist;
		this.mQiangItem  =mQiangItem;
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
			viewHolder.replyComment = (TextView)convertView.findViewById(R.id.reply_comment);
			viewHolder.replyto = (TextView)convertView.findViewById(R.id.replyto);
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
			viewHolder.commentContent.setText(comment.getCommentContent());
			viewHolder.time.setText(comment.getCreatedAt());
			if(comment.getReplyTo()!=null) {
				viewHolder.replyto.setText("回复 "+comment.getReplyTo()+":");
				viewHolder.replyto.setVisibility(View.VISIBLE);
			}else{
				viewHolder.replyto.setVisibility(View.GONE);
			}
			ImageLoader.getInstance().displayImage(comment.getUser().getAvatar().getFileUrl(mContext), viewHolder.userIcon,ImageUtils.getOptions(R.drawable.user_icon_default_main));
			viewHolder.replyComment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent =new Intent(mContext,EditCommentActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(Constant.BUNDLE_KEY_QIANGITEM,mQiangItem);
					intent.putExtra(Constant.BUNDLE_KEY_REPLYTO, comment.getUser());
					mContext.startActivity(intent);
				}
			});
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
		public TextView replyComment;
		public TextView replyto;
	}
}
