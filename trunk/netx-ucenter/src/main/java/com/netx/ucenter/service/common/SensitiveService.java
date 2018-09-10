package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonSensitiveMapper;
import com.netx.ucenter.model.common.CommonSensitive;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Create by CHEN-QIAN 2018-5-29 重写
 */
@Service
public class SensitiveService extends ServiceImpl<CommonSensitiveMapper, CommonSensitive>{

    @Autowired
    CommonSensitiveMapper wzCommonSensitiveMapper;

    public CommonSensitiveMapper getWzCommonSensitiveMapper() {
        return wzCommonSensitiveMapper;
    }

    /**
     * 清除数据
     * @param userId
     * @throws Exception
     */
    public void delByUserId(String userId) throws Exception {
        delete(new EntityWrapper<CommonSensitive>().eq("suggest_user_id",userId));
    }

    public List<CommonSensitive> getWzCommonSensitiveList() throws Exception{
        Wrapper wrapper = new EntityWrapper();
        return this.selectList(wrapper);
    }

    /**
     * 敏感词判断
     * @param key
     * @return
     */
    public int checkValue(String key){
        Integer count = wzCommonSensitiveMapper.checkValue(key);
        return count==null?0:count;
    }

    /**
     * 根据value查询关键词条数
     * @param value
     * @return
     */
    public Integer getCountByValue(String value){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("value = {0} AND deleted = 0", value);
        Integer count = selectCount(wrapper);
        if(count == null){
            count = 0;
        }
        return count;
    }

    /**
     * 根据过滤词获取列表
     * @param value
     * @return
     */
    public List<CommonSensitive> getCommonSensitiveByValue(String value){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("value = {0} AND deleted = 0", value);
        return this.selectList(wrapper);
    }

    public boolean checkValue(Collection<Object> values){
        if(values.size()>0){
            Wrapper wrapper = new EntityWrapper();
            String str;
            boolean flag = false;
            for(Object value:values){
                if(value!=null){
                    str = value.toString();
                    if(StringUtils.isNotBlank(str)){
                        wrapper.or("locate(`value`,({0}))",str);
                        flag=true;
                    }
                }
            }
            if(flag){
                wrapper.andNew("deleted=0");
                return selectCount(wrapper)>0;
            }
        }
        return false;
    }
}
