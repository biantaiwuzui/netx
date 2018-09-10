package com.netx.common.wz.comparator;

import com.netx.common.wz.vo.common.DistrictVo;
import java.util.Comparator;

public class DistrictComparator implements Comparator<DistrictVo> {

    @Override
    public int compare(DistrictVo d1, DistrictVo d2) {
        double d1Distance = d1.getDistance();
        double d2Distance = d2.getDistance();

        if (d1Distance < d2Distance) {
            return -1;
        } else if (d1Distance > d2Distance) {
            return 1;
        } else {
            return 0;
        }
    }
}