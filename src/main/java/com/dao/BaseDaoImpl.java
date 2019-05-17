package com.dao;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
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
}
