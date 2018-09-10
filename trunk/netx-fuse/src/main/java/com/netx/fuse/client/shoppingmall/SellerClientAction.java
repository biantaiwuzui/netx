package com.netx.fuse.client.shoppingmall;

import com.netx.common.user.util.MapOrObjectUtil;
import com.netx.common.vo.business.*;
import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.common.vo.currency.GetSellersBuyIdRequestDto;
import com.netx.fuse.biz.shoppingmall.business.SellerFuseAction;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantFuseAction;
import com.netx.fuse.client.ucenter.WalletBillClientAction;
import com.netx.shopping.biz.business.SellerAction;
import com.netx.shopping.biz.merchantcenter.MerchantAction;
import com.netx.shopping.biz.merchantcenter.MerchantManagerAction;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.vo.GetMerchantByUserIdRequestDto;
import com.netx.shopping.vo.GetMerchantListVo;
import com.netx.shopping.vo.GetRegisterMerchantCountResponseVo;
import com.netx.shopping.vo.GetSellerListResponseVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SellerClientAction {

    private Logger logger = LoggerFactory.getLogger(SellerClientAction.class);

    @Autowired
    SellerAction sellerAction;

    @Autowired
    SellerFuseAction sellerFuseAction;

    @Autowired
    MerchantAction merchantAction;

    @Autowired
    MerchantFuseAction merchantFuseAction;

    @Autowired
    WalletBillClientAction walletBillClientAction;

    @Autowired
    MerchantManagerAction merchantManagerAction;

    /**
     * 获取相关人员对象
     */
    public MerchantManager getMerchantManagerByMerchantId(String merchantId, Integer status){
        if (StringUtils.isNotEmpty(merchantId)){
            return merchantManagerAction.getMerchantManagerListByMerchantId(merchantId, status).get(0);
        }
        return null;
    }

    /**
     * 商家
     * 根据用户ID查询
     * @param request   1.CommonUserIdRequestDto 用户ID
     * @return GetRelatedSellerMessageResponseVo 对象,以下是属性
     *  1.Integer manageSellers     经营的商店数
     *  2.Integer collectSellers   收藏的商店数
     */
    public GetRelatedSellerMessageResponseVo getRelatedSellerMessage(CommonUserIdRequestDto request){
        if (StringUtils.isNotEmpty(request.getUserId())){
            try {
                GetRelatedSellerMessageResponseVo res = sellerFuseAction.getMessage(request.getUserId());
                return res;
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }

        return null;
    }

    /**
     * 根据userId和指标查询收藏和经营商家详情和注册商家详情
     * 1为经营商家，2为收藏商家，3为注册商家
     * @return GetMerchantListVo
     */
    public GetMerchantListVo getMerchantByUserId(GetMerchantByUserIdRequestDto request, Double lat, Double lon){
        if (StringUtils.isNotEmpty(request.getUserId())){
            try{
                GetMerchantListVo res= merchantFuseAction.getMerchantByUserId(request, lat, lon);
                return res;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        return new GetMerchantListVo();
    }

    public GetMerchantListVo newgetSellerByUserId(String userId, Double lat, Double lon){
        GetMerchantByUserIdRequestDto requestDto = new GetMerchantByUserIdRequestDto();
        requestDto.setUserId(userId);
        requestDto.setIndex(1);
        return getMerchantByUserId(requestDto, lat, lon);
    }

    /**
     * 根据UseuId查询现有注册的商店数、总经营的商店数
     * @param request
     * @return
     */
    public GetRegisterMerchantCountResponseVo getRegisterSellerMessage(CommonUserIdRequestDto request){
        if (StringUtils.isNotEmpty(request.getUserId())){
            try {
                GetRegisterMerchantCountResponseVo res = merchantFuseAction.getRegisterMerchantCountByUserId(request.getUserId());
                return res;
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }

        return new GetRegisterMerchantCountResponseVo();
    }


    /**
     *注册商家
     * 根据用户ID查询
     * @param request   1.CommonUserIdRequestDto 用户ID
     * @return List<GetSellerByUserIdVo> 对象,以下是属性
     *  1.GetSellerByUserIdVo   现有注册的商店数
     */
    public List<GetSellerByUserIdVo> getSellersByUserId(CommonUserIdRequestDto request){
        if (StringUtils.isNotEmpty(request.getUserId())){
            try {
                List<GetSellerByUserIdVo> res = sellerAction.getSellersByUserId(request.getUserId());
                return res;
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }

        return new ArrayList<>();
    }


    /**
     *商家后台管理1
     * 根据商家ID判断商家注册后每30天是否发布商品，否扣减信用值5分
     */
    public boolean businessManagement(Map map){
        if (StringUtils.isNotEmpty(map.get("sellerId").toString())){
            try {
                BusinessManagementRequestDto request = MapOrObjectUtil.mapToObject(map,BusinessManagementRequestDto.class);
                boolean res= sellerFuseAction.subtractCredit(request);
                return res;
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }

        return false;
    }

    /**
     *商家后台管理1
     * 根据商家ID判断商家注册后90天内无成交记录或交易记录或支付记录，每90天扣减信用值5分
     */
    public boolean businessManagement1(Map map){
        if (StringUtils.isNotEmpty(map.get("sellerId").toString())){
            try {
                BusinessManagementRequestDto request = MapOrObjectUtil.mapToObject(map, BusinessManagementRequestDto.class);
                boolean res= sellerFuseAction.subtractCredit1(request);
                return res;
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }
        return false;
    }


    /**
     * 商家红包金额清空定时器
     */
    public void emptySellerRedpacket(@RequestBody Map map){
        try{
            sellerAction.updateSellerRedpacket();
            sellerAction.emptySellerRedpacket();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    /**
     * 根据商家id查询商家详情，供跨域调用
     */
    public List<GetSellersBuyIdResponseVo> getSellersBuyId(Map map){
        if (!map.isEmpty()){
            try {
                GetSellersBuyIdRequestDto request = MapOrObjectUtil.mapToObject(map, GetSellersBuyIdRequestDto.class);
                List<GetSellersBuyIdResponseVo> res= sellerAction.getSellersBuyId(request);
                return res;
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }

        return null;
    }

    public void changeSellerStatus(Map map){
        if (StringUtils.isNotEmpty(map.get("sellerId").toString())){
            try{
                sellerFuseAction.changSellerStatus((String) map.get("sellerId"));
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
    }

    public void messagePush(Map map){
        if (StringUtils.isNotEmpty(map.get("sellerId").toString())){
            try{
                sellerFuseAction.messagePush((String) map.get("sellerId"));
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
    }

    public void changeSellerPayStatus(Map map){
        if (StringUtils.isNotEmpty(map.get("sellerId").toString())){
            try{
                sellerFuseAction.changeSellerPayStatus((String) map.get("seller"));
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
    }

    /**
     * d定时任务:自用户注册商家起后，24小时后未填写引荐人的客服代码，则执行这个服务
     * @param sellerId
     * @return
     */
    public void timingDoSeller(String sellerId){
        if (StringUtils.isNotEmpty(sellerId)){

        }
    }

    /**
     * d定时任务,商家列表,给定时任务使用的
     */
    public List<GetSellersResponseDto> listForQuartz(){
        try {
            List<GetSellersResponseDto> list =  sellerAction.listSellersForQuartz();
            return list;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 注销用户
     */
    public boolean cancelBuyUserId(String userId){
        if (StringUtils.isNotEmpty(userId)){
            try {
                boolean res = sellerFuseAction.cancelBuyUserId(userId);
                return res;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        return false;
    }

    /**
     * 添加流水（平台给用户零钱）
     */
    public void addBill(String description, BigDecimal amount, String userId){
        BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
        billAddRequestDto.setToUserId(userId);
        billAddRequestDto.setPayChannel(3);
        billAddRequestDto.setType(0);
        billAddRequestDto.setDescription(description);
        billAddRequestDto.setAmount(amount);
        walletBillClientAction.addBill("999",billAddRequestDto);
    }

}
