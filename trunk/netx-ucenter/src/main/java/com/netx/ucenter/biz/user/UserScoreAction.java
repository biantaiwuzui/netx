package com.netx.ucenter.biz.user;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.constant.ScoreConstant;
import com.netx.common.user.dto.wangMing.AddScoreRecordRequestDto;
import com.netx.common.user.enums.UserLvEnum;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserScore;
import com.netx.ucenter.service.user.ArticleLikesService;
import com.netx.ucenter.service.user.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户积分表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserScoreAction{

    //“超过10天都没登录”的积分规则的天数
    private static final int DAYS = 10;

    @Autowired
    UserAction userAction;
    @Autowired
    ArticleLikesService articleLikesService;
    @Autowired
    UserScoreService userScoreService;

    public void delScoreRecord(String userId) throws Exception{
        userScoreService.delScoreRecord(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addScoreRecord(AddScoreRecordRequestDto request){
        User user = userAction.getUserService().selectById(request.getUserId());
        if(user==null){
            throw new RuntimeException("用户不存在:"+request.getUserId());
        }
        return addScoreRecord(request,user);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addScoreRecord(AddScoreRecordRequestDto request,User user){
        String userId = request.getUserId();
        Integer code = request.getCode();
        String relatableId = request.getRelatableId();
        String relatableType = request.getRelatableType();
        //若达到积分日上限，添加流水失败
        if(this.isToplimit(userId, code)){ return false; }
        user.setLockVersion(user.getLockVersion()+1);
        //3、更新用户表的积分（和lv）
        boolean flagUpdate = this.updateUserScore(user, ScoreConstant.CODE_MAP_SCORE.get(code));
        //4、插入积分流水
        if(flagUpdate){
            return insertScoreRecord(userId, code, relatableId, relatableType);
        }
        return false;
    }

    
    public List<UserScore> selectScoreRecordList(String userId,Integer current,Integer size) throws Exception{
        Page<UserScore> page = new Page<UserScore>(current, size);
        page = userScoreService.selectScoreRecordList(page,userId,false);
        return page.getRecords();
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean updateScoreByLoginStatus(){
        //计算前n天的开始时间，天数由 DAYS 确定
        Date date = DateTimestampUtil.addDayStartOrEndOfDate(new Date(),3,-DAYS);
        //找到符合条件的用户
        List<User> list = userAction.getUserService().getNoLastLogin(date);
        if(list.isEmpty() || list.size()<1){ return false; }//如果没有符合条件的用户，返回false
        BigDecimal changeScore = ScoreConstant.CODE_MAP_SCORE.get(14);
        for (User user : list){
            user.setScore(user.getScore().add(changeScore));
        }
        return userAction.updateUserByIds(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserRecordInfo(){
        return userAction.getUserService().updateDayDay();
    }

    //------ 私有方法 ------

    /**
     * 插入积分流水表
     * @param userId
     * @param code
     * @param relatableId
     * @param relatableType
     * @return
     */
    private boolean insertScoreRecord(String userId, int code, String relatableId, String relatableType){
        UserScore userScore = new UserScore();
        userScore.setCreateUserId(userId);
        userScore.setUserId(userId);
        userScore.setScore(ScoreConstant.CODE_MAP_SCORE.get(code));
        userScore.setRelatableId(relatableId);
        userScore.setRelatableType(relatableType);
        //获取相关的描述和代码key
        userScore.setCode(code);
        userScore.setDescription(ScoreConstant.CODE_MAP_DESCRIPTION.get(code));
        return userScoreService.insert(userScore);
    }

    /**
     * 更新用户表积分总值
     * @param user 用户对象
     * @param score 需要进行增加或扣除的积分值
     * @return
     */
    private boolean updateUserScore(User user, BigDecimal score){
        BigDecimal totalScore = user.getScore();
        //if(totalScore == null){ throw new RuntimeException("用户id为:"+userId+", 不存在");}
        //进行增加或扣除操作
        totalScore = totalScore.add(score);
        user.setScore(totalScore);
        return this.updateLvByScore(user);
    }

    /**
     * 根据总积分值更新用户lv等级
     * @param user
     * @return
     */
    private boolean updateLvByScore(User user){
        Integer lv = UserLvEnum.getLvByScore(user.getScore());
        user.setLv(lv);
        return userAction.updateUserById(user);
    }

    /**
     * 判断积分项目是否达到日上限
     * @param userId
     * @param code
     * @return true：达到日上限   false：没有达到日上限
     */
    private boolean isToplimit(String userId, Integer code){
        int toplimit = ScoreConstant.CODE_MAP_TOPLIMIT.get(code);
        int addScore = ScoreConstant.CODE_MAP_SCORE.get(code).intValue();
        if(toplimit == -999) { return false; }//-1 的日上限表示不限
        Long nowTimestamp = System.currentTimeMillis();
        Long startTimestamp = DateTimestampUtil.getStartOrEndOfTimestamp(nowTimestamp, 1);//获取这天的开始时间戳
        Long endTimestamp = DateTimestampUtil.getStartOrEndOfTimestamp(nowTimestamp, 2);//获取这天的结束时间戳
        //查询这天的流水记录
        int count = userScoreService.countByUserAndCode(userId,code,startTimestamp,endTimestamp);
        //判断是否有达到日上限
        if(count * addScore < toplimit){
            return false;
        }
        return true;
    }
}
