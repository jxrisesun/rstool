package com.jxrisesun.rstool.core.util;

/**
 * 字符工具类
 * @author zhangzl
 *
 */
public class CharUtils {

	/** 斜杠 */
	public static final char CHAR_SLASH = '/';
	
	/** 反斜杠 */
	public static final char CHAR_BACKSLASH = '\\';
	
	/** 字符常量：花括号（左） <code>'{'</code> */
	public static final char CHAR_DELIM_START = '{';
	
	/**  字符常量：花括号（右） <code>'}'</code> */
	public static final char CHAR_DELIM_END = '}';
	
	/**
	 * 是否为字母（包括大写字母和小写字母），字母包括A~Z和a~z
	 * @param ch
	 * @return
	 */
	public static boolean isLetter(char ch) {
		return isLetterUpper(ch) || isLetterLower(ch);
	}
	
	/**
	 * 是否为大写字母，大写字母包括A~Z
	 * @param ch
	 * @return
	 */
	public static boolean isLetterUpper(final char ch) {
		return ch >= 'A' && ch <= 'Z';
	}
	
	/**
	 * 是否为小写字母,小写字母包括a~z
	 * @param ch
	 * @return
	 */
	public static boolean isLetterLower(final char ch) {
		return ch >= 'a' && ch <= 'z';
	}
	
	/**
	 * 是否为数字,数字包括0~9
	 * @param ch
	 * @return
	 */
	public static boolean isNumber(char ch) {
		return ch >= '0' && ch <= '9';
	}
	
	/**
	 * 是否为字符类型
	 * @param value
	 * @return
	 */
	public static boolean isChar(Object value) {
		return value != null && (value instanceof Character || value.getClass() == char.class);
	}
	
	/**
	 * 是否为空白字符,空白符包括空格、制表符、全角空格和不间断空格
	 * @param c
	 * @return
	 */
	public static boolean isBlankChar(char c) {
		return isBlankChar((int) c);
	}
	
	/**
	 * 是否空白符<br>
	 * 空白符包括空格、制表符、全角空格和不间断空格<br>
	 *
	 * @param c 字符
	 * @return 是否空白符
	 * @see Character#isWhitespace(int)
	 * @see Character#isSpaceChar(int)
	 */
	public static boolean isBlankChar(int c) {
		return Character.isWhitespace(c)
				|| Character.isSpaceChar(c)
				|| c == '\ufeff'
				|| c == '\u202a'
				|| c == '\u0000';
	}
	
