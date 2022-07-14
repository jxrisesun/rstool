package com.jxrisesun.rstool.core.constant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * 字符集常量
 * @author zhangzl
 *
 */
public class CharsetConstants {

	/** ISO-8859-1 */
	public static final String ISO_8859_1 = "ISO-8859-1";
	
	/** UTF-8 */
	public static final String UTF_8 = "UTF-8";
	
	/** GBK */
	public static final String GBK = "GBK";

	/** ISO-8859-1 */
	public static final Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1;
	
	/** UTF-8 */
	public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;
	
	/** GBK */
	public static final Charset CHARSET_GBK;
	
	static {
		//避免不支持GBK的系统中运行报错 issue#731
		Charset gbk = null;
		try {
			gbk = Charset.forName(GBK);
		} catch (UnsupportedCharsetException e) {
			//ignore
		}
		CHARSET_GBK = gbk;
	}
}
