package com.netx.ucenter.biz.common;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.EvaluateTypeEnum;
import com.netx.common.user.dto.wangMing.AddCreditRecordRequestDto;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.*;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserCreditAction;
import com.netx.ucenter.model.common.CommonEvaluate;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.common.EvaluateService;
import com.netx.ucenter.vo.response.CommentResponseDto;
import com.netx.ucenter.vo.response.ReplyResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by wongloong on 17-9-9
 */
@Service
public class EvaluateAction{
    private Logger logger = LoggerFactory.getLogger(EvaluateAction.class);

    @Autowired
    private UserCreditAction userCreditAction;
    @Autowired
    private UserAction userAction;
    @Autowired
    private EvaluateService evaluateService;

    public Integer getCount(CommonEvaluate wzCommonEvaluate){
        Integer total = evaluateService.getCount(wzCommonEvaluate);
        return total==null?0:total;
    }

    public Map<String,Object> getMyDynamic(String userId,Integer current,Integer size){
        List<CommonEvaluate> commonEvaluates = evaluateService.getDynamic(new Page(current,size),userId);
        Map<String,Object> map = new HashMap<>();
        //getUsersAndHeadImg
        List<String> userIds = new ArrayList<>();
        List<EvaluateResponseDto> list = new ArrayList<>();
        Map<String,Object> userData = new HashMap<>();
        if(commonEvaluates.size()>0){
            commonEvaluates.forEach(commonEvaluate -> {
                userIds.add(commonEvaluate.getFromUserId());
                list.add(createMyDynamic(commonEvaluate));
            });
            userData = userAction.getUsersAndHeadImg(userIds);
        }
        map.put("list",list);
        map.put("userData",userData);
        return map;
    }

    private EvaluateResponseDto createMyDynamic(CommonEvaluate commonEvaluate){
        EvaluateResponseDto evaluateResponseDto = VoPoConverter.copyProperties(commonEvaluate,EvaluateResponseDto.class);
        Boolean flag = commonEvaluate.getpId()!=null;
        evaluateResponseDto.setReply(flag);
        evaluateResponseDto.setReplyStr((flag?"回复":"评论")+"了我");
        evaluateResponseDto.setUserId(commonEvaluate.getFromUserId());
        return evaluateResponseDto;
    }

