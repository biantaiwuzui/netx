package com.netx.shopping.biz.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.ProductPictureTypeEnum;
import com.netx.common.user.util.VoPoConverter;
import com.netx.shopping.biz.BaseAction;
import com.netx.shopping.biz.merchantcenter.ShippingFeeAction;
import com.netx.shopping.biz.ordercenter.HashCheckoutAction;
import com.netx.shopping.enums.ProductStatusEnum;
import com.netx.shopping.model.productcenter.Product;
import com.netx.shopping.model.productcenter.ProductManagement;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.shopping.service.productcenter.ProductManagementService;
import com.netx.shopping.service.productcenter.ProductPictureService;
import com.netx.shopping.service.productcenter.ProductService;
import com.netx.shopping.vo.AddGoodsRequestDto;
import com.netx.shopping.vo.ProductForceUpRequestDto;
import com.netx.shopping.vo.QueryBusinessProductRequestDto;
import com.netx.shopping.vo.QueryProductListRequestDto;
import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网商-商品表
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service("newProductAction")
public class ProductAction extends BaseAction{
	private Logger logger = LoggerFactory.getLogger(ProductAction.class);

	@Autowired
	private HashCheckoutAction hashCheckoutAction;
	@Autowired
	private ProductService productService;
	@Autowired
	private SkuAction skuAction;
	@Autowired
	private CategoryProductAction categoryProductAction;
	@Autowired
	private ProductPictureAction productPictureAction;
	@Autowired
	private ShippingFeeAction shippingFeeAction;
	@Autowired
	private ProductPictureService productPictureService;
	@Autowired
	MerchantService merchantService;
	@Autowired
    ProductManagementService productManagementService;
	@Autowired
	ProductDeliveryAction productDeliveryAction;

	public ProductService getProductService() {
		return productService;
	}

	/**
	 * 发布商品
	 * @param addGoodsDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public Product addPuduct(AddGoodsRequestDto addGoodsDto, String userId){
		if (!hashCheckoutAction.hashCheckout(userId, addGoodsDto.getHash())){
			throw new RuntimeException("请勿重复提交！");
		}
		Product product = new Product();
		if(StringUtils.isNotBlank(addGoodsDto.getId())){
			product = productService.selectById(addGoodsDto.getId());
		}
		VoPoConverter.copyProperties(addGoodsDto, product);
		if(StringUtils.isBlank(addGoodsDto.getId())){
			product.setPublisherUserId(userId);
			product.setVisitCount(0);
		}
		//更新费用设置
//		if(addGoodsDto.getShopping_fee() != null){
//			String shippingFeeId = shippingFeeAction.addOrUpdate(addGoodsDto.getMerchantId(), addGoodsDto.getShopping_fee());
//			if(shippingFeeId != null){
//				product.setShippingFeeId(shippingFeeId);
//			}
//		}
//		product.setShippingFee(new Money(addGoodsDto.getShopping_fee()).getCent());
		//添加商品
		productService.insertOrUpdate(product);
		//添加库存
		skuAction.addSkuList(addGoodsDto.getAddSkuDtoList(), product.getId());
		//添加配送方式和配送费用
		productDeliveryAction.addDelivery(addGoodsDto.getDeliveryWayList(), product.getId());
		//添加/更新商品-类目关联
		if(StringUtils.isBlank(addGoodsDto.getId())) {
			categoryProductAction.insertOrUpdate(false, addGoodsDto.getMerchantId(), product.getId(), addGoodsDto.getCategoryId(), addGoodsDto.getTagIds());
		}else{
			categoryProductAction.insertOrUpdate(true, addGoodsDto.getMerchantId(), product.getId(), addGoodsDto.getCategoryId(), addGoodsDto.getTagIds());
		}
		//添加商品图片
		if(addGoodsDto.getDeleteImageId() != null && addGoodsDto.getDeleteImageId().size() > 0){
			productPictureService.deleteBatchIds(addGoodsDto.getDeleteImageId());
		}
		productPictureAction.add(product.getId(), ProductPictureTypeEnum.NONE, addGoodsDto.getLogoImageUrl());
		return product;
	}

	/**
	 * 根据商家ids获取商品
	 * @param merchantIds
	 * @return
	 */
	public List<Product> getProductByMerchantIds(List<String> merchantIds){
		return productService.getProductByMerchantIds(merchantIds);
	}
	/**
	 * 根据id获取商品
	 * @param
	 * @return
	 */
	public Product getProductByd(String id){
		return productService.getProductById(id);
	}

    public List<Product> getProductByMerchantIds(List<String> merchantIds,Page page){
        return productService.getProductByMerchantIds(merchantIds,page);
    }

