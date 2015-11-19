package com.dz4.ishop.domain;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 
 * UserµÄJavaBean
 * @author MZone
 *
 */
public class User extends BmobUser implements Serializable{
	
	private String signature;
	private BmobFile avatar;
	private BmobRelation favorite;
	private BmobRelation focus;
	private String sex;
	private String location;

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public BmobRelation getFavorite() {
		return favorite;
	}

	public void setFavorite(BmobRelation favorite) {
		this.favorite = favorite;
	}

	public BmobRelation getFocus() {
		return this.focus;
	}

	public void setFocus(BmobRelation focus) {
		this.focus = focus;
	}

	public BmobFile getAvatar() {
		return avatar;
	}

	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}


	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	
	
	
	
	
}
