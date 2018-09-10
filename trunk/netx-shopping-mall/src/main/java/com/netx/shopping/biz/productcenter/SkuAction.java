package com.netx.shopping.biz.productcenter;


import com.netx.common.user.util.VoPoConverter;
import com.netx.shopping.model.productcenter.Sku;
import com.netx.shopping.service.productcenter.SkuService;
import com.netx.shopping.vo.AddSkuRequestDto;
import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品中心-SKU
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-05
 */
@Service
public class SkuAction {

    @Autowired
    private SkuService skuService;
    @Autowired
    private ProductSkuSpecAction productSkuSpecAction;

    public final int max = 5;

    public SkuService getSkuService() {
        return skuService;
    }

    /**
     * 添加商品多个规格库存
     * @param addSkuDtoList
     * @param productId
     * @return
     */
    public boolean addSkuList(List<AddSkuRequestDto> addSkuDtoList, String productId){
        for(AddSkuRequestDto addSkuDto : addSkuDtoList){
            addSkuDto.setProductId(productId);
            addSku(addSkuDto);
        }
        return true;
    }

    /**
     * 添加商品库存
     * @param addSkuDto
     * @return
     */
    public Sku addSku(AddSkuRequestDto addSkuDto){
        Sku sku = new Sku();
        VoPoConverter.copyProperties(addSkuDto, sku);
        sku.setMarketPrice(new Money(addSkuDto.getMarketPrice()).getCent());
        sku.setPrice(new Money(addSkuDto.getPrice()).getCent());
        if(StringUtils.isBlank(sku.getId())){
            sku.setDeleted(0);
        }
        if(skuService.insert(sku)){
            productSkuSpecAction.addProductSkuSpecList(addSkuDto.getAddProductSkuSpecDtoList(), sku.getId());
        }
        return sku;
    }

    /**
     * 根据producId获取skuId
     * @param productId
     * @return
     */
    public List<String> getSkuIdByProductId(String productId){
        return skuService.getSkuIdByProductId(productId);
    }

    /**
     * 根据商品id删除商品规格，库存
     * @param productIds
     * @return
     */
    public boolean delete(List<String> productIds){
        List<Sku> skus = skuService.getSkuIdByProductIds(productIds);
        List<String> skuIds = new ArrayList<>();
        for(Sku sku : skus){
            skuIds.add(sku.getId());
            sku.setDeleted(1);
        }
        if(skus != null && skus.size() > 0) {
            return skuService.updateBatchById(skus) && productSkuSpecAction.delete(skuIds);
        }
        return false;
    }

    /**
     * 获取最低价格
     * @param productId
     * @return
     */
    public Sku getSkuMinMarketPrice(String productId){
        return skuService.getSkuMinMarketPrice(productId);
    }

    /**
     * 获取商品总销量
     * @param productId
     * @return
     */
    public Long getSumSellNums(String productId){
        return skuService.getSumSellNums(productId);
    }

    /**
     * 判断库存数量是否足够
     * @param skuId
     * @param quantity
     * @return
     */
    public Sku checkSkuNum(String skuId,int quantity){
        Sku sku = skuService.selectById(skuId);
        if(sku==null){
            throw new RuntimeException("此规格不存在");
        }
        if(sku.getStorageNums()<quantity){
            throw new RuntimeException("此规格库存不足");
        }
        if(sku.getTradeMaxNums()!=0 && sku.getTradeMaxNums()<quantity){
            throw new RuntimeException("此规格最多只能买"+sku.getTradeMaxNums()+"件");
        }
        return sku;
    }
}
