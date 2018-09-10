package com.netx.worth.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.mysql.cj.mysqlx.protobuf.MysqlxCrud;
import com.netx.common.wz.dto.wish.WishApplyDto;
import com.netx.common.wz.dto.wish.WishApplyReceiveDto;
import com.netx.common.wz.dto.wish.WishWithdrawalDto;
import com.netx.worth.enums.WishHistoryStatus;
import com.netx.worth.mapper.WishHistoryMapper;
import com.netx.worth.model.WishHistory;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import java.util.List;

@Service
public class WishHistoryService extends ServiceImpl<WishHistoryMapper, WishHistory> {


    /**根据使用表Id创建心愿历史*/
    public boolean createHistory(WishApplyReceiveDto wishApplyReceiveDto) {
        EntityWrapper<WishHistory> entityWrapper = new EntityWrapper<>();
        WishHistory wishHistory = new WishHistory();
        wishHistory.setWishApplyId(wishApplyReceiveDto.getId());
        wishHistory.setUserId(wishApplyReceiveDto.getUserId());
        return insert(entityWrapper.getEntity());
    }

    /**通过心愿使用Id列表获得心愿历史列表*/
    public List<WishHistory> getListByWishApplyIds(List<String> wishApplyIds) {
        EntityWrapper<WishHistory> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("wish_apply_id={0}", wishApplyIds);
        return selectList(entityWrapper);
    }

    /**提现成功*/
    public boolean withdrawalSuccess(WishWithdrawalDto withdrawalDto) {
        WishHistory wishHistory = new WishHistory();
        wishHistory.setStatus(WishHistoryStatus.ACCEPT.status);
        wishHistory.setWishApplyId(withdrawalDto.getWishApplyId());
        wishHistory.setReason(withdrawalDto.getReason());
        wishHistory.setAdminUserId(withdrawalDto.getUserId());
        EntityWrapper<WishHistory> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", withdrawalDto.getId());
        return update(wishHistory, entityWrapper);
    }

    /**提现失败*/
    public boolean withdrawalFailure(WishWithdrawalDto withdrawalDto) {
        WishHistory wishHistory = new WishHistory();
        wishHistory.setStatus(WishHistoryStatus.REFUSE.status);
        wishHistory.setWishApplyId(withdrawalDto.getWishApplyId());
        wishHistory.setReason(withdrawalDto.getReason());
        wishHistory.setAdminUserId(withdrawalDto.getUserId());
        EntityWrapper<WishHistory> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", withdrawalDto.getId());
        return update(wishHistory, entityWrapper);
    }

    /**通过心愿支持id查询历史信息*/
    public WishHistory getWishistoryWishapplyId(String wishApplyId, Integer status) {
        EntityWrapper<WishHistory> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_apply_id={0} AND deleted = 0", wishApplyId);
        if(status != null){
            entityWrapper.and("status = {0}", status);
        }
        return selectOne(entityWrapper);
    }

    /**
     * CHEN-QIAN
     * 根据userId和提现状态查询提现列表
     * @param userId
     * @param status
     * @param page
     * @return
     */
    public Page<WishHistory> getWishHistoryList(String userId, Integer status, Page page){
        EntityWrapper<WishHistory> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("deleted = 0");
        if(StringUtils.isNotBlank(userId)){
            entityWrapper.and("user_id = {0}", userId);
        }
        if(status != null){
            entityWrapper.and("status = {0}", status);
        }
        return selectPage(page, entityWrapper);
    }


}