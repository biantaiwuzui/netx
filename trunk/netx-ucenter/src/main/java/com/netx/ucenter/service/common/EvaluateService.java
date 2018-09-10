package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.common.enums.EvaluateTypeEnum;
import com.netx.ucenter.mapper.common.CommonEvaluateMapper;
import com.netx.ucenter.model.common.CommonEvaluate;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Create by wongloong on 17-9-9
 */
@Service
public class EvaluateService extends ServiceImpl<CommonEvaluateMapper, CommonEvaluate>{

    public void delByUserId(String userId) throws Exception{
        delete(createWrapperByFromOrTo(userId));
    }

    private Wrapper createWrapperByFromOrTo(String userId){
        return new EntityWrapper<CommonEvaluate>().where("from_user_id={0} or to_user_id={0}",userId);
    }

    public List<CommonEvaluate> getDynamic(Page page,String userId){
        Wrapper wrapper = new EntityWrapper<CommonEvaluate>();
        wrapper.where("to_user_id = {0}",userId);
        wrapper.orderBy("create_time desc");
        return selectPage(page,wrapper).getRecords();
    }

    public Integer getCount(CommonEvaluate wzCommonEvaluate){
        EntityWrapper<CommonEvaluate> wrapper = new EntityWrapper();
        if(wzCommonEvaluate!=null){
            wrapper.setEntity(wzCommonEvaluate);
        }
        return selectCount(wrapper);
    }

    public CommonEvaluate getWzCommonEvaluate(String fromUserId,String typeId,String toUserId,String orderId){
        EntityWrapper ew = new EntityWrapper(new CommonEvaluate());
        ew.where("score is not null");
        ew.and("from_user_id={0} and type_id={1}", fromUserId,typeId);
        if(StringUtils.isNotBlank(toUserId)){
            ew.and("to_user_id={0}", toUserId);
        }
        if(StringUtils.isNotBlank(orderId)){
            ew.and("order_id={0}", orderId);
        }
        return this.selectOne(ew);
    }

    public Page<CommonEvaluate> getWzCommonEvaluatePage(Page page,String toUserId,String fromUserId,String typeId,EvaluateTypeEnum evaluateType){
        EntityWrapper entityWrapper = new EntityWrapper(new CommonEvaluate());
        entityWrapper.and("p_id is null");
        if (!StringUtils.isEmpty(toUserId)) {
            entityWrapper.and("to_user_id={0}", toUserId);
        }
        if (!StringUtils.isEmpty(fromUserId)) {
            entityWrapper.and("from_user_id={0}", fromUserId);
        }
        if (!StringUtils.isEmpty(typeId)) {
            entityWrapper.and("type_id={0}", typeId);
        }
        if (evaluateType!=null) {
            entityWrapper.and("evaluate_type={0}",evaluateType.getValue());
        }
        entityWrapper.orderBy("create_time desc");
        return this.selectPage(page,entityWrapper);
    }
    
    /* 查询订单评论 **/
    public List<CommonEvaluate> getWzCommonEvaluatePageAndTotal(Page page,String toUserId,String fromUserId,String typeId,EvaluateTypeEnum evaluateType){
        EntityWrapper entityWrapper = new EntityWrapper(new CommonEvaluate());
        entityWrapper.and("p_id is null");
        if (!StringUtils.isEmpty(toUserId)) {
            entityWrapper.and("to_user_id={0}", toUserId);
        }
        if (!StringUtils.isEmpty(fromUserId)) {
            entityWrapper.and("from_user_id={0}", fromUserId);
        }
        if (!StringUtils.isEmpty(typeId)) {
            entityWrapper.and("type_id={0}", typeId);
        }
        if (evaluateType!=null) {
            entityWrapper.and("evaluate_type={0}",evaluateType.getValue());
        }
        entityWrapper.orderBy("create_time desc");
        return selectPage(page,entityWrapper).getRecords();
    }
    
    /* 可以根据任意条件查询订单评论 **/
    public List<CommonEvaluate> getWzCommonEvaluate(Page page, String toUserId, String fromUserId, String typeId, EvaluateTypeEnum evaluateTypeEnum, String orderIds){
        EntityWrapper wrapper = new EntityWrapper(new CommonEvaluate());
        wrapper.and("p_id is null");
        if (!StringUtils.isEmpty(toUserId)) {
            wrapper.and("to_user_id={0}", toUserId);
        }
        if (!StringUtils.isEmpty(fromUserId)) {
            wrapper.and("from_user_id={0}", fromUserId);
        }
        if (!StringUtils.isEmpty(typeId)) {
            wrapper.and("type_id={0}", typeId);
        }
        if (evaluateTypeEnum!=null) {
            wrapper.and("evaluate_type={0}",evaluateTypeEnum.getValue());
        }
        if (orderIds != null){
            wrapper.and("order_id={0}",orderIds);
        }
        wrapper.orderBy("create_time desc");
        return selectPage(page,wrapper).getRecords();
    }

    public Page getWzCommonEvaluatePageByToUserId(Page page, String toUserId, EvaluateTypeEnum evaluateType, Boolean isAsc){
        EntityWrapper entityWrapper = new EntityWrapper(new CommonEvaluate());
        entityWrapper.where("to_user_id={0}",toUserId);
        if(evaluateType!=null){
            entityWrapper.and("evaluate_type={0}",evaluateType.getValue());
        }
        entityWrapper.orderBy("create_time"+getIsAscStr(isAsc));
        return this.selectPage(page,entityWrapper);
    }

    private String getIsAscStr(Boolean isAsc){
        return isAsc?" asc":" desc";
    }

    public List<CommonEvaluate> getWzCommonEvaluateByPId(String pId,Boolean isAsc){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("p_id={0}", pId);
        wrapper.orderBy("create_time"+getIsAscStr(isAsc));
        return this.selectList(wrapper);
    }

    public int getEvaluateNums(String typeId,EvaluateTypeEnum typeEnum){
        EntityWrapper entityWrapper = new EntityWrapper(new CommonEvaluate());
        entityWrapper.where("type_id={0} and deleted = 0",typeId);
        if(typeEnum!=null){
            entityWrapper.and("evaluate_type = {0}",typeEnum.getValue());
        }
        return this.selectCount(entityWrapper);
    }

    public Page getPageByTypeIdsAndPId(List<String> typeIds,String pId,Page page,EvaluateTypeEnum typeEnum){
        EntityWrapper entityWrapper = new EntityWrapper(new CommonEvaluate());
        entityWrapper.in("type_id",typeIds);
        if(StringUtils.isNotBlank(pId)){
            entityWrapper.and("p_id = "+pId);
        }else{
            entityWrapper.and("p_id is null");
        }
        if(typeEnum!=null){
            entityWrapper.and("evaluate_type = {0}",typeEnum.getValue());
        }
        entityWrapper.orderBy("create_time desc");
        return this.selectPage(page,entityWrapper);
    }
}
