package com.netx.ucenter.biz.common;

import com.netx.common.common.enums.LimitEnum;
import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.user.enums.SystemBlackStatusEnum;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.ArticleSanctionRequestDto;
import com.netx.common.vo.common.PunishAuthorTransferRequestDto;
import com.netx.ucenter.biz.user.SystemBlacklistAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.common.CommonArticleLimit;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.common.ArticleLimitedService;
import com.netx.ucenter.service.user.UserPhotoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ArticleLimitedAction{
    private Logger logger = LoggerFactory.getLogger(ArticleLimitedAction.class);

    @Autowired
    ArticleLimitedService articleLimitedService;

    @Autowired
    UserPhotoService userPhotoService;

    @Autowired
    SystemBlacklistAction systemBlacklistAction;

    @Autowired
    UserAction userAction;

    private Integer getAge(Date birthday){
        return ComputeAgeUtils.getAgeByBirthday(birthday);
    }

    public UserInfoResponseDto queryByUserNetworkNum(String userNetworkNumId) {
        /*List<SelectFieldEnum> list=new ArrayList<>();
        list.add(SelectFieldEnum.USER_ID);
        list.add(SelectFieldEnum.USER_NUMBER);
        list.add(SelectFieldEnum.NICKNAME);
        list.add(SelectFieldEnum.AGE);
        list.add(SelectFieldEnum.CREDIT);
        list.add(SelectFieldEnum.SEX);
        list.add(SelectFieldEnum.LV);
        list.add(SelectFieldEnum.HEAD_IMG_URL);*/

        try {
            User user = userAction.getUserByUserNumber(userNetworkNumId);
            UserInfoResponseDto responseDto = VoPoConverter.copyProperties(user,UserInfoResponseDto.class);
            responseDto.setHeadImgUrl(userPhotoService.selectHeadImg(user.getId()));
            //UserInfoResponseDto responseDto=routerService.selectUserInfo(userNetworkNumId,SelectConditionEnum.USER_NUMBER,list);
            //return responseDto!=null?responseDto:null;
            return responseDto;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean punishAuthorByNetworkNum(PunishAuthorTransferRequestDto request) throws Exception{
        CommonArticleLimit wzCommonArticleLimited=articleLimitedService.selectById(request.getArticleLimitedId());
        if(wzCommonArticleLimited==null){
            wzCommonArticleLimited=new CommonArticleLimit();
            BeanUtils.copyProperties(request,wzCommonArticleLimited);
            /*List<SelectFieldEnum> list=new ArrayList<>();
            list.add(SelectFieldEnum.NICKNAME);
            list.add(SelectFieldEnum.AGE);
            list.add(SelectFieldEnum.SEX);
            list.add(SelectFieldEnum.LV);*/
            try {
                //UserInfoResponseDto responseDto=routerService.selectUserInfo(request.getUserId(),SelectConditionEnum.USER_ID,list);
                User user = userAction.getUserService().selectById(request.getUserId());
                if(user!=null){
                    wzCommonArticleLimited.setUserAge(getAge(user.getBirthday()));
                    wzCommonArticleLimited.setUserSex(user.getSex()=="男"?1:2);
                    wzCommonArticleLimited.setUserLevel(user.getLv());
                    wzCommonArticleLimited.setUserNickname(user.getNickname());
                }
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }try {
                if(StringUtils.isNotBlank(request.getOperatorUserId())){
                    User user = userAction.getUserService().selectById(request.getOperatorUserId());
                    if(user==null){
                        throw new RuntimeException("用户不存在："+request.getOperatorUserId());
                    }
                    String operatorNickName=user.getNickname();
                    wzCommonArticleLimited.setOperatorNickname(operatorNickName);
                }
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
            return articleLimitedService.insertWzCommonArticleLimited(wzCommonArticleLimited);
        }else {
            BeanUtils.copyProperties(request, wzCommonArticleLimited);
            if (StringUtils.isNotBlank(request.getOperatorUserId())) {
                try {
                    User user = userAction.getUserService().selectById(request.getOperatorUserId());
                    if(user==null){
                        throw new RuntimeException("用户不存在："+request.getOperatorUserId());
                    }
                    String operatorNickName = user.getNickname();
                    wzCommonArticleLimited.setOperatorNickname(operatorNickName);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
            wzCommonArticleLimited.setUpdateTime(new Date());
            return articleLimitedService.updateById(wzCommonArticleLimited);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean sanctionAritcle(ArticleSanctionRequestDto request) {
        CommonArticleLimit wzCommonArticleLimited=articleLimitedService.selectById(request.getArticleLimitedId());
        wzCommonArticleLimited.setReason(request.getReason());
        wzCommonArticleLimited.setUpdateTime(new Date());
        wzCommonArticleLimited.setUpdateUserId(request.getUserId());
        wzCommonArticleLimited.setDeleted(0);
        if(wzCommonArticleLimited.getLimitMeasure()== LimitEnum.LIMIT_BLACKLIST_PUSH.getCode()){//解除系统黑名单操作
            try {
                systemBlacklistAction.operateSystemBlacklist(wzCommonArticleLimited.getUserId(),request.getUserId(),"资讯受限名单解除黑名单处罚", SystemBlackStatusEnum.RELEASE.getValue());
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }

        }
        return  articleLimitedService.updateById(wzCommonArticleLimited);
    }

    public boolean isCanReleaseArticle(String userId, long releaseTime) {
        CommonArticleLimit wzCommonArticleLimited=articleLimitedService.selectOneByUserId(userId);
        if(wzCommonArticleLimited==null){
            return true;
        }
        else{
            LimitEnum limitEnum=LimitEnum.getEnumByCode(wzCommonArticleLimited.getLimitMeasure());
            Long start=new Long(0);
            Long end=new Long(0);
            Integer limitValue=wzCommonArticleLimited.getLimitValue();
            switch (limitEnum){
                case LIMIT_MONTHLY_PUBLISH://每月发布的资讯
                {
                    start=DateTimestampUtil.getTimestampOfStartOrEndDate(releaseTime,1).getTime();
                    end=DateTimestampUtil.getTimestampOfStartOrEndDate(releaseTime,2).getTime();
                    Long count= articleLimitedService.countByStartToEnd(start,end);
                    if(count<limitValue){
                        return true;
                    }
                }
                break;
                case LIMIT_DAYLY_PUBLISH://每天发布的资讯
                {
                    start= DateTimestampUtil.getStartOrEndOfTimestamp(releaseTime,1);
                    end=DateTimestampUtil.getStartOrEndOfTimestamp(releaseTime,2);
                    Long count= articleLimitedService.countByStartToEnd(start,end);
                    if(count<limitValue){
                        return true;
                    }
                }
                break;
//                case LIMIT_EVERY_PAY_PUBLISH://付费发布资讯
//                {
//
//                }
//                    break;
                case LIMIT_FORBID_DAYLY_PUBLISH://禁止发布持续天数
                {
                    //发布的时间如果还在受限日期内，就不能发布，否则可以发布资讯
                    if(DateTimestampUtil.TIME_DIFFERENCE*limitValue+wzCommonArticleLimited.getDate().getTime()<=releaseTime){
                        return true;
                    }
                }
                break;
                case LIMIT_FORBID_PUBLISH://禁止发布任何资讯
                    break;
                default:
                    return true;
            }
        }
        return false;
    }

    public List<CommonArticleLimit> selectArticleLimitedList(Integer current,Integer size){
        return articleLimitedService.selectArticleLimitedList(current, size);
    }
}
