package com.util;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    private List<Object> params = new ArrayList<>();
    private StringBuilder stringBuilder;
    private boolean hasWhere = false;
    private boolean hasHaving = false;


    public QueryBuilder(String sql){
        this.stringBuilder = new StringBuilder(sql);
    }

    public QueryBuilder appendAndWhere(String where, Object...params){
        if(!hasWhere){
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
    
    public QueryBuilder appendAndHaving(String having, Object...params){
        if(!hasHaving){
            this.stringBuilder.append(" having ");
            hasHaving = true;
        }else{
            this.stringBuilder.append(" and ");
        }
        this.stringBuilder.append(having).append(" ");
        if(params != null){
            for(int i=0;i<params.length;i++){
                this.params.add(params[i]);
            }
        }
        return this;
    }
    
    public QueryBuilder appendInOr(String where,String[] params){
        if(!hasHaving){
            this.stringBuilder.append(" where ");
            hasWhere = true;
        }else{
            this.stringBuilder.append(" and ");
        }
        for(int i=0;i<params.length;i++){
            this.stringBuilder.append(where).append(" = ? or");
            this.params.add(params[i]);
        }
        stringBuilder.delete(stringBuilder.length()-2, stringBuilder.length());
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
