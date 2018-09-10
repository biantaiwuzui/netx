package com.netx.ucenter.aop;

import com.netx.common.user.enums.UserIdIndexEnum;
import com.netx.ucenter.biz.user.UserProfileAction;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * 用于计算资料总分
 * @author 李卓
 */
@Component
@Aspect
public class CalculateProfileScoreAspect {

    private Logger logger = LoggerFactory.getLogger(CalculateProfileScoreAspect.class);

    @Autowired
    private UserProfileAction userProfileAction;

    /**
     * 连接点，仅作用于用户中心
     */
    @Pointcut("execution(* com.netx.ucenter.service.user..*(..))")
    public void ucenter(){};

    /**
     * 返回通知
     * @param joinPoint
     */
    @Transactional
    @AfterReturning(value = "ucenter() && @annotation(target)")
    public void calculateProfileScore(JoinPoint joinPoint, CalculateProfileScore target) {
        int index = target.index();//userId 入参下标
        String userId = "-1";//记录 userId 值，先赋予不可能存在的userId，防止更新对象出错。

        if (target.type() == UserIdIndexEnum.OBJECT) {//userId 在入参对象里面
            //System.out.println("入参对象");
            userId = (String) getFieldValueByName(target.userIdFieldName(), joinPoint.getArgs()[index]);
        } else if (target.type() == UserIdIndexEnum.STRING) {//userId 为单独的字符串入参
            //System.out.println("单独字符串");
            userId = (String) joinPoint.getArgs()[index];
        }
        if(userId == null || userId == "-1"){
            System.out.println("<------ 请检查 @CalculateProfileScore 注解的参数 ------>\n" +
                    "你目前设置 userIdFieldName 的值是：" + target.userIdFieldName() + "\n" +
                    "你目前设置 index 的值是：" + target.index() + "\n" +
                    "你目前设置 UserIdIndexEnum 的值是：" + target.type() + "\n" +
                    "现在获取得到的 userId 为：" + userId + "\n" +
                    "注意: 本注解暂不支持嵌套的对象（vo/dto/po），" + "\n" +
                    "如：aaVo 有对象属性 bbVo，aaVo 其他属性不存在 userId，但 bbVo 存在 userId，此注解无法检测 bbVo 的 userId" + "\n" +
                    "<---------------------------------------------------->");
            throw new RuntimeException("资料计分出现异常");
        }
        try {
            userProfileAction.updateUserProfileScore(userId);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 根据属性名获取属性值
     * @param fieldName 属性名
     * @param o 想要获取的类
     * */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }
}
