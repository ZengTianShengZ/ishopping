package com.dz4.ishop.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
/**
 * 
 * Ê×Ò³µÄÇ½ÓïJavaBean
 * @author MZone
 *
 */
public class QiangItem extends BmobObject{
	private User author;
	private String content;
	private BmobFile Contentfigureurl;
	
	private int love;
	private int hate;
	private int share;
	private int comment;
	
	private boolean isPass;
	private boolean focus;//¹Ø×¢
	private boolean myLove;//ÔÞ
	private BmobRelation relation;
	public User getAuthor() {
		return this.author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public String getContent() {
		return this.content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public BmobFile getContentfigureurl() {
		return this.Contentfigureurl;
	}
	public void setContentfigureurl(BmobFile contentfigureurl) {
		this.Contentfigureurl = contentfigureurl;
	}
	public int getLove() {
		return this.love;
	}
	public void setLove(int love) {
		this.love = love;
	}
	public int getHate() {
		return this.hate;
	}
	public void setHate(int hate) {
		this.hate = hate;
	}
	public int getShare() {
		return this.share;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public int getComment() {
		return this.comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public boolean isPass() {
		return this.isPass;
	}
	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}
	public boolean isFocus() {
		return this.focus;
	}
	public void setFocus(boolean focus) {
		this.focus = focus;
	}
	public boolean isMyLove() {
		return this.myLove;
	}
	public void setMyLove(boolean myLove) {
		this.myLove = myLove;
	}
	public BmobRelation getRelation() {
		return this.relation;
	}
	public void setRelation(BmobRelation relation) {
		this.relation = relation;
	}
	@Override
	public String toString() {
		return "QiangItem [author=" + this.author + ", content="
				+ this.content + ", Contentfigureurl="
				+ this.Contentfigureurl + ", love=" + this.love
				+ ", hate=" + this.hate + ", share="
				+ this.share + ", comment=" + this.comment
				+ ", isPass=" + this.isPass + ", myFav="
				+ this.focus + ", myLove=" + this.myLove
				+ ", relation=" + this.relation + "]";
	}
	
	
}
