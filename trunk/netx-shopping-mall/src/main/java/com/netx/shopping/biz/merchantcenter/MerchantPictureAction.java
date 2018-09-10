package com.netx.shopping.biz.merchantcenter;


import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.shopping.model.merchantcenter.MerchantPicture;
import com.netx.shopping.model.merchantcenter.constants.MerchantPictureEnum;
import com.netx.shopping.service.merchantcenter.MerchantPictureService;
import com.netx.shopping.vo.PictureResponseVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务action
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class MerchantPictureAction {

    @Autowired
    private MerchantPictureService merchantPictureService;

    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    public MerchantPictureService getMerchantPictureService() {
        return merchantPictureService;
    }


    /**
     * 添加图片
     * @param pictures
     * @param merchantPictureType
     * @param merchantId
     * @return
     */
    @Transactional
    public boolean addMerchantPicture(List<PictureResponseVo> pictures, MerchantPictureEnum merchantPictureType, String merchantId){
        MerchantPicture merchantPicture = new MerchantPicture();
        merchantPicture.setMerchantId(merchantId);
        merchantPicture.setMerchantPictureType(merchantPictureType.getType());
        int priority = merchantPictureService.getMaxPriorityByMerchantIdAndType(merchantId, merchantPictureType);
        merchantPicture.setDeleted(0);
        for(PictureResponseVo picture : pictures){
            if(StringUtils.isBlank(picture.getId())) {
                merchantPicture.setPictureUrl(picture.getPictureUrl());
                merchantPicture.setPriority(priority++);
                merchantPictureService.insert(merchantPicture);
                merchantPicture.setId(null);
            }
        }
        return true;
    }

    public List<String> getPictureUrl(String merchantId, String merchantPictureType){
        List<String> lists = merchantPictureService.getPictureUrl(merchantId, merchantPictureType);
        List<String> url = new ArrayList<>();
        for(String list: lists){
            url.add(updateImages(list));
        }
        return url;
    }

    public List<PictureResponseVo> getPicture(String merchantId, String merchantPictureType){
        List<MerchantPicture> merchantPictures = merchantPictureService.getPicture(merchantId, merchantPictureType);
        List<PictureResponseVo> pictureResponseVos = new ArrayList<>();
        for(MerchantPicture merchantPicture : merchantPictures){
            PictureResponseVo responseVo = new PictureResponseVo();
            responseVo.setId(merchantPicture.getId());
            responseVo.setPictureUrl(updateImages(merchantPicture.getPictureUrl()));
            pictureResponseVos.add(responseVo);
        }
        return pictureResponseVos;
    }

    /**
     * 删除相关商家图片
     * @param merchantId
     * @return
     */
    @Transactional
    public boolean deleteByMerchantId(String merchantId){
        List<MerchantPicture> lists = merchantPictureService.getMerchantPictureByMerchantId(merchantId);
        if(lists != null && lists.size() > 0){
            for(MerchantPicture list : lists){
                list.setDeleted(1);
            }
            return merchantPictureService.updateBatchById(lists);
        }
        return false;
    }

    public String updateImages(String url) {
        if (StringUtils.isNotBlank(url)) {
            //判断是否要加前缀
            if (!url.contains("http")) {
                url = addImgUrlPreUtil.getProductImgPre(url);
            }
        }
        return url;
    }
	
}
