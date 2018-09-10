package com.netx.ucenter.biz.common;
import com.netx.common.common.enums.ArbitrationEnum;
import com.netx.common.vo.common.ArbitrationResultResponseVo;
import com.netx.ucenter.model.common.CommonArbitrationResult;
import com.netx.ucenter.model.common.CommonManageArbitration;
import com.netx.ucenter.service.common.ArbitrationResultService;
import com.netx.ucenter.service.common.ArbitrationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 仲裁管理处理结果表服务 实现类
 * @Author haojun
 * @Date create by 2017/9/30
 */
@Service
public class ArbitrationResultAction {

    @Autowired
    ArbitrationResultService arbitrationResultService;
    @Autowired
    ArbitrationService arbitrationService;

    public ArbitrationResultResponseVo getArbitrationResultResponseVo(String arbitrationId) throws Exception{
        CommonManageArbitration wzCommonManageArbitration=arbitrationService.selectById(arbitrationId);
        if (StringUtils.isNotBlank(wzCommonManageArbitration.getResultId())) {
            CommonArbitrationResult wzCommonArbitrationResult = arbitrationResultService.selectById(wzCommonManageArbitration.getResultId());
            ArbitrationResultResponseVo responseVo = new ArbitrationResultResponseVo();
            //返回仲裁结果信息
            if (wzCommonArbitrationResult != null && wzCommonArbitrationResult.getStatusCode() == ArbitrationEnum.ARBITRATION_SETTLED.getCode()) {
                BeanUtils.copyProperties(wzCommonArbitrationResult, responseVo);
                return responseVo;
            }
        }
        return null;
    }
}
