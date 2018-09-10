package com.netx.worth.util;

import com.netx.worth.model.*;

import java.util.ArrayList;
import java.util.List;

public class UserIdsUtil {

    public static <T> List<String> getFromUserIds(List<T> list) {
        List<String> ids = new ArrayList<>();
        for (T t : list) {
            if (t instanceof Meeting) {
                Meeting meeting = (Meeting) t;
                ids.add(meeting.getUserId());
            }
            if (t instanceof MeetingSend) {
                MeetingSend meetingSend = (MeetingSend) t;
                ids.add(meetingSend.getUserId());
            }
            if (t instanceof Demand) {
                Demand demand = (Demand) t;
                ids.add(demand.getUserId());
            }
            if (t instanceof DemandRegister) {
                DemandRegister demandRegister = (DemandRegister) t;
                ids.add(demandRegister.getUserId());
            }
            if (t instanceof WishSupport) {
                WishSupport wishSupport = (WishSupport) t;
                ids.add(wishSupport.getUserId());
            }
            if (t instanceof Wish) {
                Wish wish = (Wish) t;
                ids.add(wish.getUserId());
            }
        }
        return ids;
    }
}
