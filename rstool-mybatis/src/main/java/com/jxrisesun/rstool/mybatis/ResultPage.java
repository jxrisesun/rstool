package com.jxrisesun.rstool.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "分页查询结果")
@Getter
@Setter
public class ResultPage<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4684417923426780683L;

	@ApiModelProperty(value = "分页序号")
	private Integer pageNo;
	
	@ApiModelProperty(value = "每页记录数")
	private Integer pageSize;
	
	/**
	 * 总页数
	 */
	@ApiModelProperty(value = "总页数")
	private Integer pages;
	
	public Integer getPages() {
		if(this.pages == null) {
			this.pages = getPages(this.pageSize,  this.total);
		}
		return this.pages;
	}
	
	public void setPages(Integer pages) {
		this.pages = pages;
	}
	
	@ApiModelProperty(value = "总记录是")
	private Long total;
	
	@ApiModelProperty(value = "数据行")
	private List<T> records;
	
	public List<T> getRecords() {
		if(this.records == null) {
			this.records = new ArrayList<>();
		}
		return this.records;
	}
	
	public void setRecords(List<T> records) {
		this.records = records;
	}
	
	/**
	 * 计算分页数
	 * @param pageS 每页记录数
	 * @param tot 总记录数
	 * @return
	 */
	public static Integer getPages(Integer pageS, Long tot) {
		if(pageS == null || pageS.intValue() < 1) {
			pageS = 1;
		}
		if(tot == null || tot.intValue() < 0) {
			tot = 0L;
		}
		if(tot.intValue() % pageS == 0) {
			return tot.intValue() / pageS;
		} else {
			return tot.intValue() / pageS + 1;
		}
	}
}
