package com.netx.shopping.service.merchantcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.netx.shopping.model.merchantcenter.MerchantPicture;
import com.netx.shopping.mapper.merchantcenter.MerchantPictureMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.model.merchantcenter.constants.MerchantPictureEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class MerchantPictureService extends ServiceImpl<MerchantPictureMapper, MerchantPicture>{

    /**
     * 查询图片url
     * @param merchantId
     * @param merchantPictureType
     * @return
     */
    public List<String> getPictureUrl(String merchantId, String merchantPictureType){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("picture_url").where("merchant_id = {0} AND merchant_picture_type = {1} AND deleted = 0",merchantId, merchantPictureType);
        return this.selectObjs(wrapper);
    }

    /**
     * 查询图片
     * @param merchantId
     * @param merchantPictureType
     * @return
     */
    public List<MerchantPicture> getPicture(String merchantId, String merchantPictureType){
        EntityWrapper<MerchantPicture> wrapper = new EntityWrapper();
        wrapper.where("merchant_id = {0} AND merchant_picture_type = {1} AND deleted = 0",merchantId, merchantPictureType);
        return this.selectList(wrapper);
    }

    /**
     * 根据merchantId查询该商家相关图片
     * @param merchantId
     * @return
     */
    public List<MerchantPicture> getMerchantPictureByMerchantId(String merchantId){
        EntityWrapper<MerchantPicture> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id = {0} AND deleted = {1}", merchantId, 0);
        return this.selectList(wrapper);
    }

    public int getMaxPriorityByMerchantIdAndType(String merchantId, MerchantPictureEnum typeEnum){
        Wrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("MAX(priority)");
        wrapper.where("merchant_id = {0}",merchantId);
        if(typeEnum != null){
            wrapper.and("merchant_picture_type = {0}", typeEnum.getType());
        }
        Integer max = (Integer) selectObj(wrapper);
        return max==null?0:max;
    }

    // 网信 - 根据商家id获取商家logo
    public String getMerchantLogoByMerchantId(String merchantId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("picture_url");
        wrapper.where("merchant_id = {0} and merchant_picture_type = {1}", merchantId, "logo");
        return (String)selectObj(wrapper);
    }
}