	/**
	 * 是否为16进制规范的字符，判断是否为如下字符
	 * <pre>
	 * 1. 0~9
	 * 2. a~f
	 * 4. A~F
	 * </pre>
	 *
	 * @param c 字符
	 * @return 是否为16进制规范的字符
	 */
	public static boolean isHexChar(char c) {
		return isNumber(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
	}
	
	/**
	 * 是否为emoji表情符
	 * @param c
	 * @return
	 */
	public static boolean isEmoji(char c) {
		return false ==  ((c == 0x0) || 
				(c == 0x9) || 
				(c == 0xA) || 
				(c == 0xD) || 
				((c >= 0x20) && (c <= 0xD7FF)) || 
				((c >= 0xE000) && (c <= 0xFFFD)) || 
				((c >= 0x10000) && (c <= 0x10FFFF)));
	}
	
	/**
	 * 是否为ASCII字符，ASCII字符位于0~127之间
	 *
	 * <pre>
	 *   isAscii('a')  = true
	 *   isAscii('A')  = true
	 *   isAscii('3')  = true
	 *   isAscii('-')  = true
	 *   isAscii('\n') = true
	 *   isAscii('&copy;') = false
	 * </pre>
	 *
	 * @param ch 被检查的字符处
	 * @return true表示为ASCII字符，ASCII字符位于0~127之间
	 */
	public static boolean isAscii(char ch) {
		return ch < 128;
	}

	/**
	 * 是否为可见ASCII字符，可见字符位于32~126之间
	 *
	 * <pre>
	 *   isAsciiPrintable('a')  = true
	 *   isAsciiPrintable('A')  = true
	 *   isAsciiPrintable('3')  = true
	 *   isAsciiPrintable('-')  = true
	 *   isAsciiPrintable('\n') = false
	 *   isAsciiPrintable('&copy;') = false
	 * </pre>
	 *
	 * @param ch 被检查的字符处
	 * @return true表示为ASCII可见字符，可见字符位于32~126之间
	 */
	public static boolean isAsciiPrintable(char ch) {
		return ch >= 32 && ch < 127;
	}

	/**
	 * 是否为ASCII控制符（不可见字符），控制符位于0~31和127
	 *
	 * <pre>
	 *   isAsciiControl('a')  = false
	 *   isAsciiControl('A')  = false
	 *   isAsciiControl('3')  = false
	 *   isAsciiControl('-')  = false
	 *   isAsciiControl('\n') = true
	 *   isAsciiControl('&copy;') = false
	 * </pre>
	 *
	 * @param ch 被检查的字符
	 * @return true表示为控制符，控制符位于0~31和127
	 */
	public static boolean isAsciiControl(final char ch) {
		return ch < 32 || ch == 127;
	}
	
    /**
     * Indicates whether {@code ch} is a high- (or leading-) surrogate code unit
     * that is used for representing supplementary characters in UTF-16
     * encoding.
     *
     * @param ch
     *            the character to test.
     * @return {@code true} if {@code ch} is a high-surrogate code unit;
     *         {@code false} otherwise.
     */
    public static boolean isHighSurrogate(char ch) {
        return ('\uD800' <= ch && '\uDBFF' >= ch);
    }
    
    /**
	 * 获取给定字符的16进制数值
	 *
	 * @param b 字符
	 * @return 16进制字符
	 * @since 5.3.1
	 */
	public static int digit16(int b) {
		return Character.digit(b, 16);
	}
	
    /**
	 * 将字母、数字转换为带圈的字符：
	 * <pre>
	 *     '1' -》 '①'
	 *     'A' -》 'Ⓐ'
	 *     'a' -》 'ⓐ'
	 * </pre>
	 * <p>
	 * 获取带圈数字 /封闭式字母数字 ，从1-20,超过1-20报错
	 *
	 * @param c 被转换的字符，如果字符不支持转换，返回原字符
	 * @return 转换后的字符
	 * @see <a href="https://en.wikipedia.org/wiki/List_of_Unicode_characters#Unicode_symbols">Unicode_symbols</a>
	 * @see <a href="https://en.wikipedia.org/wiki/Enclosed_Alphanumerics">Alphanumerics</a>
	 */
	public static char toCloseChar(char c) {
		int result = c;
		if (c >= '1' && c <= '9') {
			result = '①' + c - '1';
		} else if (c >= 'A' && c <= 'Z') {
			result = 'Ⓐ' + c - 'A';
		} else if (c >= 'a' && c <= 'z') {
			result = 'ⓐ' + c - 'a';
		}
		return (char) result;
	}

	/**
	 * 将[1-20]数字转换为带圈的字符：
	 * <pre>
	 *     1 -》 '①'
	 *     12 -》 '⑫'
	 *     20 -》 '⑳'
	 * </pre>
	 * 也称作：封闭式字符，英文：Enclosed Alphanumerics
	 *
	 * @param number 被转换的数字
	 * @return 转换后的字符
	 * @author dazer
	 * @see <a href="https://en.wikipedia.org/wiki/List_of_Unicode_characters#Unicode_symbols">维基百科wikipedia-Unicode_symbols</a>
	 * @see <a href="https://zh.wikipedia.org/wiki/Unicode%E5%AD%97%E7%AC%A6%E5%88%97%E8%A1%A8">维基百科wikipedia-Unicode字符列表</a>
	 * @see <a href="https://coolsymbol.com/">coolsymbol</a>
	 * @see <a href="https://baike.baidu.com/item/%E7%89%B9%E6%AE%8A%E5%AD%97%E7%AC%A6/112715?fr=aladdin">百度百科 特殊字符</a>
	 */
	public static char toCloseByNumber(int number) {
		if (number > 20) {
			throw new IllegalArgumentException("Number must be [1-20]");
		}
		return (char) ('①' + number - 1);
	}
	
	/**
	 * 比较两个字符是否相同
	 *
	 * @param c1         字符1
	 * @param c2         字符2
	 * @param ignoreCase 是否忽略大小写
	 * @return 是否相同
	 */
	public static boolean equals(char c1, char c2, boolean ignoreCase) {
		if (ignoreCase) {
			return Character.toLowerCase(c1) == Character.toLowerCase(c2);
		}
		return c1 == c2;
	}
}
