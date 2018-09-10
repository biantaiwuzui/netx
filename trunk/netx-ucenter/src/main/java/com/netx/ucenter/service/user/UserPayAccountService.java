package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.vo.common.UserPayAccountPageRequestDto;
import com.netx.ucenter.mapper.user.UserPayAccountMapper;
import com.netx.ucenter.model.user.UserPayAccount;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hj.Mao
 * @since 2018-03-17
 */
@Service
public class UserPayAccountService extends ServiceImpl<UserPayAccountMapper, UserPayAccount> {

    /**
     * 移除某个账号
     * @param id
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean delete(String id  , String userId) throws Exception{
        if(StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(userId)){
            EntityWrapper<UserPayAccount> wrapper=new EntityWrapper<>();
            wrapper.andNew("id ={0} and user_id = {1}",id,userId);
            UserPayAccount account=this.selectOne(wrapper);
            if(account!=null){
                account.setDeleted(1);
                return this.update(account,wrapper);
            }
        }
        return false;
    }

    /**
     * 获取某绑定的账号
     * @param userId
     * @param accountType
     * @return
     */
    public UserPayAccount get(String userId, Integer accountType) {
        if(StringUtils.isNotEmpty(userId) && accountType!=null){
            return this.selectOne(new EntityWrapper<UserPayAccount>().where("user_id ={0} and account_type = {1} and priority = 999",userId,accountType));
        }
        return  null;
    }

    /**
     * 返回用户的某类型的分页账号列表
     *  userId
     *  accountType 1.微信      2.支付宝
     * @return
     */
    public List<UserPayAccount> getListByPage(UserPayAccountPageRequestDto dto) throws Exception{
        EntityWrapper<UserPayAccount> wrapper=new EntityWrapper<>();
        Page page = new Page();
        page.setSize(dto.getSize());
        page.setCurrent(dto.getCurrent());
        switch (dto.getAccountType()){
            case 1:
                wrapper.andNew("user_id = {0} and account_type = 1",dto.getUserId());
                break;
            case 2:
                wrapper.andNew("user_id = {0} and account_type = 2",dto.getUserId());
                break;
        }
        return this.selectPage(page,wrapper).getRecords();
    }

    /**
     * 获取最新的最多为5条的记录列表
     * @param userId
     * @param accountType  1. 微信    2.支付宝
     * @return
     * @throws Exception
     */
    public List<UserPayAccount> getCommonAccounts(String userId ,int accountType) throws Exception{
        if(StringUtils.isNotEmpty(userId)) {
            Page page = new Page();
            page.setCurrent(0);
            page.setSize(5);
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.where("user_id = {0} and account_type = {1}", userId, accountType).orderBy("create_time", false);
            return this.selectPage(page, wrapper).getRecords();
        }
        return null;
    }


    /**
     *
     * @param userId 修改的用户id
     * @param accountId   数据库中的账号表主键id
     * @return
     */
    public String modifyAccount(String userId , String accountId){
        UserPayAccount account=null;
        if(StringUtils.isNotEmpty(userId) ||StringUtils.isNotEmpty(accountId)){
            account = this.selectById(accountId);
            if(account == null ){
                return "没有该账号的记录";
            }
            if(!account.getUserId().equals(userId)){
                return "当前不是您的账号记录";
            }
            if(account.getPriority()==999){
                return "当前账号已经绑定";
            }
            account.setPriority(999);
            UserPayAccount updateAccount=new UserPayAccount();
            updateAccount.setPriority(1);//其他的同用户，同类型的账号设置为1
            this.update(updateAccount,new EntityWrapper<UserPayAccount>().where("user_id ={0} and account_type ={1} and id !={2}",userId,account.getAccountType(),account.getId()));
            this.updateById(account);
            return "success";
        }
        return "修改提现账号传参异常";
    }

    public String getBindAccount(String userId, int accountType){
        UserPayAccount account = this.get(userId,accountType);
        if(account!=null){
            return account.getAccountDisplay();
        }
        return null;
    }
    /**
     *
     * @param userId   关联的用户
     * @param account   第三方的加密账号
     * @param accountIdentity   第三方的唯一用户id
     * @param accountType   微信还是支付宝     1.微信    2.支付宝
     * @return
     */
    public String addAccount(String userId , String account , String accountIdentity, Integer accountType){
        UserPayAccount dbAccount=this.selectOne(new EntityWrapper<UserPayAccount>().where("user_id = {0}  and account_identity = {1} and account_type = {2}",userId,accountIdentity,accountType));
        if(dbAccount==null) {
            UserPayAccount newAccount = new UserPayAccount();
            newAccount.setUserId(userId);
            newAccount.setAccountDisplay(account);
            newAccount.setAccountIdentity(accountIdentity);
            newAccount.setAccountType(accountType);
            newAccount.setPriority(1);
            this.insert(newAccount);
            return newAccount.getId();
        }else{
            if(!dbAccount.getAccountDisplay().equals(account)) {
                dbAccount.setAccountDisplay(account);
                this.updateById(dbAccount);
            }
        }
        return  dbAccount.getId();
    }

}
