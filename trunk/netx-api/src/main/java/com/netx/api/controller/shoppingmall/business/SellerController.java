package com.netx.api.controller.shoppingmall.business;

import com.netx.api.controller.BaseController;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.vo.business.*;
import com.netx.common.vo.common.PageRequestDto;
import com.netx.common.vo.currency.GetCanCurrencyMessageResquesDto;
import com.netx.common.vo.currency.GetCurrencyMessageResponseVo;
import com.netx.common.vo.currency.GetSellersBuyIdRequestDto;
import com.netx.common.vo.business.GetSellersBuyIdResponseVo;
import com.netx.fuse.biz.shoppingmall.business.SellerFuseAction;
import com.netx.fuse.biz.shoppingmall.business.SellerRegisterFuseAction;
import com.netx.fuse.biz.shoppingmall.order.ProductOrderFuseAction;
import com.netx.shopping.biz.business.ManageAction;
import com.netx.shopping.biz.business.SellerRegisterAction;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.biz.business.SellerCollectAction;
import com.netx.shopping.biz.business.SellerAction;
import com.netx.shopping.vo.BusinessHomePageResponseVo;
import com.netx.shopping.vo.GetSellerListResponseVo;
import com.netx.shopping.vo.GetSellerResponseVo;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created By wj.liu
 * Description: 网商控制层
 * Date: 2017-09-04
 */
