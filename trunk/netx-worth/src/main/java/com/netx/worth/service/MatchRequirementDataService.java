package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchRequirementDataDTO;
import com.netx.worth.mapper.MatchRequirementDataMapper;
import com.netx.worth.model.MatchRequirementData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 参赛资料管理
 * Created by Yawn on 2018/8/1 0001.
 */
@Service
public class MatchRequirementDataService extends ServiceImpl<MatchRequirementDataMapper, MatchRequirementData> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 上传或更新参赛资料
     * @param dto
     * @return
     */
    public boolean insertData(MatchRequirementDataDTO dto) {
        MatchRequirementData matchRequirement = new MatchRequirementData();
        matchRequirement.setImagesUrl(dto.getImagesUrl());
        matchRequirement.setIntroduction(dto.getIntroduction());
        return insert(matchRequirement);
    }

    /**
     * 通过主键更新
     * @param dto
     * @return
     */
    public boolean updateData(MatchRequirementDataDTO dto, String requirementId){
        MatchRequirementData matchRequirement = new MatchRequirementData();
        matchRequirement.setImagesUrl(dto.getImagesUrl());
        matchRequirement.setIntroduction(dto.getIntroduction());
        EntityWrapper<MatchRequirementData> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", requirementId);
        return update(matchRequirement, entityWrapper);
    }
    /**
     * 根据ID删除参赛资料
     * @param id
     * @return
     */
    public boolean deleteRequirementDataById(String id) {
        EntityWrapper<MatchRequirementData> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", id);
        return delete(entityWrapper);
    }

    /**
     * 根据获取赛事参赛者资料
     * @return
     */
    public List<MatchRequirementData> listMatchRequirementData(String MatchId) {
        EntityWrapper<MatchRequirementData> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id={0}", MatchId);
        return selectList(entityWrapper);
    }

    /**
     * 根据用户id获取上传的资料
     * @param userId
     * @return
     */
    public List<MatchRequirementData> listUserRequirementData(String userId) {
        EntityWrapper<MatchRequirementData> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId).orderBy("requirement_title");
        return selectList(entityWrapper);
    }
    /**
     * 根据用户id获取上传的资料
     * @param userId
     * @return
     */
    public List<MatchRequirementData> listChildRequirementData(String userId) {
        EntityWrapper<MatchRequirementData> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId).and("is_child_or_team",1).orderBy("requirement_title");
        return selectList(entityWrapper);
    }
    /**
     * 获取参赛者的照片 （已经更改 了）
     * @param id
     * @return
     */
    public String getImageUrlByUserId(String id) {
        EntityWrapper<MatchRequirementData> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id = {0}", id);
        MatchRequirementData matchRequirementData = selectOne(entityWrapper);
        String images = "";
        if(matchRequirementData != null) {
            images = matchRequirementData.getImagesUrl();
        }
        String[] imageArray = images.split(",");
        if (imageArray.length > 0) {
            return imageArray[0];
        }
        return "";
    }

    /**
     * 根据参赛者ID删除参赛信息
     * user 存的是参赛者ID
     * @return
     */
    public boolean deleteImageDataByUserId(String participantId) {
        EntityWrapper<MatchRequirementData> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id = {0}", participantId);
        return delete(entityWrapper);
    }
}
