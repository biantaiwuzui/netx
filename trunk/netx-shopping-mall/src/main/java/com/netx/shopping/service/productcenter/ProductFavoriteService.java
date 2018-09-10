package com.netx.shopping.service.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.shopping.model.productcenter.ProductFavorite;
import com.netx.shopping.mapper.productcenter.ProductFavoriteMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 网商-商品关注表 服务实现类
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service("newProductFavoriteService")
public class ProductFavoriteService extends ServiceImpl<ProductFavoriteMapper, ProductFavorite> {

    public ProductFavorite queryByUserIdAndProductId(String userId,String productId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("user_id={0} and product_id={1}",userId,productId);
        return selectOne(wrapper);
    }

    /**
     * 根据用户id查询已收藏的商品id
     * @param userId
     * @return
     */
    public List<String> queryProductIdByUserId(String userId){
        Wrapper<ProductFavorite> wrapper = createWrapperByUserId(userId);
        wrapper.setSqlSelect("product_id");
        return (List<String>)(List) selectObjs(wrapper);
    }
    
    /*
    * 根据用户Id分页获取已收藏的商品
    */
    public List<ProductFavorite> queryProductIdByUserId(String userId,Page<ProductFavorite> page){
        Wrapper<ProductFavorite> wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0}", userId);
        return selectPage(page,wrapper).getRecords();
    }
    
    private Wrapper<ProductFavorite> createWrapperByUserId(String userId){
        Wrapper<ProductFavorite> wrapper = new EntityWrapper<>();
        if(StringUtils.isNotBlank(userId)){
            wrapper.where("user_id = {0}",userId);
        }
        return wrapper;
    }

    /**
     * 根据用户id查询收藏的商品数
     * @param userId
     * @return
     */
    public int countProductByUserId(String userId){
        Wrapper<ProductFavorite> wrapper = createWrapperByUserId(userId);
        Integer count = selectCount(wrapper);
        return count==null?0:count;
    }

    /**
     * 根据用户id和商品id查询商品
     * @param userId
     * @param productId
     * @return
     */
    public ProductFavorite getProductFavoriteByUserIdAndProductId(String userId, String productId){
        EntityWrapper<ProductFavorite> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} AND product_id = {1} AND deleted = {2}", userId, productId, 0);
        return selectOne(wrapper);
    }
}