@Api(description = "网商商家相关接口")
@RestController
@RequestMapping("/api/business/seller")
public class SellerController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(SellerController.class);

    @Autowired
    SellerAction sellerAction;

    @Autowired
    SellerCollectAction sellerCollectAction;

    @Autowired
    SellerRegisterAction sellerRegisterAction;

    @Autowired
    SellerRegisterFuseAction sellerRegisterFuseAction;

    @Autowired
    SellerFuseAction sellerFuseAction;
    @Autowired
    ProductOrderFuseAction productOrderFuseAction;

    @ApiOperation(value = "商家注册或编辑", notes = "商家注册或编辑，符合条件的用户，即可注册商家。允许同一用户注册多个商家，" +
            "不符合条件者，需提示其因何原因暂不具备注册商家的资格。")
    @PostMapping("/register")
    public JsonResult register(@Valid @RequestBody RegisterSellerRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            Seller seller = sellerRegisterFuseAction.register(request);
            if(seller==null){
                return JsonResult.fail("注册失败！");
            }
            return JsonResult.success().addResult("seller",seller);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("注册异常！"+e.getMessage());
        }
    }

    @ApiOperation(value = "商家管理费处理", notes = "注册或续费商家管理费之后调用")
    @PostMapping("/manageFee")
    public JsonResult manageFee(@Valid @RequestBody SellerManageFeeRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            Seller res = sellerFuseAction.manageFee(request);
            if(res==null){
                return JsonResult.fail("商家管理费处理操作失败！");
            }else {
                sellerRegisterFuseAction.payStatusSuccess(res);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商家管理费处理操作异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "商家详情", notes = "商家详情")
    @PostMapping("/get")
    public JsonResult get(@Valid @RequestBody GetSellerRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = new UserGeo();
        try {
            userGeo = getGeoFromRequest(requestDto);
            if(StringUtils.isEmpty(userGeo.getUserId())){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage(), ApiCode.NO_AUTHORIZATION);
        }
        try {
            request.setUserId(userGeo.getUserId());
            GetSellerResponseVo object = sellerFuseAction.get(request,userGeo.getLat(),userGeo.getLon());
            if (object != null){
                return JsonResult.success().addResult("object",object);
            }
            return JsonResult.fail("获取商家详情失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商家详情异常！");
        }
    }

    @ApiOperation(value = "获取商家对象", notes = "获取商家对象，供远程调用")
    @GetMapping("/getSellerById")
    public JsonResult getSellerById(@RequestParam String sellerId){
        if(!StringUtils.isNotBlank(sellerId)){
            return JsonResult.fail( "商家id不能为空");
        }
        try {
            Seller seller = sellerAction.getSellerAndChangeSellerImageUrl(sellerId);
            if (seller != null){
                return JsonResult.success().addResult("seller",seller);
            }
            return JsonResult.fail("获取商家对象失败！");
        } catch (Exception e) {
            return JsonResult.fail("获取商家对象异常！");
        }
    }


    @ApiOperation(value = "商家列表", notes = "通用的商家列表接口，根据不同条件返回")
    @PostMapping("/list")
    public JsonResult list(@Valid @RequestBody GetSellerListRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if( bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<GetSellerListResponseVo> list =  sellerFuseAction.list(request, getLat(requestDto), getLon(requestDto));
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取商家列表失败！");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取商家列表异常！");
        }
    }

    @ApiOperation(value = "商家列表", notes = "给定时任务使用的")
    @GetMapping("/listForQuartz")
    public JsonResult list(){
        try {
            List<GetSellersResponseDto> list =  sellerAction.listSellersForQuartz();
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取商家列表失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商家列表异常！");
        }
    }

    @ApiOperation(value = "根据距离远近查询商家列表",notes = "根据距离远近，创建时间，类别来选择返回商家列表")
    @PostMapping("/selectSellerListByDistanceAndTime")
    public JsonResult selectSellerListByDistanceAndTime(@Valid @RequestBody SelectSellerListByDistanceAndTimeRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            Map<String,List> map = sellerAction.selectSellerListByDistanceAndTime(request);
            if (map != null){
                return JsonResult.success().addResult("map",map);
            }
            return JsonResult.fail("查询商家列表失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商家列表异常！");
        }
    }
    @ApiOperation(value = "收藏或取消收藏", notes = "收藏或取消收藏")
    @PostMapping("/collect")
    public JsonResult collect(@Valid @RequestBody AddCollectSellerRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            boolean res = sellerCollectAction.collectSeller(request);
            if(!res){
                return JsonResult.fail("操作失败！");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("操作异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "根据userId查询商家详情",notes = "返回收藏商家详情和经营商家详情")
    @PostMapping("/getSellerByUserId")
    public JsonResult getSellerByUserId(@Valid @RequestBody GetSellerByUserIdRequestDto request,BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            GetSellerByUserIdVo res= sellerFuseAction.getSellerByUserId(request);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询商家详情失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商家详情异常！");
        }
    }

    @ApiOperation(value = "根据UseuId查询经营、收藏商店数", notes = "根据UseuId查询经营、收藏商店数")
    @PostMapping("/getRelatedSellerMessage")
    public JsonResult getRelatedSellerMessage(String userId,HttpServletRequest requestDto) {
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            GetRelatedSellerMessageResponseVo res = sellerFuseAction.getMessage(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询经营、收藏商店数失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询经营、收藏商店数异常！");
        }
    }

    @ApiOperation(value = "根据UseuId查询现有注册的商店数、总注册过的商店数", notes = "根据UseuId查询现有注册的商店数、总注册过的商店数")
    @PostMapping("/getRegisterSellerMessage")
    public JsonResult getRegisterSellerMessage(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            GetRegisterSellerMessageResponseVo res = sellerAction.getMessage1(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询现有注册的商店数、总注册过的商店数失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询现有注册的商店数、总注册过的商店数异常！");
        }
    }

    @ApiOperation(value = "注销商家", notes = "注销商家，用户只能注销自己的商家")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DeleteSellerRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            if(StringUtils.isEmpty(request.getUserId())){
                try {
                    request.setUserId(getUserId(request.getUserId(),requestDto));
                    if(StringUtils.isEmpty(request.getUserId())){
                        return JsonResult.fail("用户id不能为空");
                    }
                }catch (Exception e){
                    return JsonResult.fail(e.getMessage());
                }
            }
            Boolean res = sellerAction.delete(request);
            if(!res){
                return JsonResult.fail("注销商家失败！");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("注销商家异常！");
        }
        return JsonResult.success();
    }
    @ApiOperation(value = "根据UseuId查询现有注册商家", notes = "根据UseuId查询现有注册商家")
    @PostMapping("/getSellersByUserId")
    public JsonResult getSellersByUserId(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            List<GetSellerByUserIdVo> res = sellerAction.getSellersByUserId(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询注册商家失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询注册商家异常！");
        }
    }

    @ApiOperation(value = "商家管理定时器1", notes = "判断商家注册后每30天是否发布商品，否扣减信用值5分")
    @PostMapping("/businessManagement")
    public JsonResult businessManagement(@Valid @RequestBody BusinessManagementRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res= sellerFuseAction.subtractCredit(request);
            if (!res){
                return JsonResult.fail("商家管理定时器1失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商家管理定时器1异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "商家管理定时器2", notes = "判断商家注册后90天内无成交记录或交易记录或支付记录，每90天扣减信用值5分")
    @PostMapping("/businessManagement1")
    public JsonResult businessManagement1(@Valid @RequestBody BusinessManagementRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res= sellerFuseAction.subtractCredit1(request);
            if (!res){
                return JsonResult.fail("商家管理定时器2失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商家管理定时器2异常！");
        }
        return JsonResult.success();
    }

/*
    @ApiOperation(value = "网商首页接口", notes = "根据用户经纬度查询附件商家商品及最新红包")
    @PostMapping("/businessHomePage")
    public JsonResult newBusinessHomePage(@Valid @RequestBody BusinessHomePageRequestDto request, HttpServletRequest servletRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        try {
            String userId = null;
            try {
                userId = getUserId(servletRequest);
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
            if (userId == null) {
                return JsonResult.fail("用户未登入，请登录");
            }
            BusinessHomePageResponseVo res= sellerFuseAction.getNearbyBusinessHomeData(request, userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("网商首页数据获取失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("网商首页数据获取异常！");
        }
    }*/
//
//    @ApiOperation(value = "网名首页的商家列表接口",notes = "根据累计成交额排序")
//    @PostMapping("/sellerListByDealAmount")
//    public JsonResult sellerListByDealAmount(@Valid @RequestBody PageRequestDto request,BindingResult bindingResult,HttpServletRequest requestDto){
//        if (bindingResult.hasErrors()) {
//            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
//        }
//        try{
//            BigDecimal lat = new BigDecimal(getLat(requestDto));
//            BigDecimal lon = new BigDecimal(getLon(requestDto));
//            List<GetSellerListResponseVo> list= sellerFuseAction.sellerListByDealAmount(request,lat,lon);
//            if (list != null){
//                return JsonResult.success().addResult("list",list);
//            }
//            return JsonResult.fail("查询商家列表失败！");
//        } catch (Exception e) {
//            logger.error(e.getMessage(),e);
//            return JsonResult.fail("查询商家列表异常！");
//        }
//    }



    @ApiOperation(value = "商家红包金额清空",notes = "点击一次在凌晨0点开始运行后24小时循环利用")
    @PostMapping("/startEmptySellerRedpacket")
    public void startEmptySellerRedpacket(@RequestBody @Valid String id){
        Long time= DateTimestampUtil.getStartOrEndOfTimestamp(new Date().getTime(),2);
//        businessQuartzService.emptySellerRedpacket(id,time,24*60*60,true);
        //TODO 定时任务
    }

    @ApiOperation(value = "商家红包金额清空定时器",notes = "商家红包金额清空定时器")
    @PostMapping("/emptySellerRedpacket")
    public JsonResult emptySellerRedpacket(){
        try{
            boolean res0=sellerAction.updateSellerRedpacket();
            boolean res=sellerAction.emptySellerRedpacket();
            if (res&&res0){
                return JsonResult.success();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商家红包金额清空定时失败！");
        }
        return JsonResult.fail("商家红包金额清空定时异常！");
    }

    @ApiOperation(value = "根据商家id查询商家详情", notes = "根据商家id查询商家详情，供跨域调用")
    @PostMapping("/getSellersBuyId")
    public JsonResult getSellersBuyId(@RequestBody GetSellersBuyIdRequestDto request) {
        if (request.getIds().size()<1) {
            return JsonResult.fail("商家id不能为空");
        }
        try {
            List<GetSellersBuyIdResponseVo> res= sellerAction.getSellersBuyId(request);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询商家详情失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商家详情异常！");
        }
    }


    @ApiOperation(value = "生成商家二维码", notes = "生成商家二维码")
    @PostMapping("/generateSellerQrcode")
    public JsonResult generateSellerQrcode(@RequestBody String sellerId) {
        if (sellerId==null) {
            return JsonResult.fail( "商家id不能为空");
        }
        try {
            sellerRegisterAction.generateSellerQrcode(sellerId);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("生成异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "自用户注册商家起后，24小时后未填写引荐人的客服代码，则执行这个服务")
    @PostMapping("/timingDoSeller")
    public JsonResult timingDoSeller(String sellerId){
        if(StringUtils.isEmpty(sellerId)){
            return JsonResult.fail("商家id不能为空");
        }
        try {
            if(!sellerRegisterFuseAction.timingDoSeller(sellerId)){
                return JsonResult.fail("客服代码执行失败");
            }
            return  JsonResult.success();
        }catch (Exception e){
            return JsonResult.fail("客服代码执行异常");
        }
    }

    @ApiOperation(value = "是否已注册过这个商家名")
    @PostMapping("/isHaveThisName")
    public JsonResult isHaveThisName(@RequestBody @Valid IsHaveThisNameRequestDto request,BindingResult bindingResult,HttpServletRequest requestDto){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {

            boolean res = sellerAction.isHaveThisName(request.getMerchantId(),request.getName());
            return JsonResult.success().addResult("res",res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("判断是否已注册过此商家名异常！");
        }

    }

    @ApiOperation(value = "注销用户在网商模块的相关数据")
    @PostMapping("/cancelBuyUserId")
    public JsonResult cancelBuyUserId(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            boolean res = sellerFuseAction.cancelBuyUserId(userId);
            if (!res){
                return JsonResult.fail("注销用户在网商模块的相关数据失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("注销用户在网商模块的相关数据异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "根据userId查询经营商家或收藏商家")
    @PostMapping("/getSellerListByUserId")
    public JsonResult getSellerListByUserId(@Valid @RequestBody GetListByUserIdDto getListByUserIdDto, HttpServletRequest request){
        UserGeo userGeo = new UserGeo();
        try{
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            List<GetSellerListResponseVo> res= sellerFuseAction.getMyManageSeller(getListByUserIdDto, userGeo.getLat(),userGeo.getLon());
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询商家失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商家异常！");
        }

    }
/*
    @ApiOperation(value = "这是为了补充数据,前端无需调用",notes = "test")
    @PostMapping(value = "test")
    public JsonResult test(){
        Wrapper<Seller> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id");
        wrapper.orderBy("createTime");
        List<String> ids = (List<String>)(List)sellerAction.selectObjs(wrapper);
        ids.forEach(id->{
            sellerAction.createBusinessNum(id);
        });
        return Result.newSuccess();
    }*/

//    @ApiOperation(value = "这是为了补充数据,前端无需调用",notes = "test")
//    @PostMapping(value = "test")
//    public JsonResult test(String userId,HttpServletRequest request){
//        try {
//            userId = getUserId(userId,request);
//            if(StringUtils.isEmpty(userId)){
//                return JsonResult.fail("用户id不能为空");
//            }
//            String res = productOrderFuseAction.getUserNumber(userId);
//            if (res != null){
//                return JsonResult.success().addResult("res",res);
//            }
//            return JsonResult.fail("查询商家失败！");
//        } catch (Exception e) {
//            logger.error(e.getMessage(),e);
//            
//            return JsonResult.fail("查询商家异常！");
//        }
//    }
}