package com.netx.utils.cache;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.List;

public class ServiceCacheImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {
    @NetxRedisCache
    @Override
    public T selectById(@CacheKey(key="id") Serializable id) {
        return super.selectById(id);
    }

    @Override
    public List<T> selectBatchIds(List<? extends Serializable> idList) {
        return super.selectBatchIds(idList);
    }


    @Override
    public T selectOne(Wrapper<T> wrapper) {
        return super.selectOne(wrapper);
    }

}
