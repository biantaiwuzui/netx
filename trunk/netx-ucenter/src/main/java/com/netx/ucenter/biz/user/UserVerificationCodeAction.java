package com.netx.ucenter.biz.user;

import com.netx.ucenter.model.user.UserVerificationCode;
import com.netx.ucenter.service.user.UserVerificationCodeService;
import com.netx.ucenter.vo.request.WzMobileCodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
/**
 * <p>
 * 用户验证码 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserVerificationCodeAction{

    @Autowired
    UserVerificationCodeService userVerificationCodeService;
    
    public UserVerificationCode getMobileCode(String userId,String mobile) throws Exception{
        String code="";
        for(int i=0;i<6;i++){
            code+=(int)(Math.random()*10);
        }
        UserVerificationCode userVerificationCode=new UserVerificationCode();
        if(userId==null || userId.isEmpty()){
            userId="1";
        }else{
            userVerificationCode.setUserId(userId);
        }
        userVerificationCode.setMobile(mobile);
        userVerificationCode.setCode(code);
        userVerificationCode.setCreateUserId(userId);
        Date time=new Date();
        userVerificationCode.setSendAt(time);
        userVerificationCode.setExpiredAt(new Date(time.getTime()+30*60*1000));
        return userVerificationCodeService.insert(userVerificationCode)?userVerificationCode:null;
    }


    @Transactional(rollbackFor = Exception.class)
    public int checkMobileCode(WzMobileCodeRequest response,String userId) {
        Long time = System.currentTimeMillis();
        UserVerificationCode userVerificationCode=userVerificationCodeService.getNewelyCode(response.getMobile(),userId,response.getCode());
        if(userVerificationCode==null){
            return 0;
        }else if(userVerificationCode.getStatus()==1){
            return 3;
        }else if(time<userVerificationCode.getExpiredAt().getTime()){
            userVerificationCode.setPassAt(new Date(time));
            userVerificationCode.setStatus(1);
            if(userVerificationCode.getUserId()!=null){
                userVerificationCode.setUpdateUserId(userVerificationCode.getUserId());
            }
            return userVerificationCodeService.updateById(userVerificationCode)?1:2;
        }
        return 2;
    }

    public void delMobileCode(String userId,String mobile) throws Exception{
        userVerificationCodeService.delMobileCode(userId, mobile);
    }
    //======================== 黎子安  end  ========================
}
