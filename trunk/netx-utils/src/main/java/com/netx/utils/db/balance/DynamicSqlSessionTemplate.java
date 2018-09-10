package com.netx.utils.db.balance;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.PersistenceExceptionTranslator;

public class DynamicSqlSessionTemplate extends SqlSessionTemplate {

    public DynamicSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public DynamicSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
        super(sqlSessionFactory, executorType);
    }

    public DynamicSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator) {
        super(sqlSessionFactory, executorType, exceptionTranslator);
    }

    @Override
    public int delete(String statement, Object parameter) {
        DataSourceHolder.setMaster();
        int delete = super.delete(statement, parameter);
        DataSourceHolder.clearDataSource();
        return delete;
    }

    @Override
    public int delete(String statement) {
        DataSourceHolder.setMaster();
        int delete = super.delete(statement);
        DataSourceHolder.clearDataSource();
        return delete;
    }

    @Override
    public int update(String statement, Object parameter) {
        DataSourceHolder.setMaster();
        int update = super.update(statement, parameter);
        DataSourceHolder.clearDataSource();
        return update;
    }

    @Override
    public int update(String statement) {
        DataSourceHolder.setMaster();
        int update = super.update(statement);
        DataSourceHolder.clearDataSource();
        return update;
    }

    @Override
    public int insert(String statement, Object parameter) {
        DataSourceHolder.setMaster();
        int insert = super.insert(statement, parameter);
        DataSourceHolder.clearDataSource();
        return insert;
    }

    @Override
    public int insert(String statement) {
        DataSourceHolder.setMaster();
        int insert = super.insert(statement);
        DataSourceHolder.clearDataSource();
        return insert;
    }

    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        DataSourceHolder.setSlave();
        super.select(statement, parameter, rowBounds, handler);
        DataSourceHolder.clearDataSource();
    }

    @Override
    public void select(String statement, Object parameter, ResultHandler handler) {
        DataSourceHolder.setSlave();
        super.select(statement, parameter, handler);
        DataSourceHolder.clearDataSource();
    }

    @Override
    public void select(String statement, ResultHandler handler) {
        DataSourceHolder.setSlave();
        super.select(statement, handler);
        DataSourceHolder.clearDataSource();
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        DataSourceHolder.setSlave();
        List<E> res = super.selectList(statement, parameter, rowBounds);
        DataSourceHolder.clearDataSource();
        return res;
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        DataSourceHolder.setSlave();
        List<E> res = super.selectList(statement, parameter);
        DataSourceHolder.clearDataSource();
        return res;
    }

    @Override
    public <E> List<E> selectList(String statement) {
        DataSourceHolder.setSlave();
        List<E> res = super.selectList(statement);
        DataSourceHolder.clearDataSource();
        return res;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        DataSourceHolder.setSlave();
        Map<K, V> res = super.selectMap(statement, parameter, mapKey, rowBounds);
        DataSourceHolder.clearDataSource();
        return res;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        DataSourceHolder.setSlave();
        Map<K, V> res = super.selectMap(statement, parameter, mapKey);
        DataSourceHolder.clearDataSource();
        return res;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        DataSourceHolder.setSlave();
        Map<K, V> res = super.selectMap(statement, mapKey);
        DataSourceHolder.clearDataSource();
        return res;
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        DataSourceHolder.setSlave();
        T res = super.selectOne(statement, parameter);
        DataSourceHolder.clearDataSource();
        return res;
    }

    @Override
    public <T> T selectOne(String statement) {
        DataSourceHolder.setSlave();
        T res = super.selectOne(statement);
        DataSourceHolder.clearDataSource();
        return res;
    }

}
