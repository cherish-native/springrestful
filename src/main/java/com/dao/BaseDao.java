package com.dao;

import com.entity.vo.DataGridReturn;
import org.springframework.data.domain.Pageable;

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
     * 分页查询
     * @param sql
     * @param params
     * @param pageable
     * @return
     */
    DataGridReturn pageQuery(String sql, List<Object> params, Pageable pageable);

    /**
     * 拼接查询sql
     * @param sql
     * @param whereSqls
     * @return
     */
    String appendWhere(String sql, List<String> whereSqls);

    /**
     * 根据sql更新
     * @param sql
     * @param params
     * @return
     */
    int updateBySql(String sql, List<Object> params);
}
