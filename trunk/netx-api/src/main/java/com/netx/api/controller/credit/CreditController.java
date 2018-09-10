package com.netx.api.controller.credit;

import cn.jiguang.common.utils.StringUtils;
import com.netx.api.controller.BaseController;
import com.netx.common.vo.business.SelectSellerListByInPublicCreditDto;
import com.netx.credit.biz.CreditAction;
import com.netx.credit.vo.BeforPublishVo;
import com.netx.credit.vo.CreditSubscriptionDto;
import com.netx.credit.vo.PublishCreditRequestDto;
import com.netx.fuse.biz.credit.CreditFuseAction;
import com.netx.fuse.biz.credit.CreditSubscriptionFuseAction;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantFuseAction;
import com.netx.shopping.vo.MerchantListInPublishCreditVo;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author lanyingchu
 * @date 2018/7/6 16:52
 */

@RestController
@RequestMapping("/api/credit")
@Api(description = "发行网信模块")
public class CreditController extends BaseController {

    @Autowired
    private CreditFuseAction creditFuseAction;
    @Autowired
    private MerchantFuseAction merchantFuseAction;
    @Autowired
    private CreditSubscriptionFuseAction creditSubscriptionFuseAction;
    @Autowired
    private CreditAction creditAction;

    private Logger logger = LoggerFactory.getLogger(CreditController.class);

    @ResponseBody
    @PostMapping("publishCredit")
    @ApiOperation(value = "发行网信/编辑网信")
    public JsonResult publishCredit(@Valid @RequestBody PublishCreditRequestDto dto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (dto.getName().matches("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]")){
            return JsonResult.fail("网信名称不能输入表情");
        }
        try {
            String userId = getUserId(request);
            if (StringUtils.isEmpty(userId)) {
                return JsonResult.fail("授权过期，请重新登录");
            }
            Map<String, String> result = creditFuseAction.publishCredit(dto, userId);
            for (Map.Entry<String, String> entry : result.entrySet()) {
                if (!("creditId").equals(entry.getKey())) {
                    return JsonResult.fail(entry.getValue());
                }
                if (entry.getValue() == null) {
                    return JsonResult.fail("发行/编辑网信失败");
                }
                return JsonResult.success().addResult("creditId", entry.getValue());
            }
            return JsonResult.fail("发行/编辑网信异常");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }
    }

    /**
     * 取消所申请的网信
     */
    @ApiOperation(value = "取消所申请的网信", notes = "取消预售者发行的网信")
    @GetMapping("/deleteCredit")
    public JsonResult deleteCredit(String creditId, HttpServletRequest request) {
        if (StringUtils.isEmpty(creditId)) {
            return JsonResult.fail("该网信异常");
        }
        try {
            String userId = getUserId(request);
            if (StringUtils.isEmpty(userId)) {
                return JsonResult.fail("授权过期，请重新登录");
            }
            Boolean result = creditAction.deleteCredit(creditId);
            if (result) {
                return JsonResult.success("取消成功");
            } else {
                return JsonResult.fail("取消失败了呜呜呜");
            }
        } catch (Exception e) {
            logger.error(e.getMessage() + creditId,e);
            return JsonResult.fail("删除网信异常");
        }
    }


    // 是否完成加盟（pay_status = 0 and is_support_credit = true)
    // 无排序
    /**
     * 获取加盟平台的商家（适用范围)
     */
    @ApiOperation(value = "商家列表", notes = "仅查询完成缴费以及支持网信（当前开发服没有符合条件，暂时查询不支持网信 + 没有加盟 false + 1）")
    @PostMapping("/selectSellerList")
    public JsonResult selectSellerListByDistanceAndTime(@Valid @RequestBody SelectSellerListByInPublicCreditDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<MerchantListInPublishCreditVo> merchantListInPublishCreditVo = merchantFuseAction.getMerchantList(requestDto);
            if (merchantListInPublishCreditVo != null) {
                return JsonResult.success().addResult("merchantListInPublishCreditVo", merchantListInPublishCreditVo);
            }
            return JsonResult.fail("查询商家列表失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询商家列表异常！");
        }
    }
//
//    @ApiOperation(value = "邀请好友参加内购",notes = "根据credit表Id,common_friend表friend邀请好友")
//    @PostMapping(value = "/inviteFriends")
//    public JsonResult inviteFriends(String creditId,String friends,HttpServletRequest request){
//        if(friends == null){
//            return JsonResult.fail("好友Id不能为空");
//        }
//        UserGeo userGeo = null;
//        try {
//            userGeo=getGeoFromRequest(request);
//
//        }catch (Exception e){
//            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
//        }
//        try{
//            boolean success = false;
//            String cr = creditService.selectById(creditId).getId();
//            if (friendsAction.isFriend(userGeo.getUserId(),friends)){
//            success = creditInvitationFuseAction.getInviteFriends(cr,userGeo.getUserId(),friends);
//            return JsonResult.success().addResult("邀请好友",success);
//            }
//            return JsonResult.fail("邀请者不是你的好友");
//        }catch (Exception e){
//            return JsonResult.fail(e.getMessage());
//        }
//    }


    /**
     * 参加认购
     */
    @ApiOperation(value = "参加认购（包括内购，开放认购)")
    @PostMapping(value = "/agreeOrReject")
    public JsonResult agreeOrReject(@Valid @RequestBody CreditSubscriptionDto dto, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String userId = getUserId(request);
            if (StringUtils.isEmpty(userId)) {
                return JsonResult.fail("授权过期，请重新登录");
            }
            String detialId = creditSubscriptionFuseAction.subscriptionOrAgree(dto);
            if (detialId != null) {
                return JsonResult.success().addResult("detialId", detialId);
            }
            return JsonResult.fail("认购失败");
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }
    }

    /**
     * 获取用户所在商家
     */
    @ApiOperation(value = "获取用户所在商家及其发行上限金额(发行网信前)")
    @PostMapping(value = "/getUserMerchant")
    public JsonResult getUserMerchant(BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String userId = getUserId(request);
            if (StringUtils.isEmpty(userId)) {
                return JsonResult.fail("授权过期，请重新登录");
            }
            List<BeforPublishVo> beforPublishVoList = creditFuseAction.getUserMerchantAndLimit(userId);
            if (beforPublishVoList.size() > 0) {
                return JsonResult.success().addResult("beforPublishVoList", beforPublishVoList);
            } else {
                return JsonResult.fail("您不能发行网信");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }
    }
}


