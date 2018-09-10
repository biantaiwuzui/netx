package com.netx.shopping.model.productcenter;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 商品中心-SKU
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@TableName("sku")
public class Sku extends Model<Sku> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 对应的商品id
     */
	@TableField("product_id")
	private String productId;
    /**
     * 此sku实际库存量,每次销售需要减少
     */
	@TableField("storage_nums")
	private Integer storageNums;
    /**
     * 此sku当前销量，每次销售需要增加
     */
	@TableField("sell_nums")
	private Integer sellNums;
    /**
     * 单笔订单最大能购买的数量,0为不限制
     */
	@TableField("trade_max_nums")
	private Integer tradeMaxNums;
    /**
     * 专柜价
     */
	@TableField("market_price")
	private Long marketPrice;
    /**
     * SFM价
     */
	private Long price;
    /**
     * 条形码
     */
	@TableField("sku_bar_code")
	private String skuBarCode;
    /**
     * 为1时作为默认sku
     */
	@TableField("default_sku")
	private Integer defaultSku;
    /**
     * 记录新增的时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
    /**
     * 记录最近修改的时间，如果为新增后未改动则保持与createTime一致
     */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;
	
	@TableField(value = "unit")
	private String unit;


	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getStorageNums() {
		return storageNums;
	}

	public void setStorageNums(Integer storageNums) {
		this.storageNums = storageNums;
	}

	public Integer getSellNums() {
		return sellNums;
	}

	public void setSellNums(Integer sellNums) {
		this.sellNums = sellNums;
	}

	public Integer getTradeMaxNums() {
		return tradeMaxNums;
	}

	public void setTradeMaxNums(Integer tradeMaxNums) {
		this.tradeMaxNums = tradeMaxNums;
	}

	public Long getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Long marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getSkuBarCode() {
		return skuBarCode;
	}

	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
	}

	public Integer getDefaultSku() {
		return defaultSku;
	}

	public void setDefaultSku(Integer defaultSku) {
		this.defaultSku = defaultSku;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Sku{" +
			"id=" + id +
			", productId=" + productId +
			", storageNums=" + storageNums +
			", sellNums=" + sellNums +
			", tradeMaxNums=" + tradeMaxNums +
			", marketPrice=" + marketPrice +
			", price=" + price +
			", skuBarCode=" + skuBarCode +
			", defaultSku=" + defaultSku +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
