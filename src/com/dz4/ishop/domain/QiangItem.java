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
	private BmobFile contentfigureurl;
 	private BmobFile contentfigureurl1;
	private BmobFile contentfigureurl2;
	private BmobFile contentfigureurl3;
	private BmobFile contentfigureurl4;
	private BmobFile contentfigureurl5;
	private BmobFile contentfigureurl6;
	private BmobFile contentfigureurl7;
	private BmobFile contentfigureurl8;
	
	private int love;
	private int hate;
	private int share;
	private int comment;
	
	private boolean isPass;
	private boolean focus;//¹Ø×¢
	private boolean myLove;//ÔÞ
	private BmobRelation comments;
	
	private Goods goods;
	
	public Goods getGoods() {
		return this.goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public void setBmobFileList(BmobFile contentfigureurls,int item){
		switch (item) {
		case 0:
			setContentfigureurl(contentfigureurls);
			break;
		case 1:
			setContentfigureurl1(contentfigureurls);
			break;
		case 2:
			setContentfigureurl2(contentfigureurls);
			break;
		case 3:
			setContentfigureurl3(contentfigureurls);
			break;
			
		case 4:
			setContentfigureurl4(contentfigureurls);
			break;
		case 5:
			setContentfigureurl5(contentfigureurls);
			break;
		case 6:
			setContentfigureurl6(contentfigureurls);
			break;
		case 7:
			setContentfigureurl7(contentfigureurls);
			break;
		case 8:
			setContentfigureurl8(contentfigureurls);
			break;

		default:
			break;
		}
	}
	
	public BmobFile getContentfigureurl1() {
		return this.contentfigureurl1;
	}
	public void setContentfigureurl1(BmobFile contentfigureurl1) {
		this.contentfigureurl1 = contentfigureurl1;
	}
	public BmobFile getContentfigureurl2() {
		return this.contentfigureurl2;
	}
	public void setContentfigureurl2(BmobFile contentfigureurl2) {
		this.contentfigureurl2 = contentfigureurl2;
	}
	public BmobFile getContentfigureurl3() {
		return this.contentfigureurl3;
	}
	public void setContentfigureurl3(BmobFile contentfigureurl3) {
		this.contentfigureurl3 = contentfigureurl3;
	}
	public BmobFile getContentfigureurl4() {
		return this.contentfigureurl4;
	}
	public void setContentfigureurl4(BmobFile contentfigureurl4) {
		this.contentfigureurl4 = contentfigureurl4;
	}
	public BmobFile getContentfigureurl5() {
		return this.contentfigureurl5;
	}
	public void setContentfigureurl5(BmobFile contentfigureurl5) {
		this.contentfigureurl5 = contentfigureurl5;
	}
	public BmobFile getContentfigureurl6() {
		return this.contentfigureurl6;
	}
	public void setContentfigureurl6(BmobFile contentfigureurl6) {
		this.contentfigureurl6 = contentfigureurl6;
	}
	public BmobFile getContentfigureurl7() {
		return this.contentfigureurl7;
	}
	public void setContentfigureurl7(BmobFile contentfigureurl7) {
		this.contentfigureurl7 = contentfigureurl7;
	}
	public BmobFile getContentfigureurl8() {
		return this.contentfigureurl8;
	}
	public void setContentfigureurl8(BmobFile contentfigureurl8) {
		this.contentfigureurl8 = contentfigureurl8;
	}
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
		return this.contentfigureurl;
	}
	public void setContentfigureurl(BmobFile contentfigureurl) {
		this.contentfigureurl = contentfigureurl;
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
	public BmobRelation getComments() {
		return this.comments;
	}
	public void setComments(BmobRelation comments) {
		this.comments = comments;
	}
	@Override
	public String toString() {
		return "QiangItem [author=" + this.author + ", content="
				+ this.content + ", Contentfigureurl="
				+ this.contentfigureurl + ", love=" + this.love
				+ ", hate=" + this.hate + ", share="
				+ this.share + ", comment=" + this.comment
				+ ", isPass=" + this.isPass + ", myFav="
				+ this.focus + ", myLove=" + this.myLove
				+ ", comments=" + this.comments + "]";
	}
	
	
}