	/**
	 * 根据商家id分页获取商品
	 * @param requestDto
	 * @return
	 */
	public List<Product> getProductByMerchantId(QueryProductListRequestDto requestDto){
		return productService.getProductByMerchantId(requestDto);
	}

	/**
	 * 根据商家id获取商品id集
	 * @param merchantId
	 * @return
	 */
	public List<String> getProductIdByMerchantIds(String merchantId){
		return productService.getProductIdsByMerchantIds(merchantId);
	}


	/**
	 * 根据商家id删除商品
	 * @param merchantId
	 * @return
	 */
	public boolean deleteByMerchantId(String merchantId){
		List<Product> products = productService.getProductByMerchantId(merchantId);
		if(products != null && products.size() > 0) {
			return delete(products);
		}
		return false;
	}

	/**
	 * 根据商品id删除商品
	 * @param id
	 * @return
	 */
	@Transactional
	public boolean deleteById(String id){
		Product product = productService.selectById(id);
		List<Product> products = new ArrayList<>();
		if(product != null){
			products.add(product);
			return delete(products);
		}
		return false;
	}

	/**
	 * 删除商品
	 * @param products
	 * @return
	 */
	public boolean delete(List<Product> products){
		List<String> productIds = new ArrayList<>();
		for(Product product : products){
			productIds.add(product.getId());
			product.setDeleted(1);
		}
		//根据商品id删除商品规格，库存
		skuAction.delete(productIds);
		//删除商品类目关联表、更新类目被使用数量
		categoryProductAction.delete(productIds);
		//删除商品相关图片
		productPictureAction.deleteByProductIds(productIds);
		return productService.updateBatchById(products);
	}

	/**
	 * 是否已发布过这个商品名
	 * @param name
	 * @param merchantId
	 * @return
	 */
	public boolean isHaveThisName(String name, String merchantId){
		Product product = productService.isHaveThisName(name, merchantId);
		if(product != null){
			return false;
		}
		return true;
	}

	/**
	 * 获取某个用户经营的商品(包含下架的)
	 * @param merchantIds
	 * @return
	 */
	public Integer getAllProductCount(List<String> merchantIds){
		return productService.getAllProductCount(merchantIds);
	}

	public int getProductCountByMerchantId(String merchantId){
		return productService.getProductCountByMerchantId(merchantId);
	}

	/**
	 * 获取某个用户发布的商品
	 * @param userId
	 * @return
	 */
	public Integer getAllUpProductCount(String userId){
		return productService.getPublishProductCount(userId);
	}

	/**
	 * 获取商品名
	 * @param id
	 * @return
	 */
	public String getProductNameById(String id){
		return productService.getProductNameById(id);
	}

	/**
	 * 商品上/下架
	 * @param productId
	 * @return
	 */
	public Boolean optProduct(String productId){
		Product product = productService.selectById(productId);
		if(product == null){
			return false;
		}
		if(product.getOnlineStatus() == ProductStatusEnum.UP.getCode()){
			product.setOnlineStatus(ProductStatusEnum.DOWN.getCode());
		}else{
			product.setOnlineStatus(ProductStatusEnum.UP.getCode());
		}
		return productService.updateById(product);
	}

	public List<Map<String,String>> selectGoodsList(){
		return productService.selectGoodsList();
	}

    /**
     * boss 后台强制下架改变商品状态(3),并将操作记录存于下架表
     * @param productForceUpRequestDto
     * @return
     */
    public boolean forceDownProduct(ProductForceUpRequestDto productForceUpRequestDto){
       if (productService.forceChangeProductOnlineStatusByProductId(productForceUpRequestDto.getProductId(), productForceUpRequestDto.getOnlineStatus())){
           productManagementService.insertforceDownProduct(productForceUpRequestDto);
           return true;
       }
       return false;
    }

    /**
     * boss 后台将商品状态改为上架(1),并将下架表该商品状态改为1
     * @param productForceUpRequestDto
     * @return
     */
    public boolean forceUpProduct(ProductForceUpRequestDto productForceUpRequestDto){
        if (productService.forceChangeProductOnlineStatusByProductId(productForceUpRequestDto.getProductId(), productForceUpRequestDto.getOnlineStatus())){
            productManagementService.changeProductManagerOnlineStatusByProductId(productForceUpRequestDto.getProductId(), productForceUpRequestDto.getOnlineStatus(), productForceUpRequestDto.getReason());
            return true;
        }
        return false;
    }

    public Integer queryDeliveryWay(String id){
		return productService.getDeliveryWay(id);
	}

}
