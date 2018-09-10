package com.netx.ucenter.biz.common;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.ArbitrationEnum;
import com.netx.common.common.enums.ArbitrationQueryTypeEnum;
import com.netx.common.common.enums.ArbitrationReturnTypeEnum;
import com.netx.common.user.dto.wangMing.AddCreditRecordRequestDto;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.vo.common.*;
import com.netx.ucenter.biz.user.UserCreditAction;
import com.netx.ucenter.model.common.CommonArbitrationResult;
import com.netx.ucenter.model.common.CommonManageArbitration;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.common.ArbitrationResultService;
import com.netx.ucenter.service.common.ArbitrationService;
import com.netx.ucenter.service.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * 处理仲裁管理的服务实现类
 * @Author haojun
 * @Date create by 2017/9/30
 */

@Service
public class ArbitrationAction{

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private ArbitrationService arbitrationService;
    @Autowired
    private ArbitrationResultService arbitrationResultService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserCreditAction userCreditAction;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    private Integer getAge(Date birthday){
        return ComputeAgeUtils.getAgeByBirthday(birthday);
    }

    public CommonManageArbitration addComplaintRequestDtoToCommonManageArbitration(ArbitrationAddComplaintRequestDto request) throws Exception{
        CommonManageArbitration wzCommonManageArbitration = new CommonManageArbitration();
        wzCommonManageArbitration.setTime(new Date());
        wzCommonManageArbitration = this.searchUserByParam(request.getFromUserId(), true, false, wzCommonManageArbitration);
        if (request.getType() != null && request.getType() == 2) {
            wzCommonManageArbitration = this.searchUserByParam(request.getToUserNetworkNum(), false, true, wzCommonManageArbitration);
        }
        if (StringUtils.isNotEmpty(request.getToUserId()) && !request.getToUserId().trim().equals("")) {
            wzCommonManageArbitration = this.searchUserByParam(request.getToUserId(), false, false, wzCommonManageArbitration);
        }
        wzCommonManageArbitration.setFromSrcUrl(request.getUrl());
        wzCommonManageArbitration.setTheme(request.getTheme());
        wzCommonManageArbitration.setTypeId(request.getTypeId());
        wzCommonManageArbitration.setType(request.getType());
        wzCommonManageArbitration.setReason(request.getReason());
        wzCommonManageArbitration.setStatusCode(ArbitrationEnum.ARBITRATION_DOESNT_SETTLE.getCode());
        wzCommonManageArbitration.setCreateTime(new Date());
        wzCommonManageArbitration.setCreateUserId(request.getFromUserId());
        return wzCommonManageArbitration;
    }

    private CommonManageArbitration searchUserByParam(String data, boolean isFromUserId, boolean userNumberOrUserId, CommonManageArbitration wzOne) throws Exception {
        User user = null;
        if (StringUtils.isNotBlank(data)) {
            if (userNumberOrUserId) {//网号查询
                user = userService.getUserByUserNumber(data);
            } else {//根据用户id查询
                user = userService.selectById(data);
            }
        }
        if (user != null) {
            if (isFromUserId) {//赋值fromUserId
                wzOne.setFromNickname(user.getNickname());
                wzOne.setFromUserCreditValue(user.getCredit());
                wzOne.setFromUserAge(getAge(user.getBirthday()));
                wzOne.setFromUserId(user.getId());
                wzOne.setFromUserLevel(user.getLv());
                wzOne.setFromUserSex(user.getSex());
            } else {//赋值toUserId
                wzOne.setToNickname(user.getNickname());
                wzOne.setToUserCreditValue(user.getCredit());
                wzOne.setToUserAge(getAge(user.getBirthday()));
                wzOne.setToUserId(user.getId());
                wzOne.setToUserLevel(user.getLv());
                wzOne.setToUserSex(user.getSex());
            }
        }
        return wzOne;
    }

