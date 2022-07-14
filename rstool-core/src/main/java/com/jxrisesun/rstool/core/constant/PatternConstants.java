package com.jxrisesun.rstool.core.constant;

import java.util.regex.Pattern;

/**
 * 正则模式常量
 * @author zhangzl
 *
 */
public class PatternConstants {

	/** 英文字母 、数字和下划线 */
	public final static Pattern PATTERN_GENERAL = Pattern.compile("^\\w+$");
	
	/** 数字 */
	public final static Pattern PATTERN_NUMBERS = Pattern.compile("\\d+");
	
	/** 字母 */
	public final static Pattern PATTERN_LETTERS = Pattern.compile("[a-zA-Z]+");
	
	/** 单个中文汉字 */
	public final static Pattern PATTERN_CHINESE = Pattern.compile("[\u4E00-\u9FFF]");
	
	/** 中文汉字 */
	public final static Pattern PATTERN_CHINESES = Pattern.compile("[\u4E00-\u9FFF]+");
	
	/** 邮箱 */
	public final static Pattern PATTERN_EMAIL = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", Pattern.CASE_INSENSITIVE);
	
	/** 邮编 */
	public final static Pattern PATTERN_ZIP_CODE = Pattern.compile("[1-9]\\d{5}(?!\\d)");
	
	/** 18位身份证号码 */
	public final static Pattern PATTERN_CITIZEN_ID = Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)");
	
	/** 生日 */
	public final static Pattern PATTERN_BIRTHDAY = Pattern.compile("^(\\d{2,4})([/\\-.年]?)(\\d{1,2})([/\\-.月]?)(\\d{1,2})日?$");
	
	/** IP v4 */
	public final static Pattern PATTERN_IPV4 = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

	/** MAC 地址 */
	public static final Pattern PATTERN_MAC_ADDRESS = Pattern.compile("((?:[A-F0-9]{1,2}[:-]){5}[A-F0-9]{1,2})|(?:0x)(\\d{12})(?:.+ETHER)", Pattern.CASE_INSENSITIVE);
}
