package com.netx.credit.model.constants;

import java.io.Serializable;

public class CreditStageNameSetting implements Serializable {

    private CreditStageName creditStageName;

    private Double ratio;

    public CreditStageName getCreditStageName() {
        return creditStageName;
    }

    public Double getRatio() {
        this.ratio = creditStageName.getSubscriptionRatio ();
        return ratio;
    }
}
