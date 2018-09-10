package com.netx.ucenter.biz.user;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.dto.wangMing.AddCreditRecordRequestDto;
import com.netx.common.user.dto.wangMing.AddScoreRecordRequestDto;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserCredit;
import com.netx.ucenter.service.user.UserCreditService;
import com.netx.ucenter.service.user.UserServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * <p>
 * 用户信用表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserCreditAction{
    @Autowired
    private UserServiceProvider userServiceProvider;
    @Autowired
    private UserScoreAction userScoreAction;
    @Autowired
    UserCreditService userCreditService;
    @Autowired
    UserAction userAction;

    @Transactional(rollbackFor = Exception.class)
    public boolean addCreditRecord(AddCreditRecordRequestDto request){
        //先查询用户
        User user = userServiceProvider.getUserService().selectById(request.getUserId());
        if(user==null){
            throw new RuntimeException("此用户("+request.getUserId()+")不存在");
        }
        return addCreditRecord(request,user);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addCreditRecord(AddCreditRecordRequestDto request,User user){
        return addCreditRecord(user,request.getCredit(),request.getDescription(),request.getRelatableId(),request.getRelatableType());
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addCreditRecord(User user,int credit,String description,String relatableId,String relatableType){
        //(2)进行更新用户表，若更新不了，没必要去插入流水
        user.setUpdateUserId(user.getId());
        user.setLockVersion(user.getLockVersion()+1);
        //进行增加或扣除操作
        user.setCredit(credit+user.getCredit());
        boolean flagUpdate = userAction.updateUserById(user);//记录是否更新成功
        //(2)插入信用流水，若前一步操作没有成功，则不插入
        boolean flagRecord = false;//记录是否插入流水成功
        UserCredit userCredit = new UserCredit();
        if(flagUpdate) {
            userCredit.setCreateUserId(user.getId());
            userCredit.setUserId(user.getId());
            userCredit.setCredit(credit);
            userCredit.setDescription(description);
            userCredit.setRelatableId(relatableId);
            userCredit.setRelatableType(relatableType);
            flagRecord = userCreditService.insert(userCredit);
        }
        updateScoreByCredit(user, credit, userCredit.getId());
        return flagRecord;
    }

    
    public List<UserCredit> selectCreditRecordList(String userId ,Integer current,Integer size ) throws Exception{
        Page<UserCredit> page = new Page<UserCredit>(current, size);
        page = userCreditService.selectUserCreditPage(userId,page);
        return page.getRecords();
    }

    //------ 私有 ------

    /**
     * 因信用值降低，更新积分
     * @param user
     * @param addCredit
     * @param userCreditId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateScoreByCredit(User user, int addCredit, String userCreditId){
        //信用每降低1分，扣掉2积分。
        if(addCredit < 0){
            addCredit = -addCredit;
            AddScoreRecordRequestDto addScoreRecordRequestDto = new AddScoreRecordRequestDto();
            addScoreRecordRequestDto.setUserId(user.getId());
            addScoreRecordRequestDto.setCode(13);
            addScoreRecordRequestDto.setRelatableType(UserCredit.class.getSimpleName());
            addScoreRecordRequestDto.setRelatableId(userCreditId);
            boolean flag = false;
            for(int i=0; i<addCredit; i++){
                return userScoreAction.addScoreRecord(addScoreRecordRequestDto,user);
                //if(!flag) throw new RuntimeException("因信用每降低1分而扣2个积分出现异常，无法正常插入记录");
            }
        }
        return true;
    }
    public void deleteByUserId(String userId) throws Exception{
        userCreditService.deleteByUserId(userId);
    }
    /**更新替换删除*/
    public void deleteByUserIdUpdate(String userId) throws Exception{
        userCreditService.deleteByUserId(userId);
    }
}
