package com.netx.shopping.biz.productcenter;


import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.ProductPictureTypeEnum;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.shopping.model.productcenter.ProductPicture;
import com.netx.shopping.service.productcenter.ProductPictureService;
import com.netx.shopping.vo.PictureRequestDto;
import com.netx.shopping.vo.PictureResponseVo;
import com.netx.shopping.vo.UsePrictureInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  商品中心-商品图片 前端控制器
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service
public class ProductPictureAction {

    private Logger logger = LoggerFactory.getLogger(ProductPictureAction.class);

    @Autowired
    ProductPictureService productPictureService;

    @Autowired
    AddImgUrlPreUtil addImgUrlPreUtil;

    public ProductPictureService getProductPictureService() {
        return productPictureService;
    }

    /**
     * 查询已带前缀的图片
     * @param productId
     * @param typeEnum
     * @param page
     * @return
     */
    public List<ProductPicture> queryPicture(String productId, ProductPictureTypeEnum typeEnum, Page page){
        List<ProductPicture> productPictures = productPictureService.queryByTypeAndProductId(productId, typeEnum, page);
        productPictures.forEach(productPicture -> {
            String url = productPicture.getPictureUrl();
            if(StringUtils.isNotBlank(url)){
                productPicture.setPictureUrl(addImgUrlPreUtil.getProductImgPre(url));
            }
        });
        return productPictures;
    }

    private UsePrictureInfo createUsePrictureInfo(String id,String url){
        UsePrictureInfo usePrictureInfo = new UsePrictureInfo();
        usePrictureInfo.setId(id);
        usePrictureInfo.setUrl(url);
        return usePrictureInfo;
    }

    /**
     * 添加单张商品照片
     * @param productId
     * @param url
     * @param typeEnum
     * @return
     */
    public Boolean add(String productId, String url, ProductPictureTypeEnum typeEnum){
        int priority = productPictureService.getPriorityByProductIdAndType(productId, typeEnum)+1;
        return productPictureService.insert(createProductPicture(productId,url, typeEnum, priority));
    }

    /**
     * 批量添加商品照片
     * @param productId
     * @param typeEnum
     * @param pictures
     * @return
     */
    public Boolean add(String productId, ProductPictureTypeEnum typeEnum, List<PictureResponseVo> pictures){
        ProductPicture productPicture = new ProductPicture();
        productPicture.setProductId(productId);
        productPicture.setDeleted(0);
        if(typeEnum != null){
            productPicture.setProductPictureType(typeEnum.getValue());
        }
        int priority = productPictureService.getPriorityByProductIdAndType(productId, typeEnum);
        for(PictureResponseVo picture : pictures){
            if(StringUtils.isBlank(picture.getId())){
                productPicture.setPriority(priority++);
                productPicture.setPictureUrl(picture.getPictureUrl());
                productPictureService.insert(productPicture);
                productPicture.setId(null);
            }
        }
        return true;
    }

    private ProductPicture createProductPicture(String productId,String url, ProductPictureTypeEnum typeEnum,int priority){
        ProductPicture productPicture = new ProductPicture();
        productPicture.setPictureUrl(url);
        productPicture.setPriority(priority);
        productPicture.setProductId(productId);
        if(typeEnum!=null){
            productPicture.setProductPictureType(typeEnum.getValue());
        }
        return productPicture;
    }

    /**
     * 更新照片
     * @param productId
     * @param usePrictureInfos
     * @param typeEnum
     * @return
     */
    private Boolean updateProductPrictureList(String productId,List<UsePrictureInfo> usePrictureInfos,ProductPictureTypeEnum typeEnum){
        int size = usePrictureInfos.size();
        List<ProductPicture> productPictures = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            UsePrictureInfo usePrictureInfo = usePrictureInfos.get(i);
            if(usePrictureInfo!=null){
                productPictures.add(queryProductPicture(productId,usePrictureInfo,typeEnum,i+1));
            }
        }
        return productPictureService.insertOrUpdateBatch(productPictures);
    }

    private ProductPicture queryProductPicture(String productId,UsePrictureInfo prictureInfo,ProductPictureTypeEnum typeEnum,int priority){
        if(prictureInfo!=null && StringUtils.isNotBlank(prictureInfo.getId())){
            ProductPicture productPicture = productPictureService.selectById(prictureInfo.getId());
            if(productPicture!=null){
                if(typeEnum!=null){
                    productPicture.setProductPictureType(typeEnum.getValue());
                }
                productPicture.setPriority(priority);
                productPicture.setPictureUrl(prictureInfo.getUrl());
                return productPicture;
            }
        }
        return createProductPicture(productId,prictureInfo.getUrl(),typeEnum,priority);
    }

    /**
     * 根据照片id删除照片
     * @param ids
     * @return
     */
    private Boolean delProductPrictureList(List<String> ids){
        return productPictureService.deleteBatchIds(ids);
    }

    /**
     * 根据商品id删除图片
     * @param productIds
     * @return
     */
    public boolean deleteByProductIds(List<String> productIds){
        List<ProductPicture> productPictures = productPictureService.getProductPictureByProductIds(productIds);
        for(ProductPicture productPicture : productPictures){
            productPicture.setDeleted(1);
        }
        if(productPictures != null && productPictures.size() > 0){
            return productPictureService.updateBatchById(productPictures);
        }
        return false;
    }

    public String updateImnagesUrl(String imangesUrl){
        if (imangesUrl != null){
            if (!imangesUrl.contains("http")) {
                return addImgUrlPreUtil.getProductImgPre(imangesUrl);
            }
            return imangesUrl;
        }
        return null;
    }

    public List<PictureResponseVo> getPicture(String productId, ProductPictureTypeEnum productPictureTypeEnum){
        List<ProductPicture> productPictures = productPictureService.getPicture(productId, productPictureTypeEnum);
        List<PictureResponseVo> pictureResponseVos = new ArrayList<>();
        for(ProductPicture productPicture : productPictures){
            PictureResponseVo responseVo = new PictureResponseVo();
            responseVo.setId(productPicture.getId());
            responseVo.setPictureUrl(updateImages(productPicture.getPictureUrl()));
            pictureResponseVos.add(responseVo);
        }
        return pictureResponseVos;
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
