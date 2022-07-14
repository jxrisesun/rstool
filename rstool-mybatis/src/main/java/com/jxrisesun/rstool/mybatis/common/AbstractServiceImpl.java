package com.jxrisesun.rstool.mybatis.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.jxrisesun.rstool.mybatis.QueryCondition;
import com.jxrisesun.rstool.mybatis.QueryPage;
import com.jxrisesun.rstool.mybatis.ResultPage;

public abstract class AbstractServiceImpl<T> implements AbstractService<T> {

	/**
	 * 获取 mapper 工具类
	 * @return
	 */
	@Override
	public abstract AbstractMapperUtils<T> getMapperUtils();
	
	@Override
	public int saveEntity(T entity) {
		return this.getMapperUtils().saveEntity(entity);
	}

	@Override
	public int updateEntity(T entity) {
		return this.getMapperUtils().updateEntity(entity);
	}

	@Override
	public int deleteById(Serializable id) {
		return this.getMapperUtils().deleteById(id);
	}

	@Override
	public int deleteByIds(Collection<? extends Serializable> ids) {
		return this.getMapperUtils().deleteByIds(ids);
	}

	@Override
	public T getEntityById(Serializable id) {
		return this.getMapperUtils().getEntityById(id);
	}

	@Override
	public ResultPage<T> getEntityByPage(QueryPage queryPage) {
		return this.getMapperUtils().getEntityByPage(queryPage);
	}
	
	@Override
	public List<T> getList() {
		return this.getMapperUtils().getList();
	}
	
	@Override
	public List<T> getListByMap(HashMap<String, Object> map) {
		return this.getMapperUtils().getListByMap(map);
	}
	
	@Override
	public Long getCountByCondition(Collection<QueryCondition> conditions) {
		return this.getMapperUtils().getCountByCondition(conditions);
	}
	
	@Override
	public List<T> getListByCondition(Collection<QueryCondition> conditions, String orderBy, String... columns) {
		return this.getMapperUtils().getListByCondition(conditions, orderBy, columns);
	}
}
