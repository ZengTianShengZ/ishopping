package com.dz4.ishop.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject implements Serializable{
	
	public static final String TAG = "Comment";

	private User user;
	private QiangItem qiang;
	
	public QiangItem getQiang() {
		return this.qiang;
	}
	public void setQiang(QiangItem qiang) {
		this.qiang = qiang;
	}
	private String commentContent;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	
	

}
