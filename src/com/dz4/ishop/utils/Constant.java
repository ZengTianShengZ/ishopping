package com.dz4.ishop.utils;

/**
 * @author kingofglory email: kingofglory@yeah.net blog: http:www.google.com
 * @date 2014-2-21 TODO
 */

public interface Constant {

	//String BMOB_APP_ID = "070dd04beba0e7efd84a59875539029b";
	
	String BMOB_APP_ID = "cb23185f8c0196d590fe317d74689f34";
	String TABLE_AI = "Mood";
	String TABLE_COMMENT = "Comment";

	String NETWORK_TYPE_WIFI = "wifi";
	String NETWORK_TYPE_MOBILE = "mobile";
	String NETWORK_TYPE_ERROR = "error";

	int AI = 0;
	int HEN = 1;
	int CHUN_LIAN = 2;
	int BIAN_BAI = 3;

	int CONTENT_TYPE = 4;
	//MSG
	public static final int MSG_LOGIN_CHANGE =0x121212;
	public static final int MSG_USERINFO_CHANGE =0x111;

	public static final int PUBLISH_COMMENT = 1;
	public static final int NUMBERS_PER_PAGE = 15;// 每次请求返回评论条数
	public static final int SAVE_FAVOURITE = 2;
	public static final int GET_FAVOURITE = 3;
	public static final int GO_SETTINGS = 4;

	public static final String SEX_MALE = "male";
	public static final String SEX_FEMALE = "female";
	
	public static final String BUNDLE_KEY_SIGNATURE = "pass_signature";
	public static final String BUNDLE_KEY_USER = "pass_user";
	public static final String BUNDLE_KEY_HANDLER = "pass_Hanlder";
	public static final String BUNDLE_KEY_AUTHOR = "pass_author";
	public static final String BUNDLE_KEY_QIANGITEM = "pass_qiangitem";
	
	
	/**
	 * sharepreference
	 */
	public static final String PRE_NAME = "my_pre";
	public static final String SHARE_KEY_LOGINFLAG ="islogin";
}