    /**
     * 提交申诉
     *
     * @param requestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean appealArbitration(ArbitrationAppealRequestDto requestDto) {
        boolean bRet = false;
        CommonManageArbitration wzCommonManageArbitration = arbitrationService.selectById(requestDto.getId());
        if (wzCommonManageArbitration != null) {
            //添加申诉信息
            BeanUtils.copyProperties(requestDto, wzCommonManageArbitration);
            wzCommonManageArbitration.setAppealDate(new Date());
            //改变状态
            wzCommonManageArbitration.setStatusCode(ArbitrationEnum.ARBITRATION_OTHER_COMPLAINT.getCode());
            if (arbitrationService.updateById(wzCommonManageArbitration)) {
                bRet = true;
            }
        }
        return bRet;
    }

    public List<ArbitrationSelectResponseVo> selectByParam(ArbitrationQueryParamaterRequestDto requestDto) {
        //Map<String, Object> map = new HashMap<>();
        Integer queryType = requestDto.getQueryType();
        Integer returnType = requestDto.getReturnType();
        //List<ArbitrationSelectResponseVo> list = null;
        //通过用户本人自己查询
        /*if (queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_MYUSERID.getCode()
                && returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_ALL.getCode()) {
            list = (List<ArbitrationSelectResponseVo>) wzCommonManageArbitrationMapper.selectByUserIdAll(requestDto.getUserId());
        } else if (queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_MYUSERID.getCode()
                && returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_CANUPDATE.getCode()) {
            list = (List<ArbitrationSelectResponseVo>) wzCommonManageArbitrationMapper.selectByNicknameAndStatusCodes(requestDto.getUserId(), new Integer[]{ArbitrationEnum.ARBITRATION_DOESNT_SETTLE.getCode(),
                    ArbitrationEnum.ARBITRATION_OTHER_COMPLAINT.getCode()});//查询没有被处理的
        } else if (queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_MYUSERID.getCode()
                && returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_READONLY.getCode()) {
            list = (List<ArbitrationSelectResponseVo>) wzCommonManageArbitrationMapper.selectByNicknameAndStatusCodes(requestDto.getUserId(), new Integer[]{ArbitrationEnum.ARBITRATION_SETTLING.getCode(),
                    ArbitrationEnum.ARBITRATION_SETTLING.getCode(), ArbitrationEnum.ARBITRATION_REFUSE_SETTLE.getCode()});//查询正在处理或者已经处理过的
        }*/
        //管理员根据nickname查询,全部
        /*else if (queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_NICKNAME.getCode()
                && returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_ALL.getCode()) {
            list = (List<ArbitrationSelectResponseVo>) wzCommonManageArbitrationMapper.selectByNicknameAll(requestDto.getNickname());
        } else if (queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_NICKNAME.getCode()
                && returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_CANUPDATE.getCode()) {
            list = (List<ArbitrationSelectResponseVo>) wzCommonManageArbitrationMapper.selectByNicknameAll(requestDto.getNickname());
        } else if (queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_NICKNAME.getCode()
                && returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_READONLY.getCode()) {
            list = (List<ArbitrationSelectResponseVo>) wzCommonManageArbitrationMapper.selectByNicknameAll(requestDto.getNickname());
        }*/
        //管理员通过输入userID查询
       /* else if (queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_INPUTUSERID.getCode()
                && returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_ALL.getCode()) {
            list = (List<ArbitrationSelectResponseVo>) wzCommonManageArbitrationMapper.selectByUserIdAll(requestDto.getInputUserId());
        } else if (queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_INPUTUSERID.getCode()
                && returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_CANUPDATE.getCode()) {
            list = (List<ArbitrationSelectResponseVo>) wzCommonManageArbitrationMapper.selectByNicknameAndStatusCodes(requestDto.getUserId(), new Integer[]{ArbitrationEnum.ARBITRATION_DOESNT_SETTLE.getCode(),
                    ArbitrationEnum.ARBITRATION_OTHER_COMPLAINT.getCode()});//查询没有被处理的
        } else if (queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_INPUTUSERID.getCode()
                && returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_READONLY.getCode()) {
            list = (List<ArbitrationSelectResponseVo>) wzCommonManageArbitrationMapper.selectByNicknameAndStatusCodes(requestDto.getUserId(), new Integer[]{ArbitrationEnum.ARBITRATION_SETTLING.getCode(),
                    ArbitrationEnum.ARBITRATION_SETTLING.getCode(), ArbitrationEnum.ARBITRATION_REFUSE_SETTLE.getCode()});//查询正在处理或者已经处理过的
        }
        //管理员通过操作者ID查询
        else if (queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_OPUSERID.getCode()) {
            if(returnType != null)
                return (List<ArbitrationSelectResponseVo>) wzCommonArbitrationResultMapper.selectByOpUserId(requestDto.getOpUserId());
        }*/
        //管理员通过操作者ID查询
        if(queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_OPUSERID.getCode()){
            if(returnType != null){
                return arbitrationResultService.selectByOpUserId(requestDto.getOpUserId());
            }
        }
        //管理员根据nickname查询,全部
        else if(queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_NICKNAME.getCode()){
            if(returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_ALL.getCode() || returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_CANUPDATE.getCode() || returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_READONLY.getCode()){
                return arbitrationService.selectByNicknameAll(requestDto.getNickname());
            }
        }else{
            if(returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_ALL.getCode()){
                if(queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_INPUTUSERID.getCode()){
                    return arbitrationService.selectByUserIdAll(requestDto.getInputUserId());
                }else if(queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_MYUSERID.getCode()){
                    return arbitrationService.selectByUserIdAll(requestDto.getUserId());
                }
            }
            else{
                if(queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_INPUTUSERID.getCode() || queryType == ArbitrationQueryTypeEnum.ARBITRATION_QUERY_BY_MYUSERID.getCode()){
                    if(returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_CANUPDATE.getCode()){
                        return arbitrationService.selectByNicknameAndStatusCodes(requestDto.getUserId(), new Integer[]{ArbitrationEnum.ARBITRATION_DOESNT_SETTLE.getCode(), ArbitrationEnum.ARBITRATION_OTHER_COMPLAINT.getCode()});
                    }
                    if(returnType == ArbitrationReturnTypeEnum.ARBITRATION_RETURN_TYPE_READONLY.getCode()){
                        return arbitrationService.selectByNicknameAndStatusCodes(requestDto.getUserId(), new Integer[]{ArbitrationEnum.ARBITRATION_SETTLING.getCode(), ArbitrationEnum.ARBITRATION_SETTLING.getCode(), ArbitrationEnum.ARBITRATION_REFUSE_SETTLE.getCode()});
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    public Map<String, Object> selectByArbitrationId(String id) {
        Map<String, Object> map = new HashMap<>();
        CommonManageArbitration wzCommonManageArbitration = this.getWzCommonManageArbitration(id);
        wzCommonManageArbitration = this.addUrlPrefix(wzCommonManageArbitration);//图片+前缀
        map.put("wzCommonManageArbitration", wzCommonManageArbitration);
        map.put("wzCommonArbitrationResult", this.getWzCommonArbitrationResult(wzCommonManageArbitration.getResultId()));
        return map;
    }

    public String isCanAddComplaint(ArbitrationAddComplaintRequestDto request) throws Exception {
        if (userService.selectById(request.getFromUserId()) == null) {
            return  "投诉者用户不存在";
        }
        if (StringUtils.isNotBlank(request.getToUserNetworkNum())) {
            User toUser = userService.getUserByUserNumber(request.getToUserNetworkNum());
            if (toUser != null) {
                Long count = arbitrationService.selectCountByToUserNetNumerAndFromUserId(request.getFromUserId(), toUser.getId());
                if (count > 0) {//如果存在一条以上尚未处理完的仲裁
                    return "你已经提交过仲裁";
                }
            } else {//被投诉对象不存在
                return "被投诉者用户不存在";
            }
        } else if (StringUtils.isNotBlank(request.getTypeId()) && request.getType() != null) {
            //只要还存在处理完的仲裁记录
            //wrapper.where("typeId={0} and type={1} and statusCode<{2}", request.getTypeId(), request.getType(), ArbitrationEnum.ARBITRATION_SETTLED.getCode());
            CommonManageArbitration wzCommonManageArbitration = arbitrationService.getArbitrationByType(request.getTypeId(),request.getType());
            if (wzCommonManageArbitration != null) {
                if (wzCommonManageArbitration.getFromUserId().equals(request.getFromUserId())) {//投诉者已经投诉过了
                    return "你已经提交过仲裁";
                } else {//别人投诉过了
                    return "当前已经被其他用户投诉中";
                }
            }
        }
        return null;
    }

    public String isCanAppealArbitration(ArbitrationAppealRequestDto request) throws Exception{
        CommonManageArbitration wzOne = arbitrationService.selectById(request.getId());
        if (wzOne == null) {//没有该仲裁
            return  "您申诉的仲裁不存在";
        }
        if (wzOne.getStatusCode() != ArbitrationEnum.ARBITRATION_DOESNT_SETTLE.getCode())//仲裁状态不是等待申诉的
        {
            //如果已经提交申诉的用户不是被投诉者
            if (!wzOne.getToUserId().equals(request.getUserId())) {
                return "您不是该仲裁的被投诉者";
            }
        }
        return null;
    }

    public String isCanRefuseAcceptHandle(ArbitrationRefuseAcceptHandleRequestDto request) {
        CommonManageArbitration wzOne = arbitrationService.selectById(request.getId());
        if (wzOne == null) {
            return "仲裁列不存在";
        }
        if (userService.selectById(request.getOpUserId()) == null){
            return "管理员不存在";
        }
        if (wzOne.getStatusCode() > ArbitrationEnum.ARBITRATION_OTHER_COMPLAINT.getCode()) {
            return "该仲裁已经被处理过";
        }
        return null;
    }

    public String checkSubmit(ArbitrationAddComplaintRequestDto request) throws Exception{
        if (request.getType() == 2) {
            if (StringUtils.isEmpty(request.getToUserNetworkNum()) || StringUtils.isEmpty(request.getTheme())) {
                 return "被投诉对象或者主题未填";
            }
        } else {
            if (StringUtils.isEmpty(request.getTypeId())) {
                return "事件id为空，投诉取消";
            }
        }
        return null;
    }

    public List<CommonManageArbitration> getWaitingSettleArbitration(ArbitrationSelectByTypeVo request) throws Exception{
        Page<CommonManageArbitration> page = new Page<>();
        page.setSize(request.getSize());
        page.setCurrent(request.getCurrent());
        List<CommonManageArbitration> list = arbitrationService.getWaitingSettleArbitration(page, request.getType(), request.getStatusCode());
        if (!list.isEmpty()) {
            int index = 0;
            for (CommonManageArbitration one : list) {
                one = this.UpdateNewlyInfo(one);//刷新用户最新信息
                one = this.addUrlPrefix(one);//添加前缀
                list.set(index++, one);
            }
        }
        return list;
    }

    /**
     * 为图片+前缀
     * @param one
     * @return
     */
    private CommonManageArbitration addUrlPrefix(CommonManageArbitration one) {
        if (one != null) {
            if(StringUtils.isNotBlank(one.getFromSrcUrl())) {
                String split[] = one.getFromSrcUrl().split(",");
                StringBuffer sb = new StringBuffer("");
                for (String temp : split) {
                    sb.append(addImgUrlPreUtil.getUserImgPre(temp) + ",");
                }
                one.setFromSrcUrl(sb.deleteCharAt(sb.toString().lastIndexOf(",")).toString());
            }
            if(StringUtils.isNotBlank(one.getAppealSrcUrl())) {
                String[] split = one.getAppealSrcUrl().split(",");
                StringBuffer sb = new StringBuffer("");
                for (String temp : split) {
                    sb.append(addImgUrlPreUtil.getUserImgPre(temp) + ",");
                }
                one.setAppealSrcUrl(sb.deleteCharAt(sb.toString().lastIndexOf(",")).toString());
            }
        }
        return one;
    }

    @Transactional
    public boolean commonAcceptHandle(ArbitrationAcceptHandleRequestDto requestDto) throws Exception {
        CommonManageArbitration wzCommonManageArbitration = arbitrationService.selectById(requestDto.getId());
        Date timeStamp = new Date();
        if (wzCommonManageArbitration != null && requestDto.getType() != 1) {
            if (wzCommonManageArbitration.getStatusCode() == ArbitrationEnum.ARBITRATION_OTHER_COMPLAINT.getCode()) {//状态是等待受理
                if (StringUtils.isNotEmpty(wzCommonManageArbitration.getResultId())) {//已经关联了仲裁结果表的
                    if (arbitrationResultService.selectById(wzCommonManageArbitration.getResultId()) == null) {//仲裁结果表是真是存在的
                        //仲裁结果是假数据
                        String resultId = this.addArbitrationResult(requestDto, new Date());
                        if (resultId != null) {
                            wzCommonManageArbitration.setResultId(resultId);
                        }
                    }
                } else {
                    //没有仲裁过的
                    String resultId = this.addArbitrationResult(requestDto, timeStamp);
                    if (resultId != null) {
                        wzCommonManageArbitration.setResultId(resultId);
                    }
                }
                wzCommonManageArbitration.setStatusCode(ArbitrationEnum.ARBITRATION_SETTLED.getCode());//改为已经仲裁
                wzCommonManageArbitration.setUpdateTime(timeStamp);
                //操作成功
                return arbitrationService.updateById(wzCommonManageArbitration);
            }
        }
        return false;
    }

    private String addArbitrationResult(ArbitrationAcceptHandleRequestDto requestDto, Date timeStamp) {
        CommonArbitrationResult one = new CommonArbitrationResult();
        one.setCreateTime(timeStamp);
        one.setOpUserId(requestDto.getOpUserId());
        one.setOpCommonAnswer(1);//管理员同意了投诉者
        if (requestDto.getSubstractReleaseFrozenMoneyRefund() != null && requestDto.getSubstractReleaseFrozenMoneyRefund().intValue() == 1) {//选中了退款
            one.setOpCommonReason(requestDto.getSubstractReleaseFrozenMoneyReason());
        }
        if (requestDto.getFromUserCreditRefund() != null && requestDto.getFromUserCreditRefund().intValue() == 1) {//选中扣投诉者信用值
            one.setFromUserCreditPointReason(requestDto.getFromUserCreditPointReason());
            one.setFromUserCreditPoint(requestDto.getFromUserCreditPoint());
        }
        if (requestDto.getToUserCreditRefund() != null && requestDto.getToUserCreditRefund().intValue() == 1) {//选中扣被投诉者信用值
            one.setToUserCreditPointReason(requestDto.getToUserCreditPointReason());
            one.setToUserCreditPoint(requestDto.getToUserCreditPoint());
        }
        one.setCreateUserId(requestDto.getOpUserId());
        one.setStatusCode(ArbitrationEnum.ARBITRATION_SETTLED.getCode());
        if (arbitrationResultService.insert(one)) {
            return one.getId();
        }
        return null;
    }

    CommonManageArbitration UpdateNewlyInfo(CommonManageArbitration one){
        if (one != null) {
            if (StringUtils.isNotEmpty(one.getFromUserId())) {//更新数据
                User fromUser = userService.selectById(one.getFromUserId());
                if(fromUser != null) {
                    one.setFromUserAge(getAge(fromUser.getBirthday()));
                    one.setFromUserLevel(fromUser.getLv());
                    one.setFromUserCreditValue(fromUser.getCredit());
                    one.setFromUserSex(fromUser.getSex());
                    one.setFromNickname(fromUser.getNickname());
                }
            }
            if (StringUtils.isNotEmpty(one.getToUserId())) {//更新数据
                User toUser = userService.selectById(one.getToUserId());
                if(toUser != null) {
                    one.setToUserAge(getAge(toUser.getBirthday()));
                    one.setToUserLevel(toUser.getLv());
                    one.setToUserCreditValue(toUser.getCredit());
                    one.setToUserSex(toUser.getSex());
                    one.setToNickname(toUser.getNickname());
                }
            }
            arbitrationService.updateById(one);
            return one;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean substractCreditValue(String arbitrationId, String userId, Integer creditValue) {
        AddCreditRecordRequestDto creditDto = new AddCreditRecordRequestDto();
        User user = userService.selectById(userId);
        if (user != null) {
            if (user.getCredit() > 0 && user.getCredit() < Math.abs(creditValue)) {//如果用户的信用值小于要扣减的信用值，就把该用户剩下的信用都减掉
                creditValue = user.getCredit();
            }
            try {
                creditDto.setDescription("管理员仲裁扣减信用值");
                creditDto.setCredit(creditValue > 0 ? -creditValue : creditValue);
                creditDto.setUserId(userId);
                creditDto.setRelatableId(arbitrationId);
                creditDto.setRelatableType("ArbitrationManagement");
                if (!userCreditAction.addCreditRecord(creditDto)) {
                    return false;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return true;
    }

    private CommonArbitrationResult getWzCommonArbitrationResult(String id) {
        if (id == null || id.equals("")) return new CommonArbitrationResult();
        CommonArbitrationResult wzCommonArbitrationResult = arbitrationResultService.selectById(id);
        if (wzCommonArbitrationResult == null) {
            wzCommonArbitrationResult = new CommonArbitrationResult();
        }
        return wzCommonArbitrationResult;
    }

    private CommonManageArbitration getWzCommonManageArbitration(String id) {
        if (id == null || id.equals("")) return new CommonManageArbitration();
        CommonManageArbitration wzCommonManageArbitration = arbitrationService.selectById(id);
        if (wzCommonManageArbitration == null) {
            wzCommonManageArbitration = new CommonManageArbitration();
        }
        return wzCommonManageArbitration;
    }
}