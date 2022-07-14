package com.jxrisesun.rstool.mybatis.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * sql 工具类
 * @author zhangzl
 *
 */
public class SqlUtils {

	/** 默认关键字前缀 */
	public static char DEFAULT_KEYWORD_PREFIX = '`';
	
	/** 默认关键字后缀 */
	public static char DEFAULT_KEYWORD_SUFFIX = '`';
	
	/**
	 * 生成 select部分"字段"语句
	 * @param fields
	 * @return
	 */
	public static String generateSelectFieldSql(String... fields) {
		return generateSelectFieldSql(DEFAULT_KEYWORD_PREFIX, DEFAULT_KEYWORD_SUFFIX, fields);
	}
	
	/**
	 * 生成 select部分 "字段"语句
	 * @param keywordPrefix 关键字前缀
	 * @param keywordSuffix 关键字后缀
	 * @param fields
	 * @return
	 */
	public static String generateSelectFieldSql(char keywordPrefix, char keywordSuffix, String... fields) {
		StringBuilder str = new StringBuilder();
		int index = 0;
		for(String field : fields) {
			if(index > 0) {
				str.append(",");
			}
			if(keywordPrefix > 0) {
				str.append(keywordPrefix);
			}
			str.append(field);
			if(keywordSuffix > 0) {
				str.append(keywordSuffix);
			}
			index++;
		}
		return str.toString();
	}
	
	/**
	 * 生成where部分"等于"或"in"语句
	 * @param values
	 * @return
	 */
	public static String generateWhereEqualOrInSql(Collection<?> values) {
		if(values == null || values.isEmpty()) {
			return null;
		}
		if(values.size() == 1) {
			return generateWhereEqualSql(values.iterator().next(), true);
		} else {
			return generateWhereInSql(values, true, true);
		}
	}
	
	public static String generateWhereEqualSql(Object value) {
		return generateWhereEqualSql(value, true);
	}
	
	/**
	 * 生成where部分"等于"语句
	 * @param value
	 * @param equalSign
	 * @return
	 */
	public static String generateWhereEqualSql(Object value, boolean equalChar) {
		StringBuilder str = new StringBuilder();
		if(value == null) {
			if(equalChar) {
				str.append("is ");
			}
			str.append("null");
		} else {
			if(equalChar) {
				str.append("= ");
			}
			if(isNumberValue(value)) {
				str.append(value);
			} else {
				str.append("'");
				str.append(value);
				str.append("'");
			}
		}
		return str.toString();
	}
	
	/**
	 * 生成 where部分"in"语句
	 * @param values
	 * @return
	 */
	public static String generateWhereInSql(Collection<?> values) {
		return generateWhereInSql(values, true, true);
	}
	
	/**
	 * 生成 where 部分"in"语句
	 * <br>如：1,2,3 或者 'a','b','c'</br>
	 * @param values 值列表
	 * @param bracket 是否包含括号
	 * @param inFlag in标记
	 * @return
	 */
	public static String generateWhereInSql(Collection<?> values, boolean bracket, boolean inChar) {
		StringBuilder str = new StringBuilder();
		if(inChar) {
			str.append("in ");
		}
		if(bracket) {
			str.append("(");
		}
		int index = 0;
		for(Object value : values) {
			if(index > 0) {
				str.append(",");
			}
			if(isNumberValue(value)) {
				str.append(value);
			} else {
				str.append("'");
				str.append(value);
				str.append("'");
			}
			index++;
		}
		if(bracket) {
			str.append(")");
		}
		return str.toString();
	}
	
	/**
	 * 是否为数值类型
	 * @param value
	 * @return
	 */
	public static boolean isNumberValue(Object value) {
		if(value instanceof Integer 
				|| value instanceof Long
				|| value instanceof Double
				|| value instanceof Float
				|| value instanceof BigDecimal) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否 = 操作符号
	 * @param op
	 * @return
	 */
	public static boolean isOpEq(String op) {
		return op != null && "=".equals(op.trim());
	}
	
	/**
	 * 是否 <>  操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpNe(String op) {
		return op != null && ("!=".equals(op.trim()) || "<>".equals(op.trim()));
	}
	
	/**
	 * 是否 like 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpLike(String op) {
		return op != null && "like".equalsIgnoreCase(op.trim());
	}
	
	/**
	 * 是否 not like 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpNotLike(String op) {
		return op != null && "not like".equalsIgnoreCase(op.trim());
	}
	
	/**
	 * 是否 > 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpGt(String op) {
		return op != null && ">".equals(op.trim());
	}
	
	/**
	 * 是否 >= 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpGe(String op) {
		return op != null && ">=".equals(op.trim());
	}
	
	/**
	 * 是否 <  操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpLt(String op) {
		return op != null && "<".equals(op.trim());
	}
	
	/**
	 * 是否 <= 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpLe(String op) {
		return op != null && "<=".equals(op.trim());
	}
	
	/**
	 * 是否 in 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpIn(String op) {
		return op != null && "in".equalsIgnoreCase(op.trim());
	}
	
	/**
	 * 是否 not in 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpNotIn(String op) {
		return op != null && "not in".equalsIgnoreCase(op.trim());
	}
	
	/**
	 * 是否 is null 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpIsNull(String op) {
		return op != null && ("isnull".equalsIgnoreCase(op.trim()) || "is null".equalsIgnoreCase(op.trim()));
	}
	
	/**
	 * 是否 is not null 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpIsNotNull(String op) {
		return op != null && "is not null".equalsIgnoreCase(op.trim());
	}
	
	/**
	 * 是否 between 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpBetween(String op) {
		return op != null && "between".equalsIgnoreCase(op.trim());
	}
	
	/**
	 * 是否 not between 操作符
	 * @param op
	 * @return
	 */
	public static boolean isOpNotBetween(String op) {
		return op != null && "not between".equalsIgnoreCase(op.trim());
	}
	
	public static void main(String[] args) {
		
		Collection<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		System.out.println(generateWhereEqualOrInSql(list));
	}
}
