package com.netx.fuse.client.ucenter;

import com.netx.common.vo.currency.WzCommonOtherSetResponseDto;
import com.netx.ucenter.biz.common.OtherSetAction;
import com.netx.ucenter.model.common.CommonOtherSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtherSetClientAction {

    private Logger logger = LoggerFactory.getLogger(OtherSetClientAction.class);

    @Autowired
    OtherSetAction otherSetAction;

    public WzCommonOtherSetResponseDto queryRemote(){
        try {
            WzCommonOtherSetResponseDto responseDto=new WzCommonOtherSetResponseDto();
            CommonOtherSet wzCommonOtherSet = otherSetAction.query(1,false);
            if (wzCommonOtherSet == null) {
                throw new RuntimeException("等待管理员设置。。。");
            }
            BeanUtils.copyProperties(wzCommonOtherSet,responseDto);
            return responseDto;
        }catch (Exception e) {
            logger.error("审核其他信息出错:"+e.getMessage(), e);
        }
        return null;
    }
}
