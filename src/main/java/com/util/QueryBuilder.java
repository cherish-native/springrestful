package com.util;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    private List<Object> params = new ArrayList<>();
    private StringBuilder stringBuilder;
    private boolean hasWhere = false;

    public QueryBuilder(String sql){
        this.stringBuilder = new StringBuilder(sql);
    }

    public QueryBuilder appendAndWhere(String where, Object...params){
        if(hasWhere){
            this.stringBuilder.append(" where ");
            hasWhere = true;
        }else{
            this.stringBuilder.append(" and ");
        }
        this.stringBuilder.append(where).append(" ");
        if(params != null){
            for(int i=0;i<params.length;i++){
                this.params.add(params[i]);
            }
        }
        return this;
    }

    public QueryBuilder appendSql(String sql){
        this.stringBuilder.append(" ").append(sql).append(" ");
        return this;
    }

    public String getSql(){
        return this.stringBuilder.toString();
    }

    public List<Object> getParams(){
        return params;
    }
}
