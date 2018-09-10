package com.netx.ucenter.biz.user;

import com.netx.common.user.dto.wangMing.AddContributionRequestDto;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserContribution;
import com.netx.ucenter.service.user.UserContributionService;
import com.netx.ucenter.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 用户贡献表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserContributionAction{

    @Autowired
    UserService userService;
    @Autowired
    UserContributionService userContributionService;

    public void deleteByUserId(String userId) throws Exception{
        userContributionService.deleteByUserId(userId);
    }

    @Transactional
    public boolean addContributionRecord(AddContributionRequestDto request) throws Exception {
        //先查询用户
        User user = userService.selectById(request.getUserId());
        if(user==null){
            throw new RuntimeException("此用户（"+request.getUserId()+"）不存在");
        }
        //(1)进行更新用户表，若更新不了，没必要去插入流水
        user.setUpdateUserId(request.getUserId());
        //进行增加或扣除操作
        BigDecimal contribution = user.getContribution();
        contribution = contribution.add(request.getContribution());
        user.setContribution(contribution);
        boolean flagUpdate = userService.updateById(user);//记录是否更新成功

        //(2)插入信用流水，若前一步操作没有成功，则不插入
        boolean flagRecord = false;//记录是否插入流水成功
        if(flagUpdate) {
            UserContribution userContribution = new UserContribution();
            userContribution.setCreateUserId(request.getUserId());
            userContribution.setUserId(request.getUserId());
            userContribution.setContribution(request.getContribution());
            userContribution.setRelatableId(request.getRelatableId());
            userContribution.setRelatableType(request.getRelatableType());
            flagRecord = userContributionService.insert(userContribution);
        }
        return flagRecord;
    }
}
