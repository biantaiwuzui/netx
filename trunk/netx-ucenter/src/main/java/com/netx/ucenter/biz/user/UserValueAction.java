package com.netx.ucenter.biz.user;


import com.netx.common.user.dto.wangMing.AddValueRecordRequestDto;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserValue;
import com.netx.ucenter.service.user.UserService;
import com.netx.ucenter.service.user.UserValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * <p>
 * 用户身价表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserValueAction{
    @Autowired
    UserService userService;
    @Autowired
    UserValueService userValueService;

    @Transactional
    public boolean addValueRecord(AddValueRecordRequestDto request) throws Exception{
        User user = userService.selectById(request.getUserId());
        //(1)进行更新用户表，若更新不了，没必要去插入流水
        //User user = new User();
        user.setUpdateUserId(request.getUserId());
        //user.setId(request.getUserId());
        //先查询用户的总值，再进行增加或扣除操作
        BigDecimal value = user.getValue();
        value = value.add(request.getValue());
        user.setValue(value);
        boolean flagUpdate = userService.updateById(user);//记录是否更新成功

        //(2)插入信用流水，若前一步操作没有成功，则不插入
        boolean flagRecord = false;//记录是否插入流水成功
        if(flagUpdate) {
            UserValue userValue = new UserValue();
            userValue.setCreateUserId(request.getUserId());
            userValue.setUserId(request.getUserId());
            userValue.setValue(request.getValue());
            userValue.setRelatableId(request.getRelatableId());
            userValue.setRelatableType(request.getRelatableType());
            flagRecord = userValueService.insert(userValue);
        }
        return flagRecord;
    }

    
    public void delValueRecord(String userId) throws Exception{
        userValueService.delValueRecord(userId);
    }
}
