package com.netx.fuse.biz.worth;

import java.util.*;

import com.netx.common.common.enums.AuthorEmailEnum;
import com.netx.common.common.enums.JobEnum;

import com.netx.fuse.biz.job.JobFuseAction;

import com.netx.worth.model.Skill;
import com.netx.worth.service.WorthServiceprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netx.fuse.proxy.EvaluateProxy;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.model.SkillOrder;
import com.netx.worth.model.SkillRegister;

import static java.util.stream.Collectors.toList;

@Service
public class SkillOrderFuseAction {

    @Autowired
    private WorthServiceprovider worthServiceprovider;
    @Autowired
    private SettlementAction settlementAction;
    @Autowired
    private JobFuseAction jobFuseAction;
    @Autowired
    private EvaluateProxy evaluateProxy;

    @Autowired
    private SkillFuseAction skillFuseAction;

    public boolean checkEvaluate(String skillOrderId) {
        Boolean flag = false;
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(skillOrderId);
        List<SkillRegister> skillRegisters = worthServiceprovider.getSkillRegisterService().RegisterList(skillOrder.getSkillRegisterId());
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillOrder.getSkillRegisterId());
        String publishUserId = skillRegister.getUserId();
        List<String> userIds = skillRegisters.stream().map(SkillRegister::getUserId).collect(toList());
        userIds.add(publishUserId);
        List<String> list = evaluateProxy.notEvaluateUsers(userIds, skillOrderId);
        Boolean removeJob = jobFuseAction.removeJob(JobEnum.SKILL_CHECK_EVALUATE_JOB, skillOrderId, skillOrderId, "定时评论");
        if (removeJob == null)
            removeJob = true;
        if (removeJob) {
            Boolean success = jobFuseAction.addJob(JobEnum.SKILL_CHECK_EVALUATE_JOB, skillOrderId, skillOrderId, "定时评论", new Date(skillRegister.getEndAt().getTime() + 24l * 3600 * 1000), AuthorEmailEnum.DAI_HO);
            if (success) {
                list.forEach(userId -> {
                    settlementAction.settlementCredit("SkillOrder", skillOrderId, userId, -2);
                });
                flag = true;
            }
        }
        return flag;
    }

    //未评论扣除信用值
    public boolean checkComment2Skill(String skillOrderId) {
        boolean flag = false;
        try {

            SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(skillOrderId);
            SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillOrder.getSkillRegisterId());
            Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
            if (skillOrder.getStatus() == 3) {
                //创建用户id集合
                List<String> userIds = new ArrayList<String>();
                //预约者用户id
                userIds.add(skillRegister.getUserId());
                //技能发布者用户id
                userIds.add(skill.getUserId());

                List<String> list = evaluateProxy.notEvaluateUsers(userIds, skillOrderId);
                list.forEach(userId -> {
                    skillFuseAction.updateUserCredit(userId, -2);
                    settlementAction.settlementCredit("SkillOrder", skillOrderId, userId, -2);
                });
                flag = true;
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


}
