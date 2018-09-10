package com.netx.credit.biz;

import com.netx.credit.model.UserCreditLikes;
import com.netx.credit.service.UserCreditLikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 梓
 * @date 2018-08-07 18:18
 */

@Service
public class UserCreditLikesAction  {

    @Autowired
    private UserCreditLikesService userCreditLikesService;

    @Autowired
    private CreditAction creditAction;

    @Transactional(rollbackFor = Exception.class)
    public boolean likeCredit(String userId, String creditId){
       UserCreditLikes userCreditLikes = userCreditLikesService.getCreditLikesByUserIdAndCreditId(userId,creditId);
        if(userCreditLikes == null) {//不存在记录
            userCreditLikes = new UserCreditLikes();
            userCreditLikes.setCreditId(creditId);
            userCreditLikes.setUserId(userId);
            userCreditLikes.setCreateUserId(userId);
            userCreditLikes.setIsLike(1);
            userCreditLikesService.insert(userCreditLikes);
//            //进行更新积分操作
//            this.updateScoreByArticleLikes(articleLikes);
            return true;
        }else{//存在记录
            if(userCreditLikes.getIsLike()!=1) {//点赞
                String targetUserId = creditAction.selectUserIdByCreditId(creditId); //获取网信发布者的用户id
                if (targetUserId == null)
                    return false;//操作失败
                userCreditLikes.setIsLike(1);
            }else{//取消点赞
                userCreditLikes.setIsLike(0);
            }
            userCreditLikesService.updateById(userCreditLikes);
            return true;
        }
    }
}
