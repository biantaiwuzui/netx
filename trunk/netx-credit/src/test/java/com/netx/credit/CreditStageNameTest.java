package com.netx.credit;

import com.netx.credit.model.constants.CreditStageName;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CreditStageNameTest {

    @Test
    public void test(){
        String name = CreditStageName.CSN_COMMON.getStageName();
        Double ratio = CreditStageName.CSN_COMMON.getSubscriptionRatio();
        Map<String,Double> map = new HashMap<>();
        map.put(name,ratio);
        System.out.println(map);
    }
}
