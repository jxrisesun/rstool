package com.jxrisesun.rstool.mybatis.fluentmybatis;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.jxrisesun.rstool.mybatis.common.AbstractMapperUtils;
import com.jxrisesun.rstool.mybatis.util.SqlUtils;
import com.jxrisesun.rstool.mybatis.QueryCondition;
import com.jxrisesun.rstool.mybatis.QueryPage;
import com.jxrisesun.rstool.mybatis.ResultPage;
import com.jxrisesun.rstool.core.util.CollectionUtils;
import com.jxrisesun.rstool.core.util.StringUtils;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.base.model.ISqlOp;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.metadata.TableMetaHelper;
import cn.org.atool.fluent.mybatis.model.StdPagedList;

public class MapperUtils<T extends IEntity> extends AbstractMapperUtils<T> {

	private BaseMapper<T> mapper;

	public BaseMapper<T> getMapper() {
		return this.mapper;
	}

	public MapperUtils(BaseMapper<T> mapper) {
		this.mapper = mapper;
	}

	@Override
	public int saveEntity(T entity) {
		return this.mapper.save(entity);
	}

	@Override
	public int updateEntity(T entity) {
		return this.mapper.updateById(entity) ? 1 : 0;
	}

	@Override
	public int deleteById(Serializable id) {
		return this.mapper.deleteById(id) ? 1 : 0;
	}

	@Override
	public int deleteByIds(Collection<? extends Serializable> ids) {
		return this.mapper.deleteByIds(ids);
	}

