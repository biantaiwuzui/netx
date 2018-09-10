package com.netx.ucenter.biz.user;

import com.netx.common.user.dto.wangMing.AddCreditRecordRequestDto;
import com.netx.common.user.enums.VerifyCreditEnum;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserVerifyCredit;
import com.netx.ucenter.service.user.UserVerifyCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-01-26
 */
@Service
public class UserVerifyCreditAction{

    @Autowired
    UserCreditAction userCreditAction;
    @Autowired
    UserVerifyCreditService userVerifyCreditService;
    
    public UserVerifyCredit selectUserIdNumber(String value,VerifyCreditEnum creditEnum) throws Exception {
        return userVerifyCreditService.selectUserIdNumber(value, creditEnum);
    }

    public UserVerifyCredit addOrUpdateVerifyCredit(Integer lockVersion,String userId, String idNumber, Integer credit) throws Exception{
        UserVerifyCredit userVerifyCredit = selectUserIdNumber(userId,VerifyCreditEnum.USERIDCREDIT_TYPE);
        UserVerifyCredit userVerifyCredit2 = selectUserIdNumber(idNumber,VerifyCreditEnum.IDNUMBERCREDIT_TYPE);
        if(userVerifyCredit2!=null){
            if(userVerifyCredit==null){
                userVerifyCredit = userVerifyCredit2;
            }
            if(credit>userVerifyCredit2.getCredit()){
                //tudo 添加信用流水
                addCreditHistory(userVerifyCredit2.getCredit(),credit,userId,lockVersion);
                credit = userVerifyCredit2.getCredit();
            }
        }else {
            if(userVerifyCredit==null){
                userVerifyCredit = new UserVerifyCredit();
            }
        }
        userVerifyCredit.setUserId(userId);
        userVerifyCredit.setCredit(credit);
        userVerifyCredit.setIdNumber(idNumber);
        if(userVerifyCredit.getCreateUserId()==null){
            userVerifyCredit.setCreateUserId(userId);
        }
        if(userVerifyCreditService.insertOrUpdate(userVerifyCredit)){
            return userVerifyCredit;
        }
        return null;
    }

    private Boolean addCreditHistory(Integer credit,Integer nowCredis,String userId,Integer lockVersion) throws Exception{
        AddCreditRecordRequestDto addCreditRecordRequestDto = new AddCreditRecordRequestDto();
        addCreditRecordRequestDto.setCredit(nowCredis-credit);
        addCreditRecordRequestDto.setUserId(userId);
        addCreditRecordRequestDto.setDescription("你的身份证信用值("+credit+")低于你现在的信用值("+nowCredis+")");
        addCreditRecordRequestDto.setRelatableType(UserVerifyCredit.class.getTypeName());
        return userCreditAction.addCreditRecord(addCreditRecordRequestDto);
    }

    
    public void deleteUserVerifyCreditByUserId(User user) throws Exception{
        userCreditAction.deleteByUserId(user.getId());
        userVerifyCreditService.getUserVerifyCreditMapper().delectUserId(user.getId(),user.getCredit());
    }
    /**更新替换删除 BXT*/
    public void deleteUserVerifyCreditByUserIdUpdate(User user) throws Exception{
        //更新用户信用表为1
        userCreditAction.deleteByUserId(user.getId());
        userVerifyCreditService.getUserVerifyCreditMapper().delectUserId(user.getId(),user.getCredit());
    }
}
