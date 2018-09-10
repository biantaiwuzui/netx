package com.netx.fuse.biz.ucenter;

import com.netx.common.common.enums.ArbitrationEnum;
import com.netx.common.vo.business.CheckOrderArbitrationRequestDto;
import com.netx.common.vo.common.ArbitrationAcceptHandleRequestDto;
import com.netx.common.vo.common.ArbitrationAddComplaintRequestDto;
import com.netx.common.vo.common.ArbitrationRefuseAcceptHandleRequestDto;
import com.netx.fuse.client.shoppingmall.OrderClientAction;
import com.netx.fuse.client.worth.DemandClientAction;
import com.netx.fuse.client.worth.WishClientAction;
import com.netx.ucenter.biz.common.ArbitrationAction;
import com.netx.ucenter.biz.common.ArbitrationResultAction;
import com.netx.ucenter.model.common.CommonArbitrationResult;
import com.netx.ucenter.model.common.CommonManageArbitration;
import com.netx.ucenter.service.common.CommonServiceProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
public class  ArbitrationFuseAction {

    private Logger logger = LoggerFactory.getLogger(ArbitrationFuseAction.class);

    @Autowired
    private ArbitrationAction arbitrationAction;

    @Autowired
    private ArbitrationResultAction arbitrationResultAction;

    @Autowired
    OrderClientAction orderClientAction;

    @Autowired
    DemandClientAction demandClientAction;

