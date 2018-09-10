package com.netx.utils.json;

/**
 * <p/>
 * Api状态码
 * <p/>
 * 1xxx	通用状态码
 * 2xxx	类目&商品&搜索状态码
 * 3xxx	预留功能状态码
 * 4xxx	订单相关状态码
 * 5xxx	购物车&收藏夹状态码
 * 6xxx	预留功能状态码
 * 7xxx	优惠FAQ等相关状态码
 * 8xxx	账户相关状态码
 * 9xxx 商品咨询状态码
 * 11xxx 退款退货
 */
public enum ApiCode {
    NONE_CODE(0) {
        public String getMessage() {
                    return "";
                }
    },
    EXIST_SENSITIVE(1500){
        public String getMessage() {
            return "内容中存在敏感词,请修改后再重新提交";
        }
    },
    NO_AUTHORIZATION(401){
        public String getMessage(){return "当前未授权，无法访问";}
    },
    WRONG_SIGN(1403) {
        public String getMessage() {
            return "签名错误";
        }
    },
    INVALID_TIME_FORMAT(1406) {
        public String getMessage() {
            return "时间格式错误";
        }
    },
    WRONG_TIME(1407) {
        public String getMessage() {
            return "时钟错误";
        }
    },
    MISS_PARAMETER(1408) {
        public String getMessage() {
            return "缺失参数";
        }
    },
    // 1XXX 通用状态码
    OK(1200) {
        public String getMessage() {
                    return "成功";
                }
    },
    REDIRECT(1302) {
        public String getMessage() {
                    return "重定向";
                }
     },
    NOT_FOUND(1404) {
        public String getMessage() {
                    return "无法找到资源";
                }
    },
    INTERNAL_ERROR(1500) {
        public String getMessage() {
                    return "服务器内部错误";
                }
    },
    INVALID_PARAMETER(1600) {
        public String getMessage() {
                    return "参数不合法";
                }
    },
    DATABASE_ERROR(1700) {
        public String getMessage() {
                    return "数据库错误";
                }
    },
    /// 3XXX 交易/支付
    TO_PAY_ERROR(3000) {
        public String getMessage() {
                    return "跳转支付";
                }
    },
    /// 4XXX 订单
    SUBMIT_ORDER_ERROR(4000) {
        public String getMessage() {
                    return "提交订单错误";
                }
    },
    ORDER_CONFIRM_ERROR(4001) {
        public String getMessage() {
                    return "获取订单确认信息错误";
                }
    },
    ORDER_LIST_ERROR(4003) {
        public String getMessage() {
                    return "查询订单列表错误";
                }
    },
    ORDER_DETAIL_ERROR(4004) {
        public String getMessage() {
                    return "查询订单详情错误";
                }
    },
    ORDER_BONUS_ERROR(4005) {
        public String getMessage() {
                    return "使用红包错误";
                }
    },
    DUPLICATE_SUBMIT_ORDER_ERROR(4006) {
        public String getMessage() {
                    return "请勿重复提交表单";
                }
    },
    ////////////////////////////华丽的分割线/////////////////////////////

