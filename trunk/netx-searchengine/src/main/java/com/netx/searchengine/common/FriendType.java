package com.netx.searchengine.common;

import com.netx.searchengine.enums.FriendTypeEnum;

import java.util.*;

public class FriendType {
    private Map<FriendTypeEnum,Query> typeMap;

    public static FriendType getFriendType(){
        return new FriendType();
    }
    /**
     * 校友：文化教育中，有相同学校名称的人，由近及远（如搜索者没有填写自己的学校，则不可以搜索）
     * 同事：工作经历中，有相同单位名称的人，由近及远（如搜索者没有填写自己的学校，则不可以搜索）
     * 美丽：外貌中，有含”美丽“、”漂亮“文字标签的人，由近及远
     * 帅气：外貌中，有含”帅气“、”英俊“文字标签的人，由近及远
     * 高管：工作经历中，职务中包含”总“字的人，由近及远
     * 多金：收入由高到低，由近及远
     * 未婚：情感中，有包含”未婚“就文字的标签的异性，由近及远
     * 博学：学历为本科、学士、硕士、博士的人，由近及远
     * 同龄之缘：年龄相差5岁的异性，由近及远
     * 共同爱好：兴趣爱好中有相同标签的人，由近及远
     * 同乡近邻：家乡在同一个地方的人，由近及远
     */
    public FriendType() {
        this.typeMap = new HashMap<>();
        typeMap.put(FriendTypeEnum.SCHOOL_TYPE,new Query(null));
        typeMap.put(FriendTypeEnum.COMPANY_TYPE,new Query(null));
        typeMap.put(FriendTypeEnum.BEAUTIFUL_TYPE,Query.listQuery(null,null,"美丽","漂亮"));
        typeMap.put(FriendTypeEnum.HANDSOME_TYPE,Query.listQuery(null,null,"帅气","英俊"));
        typeMap.put(FriendTypeEnum.EXECUTIVE_TYPE,new Query(createMap("like","总"),null,null));
        typeMap.put(FriendTypeEnum.RICH_TYPE,new Query(null,true,null));
        typeMap.put(FriendTypeEnum.UNMARRIED_TYPE,Query.listQuery(null,null,"未婚"));
        typeMap.put(FriendTypeEnum.ERUDITE_TYPE,Query.listQuery(null,null,"本科","学士","硕士","博士"));
        typeMap.put(FriendTypeEnum.SAME_AGE_TYPE,new Query(null));
        typeMap.put(FriendTypeEnum.COMMON_HOBBY_TYPE,new Query(null));
        typeMap.put(FriendTypeEnum.SAME_HOMETOWN_TYPE,new Query(null));
    }

    private Map<String,Object> createMap(String key,Object value){
        Map<String,Object> map = new HashMap<>();
        map.put(key,value);
        return map;
    }

    public Map<FriendTypeEnum, Query> getTypeMap() {
        return typeMap;
    }

    public Query getQueryByTypeEnum(FriendTypeEnum friendTypeEnum){
        return typeMap.get(friendTypeEnum);
    }
}
