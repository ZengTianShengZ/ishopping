package com.dz4.ishop.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ������ʽ ����
 * @author ZTS
 *
 */
public class RegularExpression {

	/**
	 * ������ʽ ��֤�ֻ����Ƿ���� ����
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
	 * ������ʽ��֤ �Ƿ�Ϊ ��  6 λ ����
	 * @param sixNumber
	 * @return Boolean
	 */
	public static boolean RegExp_is_SixNumber(String sixNumber){
		
		String regExp_SMS_code ="^[0-9]{6}$"; // ֻ���� 6λ����
	    Pattern p_s = Pattern.compile(regExp_SMS_code);  				  
	    Matcher m_s = p_s.matcher(sixNumber);
	    
	    return m_s.find(); // boolean
	}
	/**
	 * ������ʽ��֤ �Ƿ�Ϊ ��  ����
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
