package com.netx.worth.biz.skill;

import java.math.BigDecimal;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.utils.DistrictUtil;
import com.netx.utils.money.Money;
import com.netx.worth.enums.SkillRegisterStatus;
import com.netx.worth.model.Skill;
import com.netx.worth.model.SkillOrder;
import com.netx.worth.service.WorthServiceprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netx.common.wz.dto.skill.SkillRegisterDto;
import com.netx.worth.model.SkillRegister;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 技能预约表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-26
 */
@Service
public class SkillRegisterAction{

    @Autowired
    private WorthServiceprovider worthServiceprovider;

    public long querySkillIncome(String skillIds){
        if(!StringUtils.isEmpty(skillIds)){
            String[] skillId = skillIds.split(",");
            List<String> skillList = worthServiceprovider.getSkillRegisterService().querySkillRegister(skillId);
            if(skillList!=null && skillList.size()>0){
                Long total = worthServiceprovider.getSkillOrderService().getSkillIncomeTotal(skillList);
                return total==null?0:total;
            }
        }
        return 0;
    }

    public  Map<String,Object> SkillRegisterDtoToSkill(Long skillAmount,SkillRegisterDto skillRegisterDto) throws Exception {
        Map<String,Object> map = new HashMap<>();
        boolean register = !StringUtils.hasText(skillRegisterDto.getId());//true:发起预约 false:修改预约
        SkillRegister skillRegister = new SkillRegister();
        if (register) {
            skillRegister.setUserId(skillRegisterDto.getUserId());
            skillRegister.setCreateUserId(skillRegisterDto.getUserId());
            skillRegister.setSkillId(skillRegisterDto.getSkillId());
        } else {
            skillRegister.setId(skillRegisterDto.getId());
            skillRegister.setUpdateUserId(skillRegisterDto.getUserId());
        }
        if(skillRegisterDto.getStartAt()!=null){
            skillRegister.setStartAt(new Date(skillRegisterDto.getStartAt()));
        }
        if(skillRegisterDto.getEndAt()!=null){
            skillRegister.setEndAt(new Date(skillRegisterDto.getEndAt()));
        }
        skillRegister.setUnit(skillRegisterDto.getUnit());
        BigDecimal amount = skillRegisterDto.getAmount();
        Integer number = skillRegisterDto.getNumber();
        skillRegister.setAmount(skillAmount);
        skillRegister.setNumber(number);
        skillRegister.setFee(new Money(amount).getCent()*number);
        skillRegister.setDescription(skillRegisterDto.getDescription());
        skillRegister.setAddress(skillRegisterDto.getAddress());
        skillRegister.setLon(skillRegisterDto.getLon());
        skillRegister.setLat(skillRegisterDto.getLat());
        skillRegister.setAnonymity(skillRegisterDto.getAnonymity());
        skillRegister.setStatus(SkillRegisterStatus.REGISTERED.status);
        worthServiceprovider.getSkillRegisterService().insertOrUpdate(skillRegister);
        map.put("skillRegister",skillRegister.getId());
        map.put("startAt",skillRegister.getStartAt());
        return map;
    }

}
