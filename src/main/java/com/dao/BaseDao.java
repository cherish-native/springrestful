package com.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDao {

    /**
     * 根据sql查询list
     * @param sql
     * @param params
     * @return
     */
    List<Map<String, Object>>  findListBySql(String sql, List<Object> params);

    /**
     * 拼接查询sql
     * @param sql
     * @param whereSqls
     * @return
     */
    String appendWhere(String sql, List<String> whereSqls);
}
