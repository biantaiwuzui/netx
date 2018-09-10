package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.ucenter.mapper.common.CommonImHistoryMapper;
import com.netx.ucenter.model.common.CommonImHistory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-02-07
 */
@Service
public class WzCommonImHistoryService extends ServiceImpl<CommonImHistoryMapper, CommonImHistory>{

    public List<CommonImHistory> queryCommonImHistoryByUserId(MessageTypeEnum typeEnum, String userId, PushMessageDocTypeEnum docTypeEnum, Page page){
        Wrapper<CommonImHistory> wrapper = new EntityWrapper<>();
        wrapper.where("to_user_id = {0}",userId);
        if(typeEnum!=null){
            wrapper.and("type = {0}",typeEnum.getName());
        }
        if(docTypeEnum!=null){
            wrapper.and("doc_type = {0}",docTypeEnum.getValue());
        }
        wrapper.orderBy("send_time desc");
        return this.selectPage(page,wrapper).getRecords();
    }

    /**
     * 重载queryCommonImHistoryByUserId方法，查询多个docTypeEnums的动态
     * @param typeEnum
     * @param userId
     * @param docTypeEnums
     * @param page
     * @return
     */
    public List<CommonImHistory> queryCommonImHistoryByUserId(MessageTypeEnum typeEnum, String userId, List<PushMessageDocTypeEnum> docTypeEnums, Page page){
        Wrapper<CommonImHistory> wrapper = new EntityWrapper<>();
        wrapper.where("to_user_id = {0}",userId);
        if(typeEnum!=null){
            wrapper.and("type = {0}",typeEnum.getName());
        }
        if(docTypeEnums!=null && !docTypeEnums.isEmpty()){
            List<String> docTypesValues = new ArrayList<>();
            docTypeEnums.forEach(docTypeEnum ->{
                docTypesValues.add(docTypeEnum.getValue());
            });
            wrapper.in("doc_type",docTypesValues);

        }
        wrapper.orderBy("send_time desc");
        return this.selectPage(page,wrapper).getRecords();
    }

    public boolean deleteImHistory(String userId){
        Wrapper<CommonImHistory> wrapper = new EntityWrapper<>();
        wrapper.where("from_user_id={0} or to_user_id={1}",userId,userId);
        return delete(wrapper);
    }

    public CommonImHistory query(String id,String userId){
        Wrapper<CommonImHistory> wrapper = new EntityWrapper<>();
        wrapper.where("id = {0} and to_user_id={1} and deleted=0",id,userId);
        return selectOne(wrapper);
    }
    
    /*根据user_id,doc_type,type_id获取数据*/
    public CommonImHistory getNewsReadStatus(String userId, String typeId, PushMessageDocTypeEnum docTypeEnum){
        EntityWrapper<CommonImHistory> wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0} and type_id={1} and doc_type={2}",userId,typeId,docTypeEnum.getValue());
        return selectOne(wrapper);
    }
    
}
