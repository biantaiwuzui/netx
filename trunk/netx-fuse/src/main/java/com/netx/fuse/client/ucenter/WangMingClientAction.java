package com.netx.fuse.client.ucenter;

import com.netx.common.user.dto.wangMing.*;
import com.netx.common.user.util.VoPoConverter;
import com.netx.ucenter.biz.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WangMingClientAction {

    private Logger logger = LoggerFactory.getLogger(WangMingClientAction.class);

    @Autowired
    private UserContributionAction userContributionAction;
    @Autowired
    private UserCreditAction userCreditAction;
    @Autowired
    private UserIncomeAction incomeAction;
    @Autowired
    private UserScoreAction userScoreAction;
    @Autowired
    private UserValueAction userValueAction;

    /**
     * 添加贡献流水
     * @param request
     * @return
     */
    public boolean addContributionRecord(AddContributionRequestDto request) {
        try{
            if(userContributionAction.addContributionRecord(request)){
                return true;
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean addCreditRecord(Map map) {
        return addCreditRecord(VoPoConverter.copyProperties(map,AddCreditRecordRequestDto.class));
    }

    /**
     * 添加信用流水
     * @param request
     * @return
     */
    public Boolean addCreditRecord(AddCreditRecordRequestDto request) {
        try{
            if(userCreditAction.addCreditRecord(request)){
                return true;
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 添加收益流水
     * @param request
     * @return
     */
    public Boolean addIncomeRecord(AddIncomeRecordRequestDto request) {
        try{
            if(incomeAction.addIncomeRecord(request)){
                return true;
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 添加积分流水
     * @param request
     * @return
     */
    public Boolean addScoreRecord(AddScoreRecordRequestDto request) {
        try{
            if(userScoreAction.addScoreRecord(request)){
                return true;
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 添加身价流水
     * @param request
     * @return
     */
    public Boolean addValueRecord(AddValueRecordRequestDto request) {
        try{
            if(userValueAction.addValueRecord(request)){
                return true;
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 定时任务：根据用户登录状态更新积分
     * @return
     */
    public Boolean updateScoreByLoginStatus() {
        try {
            return userScoreAction.updateUserRecordInfo();
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
