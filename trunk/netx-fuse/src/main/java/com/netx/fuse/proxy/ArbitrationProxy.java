package com.netx.fuse.proxy;

import com.netx.common.common.enums.ArbitrationEnum;
import com.netx.common.vo.business.CheckOrderArbitrationRequestDto;
import com.netx.common.vo.common.ArbitrationAddComplaintRequestDto;
import com.netx.common.vo.common.ArbitrationAppealRequestDto;
import com.netx.common.vo.common.ArbitrationResultResponseVo;
import com.netx.fuse.biz.ucenter.ArbitrationFuseAction;
import com.netx.ucenter.biz.common.ArbitrationAction;
import com.netx.ucenter.biz.common.ArbitrationResultAction;
import com.netx.ucenter.model.common.CommonManageArbitration;
import com.netx.ucenter.service.common.CommonServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 仲裁投诉工具类
 * hj.Mao
 * 2017-11-10
 */
@Component
public class ArbitrationProxy {

    private Logger logger = LoggerFactory.getLogger(ArbitrationProxy.class);

    @Autowired
    private ArbitrationAction arbitrationAction;

    @Autowired
    private ArbitrationFuseAction arbitrationFuseAction;

    @Autowired
    private ArbitrationResultAction arbitrationResultAction;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    /**
     * 添加投诉接口
     * @param requestDto
     * @return
     */
    public String addComplaint(ArbitrationAddComplaintRequestDto requestDto){
        try {
            arbitrationAction.checkSubmit(requestDto);
            arbitrationAction.isCanAddComplaint(requestDto);
            String arbitrationId=arbitrationFuseAction.addComplaint(requestDto);
            if(arbitrationId!=null){
                return arbitrationId;
            }
            throw new RuntimeException("确认,立即投诉插入数据库异常");
        }
        catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public boolean  checkIsArbitrationResultDone(CheckOrderArbitrationRequestDto request){
       /*Result result=arbitrationClient.checkIsComplainted(request);
        JSONObject jsonObject= JSON.parseObject(JSONObject.toJSONString(result.getObject()));
        boolean isTrue= (boolean) jsonObject.get("result");
        if(isTrue){
            return true;
        }
        return false;*/
        try {
            CommonManageArbitration wzCommonManageArbitration=commonServiceProvider.getArbitrationService().selectById(request.getArbitrationId());
            if (wzCommonManageArbitration != null) {
                if (wzCommonManageArbitration.getStatusCode() != ArbitrationEnum.ARBITRATION_DOESNT_SETTLE.getCode()) {
                    return true;
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return false;
    }

    /**
     * 获取投诉结果接口
     * @param request
     * @return
     */
    public ArbitrationResultResponseVo getArbitrationResult(CheckOrderArbitrationRequestDto request){
        /*Result<ArbitrationResultResponseVo> result=arbitrationClient.getArbitrationResultVo(request);
//        System.out.println(result.getObject()+"$$$$$$$$$$$$$$$$$$");
        if(result.getCode()==0&&result.getObject()!=null){
            JSONObject jsonObject= JSON.parseObject(JSONObject.toJSONString(result.getObject()));
//            System.out.println(result+"###################");
            ArbitrationResultResponseVo responseVo=JSONObject.parseObject(jsonObject.toJSONString(),ArbitrationResultResponseVo.class);
            if(responseVo!=null){
                return responseVo;
            }
        }
        return null;*/
        try {
            ArbitrationResultResponseVo arbitrationResultResponseVo = arbitrationResultAction.getArbitrationResultResponseVo(request.getArbitrationId());
            if(arbitrationResultResponseVo != null){
                return arbitrationResultResponseVo;
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 申诉
     * @param requestDto
     * @return
     */
    public boolean appealArbitration(ArbitrationAppealRequestDto requestDto){
        /*Result result=arbitrationClient.appealArbitration(requestDto);
        System.out.println(result);
        if(result.getCode()==0){
            return true;
        }
        return false;*/
        try {
           String errorStr= arbitrationAction.isCanAppealArbitration(requestDto);
           if(errorStr!=null){
               logger.error(errorStr);
               return false;
           }
            if(!arbitrationAction.appealArbitration(requestDto)){
                throw new RuntimeException("申诉插入数据库异常");
            }
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return false;
        }
        return true;
    }
}
