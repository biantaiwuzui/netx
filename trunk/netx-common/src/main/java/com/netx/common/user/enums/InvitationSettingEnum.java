package com.netx.common.user.enums;

/**
 * 邀请设置枚举
 * @author 李卓
 */
public enum InvitationSettingEnum {

    REJECT("不接受邀请", 0),
    ACCEPT_FRIENDS("接受好友的邀请", 1),
    ACCEPT_WATCH_TO("接受我关注的人的邀请", 2);

    InvitationSettingEnum(String name, int value){
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
     * 根据值来获取相应的枚举
     * @param value
     * @return
     */
    public static InvitationSettingEnum getEnumByValue(Integer value){
        if(value == null) return null;
        InvitationSettingEnum invitationSettingEnum = null;
        switch (value) {
            case 0:
                invitationSettingEnum = InvitationSettingEnum.REJECT;
                break;
            case 1:
                invitationSettingEnum = InvitationSettingEnum.ACCEPT_FRIENDS;
                break;
            case 2:
                invitationSettingEnum = InvitationSettingEnum.ACCEPT_WATCH_TO;
                break;
            default:
                invitationSettingEnum = InvitationSettingEnum.REJECT;
                break;
        }
        return invitationSettingEnum;
    }
}
