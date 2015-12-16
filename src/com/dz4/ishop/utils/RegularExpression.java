package com.dz4.ishop.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式 工具
 * @author ZTS
 *
 */
public class RegularExpression {

	/**
	 * 正则表达式 验证手机号是否符合 正常
	 * @param telehoneNumber
	 * @return  boolean
	 */
	public static boolean RegExp_telephoneNumber(String telehoneNumber){
		
		String regExp_telephone = "^((13[0-9])|(15[^4,\\D])|(18[0,1,5-9]))\\d{8}$";  
		Pattern p_t = Pattern.compile(regExp_telephone);  				  
	    Matcher m_t = p_t.matcher(telehoneNumber); 
	    
		return m_t.find(); // boolean		
		
	}
	
	/**
	 * 正则表达式验证 是否为 纯  6 位 数字
	 * @param sixNumber
	 * @return Boolean
	 */
	public static boolean RegExp_is_SixNumber(String sixNumber){
		
		String regExp_SMS_code ="^[0-9]{6}$"; // 只能是 6位数字
	    Pattern p_s = Pattern.compile(regExp_SMS_code);  				  
	    Matcher m_s = p_s.matcher(sixNumber);
	    
	    return m_s.find(); // boolean
	}
	/**
	 * 正则表达式验证 是否为 纯  数字
	 * @param allNumber
	 * @return Boolean
	 */
	public static boolean RegExp_is_allNumber(String allNumber){
		
		String regExp_code ="^[0-9]*$";  
	    Pattern p_s = Pattern.compile(regExp_code);  				  
	    Matcher m_s = p_s.matcher(allNumber);
	    
	    return m_s.find(); // boolean
	}
}
