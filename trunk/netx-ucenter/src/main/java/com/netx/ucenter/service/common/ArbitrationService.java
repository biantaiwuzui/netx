package com.netx.ucenter.service.common;

import cn.jmessage.api.utils.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.common.enums.ArbitrationEnum;
import com.netx.common.vo.common.ArbitrationSelectResponseVo;
import com.netx.ucenter.mapper.common.CommonManageArbitrationMapper;
import com.netx.ucenter.model.common.CommonManageArbitration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理仲裁管理的服务实现类
 * @Author haojun
 * @Date create by 2017/9/30
 */
@Service
public class ArbitrationService extends ServiceImpl<CommonManageArbitrationMapper,CommonManageArbitration>{

    private Logger logger = LoggerFactory.getLogger(ArbitrationService.class);

    @Autowired
    private CommonManageArbitrationMapper wzCommonManageArbitrationMapper;

    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonManageArbitration>().where("from_user_id={0} or to_user_id={0}",userId));
    }

    public List<ArbitrationSelectResponseVo> selectByUserIdAll(String userId){
        try {
            return wzCommonManageArbitrationMapper.selectByUserIdAll(userId);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new ArrayList<>();
    }

    public List<ArbitrationSelectResponseVo> selectByNicknameAndStatusCodes(String userId,Integer[] code){
        try {
            return wzCommonManageArbitrationMapper.selectByNicknameAndStatusCodes(userId, code);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new ArrayList<>();
    }

    public List<ArbitrationSelectResponseVo> selectByNicknameAll(String nickName){
        try {
            wzCommonManageArbitrationMapper.selectByNicknameAll(nickName);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new ArrayList<>();
    }

    public Long selectCountByToUserNetNumerAndFromUserId(String fromUserId,String toUserId) throws Exception{
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("count(1)").andNew("status_code <4 ");
        if(StringUtils.isNotEmpty(fromUserId)){
            wrapper.andNew("from_user_id = {0}",fromUserId);
        }
        if(StringUtils.isNotEmpty(toUserId)){
            wrapper.andNew("to_user_id = {0}",toUserId);
        }
        return (Long) this.selectObj(wrapper);
    }

    public CommonManageArbitration getArbitrationByType(String typeId,Integer type) throws Exception{
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("type_id={0} and type={1} and status_code<{2}", typeId, type, ArbitrationEnum.ARBITRATION_SETTLED.getCode());
        return this.selectOne(wrapper);
    }

    public List<CommonManageArbitration> getWaitingSettleArbitration(Page page,Integer type, Integer statusCode) throws Exception{
        EntityWrapper<CommonManageArbitration> wrapper = new EntityWrapper<>();
        wrapper.where("deleted = 0");
        if(type != null){
            wrapper.and("type = {0}", type);
        }
        if(statusCode != null){
            wrapper.and("status_code = {0}", statusCode);
        }
        wrapper.orderBy("time", true);
        return selectPage(page, wrapper).getRecords();
    }
}