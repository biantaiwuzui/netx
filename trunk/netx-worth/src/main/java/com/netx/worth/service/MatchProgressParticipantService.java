package com.netx.worth.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchProgressParticipantDTO;
import com.netx.common.wz.dto.matchEvent.MatchZoneProgressPlayerDTO;
import com.netx.worth.mapper.MatchProgressParticipantMapper;
import com.netx.worth.model.MatchProgress;
import com.netx.worth.model.MatchProgressParticipant;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class MatchProgressParticipantService extends ServiceImpl<MatchProgressParticipantMapper,MatchProgressParticipant> {


    public boolean addMatchProgressParticipant(MatchProgressParticipantDTO matchProgressParticipantDTO) {
        MatchProgressParticipant matchProgressParticipant=new MatchProgressParticipant();
        matchProgressParticipant.setGroupId(matchProgressParticipantDTO.getGroupId());;
        matchProgressParticipant.setMatchId(matchProgressParticipantDTO.getMatchId());
        matchProgressParticipant.setProgressId(matchProgressParticipantDTO.getProgressId());
        matchProgressParticipant.setParticipantId(matchProgressParticipantDTO.getParticipantId());
        matchProgressParticipant.setZoneId(matchProgressParticipantDTO.getZoneId());
        return insert(matchProgressParticipant);
    }

    public boolean addMatchParticipantProgress(String progressId, String participantId) {
        MatchProgressParticipant matchProgress = new MatchProgressParticipant();
        matchProgress.setParticipantId(participantId);
        matchProgress.setProgressId(progressId);
        return insert(matchProgress);
    }



    public List<MatchProgress> getZoneProgressPlayer(MatchZoneProgressPlayerDTO matchZoneProgressPlayerDTO){
        Map<String,String> map=new HashMap<>();
        map.put("zone_id",matchZoneProgressPlayerDTO.getZoneId());
        map.put("progress_id",matchZoneProgressPlayerDTO.getProgressId());
        return super.baseMapper.getPassParticipant(map);
    }
}
