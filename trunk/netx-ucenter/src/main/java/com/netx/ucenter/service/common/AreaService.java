package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonAreaMapper;
import com.netx.ucenter.model.common.CommonArea;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Create by wongloong on 17-8-29
 */
@Service
public class AreaService extends ServiceImpl<CommonAreaMapper, CommonArea>{
    private Logger logger = LoggerFactory.getLogger(AreaService.class);
    @Autowired
    CommonAreaMapper wzCommonAreaMapper;

    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonArea>().where("create_user_id={0}",userId));
    }

    public List<CommonArea> selectByPidAndFlag(Integer flag,String pid,String id) throws Exception{
        EntityWrapper<CommonArea> entityWrapper = new EntityWrapper<>();
        entityWrapper.setEntity(new CommonArea());
        if (null != flag && flag != -1) {
            entityWrapper.where("flag={0}", flag);
        }
        if (null != pid && !pid.equals("-1")) {
            entityWrapper.where("pid={0}", pid);
        }
        if (StringUtils.isNotBlank(id)) {
            entityWrapper.where("id={0}", id);
        }
        entityWrapper.orderBy("value desc");
        return this.selectList(entityWrapper);
    }

    public boolean deleteByIdAndValidationChildren(String id) throws Exception{
        EntityWrapper<CommonArea> entityWrapper = new EntityWrapper<>();
        entityWrapper.setEntity(new CommonArea());
        entityWrapper.where("pid={0}", id);
        int count = this.selectCount(entityWrapper);
        if (count == 0) {//没有子元素
            this.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
