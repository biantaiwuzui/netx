package com.netx.ucenter.biz.common;

import com.netx.common.vo.common.OrtherSetRequestDto;
import com.netx.common.vo.common.OtherSettingAddRequestDto;
import com.netx.ucenter.model.common.CommonOtherSet;
import com.netx.ucenter.service.common.OtherSetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Create by wongloong on 17-9-4
 */
@Service
public class OtherSetAction{
    private Logger logger = LoggerFactory.getLogger(OtherSetAction.class);

    @Autowired
    private OtherSetService otherSetService;

    public OtherSetService getOtherSetService() {
        return otherSetService;
    }

    public boolean save(OtherSettingAddRequestDto otherSettingAddRequestDto) throws Exception {
        OrtherSetRequestDto d=new OrtherSetRequestDto();
        CommonOtherSet check;
        CommonOtherSet wzCommonOtherSet = new CommonOtherSet();
        BeanUtils.copyProperties(otherSettingAddRequestDto, wzCommonOtherSet);
        wzCommonOtherSet.setCreateTime(new Date());
        //超级管理员
        if (otherSettingAddRequestDto.getType()==1){
            d.setType(1);
            check = this.query(d.getType(),false);
            if (check!=null){
                otherSetService.deleteById(check);
            }
            wzCommonOtherSet.setCanUse(1);
            otherSetService.insert(wzCommonOtherSet);
        }else {
            //普通管理员
            //查询未审核的记录是否存在
            d.setType(0);
            check = this.query(d.getType(),false);
            if (check != null) {
                throw new Exception("已存在待审核设置");
            } else {
                wzCommonOtherSet.setCanUse(0);
                otherSetService.insert(wzCommonOtherSet);
            }
        }
        return true;
    }

    public CommonOtherSet query(Integer type,Boolean isAsc){
        return otherSetService.query(type,isAsc);
    }
}
