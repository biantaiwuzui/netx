package com.netx.fuse.biz.worth;

import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.user.util.AddImgUrlPreUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NetEnergyFuseAction {

    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    public String updateImagesUrl(String imagesUrl){
        if(StringUtils.isNotBlank(imagesUrl)){
            if (!imagesUrl.contains("http")){
                return addImgUrlPreUtil.addImgUrlPres(imagesUrl, AliyunBucketType.ActivityBucket);
            }
            return imagesUrl;
        }
        return null;
    }

}
