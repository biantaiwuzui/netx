package com.netx.common.user.util;

import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.Date;

@Component
public class ComputeAgeUtils {

    public static Integer getAgeByBirthday(Date birthday){
        Calendar cal = Calendar.getInstance();
        if (birthday==null || cal.before(birthday)) {
            return 0;//这是非法出生年月日
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthday);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }
}
