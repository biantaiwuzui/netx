package com.netx.shopping.service.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.ProductPictureTypeEnum;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.shopping.model.productcenter.ProductPicture;
import com.netx.shopping.mapper.productcenter.ProductPictureMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service
public class ProductPictureService extends ServiceImpl<ProductPictureMapper, ProductPicture> {

    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    /**
     * 查询商品照片url
     * @param productId
     * @param typeEnum
     * @return
     */
    public List<String> getUrlByTypeAndProductId(String productId, ProductPictureTypeEnum typeEnum){
        Wrapper wrapper = createWrapper(productId, typeEnum);
        wrapper.setSqlSelect("concat('"+addImgUrlPreUtil.getProductImgPre()+"',picture_url) as picture_url");
        return (List<String>)(List) selectObjs(wrapper);
    }

    public List<ProductPicture> getPicture(String product, ProductPictureTypeEnum typeEnum){
        EntityWrapper<ProductPicture> wrapper = new EntityWrapper<>();
        wrapper.where("product_id = {0} AND product_picture_type = {1} AND deleted = 0",product, typeEnum.getValue());
        return this.selectList(wrapper);
    }

    public String getPictureUrlOne(String productId, ProductPictureTypeEnum typeEnum){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("picture_url").where("product_id = {0} AND product_picture_type = {1} AND deleted = 0",productId, typeEnum.getValue());
        wrapper.orderBy("priority");
        return (String) this.selectObj(wrapper);
    }

    public int countByProductIdAndType(String productId, ProductPictureTypeEnum typeEnum){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("product_id = {0}",productId);
        if(typeEnum!=null){
            wrapper.and("product_picture_type = {0}",typeEnum.getValue());
        }
        return selectCount(wrapper);
    }

    public int getPriorityByProductIdAndType(String productId, ProductPictureTypeEnum typeEnum){
        Wrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("MAX(priority)");
        wrapper.where("product_id = {0}",productId);
        if(typeEnum!=null){
            wrapper.and("product_picture_type = {0}",typeEnum.getValue());
        }
        Integer max = (Integer) selectObj(wrapper);
        return  max==null?0:max;
    }

    private Wrapper createWrapper(String productId, ProductPictureTypeEnum typeEnum){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("product_id = {0}",productId);
        if(typeEnum!=null){
            wrapper.and("product_picture_type = {0}",typeEnum.getValue());
        }
        wrapper.orderBy("product_picture_type");
        wrapper.orderBy("priority");
        return wrapper;
    }

    /**
     * 查询商品照片
     * @param productId
     * @param typeEnum
     * @param page
     * @return
     */
    public List<ProductPicture> queryByTypeAndProductId(String productId, ProductPictureTypeEnum typeEnum, Page page){
        Wrapper wrapper = createWrapper(productId, typeEnum);
        if(page!=null){
            return selectPage(page,wrapper).getRecords();
        }
        return selectList(wrapper);
    }

    /**
     * 根据商品id删除照片
     * @param productId
     * @return
     */
    public Boolean deletePictureByProductId(String productId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("product_id = {0}",productId);
        return delete(wrapper);
    }

    /**
     * 根据商品ids查询商品相关照片
     * @param productIds
     * @return
     */
    public List<ProductPicture> getProductPictureByProductIds(List<String> productIds){
        EntityWrapper<ProductPicture> wrapper = new EntityWrapper<>();
        wrapper.in("product_id", productIds).where("deleted = {0}", 0);
        return selectList(wrapper);
    }

}
