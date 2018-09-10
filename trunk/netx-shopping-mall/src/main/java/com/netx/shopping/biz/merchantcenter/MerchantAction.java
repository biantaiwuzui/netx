package com.netx.shopping.biz.merchantcenter;

import com.netx.common.vo.business.GetRelatedSellerMessageResponseVo;
import com.netx.common.vo.business.GetSellerListRequestDto;
import com.netx.common.vo.business.GetSellersBuyIdResponseVo;
import com.netx.common.vo.business.SellerStatusRequestDto;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.query.SellerSearchQuery;
import com.netx.shopping.biz.productcenter.CategoryPropertyValueAction;
import com.netx.shopping.biz.productcenter.ProductAction;
import com.netx.shopping.biz.productcenter.PropertyAction;
import com.netx.shopping.biz.productcenter.ValueAction;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.model.merchantcenter.constants.MerchantStatusEnum;
import com.netx.shopping.service.merchantcenter.*;
import com.netx.shopping.vo.GetRegisterMerchantCountResponseVo;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class MerchantAction {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MerchantFavoriteAction merchantFavoriteAction;
    @Autowired
    private MerchantManagerAction merchantManagerAction;
    @Autowired
    private MerchantCategoryAction merchantCategoryAction;
    @Autowired
    private MerchantPictureAction merchantPictureAction;
    @Autowired
    private MerchantVerifyInfoAction merchantVerifyInfoAction;
    @Autowired
    private ProductAction productAction;
    @Autowired
    private PropertyAction propertyAction;
    @Autowired
    private ValueAction valueAction;
    @Autowired
    private CategoryPropertyValueAction categoryPropertyValueAction;

    public MerchantService getMerchantService() {
        return merchantService;
    }

    /**
     * 注销用户在网商模块的相关数据
     * @param userId
     * @return
     */
    public boolean cancelBuyUserId(String userId){
        List<String> merchantIds = merchantService.getIdByUserId(userId);
        for(String merchantId : merchantIds){
            delete(merchantId);
        }
        return true;
    }

    /**
     * 注销商家相应信息
     * @param merchantId
     * @return
     */
    @Transactional
    public boolean delete(String merchantId){
        //删除商家
        Merchant merchant = merchantService.selectById(merchantId);
        if(merchant != null){
            merchant.setDeleted(1);
            merchantService.updateById(merchant);
        }
        //删除商家类目关联，更新类目使用次数
        merchantCategoryAction.deleteByMerchantId(merchantId);
        //删除商家相关图片
        merchantPictureAction.deleteByMerchantId(merchantId);
        //删除商家认证
        merchantVerifyInfoAction.deleteByMerchantId(merchantId);
        //删除商家主管/收银员
        merchantManagerAction.deleteByMerchantId(merchantId);
        //删除商家属性
        propertyAction.deleteByMerchantId(merchantId);
        //删除商家属性值
        valueAction.deleteByMerchantId(merchantId);
        //删除相应商品
        productAction.deleteByMerchantId(merchantId);
        //删除商品类目属性属性值映射
        categoryPropertyValueAction.deleteByMerchantId(merchantId);
        return true;
    }

    public List<Merchant> getMerChantByIds(List<String> ids, Integer current, Integer size){
        return merchantService.getMerchantByIds(ids, current, size);
    }

    /**
     * 判断是否有重复数据，如果没有就将数据装进临时集合
     * @param list
     * @return
     */
    public List<String> getUntqueuUserId(List<String> list){
        List<String> result = new ArrayList<>();
        for (String i : list) {
            if (!result.contains(i)) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * 这个商家名是否已被注册过
     * @param name
     * @return
     */
    public boolean isHaveName(String name){
        Merchant merchant = merchantService.getMerchantByName(name);
        if(merchant == null){
            return true;
        }
        return false;
    }

    /**
     * 根据用户id获取商家id
     * @param userId
     * @return
     */
    public List<String> getMerchantIdByUserId(String userId){
        return merchantService.getIdByUserId(userId);
    }

    /**
     * 根据商家id获取用户id
     * @param id
     * @return
     */
    public String getUserIdById(String id){
        return merchantService.getUserIdById(id);
    }

    /**
     * 获取商家经纬度
     * @param ids
     * @return
     */
    public List<GetSellersBuyIdResponseVo> getMerchantsBuyId(List<String> ids){
        List<Merchant> list = merchantService.selectBatchIds(ids);
        List<GetSellersBuyIdResponseVo> responseVos = new ArrayList<>();
        for(Merchant merchant : list){
            GetSellersBuyIdResponseVo responseVo = new GetSellersBuyIdResponseVo();
            responseVo.setLat(merchant.getLat());
            responseVo.setLon(merchant.getLon());
            responseVos.add(responseVo);
        }
        return responseVos;
    }

    /**
     * 构造商家搜索条件
     * @param request
     * @param lat
     * @param lon
     * @return
     */
    public SellerSearchQuery getSellerSearchQuery(GetSellerListRequestDto request, double lat, double lon) {
        SellerSearchQuery sellerSearchQuery = new SellerSearchQuery();
        /**
         * 排序需求
         * 1.综合：销量>在线>距离>信用
         * 2.距离：距离>在线>距离>信用
         * 3.信用：信用>在线>距离>价格
         * 4.支持网信：网信>在线>距离>信用
         */
        if(request.getSort() == 1){
            sellerSearchQuery.addFristAscQueries(new LastAscQuery("volume",false));
            sellerSearchQuery.addFristAscQueries(new LastAscQuery("isLogin",false));
            sellerSearchQuery.addLastAscQuery(new LastAscQuery("credit",false));
        }else if(request.getSort() == 2){
            sellerSearchQuery.addLastAscQuery(new LastAscQuery("isLogin",false));
            sellerSearchQuery.addLastAscQuery(new LastAscQuery("credit",false));
        }else if(request.getSort() == 3){
            sellerSearchQuery.addFristAscQueries(new LastAscQuery("credit",false));
            sellerSearchQuery.addFristAscQueries(new LastAscQuery("isLogin",false));
            sellerSearchQuery.addLastAscQuery(new LastAscQuery("credit",false));
        }else if(request.getSort() == 4){
            sellerSearchQuery.setHoldCredit(true);
            sellerSearchQuery.addFristAscQueries(new LastAscQuery("isLogin",false));
            sellerSearchQuery.addLastAscQuery(new LastAscQuery("credit",false));
        }

        sellerSearchQuery.setCenterGeoPoint(new GeoPoint(lat,lon));
        sellerSearchQuery.setPage(request.getCurrent(),request.getSize());
        sellerSearchQuery.setCategoryId(request.getCategoryId());
        sellerSearchQuery.setProvinceCode(request.getProvinceCode());
        sellerSearchQuery.setCityCode(request.getCityCode());
        sellerSearchQuery.setAreaCode(request.getAreaCode());
        sellerSearchQuery.setMinVisitNum(request.getMinVisitNum());
        sellerSearchQuery.setMaxVisitNum(request.getMaxVisitNum());
        sellerSearchQuery.setName(request.getName());
        sellerSearchQuery.setMaxDistance(request.getLength());

        return sellerSearchQuery;
    }

    /**
     * --boss系统--
     * 获取商家黑名单/白名单
     * @param status
     * @return
     */
    public List<Merchant> export(Integer status){
        return merchantService.getMerchantByStatus(status);
    }

    /**
     * --boss系统--
     * 拉黑商家/解除黑名单
     * @param request
     * @return
     */
    public Boolean updateStatus(SellerStatusRequestDto request){
        Merchant merchant = merchantService.selectById(request.getId());
        if(merchant == null){
            return false;
        }
        merchant.setStatus(request.getStatus());
        if(request.getStatus() == 1){
            merchant.setEnableReason(request.getOverReason());
        }else{
            merchant.setDisableReason(request.getBackReason());
        }
        return merchantService.updateById(merchant);
    }

    public boolean updateDayNum(){
        return merchantService.updateDayNum();
    }

}