    @Autowired
    WishClientAction wishClientAction;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    public boolean commonAcceptHandle(ArbitrationAcceptHandleRequestDto requestDto) throws Exception {
        if(arbitrationAction.commonAcceptHandle(requestDto)) {
            if (this.operatorOtherModule(requestDto.getType(), requestDto.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加投诉接口
     * @param requestDto
     * @return
     */
    public String addComplaints(ArbitrationAddComplaintRequestDto requestDto) throws Exception{
        try {
            arbitrationAction.checkSubmit(requestDto);
            arbitrationAction.isCanAddComplaint(requestDto);
            String arbitrationId=addComplaint(requestDto);
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

    @Transactional(rollbackFor = Exception.class)
    public String addComplaint(ArbitrationAddComplaintRequestDto request){
        try {
            CommonManageArbitration commonManageArbitration = arbitrationAction.addComplaintRequestDtoToCommonManageArbitration(request);
            if(commonServiceProvider.getArbitrationService().insert(commonManageArbitration)){
                logger.info("投诉插入成功");
                if (request.getType() == 4 && StringUtils.isNotEmpty(request.getTypeId())) {
                    wishClientAction.wishComplaint(request.getTypeId());
                }
                return commonManageArbitration.getId();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        logger.info("投诉插入失败");
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean refuseAcceptHandle(ArbitrationRefuseAcceptHandleRequestDto requestDto) {
        CommonManageArbitration wzCommonManageArbitration = commonServiceProvider.getArbitrationService().selectById(requestDto.getId());
        CommonArbitrationResult wzCommonArbitrationResult = new CommonArbitrationResult();
        if (wzCommonManageArbitration == null) {
            return false;
        }
        wzCommonArbitrationResult.setOpCommonAnswer(0);//管理员不同意
        wzCommonArbitrationResult.setDescriptions(requestDto.getDescriptions());
        wzCommonArbitrationResult.setOpCommonReason(requestDto.getDescriptions());
        wzCommonArbitrationResult.setOpUserId(requestDto.getOpUserId());
        wzCommonArbitrationResult.setCreateUserId(requestDto.getOpUserId());
        //wzCommonArbitrationResult.setCreateTime(new Date() / 1000);
        //wzCommonArbitrationResult.setDelTag(0);
        wzCommonArbitrationResult.setStatusCode(ArbitrationEnum.ARBITRATION_REFUSE_SETTLE.getCode());
        if (commonServiceProvider.getArbitrationResultService().insert(wzCommonArbitrationResult)) {
            //String id = wzCommonArbitrationResult.getId();
            wzCommonManageArbitration.setStatusCode(ArbitrationEnum.ARBITRATION_REFUSE_SETTLE.getCode());
            wzCommonManageArbitration.setResultId(wzCommonArbitrationResult.getId());
            if (commonServiceProvider.getArbitrationService().updateById(wzCommonManageArbitration)) {
                try {
                    if (this.operatorOtherModule(wzCommonManageArbitration.getType(), wzCommonManageArbitration.getId())) {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return false;
    }


    public String acceptCheckAndHandle(ArbitrationAcceptHandleRequestDto requestDto) {
        String result = acceptHandleCheck(requestDto);
        if(result==null){
            if(!acceptHandle(requestDto)){
                return "该仲裁已经处理";
            }
        }
        return result;
    }

    private String acceptHandleCheck(ArbitrationAcceptHandleRequestDto requestDto){
        if(requestDto.getType()!=null){
            boolean isBusinessFlag=false,isCommonFlag=false;
            String temp;
            do {
                if (requestDto.getFromUserCreditRefund().intValue() == 1) {
                    String regex="^(\\+?[1-9][0-9]*)|(\\d+(\\.\\d+)?)$";
                    Pattern pattern=Pattern.compile(regex);
                    temp = requestDto.getFromUserCreditPointReason();
                    logger.info("requestDto.getFromUserCreditPoint():"+requestDto.getFromUserCreditPoint()+"requestDto:"+requestDto);
                    boolean bRet=pattern.matcher(requestDto.getFromUserCreditPoint().toString()).matches();
                    if (StringUtils.isEmpty(temp) ||!bRet) {
                        return "其表单验证不通过";
                    }
                }
                //选中被扣除投诉人信用值的复选框
                if (requestDto.getToUserCreditRefund().intValue() == 1) {
                    String regex="^(\\+?[1-9][0-9]*)|(\\d+(\\.\\d+)?)$";
                    Pattern pattern=Pattern.compile(regex);
                    boolean bRet=pattern.matcher(requestDto.getToUserCreditPoint().toString()).matches();
                    temp = requestDto.getToUserCreditPointReason();
                    if (StringUtils.isEmpty(temp) || !bRet) {
                        return "其表单验证不通过";
                    }
                }
                if (commonServiceProvider.getArbitrationService().selectById(requestDto.getId()) != null) {
                    if (requestDto.getType().intValue() == 1) {//订单类型的仲裁验证
                        if (StringUtils.isEmpty(requestDto.getRefundArbitrateReason()) || StringUtils.isEmpty(requestDto.getReturnArbitrateReason())) {
                            isBusinessFlag = true;
                            break;
                        }
                    } else {
                        if (requestDto.getSubstractReleaseFrozenMoneyRefund() != null) {
                            if (StringUtils.isEmpty(requestDto.getSubstractReleaseFrozenMoneyReason())) {
                                isCommonFlag = true;
                                break;
                            }
                        }
                    }
                }
            }while(false);
            if(isBusinessFlag || isCommonFlag){
                if(isBusinessFlag){
                    return "订单审核表单验证不通过";
                }
                if(isCommonFlag){
                    return "其他审核表单验证不通过";
                }
            }
            return null;
        }
        return "其表单验证不通过";
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean acceptHandle(ArbitrationAcceptHandleRequestDto requestDto){
        //boolean bRet = false;
        //WzCommonManageArbitration wzCommonManageArbitration = null;
        //long timeStamp = new Date() / 1000;
        if (requestDto.getType().intValue() == 1) {
            CommonManageArbitration wzCommonManageArbitration = commonServiceProvider.getArbitrationService().selectById(requestDto.getId());
            CommonArbitrationResult wzCommonArbitrationResult = RequestVoToEntity(requestDto);//dto赋值
            //wzCommonArbitrationResult.setCreateTime(timeStamp);
            //wzCommonArbitrationResult.setUpdateTime(timeStamp);
            wzCommonArbitrationResult.setCreateUserId(requestDto.getOpUserId());
            wzCommonArbitrationResult.setStatusCode(ArbitrationEnum.ARBITRATION_SETTLED.getCode());
            if (commonServiceProvider.getArbitrationResultService().insert(wzCommonArbitrationResult)) {
                String id = wzCommonArbitrationResult.getId();
                //关联结果表,为manager_arbitration记录关联arbitration_result的主键id
                wzCommonManageArbitration.setResultId(id);
                wzCommonManageArbitration.setStatusCode(ArbitrationEnum.ARBITRATION_SETTLED.getCode());
                //wzCommonManageArbitration.setUpdateTime(timeStamp);
                wzCommonManageArbitration.setUpdateUserId(requestDto.getOpUserId());
                if (commonServiceProvider.getArbitrationService().updateById(wzCommonManageArbitration)) {//管理员审核完后更新数据库
                    if (this.operatorOtherModule(requestDto.getType(), requestDto.getId())) {
                        return true;
                    }
                }
            }
        } else {
            try {
                /*if (this.commonAcceptHandle(requestDto)) {
                    return true;
                }*/
                return arbitrationAction.commonAcceptHandle(requestDto);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage());
            }
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    boolean operatorOtherModule(int type, String arbitrationId){
        CommonManageArbitration wzCommonManageArbitration = commonServiceProvider.getArbitrationService().selectById(arbitrationId);
        if (wzCommonManageArbitration == null) {
            return false;
        }
        CommonArbitrationResult wzCommonArbitrationResult = commonServiceProvider.getArbitrationResultService().selectById(wzCommonManageArbitration.getResultId());
        if (wzCommonArbitrationResult == null) {
            return false;
        }
        boolean isSettled = wzCommonArbitrationResult.getStatusCode() == ArbitrationEnum.ARBITRATION_SETTLED.getCode();
        if (isSettled) {
            if (StringUtils.isNotBlank(wzCommonManageArbitration.getFromUserId())) {
                if (wzCommonArbitrationResult.getFromUserCreditPoint() != null && wzCommonArbitrationResult.getFromUserCreditPoint().intValue() > 0) {
                    if (!arbitrationAction.substractCreditValue(wzCommonManageArbitration.getId(), wzCommonManageArbitration.getFromUserId(), wzCommonArbitrationResult.getFromUserCreditPoint().intValue())) {//扣减他人信用值
                        StringBuilder builder = new StringBuilder("扣除").append(wzCommonManageArbitration.getFromNickname()).append("的信用值：").append(wzCommonArbitrationResult.getFromUserCreditPoint().intValue()).append("分失败");
                        throw new RuntimeException(builder.toString());
                    }
                }
            }
            if (StringUtils.isNotEmpty(wzCommonManageArbitration.getToUserId())) {
                if (wzCommonArbitrationResult.getToUserCreditPoint() != null && wzCommonArbitrationResult.getToUserCreditPoint().intValue() > 0) {
                    if (!arbitrationAction.substractCreditValue(wzCommonManageArbitration.getId(), wzCommonManageArbitration.getToUserId(), wzCommonArbitrationResult.getToUserCreditPoint().intValue())) {//扣减他人信用值
                        StringBuilder builder = new StringBuilder("扣除").append(wzCommonManageArbitration.getToNickname()).append("的信用值：").append(wzCommonArbitrationResult.getToUserCreditPoint().intValue()).append("分失败");
                        throw new RuntimeException(builder.toString());
                    }
                }
            }
        }
        boolean repealMoney = isSettled && (wzCommonArbitrationResult.getSubstractReleaseFrozenMoneyRefund() != null ? wzCommonArbitrationResult.getSubstractReleaseFrozenMoneyRefund() == 1 : false);

        switch (type) {
            case 1://商品订单投诉类型(订单相关)
                CheckOrderArbitrationRequestDto requestDto = new CheckOrderArbitrationRequestDto();
                requestDto.setArbitrationId(arbitrationId);
                requestDto.setTypeId(wzCommonManageArbitration.getTypeId());
                if (orderClientAction.autoRumSettleOrderArbitrationResult(requestDto)== null) {
                    return false;
                }
                break;
            case 2: //网号其他类型(言行举止相关)
                break;
            case 3: //需求投诉
                if (repealMoney) {
                    return demandClientAction.registerRejectRefund(wzCommonManageArbitration.getTypeId(), wzCommonManageArbitration.getFromUserId());
                } else {
                    return demandClientAction.registerAcceptRefund(wzCommonManageArbitration.getTypeId(), wzCommonManageArbitration.getFromUserId());
                }
                //break;
            case 4: //心愿投诉
                if (repealMoney) {
                    try {
                        wishClientAction.wishComplaintSuccess(wzCommonManageArbitration.getTypeId());
                    }catch (Exception e){
                        return false;
                    }
                }
                break;
            case 5://技能投诉
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * vo类转换成Entity类对象
     *
     * @param requestDto
     * @return
     */
    private CommonArbitrationResult RequestVoToEntity(ArbitrationAcceptHandleRequestDto requestDto) {
        CommonArbitrationResult wzCommonArbitrationResult = new CommonArbitrationResult();
        BeanUtils.copyProperties(requestDto, wzCommonArbitrationResult);
        wzCommonArbitrationResult.setId(null);
        return wzCommonArbitrationResult;
    }
}
