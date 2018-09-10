package com.netx.common.user.util;

import com.netx.common.common.enums.AliyunBucketType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AddImgUrlPreUtil {
    @Value("${domain.upload.ugcImgPre}")
    private String userImgPre;

    @Value("${domain.upload.productImgPre}")
    private String productImgPre;

    @Value("${domain.upload.activityImgPre}")
    private String activityImgPre;

    @Value("${domain.upload.creditImgPre}")
    private String creditImgPre;

    /**
     * 获取用户空间域名
     * @return
     */
    public String getUserImgPre() {
        return userImgPre;
    }

    /**
     * 获取网商【商家、商品】空间域名
     * @return
     */
    public String getProductImgPre() {
        return productImgPre;
    }

    /**
     * 获取网值【活动、需求、技能、心愿】空间域名
     * @return
     */
    public String getActivityImgPre() {
        return activityImgPre;
    }

    /**
     * 获取网信空间空间域名
     * @return
     */
    public String getCreditImgPre() {
        return creditImgPre;
    }

    /**
     * 转换成用户空间完整图片url
     * @param url
     * @return
     */
    public String getUserImgPre(String url) {
        return addImgPre(url,userImgPre);
    }

    /**
     * 转换成网商【商家、商品】空间完整图片url
     * @param url
     * @return
     */
    public String getProductImgPre(String url) {
        return addImgPre(url,productImgPre);
    }

    /**
     * 转换成网值【活动、需求、技能、心愿】空间完整图片url
     * @param url
     * @return
     */
    public String getActivityImgPre(String url) {
        return addImgPre(url,activityImgPre);
    }

    /**
     * 转换成网信空间完整图片url
     * @param url
     * @return
     */
    public String getCreditImgPre(String url) {
        return addImgPre(url,creditImgPre);
    }

    private String addImgPre(String url,String imgPre){
        return url==null?"":imgPre+url;
    }

    /**
     * 转换成对应的完整图片url
     * @param url
     * @param bucketType
     * @return
     */
    public String addImgUrlPre(String url,AliyunBucketType bucketType){
        if(StringUtils.isNotBlank(url)){
            return getImgPre(bucketType)+url;
        }
        return "";
    }

    /**
     * 转换成对应的完整图片urls【逗号隔开】
     * @param urls
     * @param bucketType
     * @return
     */
    public String addImgUrlPres(String urls,AliyunBucketType bucketType){
        if(StringUtils.isNotBlank(urls)){
            String imgPre = getImgPre(bucketType);
            if(!imgPre.equals("")){
                urls = imgPre + urls;
                return urls.replace(",",","+imgPre);
            }
        }
        return "";
    }

    /**
     * 获取对应的空间域名
     * @param bucketType
     * @return
     */
    public String getImgPre(AliyunBucketType bucketType){
        if(bucketType!=null)
            switch (bucketType){
                case UserBucket:
                    return userImgPre;
                case CreditBucket:
                    return creditImgPre;
                case ProductBucket:
                    return productImgPre;
                case ActivityBucket:
                    return activityImgPre;
        }
        return "";
    }

    @Override
    public String toString() {
        return "AddImgUrlPreUtil{" +
                "userImgPre='" + userImgPre + '\'' +
                ", productImgPre='" + productImgPre + '\'' +
                ", activityImgPre='" + activityImgPre + '\'' +
                ", creditImgPre='" + creditImgPre + '\'' +
                '}';
    }
}
