package com.netx.searchengine.common;

import java.util.*;

public class Query {

    private Map<String,Object> filter = new HashMap<>();//模糊查询、区间查询
    private Boolean isDescSort;//null为按附近距离排序
    private List<Object> values;//此字段多个值
    private Map<String,Object> otherCondition;//其他条件

    public Query(Boolean isDescSort) {
        this.isDescSort = isDescSort;
        this.values = new ArrayList<>();
        this.filter = new HashMap<>();
        this.otherCondition = new HashMap<>();
    }

    public Query(Map<String, Object> filter, Boolean isDescSort, List<Object> values) {
        this.filter = filter;
        this.isDescSort = isDescSort;
        this.values = values;
        this.otherCondition = new HashMap<>();
    }

    public static Query listQuery( Map<String, Object> filter, Boolean isDescSort,Object... objects){
        List<Object> list = new ArrayList<>();
        for(Object o:objects){
            list.add(o);
        }
        return new Query(filter,isDescSort,list);
    }

    public Map<String, Object> getFilter() {
        return filter;
    }


    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public Boolean getDescSort() {
        return isDescSort;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public List<Object> getValues() {
        return values;
    }

    public Map<String, Object> getOtherCondition() {
        return otherCondition;
    }

    /**
     * 添加筛选条件
     * @param name
     * 1：like 模糊查询，2：min最小值，3：max最大值
     * @param o
     */
    public void addFilter(String name,Object o){
        this.filter.put(name,o);
    }

    public void addValue(Object o){
        this.values.add(o);
    }

    /**
     * 设置同龄
     * @param birthday
     */
    public void setAge(Date birthday){
        Integer year = birthday.getYear();
        birthday.setYear(year-5);
        addFilter("min",birthday.getTime());
        birthday.setYear(year+5);
        addFilter("max",birthday.getTime());
    }

    /**
     * 设置异性条件
     * @param sex
     */
    public void setSex(String sex){
        sex = sex.equals("男")?"女":"男";
        addCondition("sex",sex);
    }

    public void addCondition(String key,Object value){
        otherCondition.put(key, value);
    }

    @Override
    public String toString() {
        return "Query{" +
                "filter=" + filter +
                ", isDescSort=" + isDescSort +
                ", values=" + values +
                ", otherCondition=" + otherCondition +
                '}';
    }
}
