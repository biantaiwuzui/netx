package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchNoticeDTO;
import com.netx.worth.mapper.MatchNoticeMapper;
import com.netx.worth.model.MatchNotice;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchNoticeService extends ServiceImpl<MatchNoticeMapper,MatchNotice> {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public String insertOrUpdate(MatchNoticeDTO dto) {
        MatchNotice matchNotice = new MatchNotice();
        matchNotice.setAfficheContent(dto.getAfficheContent());
        matchNotice.setCreateTime(dto.getCreateTime());
        if (StringUtils.isNoneBlank(dto.getId())) {
            matchNotice.setId(dto.getId());
        }
        matchNotice.setMatchId(dto.getMatchId());
        matchNotice.setMerchantId(dto.getMerchantId());
        matchNotice.setMerchantType(dto.getMerchantType());
        matchNotice.setTitle(dto.getTitle());
        matchNotice.setMessageType(dto.getMessageType());
        matchNotice.setUserId(dto.getUserId());
        return insertOrUpdate(matchNotice) ? dto.getId(): "";
    }

    public boolean delete(String id) {
        return deleteById(id);
    }

    public String getUserId(String id) {
        EntityWrapper<MatchNotice> matchNoticeEntityWrapper = new EntityWrapper<>();
        matchNoticeEntityWrapper.where("id = {0}", id);
        MatchNotice notice = selectOne(matchNoticeEntityWrapper);
        return notice == null ?  "" : notice.getUserId();
    }

    public List<MatchNotice> selectMatchNoticeByUserId(String userId) {
        EntityWrapper<MatchNotice> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id = {0}", userId);
        return selectList(entityWrapper);
    }

    public Page<MatchNotice> selectMatchNoticeByMatchId(String matchId, Page<MatchNotice> page) {
        EntityWrapper<MatchNotice> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId).orderBy("message_type", false);
        return selectPage(page, entityWrapper);
    }

}
