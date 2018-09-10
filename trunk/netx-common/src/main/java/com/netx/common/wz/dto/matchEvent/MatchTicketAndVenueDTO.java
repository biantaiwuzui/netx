package com.netx.common.wz.dto.matchEvent;

public class MatchTicketAndVenueDTO {
    private String MatchTicketId;
    private String MatchVenueId;

    public String getMatchTicketId() {
        return MatchTicketId;
    }

    public void setMatchTicketId(String matchTicketId) {
        MatchTicketId = matchTicketId;
    }

    public String getMatchVenueId() {
        return MatchVenueId;
    }

    public void setMatchVenueId(String matchVenueId) {
        MatchVenueId = matchVenueId;
    }
}
