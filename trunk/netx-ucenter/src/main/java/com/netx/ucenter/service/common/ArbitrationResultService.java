package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.vo.common.ArbitrationSelectResponseVo;
import com.netx.ucenter.mapper.common.CommonArbitrationResultMapper;
import com.netx.ucenter.model.common.CommonArbitrationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 仲裁管理处理结果表服务 实现类
 * @Author haojun
 * @Date create by 2017/9/30
 */
@Service
public class ArbitrationResultService extends ServiceImpl<CommonArbitrationResultMapper,CommonArbitrationResult>{

    private Logger logger = LoggerFactory.getLogger(ArbitrationResultService.class);

    @Autowired
    private CommonArbitrationResultMapper wzCommonArbitrationResultMapper;

    public List<ArbitrationSelectResponseVo> selectByOpUserId(String userId){
        try {
            return (List<ArbitrationSelectResponseVo>) wzCommonArbitrationResultMapper.selectByOpUserId(userId);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new ArrayList<>();
    }

    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonArbitrationResult>().where("op_user_id={0}",userId));
    }
}
