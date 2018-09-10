package com.netx.common.user.enums;

/**
 * 短息模板
 * @author 老肥猪
 * @since 2018-8-15(新改动)
 */
public enum MobileSmsCode {
    //验证码
    MS_MOBILE_CODE{
        @Override
        public String code(String tel) {
            //您好，您的验证码为：${code}。该信息15分钟内有效，请及时验证。如非本人操作，请忽略本短信，本条短信免费。
            if(tel.matches("^(\\+86|0086)1[3-9]\\d{9}|1[3-9]\\d{9}")){
                return "SMS_142152998";
            }
            return "SMS_142147529";
        }
    },
    //比赛邀请成为比赛的工作人员
    MATCH_INVITATION{
        //尊敬的${user_call}， 网值用户${user_name}邀请您成为${match_title}比赛的${description}，点击http://wzubi.com/reg?c=${active_code} 注册账号完成邀请。
        @Override
        public String code(String tel) {
            if(tel.matches("^(\\+86|0086)1[3-9]\\d{9}|1[3-9]\\d{9}")){
                return "SMS_142615696";
            }
            return "SMS_142620694";
        }
    },
    //验证成功
    MS_VERIFY_SUCESS{
        @Override
        public String code(String tel) {
            //您好，您的验证码为：${code}。该信息15分钟内有效，请及时验证。如非本人操作，请忽略本短信，本条短信免费。
            if(tel.matches("^(\\+86|0086)1[3-9]\\d{9}|1[3-9]\\d{9}")){
                return "SMS_142147915";
            }
            return "SMS_142147524";
        }
    },
    //验证失败
    MS_VERIFY_FAIL{
        @Override
        public String code(String tel) {
            if(tel.matches("^(\\+86|0086)1[3-9]\\d{9}|1[3-9]\\d{9}")){
                //抱歉，您在${date}申请的网值平台${type}认证失败，原因：${reason}，请到网值APP重新提交认证申请。
                return "SMS_142153000";
            }
            return "SMS_136871766";
        }
    },
    //比赛邀请团队成员
    MATCH_INVITE_TEAM_MEMBER{
        @Override
        public String code(String tel) {
            if(tel.matches("^(\\+86|0086)1[3-9]\\d{9}|1[3-9]\\d{9}")){
                //尊敬的先生或女士， 您的网值好友${user_name}，邀请您一起参加${match_title}的比赛，点击http://wzubi.com/reg?c=${active_code} 注册账号完成邀请。
                return "SMS_143710212";
            }
            return "SMS_143711024";
        }
    },
    //比赛提醒主办方前往审核
    MATCH_INVITE_REMIND{
        @Override
        public String code(String tel) {
            if(tel.matches("^(\\+86|0086)1[3-9]\\d{9}|1[3-9]\\d{9}")){
                //尊敬的先生或女士， 您的网值好友${user_name}，邀请您一起参加${match_title}的比赛，点击http://wzubi.com/reg?c=${active_code} 注册账号完成邀请。
                return "SMS_144146407";
            }
            return "SMS_144151408";
        }
    }
    ;
    public abstract String code(String tel);
}
