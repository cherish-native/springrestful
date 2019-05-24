package com.dao;

import com.entity.vo.DataGridReturn;
import com.entity.vo.Pagination;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class BaseDaoImpl implements BaseDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Map<String, Object>> findListBySql(String sql, List<Object> params) {
        Query query = entityManager.createNativeQuery(sql);
        if(params != null){
            for(int i=0;i<params.size();i++){
                query.setParameter((i+1), params.get(i));
            }
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Override
    public DataGridReturn pageQuery(String sql, List<Object> params, Pageable pageable) {
        long count = getCount("select count(*) from (" + sql + ")", params);
        if(count == 0){
            return new DataGridReturn(0, new ArrayList());
        }
        Query query = entityManager.createNativeQuery(sql);
        if(params != null){
            for(int i=0;i<params.size();i++){
                query.setParameter((i+1), params.get(i));
            }
        }
        int start = pageable.getPageNumber()*pageable.getPageSize();
        query.setFirstResult(start).setMaxResults(pageable.getPageSize());
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        DataGridReturn dataGridReturn = new DataGridReturn(count, query.getResultList());
        return dataGridReturn;
    }

    /**
     * 查询总数
     * @param sql
     * @param params
     * @return
     */
    public long getCount(String sql, List<Object> params){
        Query query = entityManager.createNativeQuery(sql);
        if(params != null){
            for(int i=0;i<params.size();i++){
                query.setParameter((i+1), params.get(i));
            }
        }
        return Long.parseLong(query.getSingleResult().toString());
    }


    @Override
    public String appendWhere(String sql, List<String> whereSqls) {

        if(whereSqls != null && whereSqls.size() > 0){
            sql += " where " + whereSqls.get(0);
            for(int i=1;i<whereSqls.size();i++){
                sql += " and " + whereSqls.get(i);
            }
            return sql;
        }else{
            return sql;
        }
    }

    @Override
    public int updateBySql(String sql, List<Object> params) {
        Query query = entityManager.createQuery(sql);
        if(params != null){
            for(int i=0;i<params.size();i++){
                query.setParameter(i+1,params.get(i));
            }
        }
        int result = query.executeUpdate();  //修改记录数
        return  result;
    }
}
