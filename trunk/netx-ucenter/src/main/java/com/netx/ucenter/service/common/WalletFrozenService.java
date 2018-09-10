package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.common.enums.FrozenTypeEnum;
import com.netx.ucenter.mapper.common.CommonWalletFrozenMapper;
import com.netx.ucenter.model.common.CommonWalletFrozen;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Create by wongloong on 17-9-17
 */
@Service
public class WalletFrozenService extends ServiceImpl<CommonWalletFrozenMapper, CommonWalletFrozen>{
    private Logger logger = LoggerFactory.getLogger(WalletFrozenService.class);

    public List<CommonWalletFrozen> selectPage(Page page, String userId, String typeId) throws Exception{
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("create_time",false).andNew("deleted={0}",0);
        if (userId!=null&&userId!="") {
            entityWrapper.where("user_id={0}", userId);
        }
        if (typeId!=null&&typeId!="") {
            entityWrapper.where("type_id={0}",typeId);
        }
        return selectPage(page, entityWrapper).getRecords();
    }

    public List<CommonWalletFrozen> getFrozenList(String typeId,String userId,Integer hasConsume,FrozenTypeEnum type){
        EntityWrapper entityWrapper = new EntityWrapper(new CommonWalletFrozen());
        entityWrapper.where("type_id={0} and has_consume={1} and deleted=0", typeId,hasConsume);
        if(StringUtils.isNotBlank(userId)){
            entityWrapper.eq("user_id",userId);
        }
        if(type!=null){
            entityWrapper.and("frozen_type={0}",type.getName());
        }
        return this.selectList(entityWrapper);
    }

    public Boolean deleteWalletFrozen(String userId) throws Exception{
        return delete(new EntityWrapper<CommonWalletFrozen>().where("user_id={0} or to_user_id={0}",userId));
    }
}
