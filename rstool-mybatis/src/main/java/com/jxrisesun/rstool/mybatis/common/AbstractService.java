package com.jxrisesun.rstool.mybatis.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.jxrisesun.rstool.mybatis.QueryCondition;
import com.jxrisesun.rstool.mybatis.QueryPage;
import com.jxrisesun.rstool.mybatis.ResultPage;

public interface AbstractService<T> {
	
	/**
	 * 获取 mapper 工具类
	 * @return
	 */
	AbstractMapperUtils<T> getMapperUtils();

	/**
	 * 保存实体
	 * @param entity
	 * @return
	 */
	int saveEntity(T entity);
	
	/**
	 * 更新实体
	 * @param entity
	 * @return
	 */
	int updateEntity(T entity);
	
	/**
	 * 根据id更新实体
	 * @param id
	 * @return
	 */
	int deleteById(Serializable id);
	
	/**
	 * 根据id批量更新实体
	 * @param ids
	 * @return
	 */
	int deleteByIds(Collection<? extends Serializable> ids);
	
	/**
	 * 根据id获取实体
	 * @param id
	 * @return
	 */
	T getEntityById(Serializable id);
	
	/**
	 * 获取实体分页数据
	 * @param queryPage
	 * @return
	 */
	ResultPage<T> getEntityByPage(QueryPage queryPage);
	
	/**
	 * 获取实体列表
	 * @return
	 */
	List<T> getList();
	
	/**
	 * 根据 map查询实体
	 * @param map
	 * @return
	 */
	List<T> getListByMap(HashMap<String, Object> map);
	
	/**
	 * 根据条件查询记录数
	 * @param conditions 查询条件
	 * @return
	 */
	Long getCountByCondition(Collection<QueryCondition> conditions);
	
	/**
	 * 根据条件查询实体列表(支持排序和字段过滤)
	 * @param conditions 查询条件
	 * @param orderBy 排序
	 * @param columns 字段过滤
	 * @return
	 */
	List<T> getListByCondition(Collection<QueryCondition> conditions, String orderBy, String... columns);
}
