package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonSensitiveSuggestMapper;
import com.netx.ucenter.model.common.CommonSensitiveSuggest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by CHEN-QIAN 2018-5-29 重写
 */
@Service
public class SensitiveSuggestService extends ServiceImpl<CommonSensitiveSuggestMapper, CommonSensitiveSuggest>{

    private Logger logger = LoggerFactory.getLogger(SensitiveSuggestService.class);

    /**
     * 清除信息
     * @param userId
     * @throws Exception
     */
    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonSensitiveSuggest>().where("suggest_user_id = {0} OR audit_user_id = {0}", userId));
    }

    /**
     * 根据过滤词获取数量
     * @param value
     * @param delOrSave
     * @return
     */
    public Integer getCountByValueAndDelOrSave(String value, Integer delOrSave){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("value = {0} AND del_or_save = {1} AND deleted = 0", value, delOrSave);
        Integer count = selectCount(wrapper);
        if(count == null){
            count = 0;
        }
        return count;
    }

    /**
     * CHEN-QIAN
     * 分页查询已通过/未通过/全部审核列表
     * @param page
     * @param status
     * @param delOrSave
     * @return
     */
    public List<CommonSensitiveSuggest> querySensitiveSuggestList(Page page, Integer status, Integer delOrSave){
        EntityWrapper entityWrapper = new EntityWrapper(new CommonSensitiveSuggest());
        entityWrapper.where("del_or_save={0} AND deleted = 0", delOrSave);
        if(status == 0){
            entityWrapper.and("audit_user_id = '0'");
        }else if(status == 1){
            entityWrapper.and("audit_user_id != '0'");
        }
        entityWrapper.orderBy("create_time", true);
        return selectPage(page,entityWrapper).getRecords();
    }

}
