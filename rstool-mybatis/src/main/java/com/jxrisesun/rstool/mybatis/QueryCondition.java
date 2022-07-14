package com.jxrisesun.rstool.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jxrisesun.rstool.core.util.FastJson2Utils;
import com.jxrisesun.rstool.core.util.StringUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询条件
 *
 */
@ApiModel(value = "查询条件")
public class QueryCondition implements Serializable, Cloneable {

	private static final long serialVersionUID = 7365886177260173253L;
	
	private static final String DEFAULT_ANDOR = "and";
	
	private static final String DEFAULT_OP = "=";
	
	/**
	 * 操作符 map
	 */
	public static final Map<String, String> OPERATOR_MAP = new HashMap<String, String>() {
		private static final long serialVersionUID = 5683714999942954275L;
	{
		put("=", " =");
		put("==", " =");
		put("<=>", " <=>");
		put("!=", " <>");
		put("<>", " <>");
		put(">", " >");
		put(">=", " >=");
		put("<", " <");
		put("<=", " <=");
		put("like", " LIKE");
		put("not like", " NOT LIKE");
		put("in", " IN");
		put("not in", " NOT IN");
		put("isnull", " IS NULL");
		put("is null", " IS NULL");
		put("is not null", " IS NOT NULL");
	}};
	
	/**
	 * and or
	 */
	@ApiModelProperty(value = "条件关系(and/or)")
	private String andOr = DEFAULT_ANDOR;
	
	public String getAndOr() {
		return andOr;
	}
	
	public void setAndOr(String andOr) {
		this.andOr = andOr;
	}
	
	/**
	 * 字段
	 */
	@ApiModelProperty(value = "字段(同column)")
	private String field;
	
	/**
	 * 获取字段名
	 */
	public String getField() {
		return this.field;
	}
	
	/**
	 * 设置字段值
	 * @param field
	 */
	public void setField(String field) {
		this.field = field;
	}
	
	/**
	 * 运算符（大于号，小于号，等于号 like 等）
	 */
	@ApiModelProperty(value = "运算符（大于号，小于号，等于号 like 等）")
	private String operator;
	
	/**
	 * 获取运算符
	 */
	public String getOperator() {
		return this.operator;
	}
	
	/**
	 * 设置运算符
	 * @param operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	/**
	 * 值
	 */
	@ApiModelProperty(value = "值")
	private Object value;
	
	public Object getValue() {
		return this.value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * between firstValue and secondValue
	 */
	@ApiModelProperty(value = "第二个值( 用于between [value] and [secondValue])")
	private Object secondValue;
	
	public Object getSecondValue() {
		return secondValue;
	}
	
	public void setSecondValue(Object secondValue) {
		this.secondValue = secondValue;
	}
	
	/**
	 * 子条件集合
	 */
	private List<QueryCondition> conditions;
	
	public List<QueryCondition> getConditions() {
		if(this.conditions == null) {
			this.conditions = new ArrayList<>();
		}
		return this.conditions;
	}
	
	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}
	
	public QueryCondition() {
		
	}
	
	/**
	 * 初始化
	 * @param field
	 * @param value
	 */
	public QueryCondition(String field, Object value) {
		this(field, DEFAULT_OP, value, null);
	}
	
	/**
	 * 初始化
	 * @param field
	 * @param operator
	 * @param value
	 */
	public QueryCondition(String field, String operator, Object value) {
		this(field, operator, value, null);
	}
	
	/**
	 * 初始化
	 * @param field
	 * @param operator
	 * @param value
	 * @param secondValue
	 */
	public QueryCondition(String field, String operator, Object value, Object secondValue) {
		this.field = field;
		this.operator = operator;
		this.value = value;
		this.secondValue = secondValue;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		QueryCondition desCondition = new QueryCondition(this.getField(), this.getOperator(), this.getValue(), this.getSecondValue());
		copyCondition(this, desCondition);
		return desCondition;
	}
	
	public boolean isEmpty() {
		// 是否为空条件
		if (StringUtils.isEmpty(this.getField()) && StringUtils.isEmpty(this.getOperator()) && this.getValue() == null
				&& (this.getConditions() == null || this.getConditions().isEmpty())) {
			return false;
		}
		return false;
	}
	
	/**
	 * 拷贝 condition 属性
	 * @param srcCondition 源 condition
	 * @param desCondition 目标 condition
	 */
	public static void copyCondition(QueryCondition srcCondition, QueryCondition desCondition) {
		if(srcCondition == null || desCondition == null) {
			return;
		}
		desCondition.setField(srcCondition.getField());
		desCondition.setOperator(srcCondition.getOperator());
		desCondition.setValue(srcCondition.getValue());
		desCondition.setSecondValue(srcCondition.getSecondValue());
		if(!srcCondition.getConditions().isEmpty()) {
			for(QueryCondition srcSubCondition : srcCondition.getConditions()) {
				QueryCondition subDesCondition = new QueryCondition(srcSubCondition.getField(), srcSubCondition.getValue());
				copyCondition(srcSubCondition, subDesCondition);
				desCondition.getConditions().add(subDesCondition);
			}
		}
	}
	
	/**
	 * Condition 数组 转 List
	 * @param conditions
	 * @return
	 */
	public static List<QueryCondition> asList(QueryCondition... conditions){
		if(conditions == null || conditions.length == 0) {
			return new ArrayList<>();
		}
		return Arrays.asList(conditions);
	}
	
	/**
	 * 解析 json
	 * @param json
	 * @return
	 */
	public static List<QueryCondition> parseArray(String json){
		if(json == null || json.length() == 0) {
			return new ArrayList<>();
		}
		List<QueryCondition> conditionList = FastJson2Utils.parseArray(json, QueryCondition.class);
		return conditionList;
	}
}
