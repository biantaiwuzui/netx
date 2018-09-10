package com.netx.fuse.client.ucenter;


import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.CostSettingResponseDto;
import com.netx.ucenter.biz.common.CostAction;
import com.netx.ucenter.model.common.CommonCostSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CostSettingClientAction {

    private Logger logger = LoggerFactory.getLogger(CostSettingClientAction.class);

    @Autowired
    CostAction costAction;

    public CostSettingResponseDto feignQuery(){
        try {
            return costAction.query();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
