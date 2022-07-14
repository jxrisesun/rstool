package com.jxrisesun.rstool.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "分页查询条件")
@Getter
@Setter
public class QueryPage implements Serializable {

	private static final long serialVersionUID = -7728180379564527818L;
	
	@ApiModelProperty(value = "分页序号")
	private Integer pageNo;
	
	@ApiModelProperty(value = "每页记录数")
	private Integer pageSize;
	
	@ApiModelProperty(value = "查询条件")
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
	
	@ApiModelProperty(value = "排序方式")
	private String orderByClause;
	
	@ApiModelProperty(value = "过滤的字段(数组)")
	private String[] properties;
	
	public String[] getProperties() {
		if(this.properties == null) {
			this.properties = new String[] {};
		}
		return this.properties;
	}
	
	public void setProperties(String[] properties) {
		this.properties = properties;
	}
	
	/**
	 * 加载分页序号和每页记录数
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public QueryPage load(Integer pageNo, Integer pageSize) {
		this.setPageNo(pageNo);
		this.setPageSize(pageSize);
		return this;
	}
	
	/**
	 * 加载Map条件
	 * @param queryMap
	 * @return
	 */
	public QueryPage load(Map<String, Object> queryMap) {
		if(queryMap == null || queryMap.size() == 0) {
			return this;
		}
		for(Entry<String, Object> ev : queryMap.entrySet()) {
			this.getConditions().add(new QueryCondition(ev.getKey(), ev.getValue()));
		}
		return this;
	}
	
	/**
	 * 加载QueryCondition集合条件
	 * @param conditions
	 * @return
	 */
	public QueryPage load(Collection<? extends QueryCondition> conditions) {
		if(conditions == null || conditions.size() == 0) {
			return this;
		}
		this.getConditions().addAll(conditions);
		return this;
	}
	
	/**
	 * 加载QueryCondition数组条件
	 * @param queryMap
	 * @return
	 */
	public QueryPage load(QueryCondition... conditions) {
		if(conditions == null || conditions.length == 0) {
			return this;
		}
		for(QueryCondition condition : conditions) {
			this.getConditions().add(condition);
		}
		return this;
	}
}