	@Override
	public T getEntityById(Serializable id) {
		return this.mapper.selectById(id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ResultPage<T> getEntityByPage(QueryPage queryPage) {
		ResultPage<T> resultPage = new ResultPage<>();
		List<T> list = null;
		FreeQuery query = this.getQuery(queryPage.getConditions(), queryPage.getOrderByClause(), queryPage.getProperties());
		if(queryPage.getPageNo() == null || queryPage.getPageSize() == null) {
			resultPage.setPageNo(1);
			resultPage.setPageSize(resultPage.getRecords().size());
			list = this.mapper.mapper().listEntity(query);
		} else {
			query = query.limit(queryPage.getPageNo(), queryPage.getPageSize());
			StdPagedList<T> page = this.mapper.mapper().stdPagedEntity(query);
			resultPage.setPageNo(queryPage.getPageNo());
			resultPage.setPageSize(queryPage.getPageSize());
			Integer total = page.getTotal();
			resultPage.setTotal(total.longValue());
			list = page.getData();
		}
		resultPage.setRecords(list);
		return resultPage;
	}

	@Override
	public List<T> getList() {
		return this.mapper.selectByMap(new HashMap<String, Object>());
	}

	@Override
	public Long getCountByMap(Map<String, Object> map) {
		Integer count = this.mapper.mapper().count(this.getQuery(map));
		return count.longValue();
	}

	@Override
	public List<T> getListByMap(Map<String, Object> map) {
		return this.mapper.selectByMap(map);
	}

	@Override
	public Long getCountByCondition(Collection<QueryCondition> conditions) {
		Integer count = this.mapper.mapper().count(this.getQuery(conditions, null));
		return count.longValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> getListByCondition(Collection<QueryCondition> conditions, String orderBy, String... columns) {
		return this.mapper.mapper().listEntity(this.getQuery(conditions, orderBy, columns));
	}

	private TableMeta tableMeta;

	public TableMeta getTableMeta() {
		if (this.tableMeta == null) {
			this.tableMeta = TableMetaHelper.getTableInfo(this.getEntityClass());
		}
		return this.tableMeta;
	}

	@Override
	public String getTableName() {
		TableMeta tableMeta = this.getTableMeta();
		if (tableMeta != null) {
			return tableMeta.getTableName();
		}
		Class<T> clazz = this.getEntityClass();
		// FluentMybatis
		FluentMybatis fluentMybatis = clazz.getAnnotation(FluentMybatis.class);
		if (fluentMybatis != null) {
			return fluentMybatis.table();
		}
		// Table
		Table tableName = clazz.getAnnotation(Table.class);
		if (tableName != null) {
			return tableName.name();
		}
		return clazz.getSimpleName();
	}

	@Override
	public String getIdField() {
		TableMeta tableMeta = this.getTableMeta();
		if (tableMeta != null && tableMeta.getPrimary() != null) {
			return tableMeta.getPrimary().getColumn();
		}
		Class<T> clazz = this.getEntityClass();
		for (Field field : clazz.getFields()) {
			// TableId
			TableId tableId = field.getAnnotation(TableId.class);
			if (tableId != null) {
				return !StringUtils.isEmpty(tableId.value()) ? tableId.value() : field.getName();
			}
			// Id
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				Column column = field.getAnnotation(Column.class);
				return column != null ? column.name() : field.getName();
			}
		}
		return "id";
	}

	/**
	 * 获取 Query
	 * 
	 * @param queryMap
	 * @return
	 */
	public FreeQuery getQuery(Map<String, Object> queryMap) {
		FreeQuery query = this.getQuery();
		if (queryMap != null && queryMap.size() > 0) {
			for (Entry<String, Object> kv : queryMap.entrySet()) {
				if (kv.getValue() == null) {
					query.where.apply(kv.getKey(), SqlOp.IS_NULL);
				} else {
					query.where.apply(kv.getKey(), SqlOp.EQ, kv.getValue());
				}
			}
			query.where.end();
		}
		return query;
	}

	/**
	 * 获取 Query
	 * 
	 * @param conditions
	 * @param orderBy
	 * @param columns
	 * @return
	 */
	public FreeQuery getQuery(Collection<QueryCondition> conditions, String orderBy, String... columns) {
		FreeQuery query = this.getQuery();
		// conditions
		if (CollectionUtils.isNotEmpty(conditions)) {
			for (QueryCondition condition : conditions) {
				loadQuery(condition, query);
			}
		}
		// orderBy
		if (StringUtils.isNotEmpty(orderBy)) {
			String[] strs = orderBy.split(",");
			for (String str : strs) {
				if (str.contains("desc") || str.contains("DESC")) {
					query.orderBy().desc(str.replace("desc", "").replace("DESC", "").trim());
				} else if (str.contains("asc") || str.contains("ASC")) {
					query.orderBy().asc(str.replace("asc", "").replace("ASC", "").trim());
				} else {
					query.orderBy().desc(str.trim());
				}
			}
		}
		// columns
		if (CollectionUtils.isNotEmpty(columns)) {
			query.select(columns);
		}
		return query.where.end();
	}

	/**
	 * 获取 Query
	 * 
	 * @return
	 */
	public FreeQuery getQuery() {
		return new FreeQuery(this.getTableName());
	}

	/**
	 * example 条件操作递归处理
	 * 
	 * @param condition
	 * @param example
	 * @param criteriaNew
	 * @return
	 */
	private static <T> boolean loadQuery(QueryCondition condition, FreeQuery query) {
		// 是否为空条件
		if (condition.isEmpty()) {
			return false;
		}
		// operator
		String operator = !StringUtils.isEmpty(condition.getOperator()) ? condition.getOperator().toLowerCase() : "";

		// trim
		operator = operator.trim();

		// and or
		boolean isAnd = "and".equalsIgnoreCase(condition.getAndOr());

		// 字段是否效
		if (!StringUtils.isEmpty(condition.getField())) {
			ISqlOp sqlOp = null;
			if (SqlUtils.isOpEq(operator)) {
				sqlOp = SqlOp.EQ;
			} else if (SqlUtils.isOpNe(operator)) {
				sqlOp = SqlOp.NE;
			} else if (SqlUtils.isOpLike(operator)) {
				sqlOp = SqlOp.LIKE;
			} else if (SqlUtils.isOpNotLike(operator)) {
				sqlOp = SqlOp.NOT_LIKE;
			} else if (SqlUtils.isOpGt(operator)) {
				sqlOp = SqlOp.GT;
			} else if (SqlUtils.isOpGe(operator)) {
				sqlOp = SqlOp.GE;
			} else if (SqlUtils.isOpLt(operator)) {
				sqlOp = SqlOp.LT;
			} else if (SqlUtils.isOpLe(operator)) {
				sqlOp = SqlOp.LE;
			} else if (SqlUtils.isOpIn(operator)) {
				sqlOp = SqlOp.IN;
			} else if (SqlUtils.isOpNotIn(operator)) {
				sqlOp = SqlOp.NOT_IN;
			} else if (SqlUtils.isOpIsNull(operator)) {
				sqlOp = SqlOp.IS_NULL;
			} else if (SqlUtils.isOpIsNotNull(operator)) {
				sqlOp = SqlOp.NOT_NULL;
			} else if (SqlUtils.isOpBetween(operator)) {
				sqlOp = SqlOp.BETWEEN;
			} else if(SqlUtils.isOpNotBetween(operator)) {
				sqlOp = SqlOp.NOT_BETWEEN;
			}
			
			if(isAnd) {
				if(sqlOp != null) {
					if(sqlOp == SqlOp.BETWEEN || sqlOp == SqlOp.NOT_BETWEEN) {
						query.where.and.apply(condition.getField(), sqlOp, condition.getValue(), condition.getSecondValue());
					} else {
						query.where.and.apply(condition.getField(), sqlOp, condition.getValue());
					}
				} else {
					query.where.and.apply(condition.getField());
				}
			} else {
				if(sqlOp != null) {
					if(sqlOp == SqlOp.BETWEEN || sqlOp == SqlOp.NOT_BETWEEN) {
						query.where.or.apply(condition.getField(), sqlOp, condition.getValue(), condition.getSecondValue());
					} else {
						query.where.or.apply(condition.getField(), sqlOp, condition.getValue());
					}
				} else {
					query.where.or.apply(condition.getField());
				}
			}
		}
		return true;
	}
}
