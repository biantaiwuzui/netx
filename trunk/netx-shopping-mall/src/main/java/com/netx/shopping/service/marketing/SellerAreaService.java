package com.netx.shopping.service.marketing;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.marketing.SellerAreaMapper;
import com.netx.shopping.model.marketing.SellerArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class SellerAreaService extends ServiceImpl<SellerAreaMapper, SellerArea> {

    @Autowired
    SellerAreaMapper areaMapper;

    public SellerArea getArea(String areaName){
        return areaMapper.selectAreaByAreaName(areaName);
    }
}
