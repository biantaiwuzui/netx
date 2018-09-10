package com.netx.fuse.biz;

import com.netx.common.vo.currency.WzCommonOtherSetResponseDto;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.utils.datastructures.Tuple;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class FuseBaseAction {

    @Autowired
    protected UserAction userAction;
/*

    public String booleanIsCanRelease(String userId, WzCommonOtherSetResponseDto one){
        String buffer = "";
        if (one != null) {
            if (one.getSkillLimitType ().equals(0)) {//允许注册商家的人员名单
                if (StringUtils.isNotBlank ( one.getSkillLimitUserIds () )) {
                    if (one.getSkillLimitUserIds ().contains ( userId )) {//如果名单中包含用户，表示该用户可以发布需求
                        return buffer;
                    }
                }
            } else {
                User user = userAction.getUserService().selectById(userId);
                if(user==null){
                    throw new RuntimeException("此账号已被注销，请重新注册");
                }
                if (StringUtils.isNotBlank(one.getSkillLimitCondition())) {
                    String spit[] = one.getSkillLimitCondition().split(",");
                    for (String obj : spit) {
                        if (obj.equals("0")) {//信用值
                            Integer credit = one.getSkillLimitPoint();
                            if (credit!=null && user.getCredit()-credit < 0) {
                                buffer+=",信用值低于" + one.getSkillLimitPoint();
                            }
                        } else if (obj.equals("1")) {//手机验证
                            if (StringUtils.isBlank(user.getMobile())) {
                                buffer+="，没有手机验证";
                            }
                        } else if (obj.equals("2")) {//身份验证
                            if (StringUtils.isBlank(user.getIdNumber())) {
                                buffer+="，没有身份验证";
                            }
                        } else if (obj.equals("3")) {//视频验证
                            if (StringUtils.isBlank(user.getVideo())) {
                                buffer+="，没有视频验证";
                            }
                        }
                    }
                }
            }
        }
        return buffer;
    }
*/

    public Tuple<Boolean,String> booleanIsCanReleaseTuple(String userId, WzCommonOtherSetResponseDto one){
        String buffer = "";
        boolean flag = true;
        if (one != null) {
            if (one.getSkillLimitType ().equals(0)) {//允许注册商家的人员名单
                if (StringUtils.isNotBlank ( one.getSkillLimitUserIds () )) {
                    if (one.getSkillLimitUserIds ().contains ( userId )) {//如果名单中包含用户，表示该用户可以发布需求
                        return new Tuple<>(true,"");
                    }
                }
            } else {
                User user = userAction.getUserService().selectById(userId);
                if(user==null){
                    throw new RuntimeException("此账号已被注销，请重新注册");
                }
                if (StringUtils.isNotBlank(one.getSkillLimitCondition())) {
                    String spit[] = one.getSkillLimitCondition().split(",");
                    for (String obj : spit) {
                        if (obj.equals("0")) {//信用值
                            Integer credit = one.getSkillLimitPoint();
                            if (credit!=null && user.getCredit()-credit < 0) {
                                buffer+=",信用值低于" + one.getSkillLimitPoint();
                                flag=false;
                            }
                        } else if (obj.equals("1")) {//手机验证
                            if (StringUtils.isBlank(user.getMobile())) {
                                buffer+="，没有手机验证";
                                flag=false;
                            }
                        } else if (obj.equals("2")) {//身份验证
                            if (StringUtils.isBlank(user.getIdNumber())) {
                                buffer+="，没有身份验证";
                                flag=false;
                            }
                        } else if (obj.equals("3")) {//视频验证
                            if (StringUtils.isBlank(user.getVideo())) {
                                buffer+="，没有视频验证";
                                flag=false;
                            }
                        }
                    }
                }
            }
        }
        return new Tuple<>(flag,buffer);
    }

    protected void sortList(List<Map<String,Object>> mapList,String key){
        Comparator<Map<String,Object>> comparator = new Comparator<Map<String,Object>>(){
            public int compare(Map<String,Object> mapOne, Map<String,Object> mapTwo) {
                //先排年龄
                BigDecimal left = (BigDecimal) (mapOne.get(key));
                BigDecimal right = (BigDecimal) (mapTwo.get(key));
                return right.compareTo(left);
            }
        };
        mapList.sort(comparator);
    }
}
