package com.netx.shopping.biz.product;

import com.netx.common.vo.business.AddGoodsSpecRequertDto;
import com.netx.common.vo.business.DelectGoodsSpecRequestDto;
import com.netx.shopping.model.product.ProductSpec;
import com.netx.shopping.model.product.ProductSpecItem;
import com.netx.shopping.model.order.ProductOrderItem;
import com.netx.shopping.service.order.ProductOrderItemService;
import com.netx.shopping.service.product.ProductSpecItemService;
import com.netx.shopping.service.product.ProductSpecService;
import com.netx.shopping.vo.ProductSpecResponseVo;
import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 网商-商品规格表 服务实现类
 * </p>
 *
 * @author wj.liu
 * @since 2017-08-29
 */
@Service("productSpecAction")
@Transactional(rollbackFor = Exception.class)
public class ProductSpecAction {

    @Autowired
    ProductSpecService productSpecService;

    @Autowired
    ProductSpecItemService productSpecItemService;

    @Autowired
    ProductOrderItemService productOrderItemService;

    
    public ProductSpecResponseVo add(AddGoodsSpecRequertDto request){
        //如果规格分类为输入的，则直接先保存商品规格分类，再保存分类项；如果规格分类为选择的，则直接保存分类项
        ProductSpecResponseVo responseVo = new ProductSpecResponseVo();
        ProductSpec productSpec = new ProductSpec();
        ProductSpecItem productSpecItem = new ProductSpecItem();
        if(!StringUtils.isNotEmpty(request.getSpecId()) && StringUtils.isNotEmpty(request.getSpecName())){
            productSpec.setUserId(request.getUserId());
            productSpec.setName(request.getSpecName());
            productSpec.setCreateUserId(request.getUserId());
            productSpec.setDeleted(0);
            productSpecService.insert(productSpec);
        }
        if(StringUtils.isNotEmpty(request.getSpecId())){
            productSpecItem.setSpecId(request.getSpecId());
        }else{
            productSpecItem.setSpecId(productSpec.getId());
        }
        productSpecItem.setName(request.getSpecItemName());
        productSpecItem.setPrice(new Money(request.getPrice()).getCent());
        productSpecItem.setCreateUserId(request.getUserId());
        productSpecItemService.insert(productSpecItem);
        //查询返回结果
        ProductSpec dbProductSpec = productSpecService.selectById(productSpecItem.getSpecId());
        BeanUtils.copyProperties(dbProductSpec, responseVo);
        responseVo.setItemList(this.itemList(null, dbProductSpec.getId()));
        return responseVo;
    }


    public boolean delete(DelectGoodsSpecRequestDto request){
        //删除商品规格，同时要删除所有规格分类项
        productSpecService.delete(request.getId());
        return productSpecItemService.deleteBySpecId(request.getId());
    }

    
    public List<ProductSpec> specList(String userId){
        return productSpecService.specList(userId);
    }

    
    public List<ProductSpecResponseVo> specItemList(String userId){
        List<ProductSpecResponseVo> resultList = new ArrayList<>();
        List<ProductSpec> specList = productSpecService.specList(userId);
        for (ProductSpec productSpec : specList) {
            ProductSpecResponseVo goodsSpecResponseVo = new ProductSpecResponseVo();
            BeanUtils.copyProperties(productSpec, goodsSpecResponseVo);
            goodsSpecResponseVo.setItemList(this.itemList(null, productSpec.getId()));
            resultList.add(goodsSpecResponseVo);
        }
        return resultList;
    }

    private List<ProductSpecItem> itemList(String itemId, String specId) {
        return productSpecItemService.itemList(itemId,specId);
    }

    
    public List<ProductSpecResponseVo> getSpecListByIds(String ids){
        List<ProductSpecResponseVo> resultList = new ArrayList<>();
        if(StringUtils.isNotEmpty(ids)){
            List<ProductSpec> specList = productSpecService.getSpecListByIds(ids);
            for (ProductSpec productSpec : specList) {
                ProductSpecResponseVo goodsSpecResponseVo = new ProductSpecResponseVo();
                BeanUtils.copyProperties(productSpec, goodsSpecResponseVo);
                goodsSpecResponseVo.setItemList(this.itemList(null, productSpec.getId()));
                resultList.add(goodsSpecResponseVo);
            }
        }
        return resultList;
    }

    
    public List<ProductSpecResponseVo> getSpecListByIds(String orderId, String goodsId, String ids){
        List<ProductSpecResponseVo> resultList = new ArrayList<>();
        //获取订单项
        ProductOrderItem productOrderItem = productOrderItemService.getProductOrderItemByOrderIdAndProductId(orderId,goodsId);
        if( null == productOrderItem || !StringUtils.isNotEmpty(productOrderItem.getSpeId())){
            return resultList;
        }
        if(StringUtils.isNotEmpty(ids)){
            List<ProductSpec> specList = productSpecService.getSpecListByIds(ids);
            String[] specItemIds = productOrderItem.getSpeId().split(",");
            for (String specItemId: specItemIds){
                ProductSpecItem productSpecItem = productSpecItemService.selectById(specItemId);
                if(null == productSpecItem){
                    continue;
                }
                for (ProductSpec productSpec : specList) {
                    if(productSpecItem.getSpecId().equals(productSpec.getId())){
                        ProductSpecResponseVo goodsSpecResponseVo = new ProductSpecResponseVo();
                        BeanUtils.copyProperties(productSpec, goodsSpecResponseVo);
                        goodsSpecResponseVo.setItemList(this.itemList(productSpecItem.getId(),null));
                        resultList.add(goodsSpecResponseVo);
                    }
                }
            }
        }
        return resultList;
    }

    
    public List<ProductSpecResponseVo> getSpecListBySpecItemId(String goodsOrderItemSpecItemId){
        List<ProductSpecResponseVo> resultList = new ArrayList<>();
        String[] specItemids = goodsOrderItemSpecItemId.split(",");
        for(String id:specItemids){
            ProductSpecItem productSpecItem = productSpecItemService.selectById(id);
            if (productSpecItem !=null){
                ProductSpec productSpec =productSpecService.selectById(productSpecItem.getSpecId());
                ProductSpecResponseVo goodsSpecResponseVo = new ProductSpecResponseVo();
                BeanUtils.copyProperties(productSpec, goodsSpecResponseVo);
                List<ProductSpecItem> itemList=new ArrayList<>();
                itemList.add(productSpecItem);
                goodsSpecResponseVo.setItemList(itemList);
                resultList.add(goodsSpecResponseVo);
            }
        }
        return resultList;
    }
}