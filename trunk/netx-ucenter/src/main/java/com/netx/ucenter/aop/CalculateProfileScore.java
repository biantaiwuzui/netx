package com.netx.ucenter.aop;


import com.netx.common.user.enums.UserIdIndexEnum;

import java.lang.annotation.*;

/**
 * 用于个人资料计分
 * 添加此注解到方法后，自动计分并更新到用户表，要求方法参数带有 userId 的信息
 * 若没有 userId 信息，则使用传统方法更新分值，调用：userProfileService.updateUserProfileScore(String userId)
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CalculateProfileScore {

    /**
     * 存储 userId 的字段名
     * @return
     */
    String userIdFieldName() default "userId";

    /**
     * 1、若 userId 为单独的字符串入参, userId 所在方法里的位置值，位置值从 0 开始
     * 2、若 userId 在 vo/dto/po 对象里面，则这里表示带有 userId 的 vo/dto/po 的入参位置值
     * 例如：
     * （1）public void aa(String userId, String bb)，则位置值为 0
     * （2）public void aa(UserIdVo vo, XxxVo bb)，假设UserIdVo带有 userId 属性，则位置值为 0
     * 注意：
     * 1、暂不支持嵌套的 vo ，可能会报错，例如：UserVo 里带有属性 CommonVo，CommonVo 带有 userId 属性。
     * @return
     */
    int index() default 0;

    /**
     * userId 入参类型：1、单独字符串入参    2、userId 在 vo/dto/po 对象入参里面
     * @return
     */
    UserIdIndexEnum type() default UserIdIndexEnum.OBJECT;

}