    private User getUser(String userId) throws Exception{
        User user = userAction.getUserService().selectById(userId);
        if(user==null){
            throw new RuntimeException("此用户不存在");
        }
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    public CommonEvaluate addEvaluate(EvaluateAddRequestDto dto,String userId){
        CommonEvaluate evaluate = new CommonEvaluate();
        evaluate.setpId(dto.getpId());
        evaluate.setToUserId(dto.getToUserId());
        evaluate.setContent(dto.getContent());
        evaluate.setTypeId(dto.getTypeId());
        if(dto.getEvaluateType()!=null){
            evaluate.setEvaluateType(dto.getEvaluateType().getValue());
        }
        evaluate.setFromUserId(userId);
        evaluate.setOrderId(dto.getOrderId());
        evaluate.setCreateUserId(userId);
        evaluate.setScore(dto.getScore());
        evaluate.setTypeName(dto.getTypeName());
        if (null != dto.getScore()) {
            CommonEvaluate historyWithScore = evaluateService.getWzCommonEvaluate(evaluate.getFromUserId(),evaluate.getTypeId(),evaluate.getToUserId(),evaluate.getOrderId());
            String fromUserNickName = userAction.getUserService ().getNickNameById ( evaluate.getFromUserId () );
            String description = evaluate.getContent ();
            if (null != historyWithScore) {
                logger.warn("评价添加异常,只能评分一次");
                //throw new RuntimeException("只能评分一次");
            }else{
                int credit = 0;
                AddCreditRecordRequestDto recordRequestDto = new AddCreditRecordRequestDto();
                if (!StringUtils.isEmpty(evaluate.getToUserId())) {
                    recordRequestDto.setUserId(evaluate.getToUserId());
                    if (evaluate.getScore() <= -3) {
                        credit = evaluate.getScore() + 2;
                    } else if (evaluate.getScore() >= 4) {
                        credit = evaluate.getScore() - 3;
                    }
                    recordRequestDto.setDescription(fromUserNickName+"评论了你:"+description);
                    recordRequestDto.setCredit(credit);
                    recordRequestDto.setRelatableType("WzCommonEvaluate");
                }
                if (credit != 0) {
                    return (evaluateService.insert(evaluate) && userCreditAction.addCreditRecord(recordRequestDto))?evaluate:null;
                }
            }

        }
        return evaluateService.insert(evaluate)?evaluate:null;
    }

    private Map<String,Object> getUserMap(List<CommonEvaluate> list){
        List<String> userIds = new ArrayList<>();
        list.forEach(commonEvaluate -> {
            addId(userIds,commonEvaluate.getFromUserId());
            addId(userIds,commonEvaluate.getToUserId());
        });
        Map<String,Object> map = new HashMap<>();
        if(userIds.size()>0){
            map = userAction.getUsersAndHeadImg(userIds);;
        }
        return map;
    }

    /*public Map<String,Object> queryMap(EvaluateQueryRequestDto request) {
        Map<String,Object> map = new HashMap<>();
        List<CommonEvaluate> list = queryPage(request).getRecords();
        if (null != list) {
            list.forEach(evaluate -> {
                evaluate.setChildren(evaluateService.getWzCommonEvaluateByPId(evaluate.getId(),false));
            });
        }
        map.put("list",list);
        map.put("userData",getUserMap(list));
        map.put("total",evaluateService.getEvaluateNums(request.getTypeId(),request.getEvaluateType()));
        return map;
    }*/

    public Map<String,Object> querysMap(EvaluateQueryRequestDto request) {
        Page<CommonEvaluate> page = new Page(request.getCurrent(),request.getSize());
        Map<String,Object> map = new HashMap<>();
        queryEvaluateMap(evaluateService.getWzCommonEvaluate(page,request.getToUserId(),request.getFromUserId(),request.getTypeId(),request.getEvaluateType(),request.getOrderIds()),map);
        map.put("total",page.getTotal());
        return map;
    }

    public Map<String,Object> getCommentOne(String id){
        Map<String,Object> map = new HashMap<>();
        CommonEvaluate commonEvaluate = evaluateService.selectById(id);
        if(commonEvaluate!=null){
            List<String> userIds = new ArrayList<>();
            map.put("comment",getComment(commonEvaluate,userIds));
            addUserData(userIds,map);
        }
        return map;
    }

    private void addUserData(List<String> userIds,Map<String,Object> map){
        if(userIds.size()>0){
            map.put("userData",userAction.getUsersAndHeadImg(userIds));
        }
    }

    private CommentResponseDto getComment(CommonEvaluate commonEvaluate,List<String> userIds){
        addId(userIds,commonEvaluate.getFromUserId());
        addId(userIds,commonEvaluate.getToUserId());
        CommentResponseDto commentResponseDto = VoPoConverter.copyProperties(commonEvaluate,CommentResponseDto.class);
        commentResponseDto.setChildren(getChild(evaluateService.getWzCommonEvaluateByPId(commonEvaluate.getId(),false),userIds));
        return commentResponseDto;
    }

    private void queryEvaluateMap(List<CommonEvaluate> commonEvaluates,Map<String,Object> map){
        List<CommentResponseDto> list = new ArrayList<>();
        if (commonEvaluates!=null && commonEvaluates.size()>0) {
            List<String> userIds = new ArrayList<>();
            commonEvaluates.forEach(evaluate -> {
                list.add(getComment(evaluate,userIds));
            });
            addUserData(userIds,map);
        }
        map.put("list",list);
    }

    private List<String> addId(List<String> userIds,String userId){
        if(!StringUtils.isEmpty(userId)){
            userIds.add(userId);
        }
        return userIds;
    }

    private List<ReplyResponseDto> getChild(List<CommonEvaluate> list, List<String> userIds){
        if(list!=null && list.size()>0){
            List<ReplyResponseDto> responseDtos = new ArrayList<>();
            list.forEach(commonEvaluate -> {
                addId(userIds,commonEvaluate.getFromUserId());
                addId(userIds,commonEvaluate.getToUserId());
                responseDtos.add(VoPoConverter.copyProperties(commonEvaluate,ReplyResponseDto.class));
            });
            return responseDtos;
        }
        return null;
    }

    public Page<CommonEvaluate> queryPage(EvaluateQueryRequestDto request){
        Page<CommonEvaluate> page = new Page<>(request.getCurrent(),request.getSize());
        return evaluateService.getWzCommonEvaluatePage(page,request.getToUserId(),request.getFromUserId(),request.getTypeId(),request.getEvaluateType());
    }

    public Map<String,Object> queryMyEvaluate(MyEvaluateQueryRequestDto request) throws Exception {
        Map<String,Object> map = new HashMap<>();
        List<CommonEvaluate> list = evaluateService.getWzCommonEvaluatePageByToUserId(new Page(request.getCurrent(),request.getSize()), request.getToUserId(),request.getEvaluateType(),false).getRecords();
        CommonEvaluate commonEvaluate = new CommonEvaluate();
        commonEvaluate.setToUserId(request.getToUserId());
        if(request.getEvaluateType()!=null){
            commonEvaluate.setEvaluateType(request.getEvaluateType().getValue());
        }
        map.put("list",list);
        map.put("userData",getUserMap(list));
        map.put("total",getCount(commonEvaluate));
        return map;
    }

    public Map<String,Object> businessQueryPage(BusinessEvaluateQueryRequestDto request,List<String> goodsIds) throws Exception {
        Map<String,Object> map = new HashMap<>();
        if(goodsIds!=null && goodsIds.size()>0){
            Page<CommonEvaluate> page = new Page(request.getCurrent(),request.getSize());
            List<CommonEvaluate> commonEvaluates = evaluateService.getPageByTypeIdsAndPId(goodsIds,null,page,EvaluateTypeEnum.PRODUCT_EVALUATE).getRecords();
            queryEvaluateMap(commonEvaluates,map);
            map.put("total",page.getTotal());
        }
        return map;
    }
}
