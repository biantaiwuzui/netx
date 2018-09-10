package com.netx.common.user.enums;

/**
 * 礼物设置枚举
 * @author 李卓
 */
public enum GiftSettingEnum {

    REJECT("不接受礼物", 0),
    ACCEPT_FRIENDS("接受好友礼物", 1),
    ACCEPT_WATCH_TO("接受我关注的人的礼物", 2);

    GiftSettingEnum(String name, int value){
        this.name = name;
        this.value = value;
    }
    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
            return value;
        }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * 根据值来比较是否为此枚举的值
     * @param value
     * @return
     */
    public static boolean isEnumValue(Integer value){
        if(value == null) return false;
        for(GiftSettingEnum giftSettingEnum : GiftSettingEnum.values()){
            if(giftSettingEnum.getValue() == value){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据值来获取相应的枚举
     * @param value
     * @return
     */
    public static GiftSettingEnum getEnumByValue(Integer value){
        if(value == null) return null;
        GiftSettingEnum giftSettingEnum = null;
        switch (value) {
            case 0:
                giftSettingEnum = GiftSettingEnum.REJECT;
                break;
            case 1:
                giftSettingEnum = GiftSettingEnum.ACCEPT_FRIENDS;
                break;
            case 2:
                giftSettingEnum = GiftSettingEnum.ACCEPT_WATCH_TO;
                break;
            default:
                giftSettingEnum = GiftSettingEnum.REJECT;
                break;
        }
        return giftSettingEnum;
    }
}
