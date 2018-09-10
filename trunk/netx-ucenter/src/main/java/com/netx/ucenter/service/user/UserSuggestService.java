package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.user.enums.UserSuggestStatus;
import com.netx.ucenter.mapper.user.UserSuggestMapper;
import com.netx.ucenter.model.user.UserSuggest;
import com.netx.ucenter.vo.request.QuerySuggestRequestDto;
import com.netx.ucenter.vo.response.AddSuggestPassDto;
import com.netx.ucenter.vo.response.ExamineSuggestDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户建议表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserSuggestService extends ServiceImpl<UserSuggestMapper, UserSuggest> {

    public List<Map<String, Object>> querySuggestStat(int num) {
        Wrapper<UserSuggest> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("user_id as userId,count(*) as num");
        wrapper.where("deleted=0 and (is_effective={0} or is_effective=2) ", 1);
        wrapper.groupBy("userId");
        wrapper.orderBy("num");
        wrapper.having("count(*)>={0}", num);
        return selectMaps(wrapper);
    }

    public int querySuggestStat(String userId) {
        Wrapper<UserSuggest> wrapper = new EntityWrapper<>();
        wrapper.where("deleted=0 and user_id={0} and (is_effective={1} or is_effective = 2)", userId, 1);
        return selectCount(wrapper);
    }

    /**
     * 查询表
     */
    public Page<UserSuggest> tables(QuerySuggestRequestDto suggestDto, String userId, Page<UserSuggest> page) {
        UserSuggestStatus status = suggestDto.getStatus();
        Wrapper<UserSuggest> wrapper = new EntityWrapper<>();
        if (status != null && userId == null) {
            if (status.isEffective == null) {
                wrapper.isNull("is_effective");
            } else {
                wrapper.where("is_effective = {0}", status.isEffective);
            }
        } else if (status != null && userId != null) {
            if (status.isEffective == null) {
                wrapper.where("user_id = {0} and is_effective is NULL", userId);
            } else {
                wrapper.where("is_effective = {0} and user_id = {1}", status.isEffective, userId);
            }
        } else if (status == null && userId != null) {
            wrapper.where("user_id = {0}", userId);
        }
        wrapper.orderBy("create_time,is_effective");
        return selectPage(page, wrapper);
    }

    public List<UserSuggest> tables() {
        Wrapper<UserSuggest> wrapper = new EntityWrapper<>();
        return selectList(wrapper);
    }

    /**
     * 审批用户建议
     */
    public Boolean suggest(ExamineSuggestDto examineSuggestDto, String auditUserId) {
        UserSuggest userSuggest = new UserSuggest();
        userSuggest.setRealTime(new Date());
        userSuggest.setIsEffective(examineSuggestDto.getEffective());
        userSuggest.setResult(examineSuggestDto.getResult());
        userSuggest.setAuditUserName(examineSuggestDto.getAuditUserName());
        userSuggest.setAuditUserId(auditUserId);
        EntityWrapper<UserSuggest> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", examineSuggestDto.getUserId());
        entityWrapper.and("id={0}", examineSuggestDto.getId());
        return update(userSuggest, entityWrapper);
    }

    public Boolean addSuggestPass(AddSuggestPassDto addSuggestPassDto, String adminId, String userId) {
        UserSuggest userSuggest = new UserSuggest();
        userSuggest.setRealTime(new Date());
        userSuggest.setIsEffective(addSuggestPassDto.getEffective());
        userSuggest.setAuditUserId(adminId);
        userSuggest.setUserId(userId);
        userSuggest.setAuditUserName(addSuggestPassDto.getAuditUserName());
        userSuggest.setResult(addSuggestPassDto.getResult());
        userSuggest.setSuggest(addSuggestPassDto.getSuggest());
        return this.insert(userSuggest);
    }
}
