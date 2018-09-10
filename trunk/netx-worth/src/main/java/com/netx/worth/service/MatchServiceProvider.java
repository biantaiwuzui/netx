package com.netx.worth.service;


import com.netx.worth.model.MatchProgressParticipant;
import com.netx.worth.model.MatchVenueGroup;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 比赛类service
 * Created by Yawn on 2018/8/4 0004.
 */
@Service
public class MatchServiceProvider {

    @Autowired
    private MatchNoticeService matchNoticeService;
    @Autowired
    private MatchVenueGroupService matchVenueGroupService;
    @Autowired
    private MatchProgressParticipantService matchProgressParticipantService;
    @Autowired
    private MatchRequirementService matchRequirementService;
    @Autowired
    private MatchAppearanceService matchAppearanceService;
    @Autowired
    private MatchAudienceService matchAudienceService;
    @Autowired
    private MatchParticipantService matchParticipantService;
    @Autowired
    private MatchEventService matchEventService;
    @Autowired
    private MatchMemberService matchMemberService;
    @Autowired
    private MatchRatesService matchRatesService;
    @Autowired
    private MatchRequirementDataService matchRequirementDataService;
    @Autowired
    private MatchReviewService matchReviewService;
    @Autowired
    private MatchTeamService matchTeamService;
    @Autowired
    private MatchTicketService matchTicketService;
    @Autowired
    private MatchVenueService matchVenueService;
    @Autowired
    private MatchVoteService matchVoteService;
    @Autowired
    private MatchChildInfoService matchChildInfoService;
    @Autowired
    private MatchAwardService matchAwardService;
    @Autowired
    private MatchGroupService matchGruopService;
    @Autowired
    private MatchProgressService matchProgressService;
    @Autowired
    private MatchApplyDefaultTimeService matchApplyDefaultTimeService;
    @Autowired
    private MatchGroupAndZoneService matchGroupAndZoneService;
    @Autowired
    private MatchZoneService matchZoneService;
    @Autowired
    private MatchVenueAndZoneService matchVenueAndZoneService;

    public MatchProgressParticipantService getMatchProgressParticipantService() {
        return matchProgressParticipantService;
    }

    public void setMatchProgressParticipantService(MatchProgressParticipantService matchProgressParticipantService) {
        this.matchProgressParticipantService = matchProgressParticipantService;
    }

    public MatchVenueAndZoneService getMatchVenueAndZoneService() {
        return matchVenueAndZoneService;
    }

    public void setMatchVenueAndZoneService(MatchVenueAndZoneService matchVenueAndZoneService) {
        this.matchVenueAndZoneService = matchVenueAndZoneService;
    }

    public MatchApplyDefaultTimeService getMatchApplyDefaultTimeService() {
        return matchApplyDefaultTimeService;
    }

    public void setMatchApplyDefaultTimeService(MatchApplyDefaultTimeService matchApplyDefaultTimeService) {
        this.matchApplyDefaultTimeService = matchApplyDefaultTimeService;
    }

    public MatchGroupAndZoneService getMatchGroupAndZoneService() {
        return matchGroupAndZoneService;
    }

    public void setMatchGroupAndZoneService(MatchGroupAndZoneService matchGroupAndZoneService) {
        this.matchGroupAndZoneService = matchGroupAndZoneService;
    }

    public MatchZoneService getMatchZoneService() {
        return matchZoneService;
    }

    public void setMatchZoneService(MatchZoneService matchZoneService) {
        this.matchZoneService = matchZoneService;
    }

    public MatchAppearanceService getMatchAppearanceService() {
        return matchAppearanceService;
    }

    public void setMatchAppearanceService(MatchAppearanceService matchAppearanceService) {
        this.matchAppearanceService = matchAppearanceService;
    }

    public MatchAudienceService getMatchAudienceService() {
        return matchAudienceService;
    }

    public void setMatchAudienceService(MatchAudienceService matchAudienceService) {
        this.matchAudienceService = matchAudienceService;
    }

    public MatchParticipantService getMatchParticipantService() {
        return matchParticipantService;
    }

    public void setMatchParticipantService(MatchParticipantService matchParticipantService) {
        this.matchParticipantService = matchParticipantService;
    }

    public MatchEventService getMatchEventService() {
        return matchEventService;
    }

    public void setMatchEventService(MatchEventService matchEventService) {
        this.matchEventService = matchEventService;
    }

    public MatchMemberService getMatchMemberService() {
        return matchMemberService;
    }

    public void setMatchMemberService(MatchMemberService matchMemberService) {
        this.matchMemberService = matchMemberService;
    }

    public MatchRatesService getMatchRatesService() {
        return matchRatesService;
    }

    public void setMatchRatesService(MatchRatesService matchRatesService) {
        this.matchRatesService = matchRatesService;
    }

    public MatchRequirementDataService getMatchRequirementDataService() {
        return matchRequirementDataService;
    }

    public void setMatchRequirementDataService(MatchRequirementDataService matchRequirementDataService) {
        this.matchRequirementDataService = matchRequirementDataService;
    }

    public MatchReviewService getMatchReviewService() {
        return matchReviewService;
    }

    public void setMatchReviewService(MatchReviewService matchReviewService) {
        this.matchReviewService = matchReviewService;
    }

    public MatchTeamService getMatchTeamService() {
        return matchTeamService;
    }

    public void setMatchTeamService(MatchTeamService matchTeamService) {
        this.matchTeamService = matchTeamService;
    }

    public MatchTicketService getMatchTicketService() {
        return matchTicketService;
    }

    public void setMatchTicketService(MatchTicketService matchTicketService) {
        this.matchTicketService = matchTicketService;
    }

    public MatchVenueService getMatchVenueService() {
        return matchVenueService;
    }

    public void setMatchVenueService(MatchVenueService matchVenueService) {
        this.matchVenueService = matchVenueService;
    }

    public MatchVoteService getMatchVoteService() {
        return matchVoteService;
    }

    public void setMatchVoteService(MatchVoteService matchVoteService) {
        this.matchVoteService = matchVoteService;
    }

    public MatchChildInfoService getMatchChildInfoService() {
        return matchChildInfoService;
    }

    public void setMatchChildInfoService(MatchChildInfoService matchChildInfoService) {
        this.matchChildInfoService = matchChildInfoService;
    }

    public MatchAwardService getMatchAwardService() {
        return matchAwardService;
    }

    public void setMatchAwardService(MatchAwardService matchAwardService) {
        this.matchAwardService = matchAwardService;
    }
    public MatchRequirementService getMatchRequirementService() {
        return matchRequirementService;
    }

    public void setMatchRequirementService(MatchRequirementService matchRequirementService) {
        this.matchRequirementService = matchRequirementService;
    }

    public MatchGroupService getMatchGruopService() {
        return matchGruopService;
    }

    public void setMatchGruopService(MatchGroupService matchGruopService) {
        this.matchGruopService = matchGruopService;
    }

    public MatchProgressService getMatchProgressService() {
        return matchProgressService;
    }

    public void setMatchProgressService(MatchProgressService matchProgressService) {
        this.matchProgressService = matchProgressService;
    }

    public MatchVenueGroupService getMatchVenueGroupService() {
        return matchVenueGroupService;
    }

    public void setMatchVenueGroupService(MatchVenueGroupService matchVenueGroupService) {
        this.matchVenueGroupService = matchVenueGroupService;
    }

    public MatchNoticeService getMatchNoticeService() {
        return matchNoticeService;
    }

    public void setMatchNoticeService(MatchNoticeService matchNoticeService) {
        this.matchNoticeService = matchNoticeService;
    }
}
