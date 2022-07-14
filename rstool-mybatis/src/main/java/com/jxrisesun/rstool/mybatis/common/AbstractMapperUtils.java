package com.jxrisesun.rstool.mybatis.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import com.jxrisesun.rstool.mybatis.QueryCondition;
import com.jxrisesun.rstool.mybatis.QueryPage;
import com.jxrisesun.rstool.mybatis.ResultPage;


public abstract class AbstractMapperUtils<T> {

	/**
	 * 添加记录
	 * 
	 * @param entity
	 * @return
	 */
	public abstract int saveEntity(T entity);

	/**
	 * 根据 主键ID 更新记录
	 * 
	 * @param entity
	 * @return
	 */
	public abstract int updateEntity(T entity);

	/**
	 * 根据 主键ID 删除记录
	 * 
	 * @param id
	 * @return
	 */
	public abstract int deleteById(Serializable id);

	/**
	 * 根据主键ID集合 删除记录
	 * 
	 * @param ids
	 * @return
	 */
	public abstract int deleteByIds(Collection<? extends Serializable> ids);
	
	/**
	 * 根据id获取实体信息
	 * @param id
	 * @return
	 */
	public abstract T getEntityById(Serializable id);
	
	/**
	 * 获取实体分页数据
	 * @param queryPage
	 * @return
	 */
	public abstract ResultPage<T> getEntityByPage(QueryPage queryPage);
	
	/**
	 * 获取实体列表
	 * @return
	 */
	public abstract List<T> getList();

	/**
	 * 根据map查询记录数
	 * @param map
	 * @return
	 */
	public abstract Long getCountByMap(Map<String, Object> map);
	
	/**
	 * 根据map查询实体
	 * @param map
	 * @return
	 */
	public abstract List<T> getListByMap(Map<String, Object> map);
	
	/**
	 * 根据 condition 查询记录数
	 * @param conditions
	 * @return
	 */
	public abstract Long getCountByCondition(Collection<QueryCondition> conditions);
	
	/**
	 * 根据condition查询实体
	 * @param conditions
	 * @param orderBy
	 * @param columns
	 * @return
	 */
	public abstract List<T> getListByCondition(Collection<QueryCondition> conditions, String orderBy, String... columns);
	
	private Class<T> entityClass;

	/**
	 * 获取实体class
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized Class<T> getEntityClass() {
		if (this.entityClass == null && this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) this.getClass().getGenericSuperclass();
			Type argType = paramType.getActualTypeArguments().length > 0 ? paramType.getActualTypeArguments()[0] : null;
			if (argType instanceof Class) {
				this.entityClass = (Class<T>) argType;
			}
		}
		return this.entityClass;
	}

	/**
	 * 获取表名称
	 * 
	 * @return
	 */
	public abstract String getTableName();

	/**
	 * 获取ID字段
	 * 
	 * @return
	 */
	public abstract String getIdField();

	/**
	 * 获取 mybatis mappedStatement Id 
	 * @param sqlSession
	 * @param sql
	 * @param sqlCommandType
	 * @param resultType
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getMappedStatementId(SqlSession sqlSession, SqlCommandType sqlCommandType, String sql, Class<?> parameterType, Class<?> resultType) {
		StringBuilder msIdBuilder = new StringBuilder(sqlCommandType.toString());
		msIdBuilder.append(".");
		msIdBuilder.append(((resultType != null ? resultType : "") + sql + (parameterType != null ? parameterType : "")).hashCode());
    	String msId = msIdBuilder.toString();
    	Configuration configuration = sqlSession.getConfiguration();
    	if(configuration.hasStatement(msId, false)) {
    		return msId;
    	}
    	SqlSource sqlSource = null;
		if(parameterType == null) {
			sqlSource = new StaticSqlSource(configuration, sql);
		} else {
			sqlSource = configuration.getDefaultScriptingLanuageInstance().createSqlSource(configuration, sql, parameterType);
		}
    	List<ResultMap> resultMaps = new ArrayList<>();
    	resultMaps.add(new ResultMap.Builder(configuration, "defaultResultMap", resultType, new ArrayList<ResultMapping>(0)).build());
    	MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, sqlCommandType).resultMaps(resultMaps).build();
    	configuration.addMappedStatement(ms);
    	return msId;
	}
	
	/**
	 * 获取pagehelper分页总记录数
	 * 
	 * @param list
	 * @return
	 */
	public Long getPageHelperTotal(Object list) {
		if (list == null) {
			return null;
		}
		// pagehelper 分页处理兼容
		if (list.getClass().getTypeName().equals("com.github.pagehelper.Page")) {
			try {
				Field totalField = list.getClass().getField("total");
				if(totalField != null) {
					if(!totalField.isAccessible()) {
						totalField.setAccessible(true);
					}
					return totalField.getLong(list);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
