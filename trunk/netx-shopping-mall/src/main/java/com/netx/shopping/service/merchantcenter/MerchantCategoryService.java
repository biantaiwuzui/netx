package com.netx.shopping.service.merchantcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.shopping.model.merchantcenter.MerchantCategory;
import com.netx.shopping.mapper.merchantcenter.MerchantCategoryMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 网商-商家类别表 服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class MerchantCategoryService extends ServiceImpl<MerchantCategoryMapper, MerchantCategory>{

    /**
     * 根据商家id查询商家类目
     * @param merchantId
     * @return
     */
    public List<String> getMerchantCategoryIds(String merchantId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("category_id").where("merchant_id = {0} AND deleted = 0", merchantId);
        return this.selectObjs(wrapper);
    }

    public boolean deleteByMerchantId(String merchantId){
        EntityWrapper<MerchantCategory> wrapper = new EntityWrapper<>();
        wrapper.in("merchant_id",merchantId);
        return this.delete(wrapper);
    }

    public List<String> getCategoryIdByMerchantId(String merchantId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("category_id");
        wrapper.where("merchant_id = {0} AND deleted = 0", merchantId);
        return this.selectObjs(wrapper);
    }

    /**
     * 根据merchantId查询MerchantCategory
     * @param merchantId
     * @return
     */
    public List<MerchantCategory> getMerchantCategoryByMerchantId(String merchantId){
        EntityWrapper<MerchantCategory> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id = {0} AND deleted = {1}", merchantId, 0);
        return this.selectList(wrapper);
    }

    /**
     * 根据categoryIds查询MerchantCategory
     * @param categoryIds
     * @return
     */
	public List<MerchantCategory> getMerchantCategoryByCategoryIds(List<String> categoryIds, Integer deleted){
        EntityWrapper<MerchantCategory> wrapper = new EntityWrapper<>();
        wrapper.in("category_id", categoryIds);
        wrapper.where("deleted = {0}", deleted);
        return this.selectList(wrapper);
    }

    /**
     * 网信 - 适用范围 - 商家类别
     */
    public List<String> getMerchantCategoryIdsByMerchantId(String merchantId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("category_id").where("merchant_id = {0} AND deleted = 0", merchantId);
        return this.selectObjs(wrapper);
    }
}
