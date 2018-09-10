package com.netx.utils.checker;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by spark on 2015/12/2.
 */
public class CheckUtils {

    /** 检测 email 的正则 */
    public static final String EMAIL_REGEX = "^\\w\\S*@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z0-9]{2,4}$";

    /** 检测 手机号 的正则 */
    public static final String PHONE_REGEX = "^1(3[0-9]|4[57]|5[0-9]|7[0-9]|8[0-9])[0-9]{8}$";

    public static final String HK_PHONE_REGEX = "(^6|^9)[0-9]{7}$";

    /** 检测 sql 注入的正则 */
    public static final String SQL_REGEX = "(?:')|(?:--)|;|(/\\*(?:.|[\\n\\r])*?\\*/)|((?i)\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

    /**
     * 空字符串判断, <span style="color:red;">去左右空格后</span>, 如果为空返回 true<br/>
     *
     */
    public static boolean isNull(String param) {
        return (param == null || "".equals(param.trim()));
    }

    /**
     * 空字符串判断, <span style="color:red;">去左右空格后</span>, 如果不为空返回 true<br/>
     *
     */
    public static boolean isNotNull(String param) {
        return !isNull(param);
    }

    /**
     * 检测 email 格式, 匹配则返回 true<br/>
     * 格式: 下划线 数字或字母 @ 字母 数字或中横线 . 2 到 4 个字母或数字<br/>
     *
     */
    public static boolean checkEmail(String email) {
        return checkRegexWithStrict(email, EMAIL_REGEX);
    }

    /**
     * 检测手机号码, 匹配则返回 true<br/>
     * <br/>
     * 格式: 130-139, 150-159(除了154), 180,182,182,186-189, 145,147
     *
     */
    public static boolean checkPhone(String phone) {
        return checkRegexWithStrict(phone, PHONE_REGEX) || checkRegexWithRelax(phone, HK_PHONE_REGEX);
    }

    /**
     * 检测 SQL, 若有会导致注入的值则返回 true.
     *
     */
    public static boolean checkSqlValue(String sql) {
        return checkRegexWithRelax(sql, SQL_REGEX);
    }

    /**
     * 验证 指定正则 是否 <span style="color:red;">全字匹配</span> 指定字符串, 匹配则返回 true <br/>
     * <br/>
     * 左右空白符 : (?m)(^\s*|\s*$) 帐号输入(字母或数字开头, 长度 5-16, 可以有下划线) :
     * ^[a-zA-Z0-9]\\w{4,15}$<br/>
     * 空白符 : (^\\s*)|(\\s*$)<br/>
     * IP :
     * ([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])(\\.([01]?[0-9]{1,2}|2[0-4][0-9]
     * |25[0-5])){3}<br/>
     * 日期 :
     * (([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9
     * ]{3})
     * -(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12
     * ][0-
     * 9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]
     * |[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29) 匹配多行注释 :
     * /\*\*(\s|.)*?\* /<br/>
     *
     */
    public static boolean checkRegexWithStrict(String param, String regex) {
        return !isNull(param) && Pattern.compile(regex).matcher(param).matches();
    }

    /**
     * 此模式为非严格型匹配, <span style="color:red;">只要找到匹配即返回 true</span><br/>
     * <br/>
     * 中文: [\\u4e00-\\u9fa5]<br/>
     *
     */
    public static boolean checkRegexWithRelax(String param, String regex) {
        return !isNull(param) && Pattern.compile(regex).matcher(param).find();
    }

    public static void checkNull(Object obj, String errorMsg) {
        if(obj == null) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static void checkBlank(String str, String errorMsg) {
        if(StringUtils.isBlank(str)) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static void checkEqual(String str1, String str2, String errorMsg) {
        if(!StringUtils.equals(str1, str2)) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static void checkPasswordStrong(String password) {
        if(StringUtils.isNotBlank(password)) {
            int len = password.length();
            if(len < 6) {
                throw new IllegalArgumentException("密码长度至少6位");
            }
        }
    }

    public static void checkLength(String str, int len, String msg) {
        if(StringUtils.length(str) < len) {
            throw new RuntimeException(msg);
        }
    }

    public static void checkField(Object f, String msg) {
        if(f == null) {
            throw new RuntimeException(msg);
        }
        if(f instanceof String) {
            if(StringUtils.isBlank((String)f)) {
                throw new RuntimeException(msg);
            }
        }
        if(f instanceof Integer) {
            if((Integer)f == 0) {
                throw new RuntimeException(msg);
            }
        }

    }

    public static String checkConsignee(String consignee) {
        if(StringUtils.isNotBlank(consignee)) {
            consignee = consignee.replaceAll("\\s*", "");
            checkLength(consignee, 2, "收货人姓名长度至少为2");
            /*checkCannotContains(consignee, "先生", "为了保障您购买的商品能够准确送达，收货人请勿包含\"先生\"");
            checkCannotContains(consignee, "女士", "为了保障您购买的商品能够准确送达，收货人请勿包含\"女士\"");
            checkCannotContains(consignee, "太太", "为了保障您购买的商品能够准确送达，收货人请勿包含\"太太\"");
            checkCannotContains(consignee, "小姐", "为了保障您购买的商品能够准确送达，收货人请勿包含\"小姐\"");*/
        }
        return consignee;
    }

    public static String checkAddress(String address) {
        if(StringUtils.isNotBlank(address)) {
            address = address.replaceAll("[\\t\\n\\r]", "");
            checkLength(address, 4, "为了保障您购买的商品能够准确送达，收货地址长度至少4个字");
        }
        return address;
    }

    public static void checkCannotContains(String sourceStr, String targetStr, String msg) {
        if(StringUtils.contains(sourceStr, targetStr)) {
            throw new RuntimeException(msg);
        }
    }

    public static void main(String[] args){
        System.out.println(CheckUtils.checkPhone("18688153635"));
    }
}