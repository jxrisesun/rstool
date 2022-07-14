package com.jxrisesun.rstool.mybatis.tkmybatis;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;

public interface BaseMapper<T> extends tk.mybatis.mapper.common.Mapper<T>, IdsMapper<T>, ConditionMapper<T> {

}
