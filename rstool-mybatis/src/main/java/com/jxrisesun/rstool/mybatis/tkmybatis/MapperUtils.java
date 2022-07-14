package com.jxrisesun.rstool.mybatis.tkmybatis;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.session.RowBounds;

import com.jxrisesun.rstool.mybatis.common.AbstractMapperUtils;
import com.jxrisesun.rstool.mybatis.QueryCondition;
import com.jxrisesun.rstool.mybatis.QueryPage;
import com.jxrisesun.rstool.mybatis.ResultPage;
import com.jxrisesun.rstool.mybatis.util.SqlUtils;
import com.jxrisesun.rstool.core.util.CollectionUtils;
import com.jxrisesun.rstool.core.util.StringUtils;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.StringUtil;

public class MapperUtils<T> extends AbstractMapperUtils<T> {

	private BaseMapper<T> mapper;

	public BaseMapper<T> getMapper() {
		return this.mapper;
	}

	public MapperUtils(BaseMapper<T> mapper) {
		this.mapper = mapper;
	}

	@Override
	public int saveEntity(T entity) {
		return this.mapper.insertSelective(entity);
	}

	@Override
	public int updateEntity(T entity) {
		return this.mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public int deleteById(Serializable id) {
		return this.mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int deleteByIds(Collection<? extends Serializable> ids) {
		if (ids == null || ids.isEmpty()) {
			return 0;
		}
		int index = -1;
		StringBuilder str = new StringBuilder();
		for (Serializable id : ids) {
			index++;
			if (index > 0) {
				str.append(",");
			}
			str.append(id);
		}
		return this.mapper.deleteByIds(str.toString());
	}

	@Override
	public T getEntityById(Serializable id) {
		return this.mapper.selectByPrimaryKey(id);
	}

	@Override
	public ResultPage<T> getEntityByPage(QueryPage queryPage) {
		// 分页结果
		ResultPage<T> resultPage = new ResultPage<T>();
		// 查询条件
		Example example = this.getExample(queryPage);
		List<T> list = null;
		// 判断是否需要分页
		if (queryPage.getPageNo() != null && queryPage.getPageSize() != null) {
			int offset = (queryPage.getPageNo() - 1) * queryPage.getPageSize();
			RowBounds rowBounds = new RowBounds(offset, queryPage.getPageSize());
			// 查询数据列表
			list = this.mapper.selectByExampleAndRowBounds(example, rowBounds);
			// 获取总记录数
			Long total = getPageHelperTotal(list);
			if (total == null) {
				total = Long.valueOf(this.mapper.selectCountByExample(example));
			}
			resultPage.setTotal(total);
			resultPage.setPageNo(queryPage.getPageNo());
			resultPage.setPageSize(queryPage.getPageSize());
		} else {
			// 查询数据列表
			list = this.mapper.selectByExample(example);
			resultPage.setTotal(Long.parseLong(String.valueOf(list.size())));
			resultPage.setPageNo(1);
			resultPage.setPageSize(list.size());
		}
		resultPage.setRecords(list);
		return resultPage;
	}
	
	@Override
	public List<T> getList() {
		return this.mapper.selectAll();
	}
	
	@Override
	public Long getCountByMap(Map<String, Object> map) {
		Integer count = this.mapper.selectCountByExample(this.getExample(map));
		return count.longValue();
	}
	
	@Override
	public List<T> getListByMap(Map<String, Object> map) {
		return this.mapper.selectByExample(this.getExample(map));
	}
	
	@Override
	public Long getCountByCondition(Collection<QueryCondition> conditions) {
		Example example = this.getExample(conditions, null);
		Integer count = this.mapper.selectCountByExample(example);
		return count.longValue();
	}
	
	@Override
	public List<T> getListByCondition(Collection<QueryCondition> conditions, String orderBy, String... columns) {
		Example example = this.getExample(conditions, orderBy, columns);
		return this.mapper.selectByExample(example);
	}
	
	private EntityTable entityTable;
	
	/**
	 * 获取实体表对象信息
	 * @return
	 */
	public EntityTable getEntityTable() {
		if(this.entityTable == null) {
			Class<T> clazz = this.getEntityClass();
			this.entityTable = EntityHelper.getEntityTable(clazz);
		}
		return this.entityTable;
	}

	@Override
	public String getTableName() {
		EntityTable entityTable = this.getEntityTable();
		if(entityTable != null) {
			String prefix = entityTable.getPrefix();
			if (StringUtil.isNotEmpty(prefix)) {
	            return prefix + "." + entityTable.getName();
	        }
	        return entityTable.getName();
		}
		Class<T> clazz = this.getEntityClass();
		Table table = clazz.getAnnotation(Table.class);
		if(table != null) {
			return table.name();
		}
		return clazz.getSimpleName();
	}

	@Override
	public String getIdField() {
		EntityTable entityTable = this.getEntityTable();
		if(entityTable != null && CollectionUtils.isNotEmpty(entityTable.getEntityClassPKColumns())) {
			EntityColumn entityColumn = entityTable.getEntityClassPKColumns().iterator().next();
			return entityColumn.getColumn();
		}
		Class<T> clazz = this.getEntityClass();
		for(Field field : clazz.getFields()) {
			Id id = field.getAnnotation(Id.class);
			if(id != null) {
				Column column = field.getAnnotation(Column.class);
				return column != null ? column.name() : field.getName();
			}
		}
		return "id";
	}

	



	/**
	 * 创建 Example 
	 * @param enityClass 实体类型
	 * @param queryMap 查询参数
	 * @return
	 */
	public Example getExample(Map<String, Object> queryMap) {
		Example example = this.getExample();
		if (queryMap != null && queryMap.size() > 0) {
			Criteria criteria = example.createCriteria();
			for (Entry<String, Object> kv : queryMap.entrySet()) {
				if (kv.getValue() == null) {
					criteria.andIsNull(kv.getKey());
				} else {
					criteria.andEqualTo(kv.getKey(), kv.getValue());
				}
			}
		}
		return example;
	}
	
	/**
	 * 创建 Example
	 * 
	 * @param queryPage
	 * @return
	 */
	public Example getExample(QueryPage queryPage) {
		return this.getExample(queryPage.getConditions(), queryPage.getOrderByClause(), queryPage.getProperties());
	}
	
	/**
	 * 创建 Example
	 * @param conditions 查询条件
	 * @param orderBy 排序
	 * @param columns 过滤
	 * @return
	 */
	public Example getExample(Collection<QueryCondition> conditions, String orderBy, String... columns) {
		Example example = this.getExample();
		// conditions
		if(CollectionUtils.isNotEmpty(conditions)) {
			for(QueryCondition condition : conditions) {
				Criteria criteriaNew = example.and();
				loadExample(condition, example, criteriaNew); 
			}
		}
		// orderBy
		if(StringUtils.isNotEmpty(orderBy)) {
			example.setOrderByClause(orderBy);
		}
		// columns
		if(CollectionUtils.isNotEmpty(columns)) {
			example.selectProperties(columns);
		}
		return example;
	}
	
	/**
	 * 创建 Example
	 * 
	 * @param enityClass 实体类型
	 * @return
	 */
	public Example getExample() {
		Example example = new Example(this.getEntityClass());
		return example;
	}

	/**
	 * example 条件操作递归处理
	 * 
	 * @param condition
	 * @param example
	 * @param criteriaNew
	 * @return
	 */
	private static boolean loadExample(QueryCondition condition, Example example, Criteria criteriaNew) {

		// 是否为空条件
		if (StringUtils.isEmpty(condition.getField()) && StringUtils.isEmpty(condition.getOperator())
				&& condition.getValue() == null
				&& CollectionUtils.isEmpty(condition.getConditions())) {
			return false;
		}

		// operator
		String operator = !StringUtils.isEmpty(condition.getOperator()) ? condition.getOperator().toLowerCase() : "";

		// trim
		operator = operator.trim();

		// prefix
		boolean hasPrefix = !StringUtils.isEmpty(condition.getField()) && condition.getField().contains(".");

		// and or
		boolean isAnd = "and".equalsIgnoreCase(condition.getAndOr());

		// 字段是否效
		if (!StringUtils.isEmpty(condition.getField())) {

			// 字段是否包含前缀
			if (hasPrefix) {
				if (QueryCondition.OPERATOR_MAP.containsKey(operator)) {
					String opt = QueryCondition.OPERATOR_MAP.get(operator);
					if (isAnd) {
						if (condition.getValue() == null) {
							criteriaNew.andCondition(condition.getField() + " " + opt);
						} else {
							criteriaNew.andCondition(condition.getField() + " " + opt, condition.getValue());
						}
					} else {
						if (condition.getValue() == null) {
							criteriaNew.orCondition(condition.getField() + " " + opt);
						} else {
							criteriaNew.orCondition(condition.getField() + " " + opt, condition.getValue());
						}
					}
				} else {
					if (isAnd) {
						if (condition.getValue() == null) {
							criteriaNew.andCondition(condition.getField());
						} else {
							criteriaNew.andCondition(condition.getField(), condition.getValue());
						}
					} else {
						if (condition.getValue() == null) {
							criteriaNew.orCondition(condition.getField());
						} else {
							criteriaNew.orCondition(condition.getField(), condition.getValue());
						}
					}
				}
			} else {

				if (SqlUtils.isOpEq(operator)) {
					if (isAnd) {
						criteriaNew.andEqualTo(condition.getField(), condition.getValue());
					} else {
						criteriaNew.orEqualTo(condition.getField(), condition.getValue());
					}
				} else if (SqlUtils.isOpNe(operator)) {
					if (isAnd) {
						criteriaNew.andNotEqualTo(condition.getField(), condition.getValue());
					} else {
						criteriaNew.orNotEqualTo(condition.getField(), condition.getValue());
					}
				} else if (SqlUtils.isOpLike(operator)) {
					if (isAnd) {
						criteriaNew.andLike(condition.getField(), condition.getValue().toString());
					} else {
						criteriaNew.orLike(condition.getField(), condition.getValue().toString());
					}
				} else if (SqlUtils.isOpNotLike(operator)) {
					if (isAnd) {
						criteriaNew.andNotLike(condition.getField(), condition.getValue().toString());
					} else {
						criteriaNew.orNotLike(condition.getField(), condition.getValue().toString());
					}
				} else if (SqlUtils.isOpGt(operator)) {
					if (isAnd) {
						criteriaNew.andGreaterThan(condition.getField(), condition.getValue());
					} else {
						criteriaNew.orGreaterThan(condition.getField(), condition.getValue());
					}
				} else if (SqlUtils.isOpGe(operator)) {
					if (isAnd) {
						criteriaNew.andGreaterThanOrEqualTo(condition.getField(), condition.getValue());
					} else {
						criteriaNew.orGreaterThanOrEqualTo(condition.getField(), condition.getValue());
					}
				} else if (SqlUtils.isOpLt(operator)) {
					if (isAnd) {
						criteriaNew.andLessThan(condition.getField(), condition.getValue());
					} else {
						criteriaNew.orLessThan(condition.getField(), condition.getValue());
					}
				} else if (SqlUtils.isOpLe(operator)) {
					if (isAnd) {
						criteriaNew.andLessThanOrEqualTo(condition.getField(), condition.getValue());
					} else {
						criteriaNew.orLessThanOrEqualTo(condition.getField(), condition.getValue());
					}
				} else if (SqlUtils.isOpIn(operator)) {
					if (isAnd) {
						criteriaNew.andIn(condition.getField(), (List<?>) condition.getValue());
					} else {
						criteriaNew.orIn(condition.getField(), (List<?>) condition.getValue());
					}
				} else if (SqlUtils.isOpNotIn(operator)) {
					if (isAnd) {
						criteriaNew.andNotIn(condition.getField(), (List<?>) condition.getValue());
					} else {
						criteriaNew.orNotIn(condition.getField(), (List<?>) condition.getValue());
					}
				} else if (SqlUtils.isOpIsNull(operator)) {
					if (isAnd) {
						criteriaNew.andIsNull(condition.getField());
					} else {
						criteriaNew.orIsNull(condition.getField());
					}
				} else if (SqlUtils.isOpIsNotNull(operator)) {
					if (isAnd) {
						criteriaNew.andIsNotNull(condition.getField());
					} else {
						criteriaNew.orIsNotNull(condition.getField());
					}
				} else if (SqlUtils.isOpBetween(operator)) {
					if (isAnd) {
						criteriaNew.andBetween(condition.getField(), condition.getValue(), condition.getSecondValue());
					} else {
						criteriaNew.orBetween(condition.getField(), condition.getValue(), condition.getSecondValue());
					}
				}  else if (SqlUtils.isOpNotBetween(operator)) {
					if (isAnd) {
						criteriaNew.andNotBetween(condition.getField(), condition.getValue(), condition.getSecondValue());
					} else {
						criteriaNew.orNotBetween(condition.getField(), condition.getValue(), condition.getSecondValue());
					}
				} else {
					if (condition.getValue() != null) {
						if (isAnd) {
							criteriaNew.andCondition(condition.getField(), condition.getValue());
						} else {
							criteriaNew.orCondition(condition.getField(), condition.getValue());
						}
					} else {
						if (isAnd) {
							criteriaNew.andCondition(condition.getField());
						} else {
							criteriaNew.orCondition(condition.getField());
						}
					}
				}
			}
		}

		// 递归嵌套条件处理
		if (CollectionUtils.isNotEmpty(condition.getConditions())) {
			Criteria nextCriteria = criteriaNew;
			for (QueryCondition nextCondition : condition.getConditions()) {
				loadExample(nextCondition, example, nextCriteria);
			}
		}
		return true;
	}
}
