package com.netx.ucenter.biz.user;

import com.netx.common.user.dto.wangMing.AddIncomeRecordRequestDto;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserIncome;
import com.netx.ucenter.service.user.UserIncomeService;
import com.netx.ucenter.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * <p>
 * 用户收益表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserIncomeAction{

    @Autowired
    UserService userService;

    @Autowired
    UserIncomeService userIncomeService;

    public void deleteByUserId(String userId) throws Exception{
        userIncomeService.deleteByUserId(userId);
    }

    @Transactional
    public boolean addIncomeRecord(AddIncomeRecordRequestDto request) throws Exception {
        User user = userService.selectById(request.getUserId());
        if(user==null){
            throw new RuntimeException("此用户不存在："+request.getUserId());
        }
        //(1)进行更新用户表，若更新不了，没必要去插入流水
        user.setUpdateUserId(request.getUserId());
        user.setId(request.getUserId());
        //进行增加或扣除操作

        BigDecimal income = user.getIncome();
        income = income.add(request.getIncome());
        user.setIncome(income);
        boolean flagUpdate = userService.updateById(user);//记录是否更新成功

        //(2)插入信用流水，若前一步操作没有成功，则不插入
        boolean flagRecord = false;//记录是否插入流水成功
        if(flagUpdate) {
            UserIncome userIncome = new UserIncome();
            userIncome.setCreateUserId(request.getUserId());
            userIncome.setUserId(request.getUserId());
            userIncome.setIncome(request.getIncome());
            userIncome.setRelatableId(request.getRelatableId());
            userIncome.setRelatableType(request.getRelatableType());
            flagRecord = userIncomeService.insert(userIncome);
        }
        return flagRecord;
    }
}