    // 5xxx	购物车&收藏夹状态码
    FAVORIT_DEL_RPODUCT(5000) {
        public String getMessage() {
                    return "该商品已被删除！";
                }
    },
    FAVORIT_RPODUCT(5001) {
        public String getMessage() {
                    return "该商品已经被收藏！";
                }
    },
    PRODUCT_NOT_EXIST(5002) {
        public String getMessage() {
                    return "商品已下架或不存在！";
                }
    },
    EXCEED_MINIMUM(5003) {
        public String getMessage() {
                    return "购买数量不能小于1!";
                }
    },
    EXCEED_MAXiMUM(5004) {
        public String getMessage() {
                    return "超过最大购买数量!";
                }
    },
    EXCEED_UN_COLLECT(5005) {
        public String getMessage() {
                    return "取消收藏失败!";
                }
    },
    ////////////////////////////华丽的分割线/////////////////////////////
    USERNAME_OR_PASSWORD_ERROR(8000) {
        public String getMessage() {
                    return "用户名或密码错误!";
                }
    },
    ERROR_WHEN_LOGIN(8001) {
        public String getMessage() {
                    return "登录过程中发生错误!";
                }
    },
    NOT_LOGIN_YET(8002) {
        public String getMessage() {
                    return "尚未登录!";
                }
    },
    USERNAME_IS_EMPTY(8003) {
        public String getMessage() {
                    return "用户名为空!";
                }
    },
    PASSWORD_IS_EMPTY(8004) {
        public String getMessage() {
                    return "密码为空!";
                }
    },
    TOKEN_IS_EMPTY(8005) {
        public String getMessage() {
                    return "用户凭证不完整!";
                }
    },
    TOKEN_IS_INVALID(8006) {
        public String getMessage() {
                    return "非法的用户凭证!";
                }
    },
    VERIFYCODE_IS_NULL(8007) {
        public String getMessage() {
                    return "验证码为空!";
                }
    },
    USERNAME_LENGTH_INVALID(8008) {
        public String getMessage() {
                    return "用户名只能由4-20个字符组成!";
                }
    },
    EMAIL_LENGTH_INVALID(8009) {
        public String getMessage() {
                    return "邮箱长度只能由5-50个字符!";
                }
    },
    USER_ALREADY_EXISTS(8010) {
        public String getMessage() {
                    return "账号名已被注册";
                }
    },
    REGISTER_SUCCESSFULLY(8011) {
        public String getMessage() {
                    return "注册成功!";
                }
    },
    Email_IS_EMPTY(8012) {
        public String getMessage() {
                    return "邮箱为空!";
                }
    },
    Email_IS_INVALID(8013) {
        public String getMessage() {
                    return "邮箱格式错误!";
                }
    },
    Email_ALREADY_USED(8014) {
        public String getMessage() {
                    return "邮箱已被占用!";
                }
    },
    Email_IS_VALID(8015) {
        public String getMessage() {
                    return "邮箱检查通过!";
                }
    },
    USERNAME_IS_VALID(8016) {
        public String getMessage() {
                    return "用户名检查通过!";
                }
    },
    PASSWORD_LENGTH_INVALID(8017) {
        public String getMessage() {
                    return "密码长度应为6-16位!";
                }
    },
    PASSWORD_STYLE_VALID(8018) {
        public String getMessage() {
                    return "密码格式检查通过!";
                }
    },
    PHONE_NUMBER_INVALID(8019) {
        public String getMessage() {
                    return "手机号码无效!";
                }
    },
    PHONE_ALREADY_USED(8020) {
        public String getMessage() {
                    return "手机号码已被使用!";
                }
    },
    PHONE_IS_VALID(8021) {
        public String getMessage() {
                    return "手机号码检查通过!";
                }
    },
    REGISTER_WITH_ERROR(8022) {
        public String getMessage() {
                    return "注册过程中出现错误!";
                }
    },
    EMAIL_ALREADY_USED(8023) {
        public String getMessage() {
                    return "邮箱已被使用!";
                }
    },
    USERNAME_ALREADY_USED(8024) {
        public String getMessage() {
                    return "用户名已被使用!";
                }
    },
    MYINFO_QUERY_FAILED(8025) {
        public String getMessage() {
                    return "个人信息查询失败!";
                }
    },
    MYGRADE_QUERY_FAILED(8026) {
        public String getMessage() {
                    return "会员等级查询失败!";
                }
    },
    MYADDRESS_QUERY_FAILED(8027) {
        public String getMessage() {
                    return "收货地址查询失败!";
                }
    },
    SET_DEFAULT_ADDRESS_FAILED(8028) {
        public String getMessage() {
                    return "设置默认收货地址失败!";
                }
    },
    ADD_ADDRESS_FAILED(8029) {
        public String getMessage() {
                    return "添加收货地址失败!";
                }
    },
    ADDRESS_MORE_THAN_TEN(8030) {
        public String getMessage() {
                    return "最多允许添加10条地址!";
                }
    },
    LOGOUT_WITH_ERROR(8031) {
        public String getMessage() {
                    return "退出过程出现错误!";
                }
    },
    OLD_PASSWORD_ERROR(8032) {
        public String getMessage() {
                    return "原密码错误!";
                }
    },
    MODIFY_PASSWORD_WITH_ERROR(8033) {
        public String getMessage() {
                    return "修改密码过程中出现错误!";
                }
    },
    TOKEN_IS_EXPIRE(8034) {
        public String getMessage() {
                    return "用户凭证已过期!";
                }
    },
    IDENTIFY_WITH_ERROR(8035) {
        public String getMessage() {
                    return "身份验证过程中出现错误!";
                }
    },
    DELETE_ADDRESS_FAILED(8036) {
        public String getMessage() {
                    return "删除收货地址出错!";
                }
    },
    UPDATE_ADDRESS_FAILED(8036) {
        public String getMessage() {
                    return "更新收货地址出错!";
                }
    },
    COUNTRIE_ADDRESS_ERROR(8037) {
        public String getMessage() {
                    return "查询国家地址出错!";
                }
    },
    USER_NOT_EXISTS(8038) {
        public String getMessage() {
            return "账号名不存在!";
        }
    },
    LOGIN_AUTH_FAIL(8039) {
        public String getMessage() {
            return "登陆认证失败，账号名或密码错误";
        }
    },
    ////////////////////////////商品咨询/////////////////////////////
    ADD_CONSULT_FAIL(9001) {
        public String getMessage() {
                    return "咨询失败!";
                }
    },
    ////////////////////////////退款退货/////////////////////////////
    EXIST_BACK(11000) {
        public String getMessage() {
                    return "商品已经伸请退款或退货";
                }
    };
    //

    ////////////////////////////华丽的分割线/////////////////////////////
    private final int value;

    private ApiCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public abstract String getMessage();
}
