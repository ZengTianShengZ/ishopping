package com.dz4.ishop.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class PushNews extends BmobObject{
	
	private String newsTitle;
	private BmobFile newsImage;
	private String newsContent;
	private String newsUrl;
	public String getNewsUrl() {
		return newsUrl;
	}
	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	public BmobFile getNewsImage() {
		return newsImage;
	}
	public void setNewsImage(BmobFile newsImage) {
		this.newsImage = newsImage;
	}
	public String getNewsContent() {
		return newsContent;
	}
	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}
	
	@Override
	public String toString() {
		return "PushNews [newsTitle=" + this.newsTitle + ", newsImage="
				+ this.newsImage + ", newsContent="
				+ this.newsContent + "]";
	}

}
