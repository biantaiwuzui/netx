package com.netx.api.controller.ucenter.common;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.netx.common.common.utils.WeChatPayUtil;
import com.netx.common.common.utils.CommonXMLUtil;
import com.netx.common.pay.PayInfoHolder;
import com.netx.common.pay.WeChatWithdrawInfo;
import com.netx.ucenter.biz.common.BillAction;
import com.netx.ucenter.biz.common.WalletAction;
import com.netx.ucenter.model.common.CommonBill;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.utils.money.Money;
import com.netx.utils.xml.XMLUtil;
import org.apache.commons.lang.StringUtils;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Create by wongloong on 17-9-24
 */
@RestController
@RequestMapping("/callback")
public class PayCallbackController{
    private Logger logger = LoggerFactory.getLogger(PayCallbackController.class);

    @Autowired
    private BillAction billAction;

    @Autowired
    private WalletAction walletAction;

    @Autowired
    private PayInfoHolder payInfoHolder;

    @Autowired
    private CommonServiceProvider commonServiceProvider;


    /**
     * 阿里支付回调方法
     *
     * @param request
     * @return
     */
    @PostMapping("/ali")
    public String aliPayCallback(HttpServletRequest request) {
        try {
            logger.info("------------------------------------pc异步通知-------------------------------------------");
            Map<String,String> params = new HashMap<String,String>();
            Map<String,String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化。
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
                params.put(name, valueStr);
            }
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            //https://api.xx.com/receive_notify.htm?total_amount=2.00&buyer_id=2088102116773037&body=大乐透2.1&trade_no=2016071921001003030200089909&refund_fee=0.00&notify_time=2016-07-19 14:10:49&subject=大乐透2.1&sign_type=RSA2&charset=utf-8&notify_type=trade_status_sync&out_trade_no=0719141034-6418&gmt_close=2016-07-19 14:10:46&gmt_payment=2016-07-19 14:10:47&trade_status=TRADE_SUCCESS&version=1.0&sign=kPbQIjX+xQc8F0/A6/AocELIjhhZnGbcBN6G4MM/HmfWL4ZiHM6fWl5NQhzXJusaklZ1LFuMo+lHQUELAYeugH8LYFvxnNajOvZhuxNFbN2LhF0l/KL8ANtj8oyPM4NN7Qft2kWJTDJUpQOzCzNnV9hDxh5AaT9FPqRS6ZKxnzM=&gmt_create=2016-07-19 14:10:44&app_id=2015102700040153&seller_id=2088102119685838&notify_id=4a91b7a78a503640467525113fb7d8bg8e
            boolean flag = AlipaySignature.rsaCheckV1(params,payInfoHolder.getAliPublicKey(), "utf-8", "RSA2");
            logger.info("------------------------------------------支付宝异步通知页面验证成功：trade_finished------------------------------verify_result="+flag);
            if (flag) {
                logger.info("阿里支付验证成功");
                logger.info(request.toString());
                //订单号
                String outTradeNo = request.getParameter("out_trade_no");
                CommonBill bill = commonServiceProvider.getBillService().selectById(outTradeNo);
                String tradeNo = request.getParameter("trade_no");
//                String totalAmount = request.getParameter("total_amount");
//                String requestAppId = request.getParameter("app_id");
//                String sellerId = request.getParameter("seller_id");
                String tradeStatus = request.getParameter("trade_status");
                if (tradeStatus.equals("TRADE_SUCCESS")) {
                    bill.setToAccount(1);
                    AlipayClient alipayClient = new DefaultAlipayClient(payInfoHolder.getOpenUrl(),payInfoHolder.getAppId(),payInfoHolder.getPrivateKey(), "json", "utf-8",payInfoHolder.getAliPublicKey(), "RSA2");
                    AlipayTradeQueryRequest requestDto = new AlipayTradeQueryRequest();
                    AlipayTradeQueryModel model=new AlipayTradeQueryModel();
                    model.setOutTradeNo(outTradeNo);
                    model.setTradeNo(tradeNo);
                    requestDto.setBizModel(model);
                    AlipayTradeQueryResponse response = alipayClient.execute(requestDto);
                    if(response.isSuccess()){
                        if(response.getTradeStatus().equals("TRADE_SUCCESS")){
                            String buyLoginId=response.getBuyerLogonId();//这个是买家支付者账号，有*加密了的
                            String buyerUserId=response.getBuyerUserId();
                            logger.info("\n" +
                                    "========================================############支付宝充值:buyer_user_id："+buyerUserId+",buyer_login_id:"+buyLoginId+"\r\n========================================");
                            if(StringUtils.isNotBlank(buyLoginId)){
                                bill.setDescription("支付宝账号:"+buyLoginId+"向平台充值"+getYuan(bill.getAmount().longValue()).toString()+"元");
                                walletAction.changeAliUsedAccounts(bill.getUserId(),buyerUserId,buyLoginId);
                            }
                        }
                        logger.info("调用成功");
                    } else {
                        logger.info("调用失败");
                    }
                }else{
                    bill.setDescription(bill.getDescription()+"支付失败");
                }
                bill.setThirdBillId(tradeNo);
                bill.setUpdateTime(new Date());
                logger.info("回调数据:"+bill.toString());
                billAction.notifySuccess(bill,0);
                return "success";
            } else {
                logger.info("阿里支付验证失败");
                return "failure";
            }
        } catch (AlipayApiException e) {
            logger.error("阿里支付验证异常", e);
            return "failure";
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return "failure";
        }
    }

    public BigDecimal getYuan(Long cent){
        return Money.CentToYuan(cent).getAmount();
    }

    @RequestMapping("/wechat")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException {
        try {
            //读取参数
            InputStream inputStream;
            //StringBuffer sb = new StringBuffer();
            inputStream = request.getInputStream();
            String sb = "";
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                //sb.append(s);
                sb+=s;
            }
            in.close();
            inputStream.close();
            //解析xml成map
            //Map<String, String> m;
            logger.error("微信充值："+sb);
            Map<String, String> m = CommonXMLUtil.doXMLParse(sb);
            logger.error("commonXmlMap："+m);
            try {
                WeChatWithdrawInfo weChatWithdrawInfo = XMLUtil.fromXML(sb, WeChatWithdrawInfo.class);
                logger.error("commonXml："+weChatWithdrawInfo);
            }catch (Exception e){
                logger.warn(e.getMessage());
            }
            //过滤空 设置 TreeMap
            SortedMap<Object, Object> packageParams = new TreeMap<>();
            Iterator it = m.keySet().iterator();
            while (it.hasNext()) {
                String parameter = (String) it.next();
                String parameterValue = m.get(parameter);
                String v = "";
                if (null != parameterValue) {
                    v = parameterValue.trim();
                }
                packageParams.put(parameter, v);
            }
            //判断签名是否正确
            String resXml = "";
            logger.info("（------------++微信支付++------------）");
            if (WeChatPayUtil.isTenpaySign("UTF-8", packageParams, payInfoHolder.getWechatApiKey())) {
                if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                    // 这里是支付成功
                    logger.info("支付成功++++");
                    String mch_id = (String) packageParams.get("mch_id"); //商户号
                    String openId = (String) packageParams.get("openid");  //用户标识
                    String out_trade_no = (String) packageParams.get("out_trade_no"); //商户订单号
                    String total_fee = (String) packageParams.get("total_fee");
                    String transaction_id = (String) packageParams.get("transaction_id"); //微信支付订单号
                    CommonBill bill = commonServiceProvider.getBillService().selectById(out_trade_no);
                    if ((StringUtils.isNotBlank(mch_id) && !payInfoHolder.getWechatMchId().equals(mch_id)) || StringUtils.isBlank(bill.getId()) || (total_fee!=null && Long.parseLong(total_fee) != bill.getAmount())) {
                        bill.setUpdateTime(new Date());
                        bill.setThirdBillId(transaction_id);
                        bill.setDescription(bill.getDescription() + "失败：参数错误");
                        commonServiceProvider.getBillService().updateById(bill);
                        logger.info("支付失败,错误信息：" + "参数错误");
                        resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                                + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                    } else {
                        if (bill.getToAccount() == 0 && bill.getTradeType() == 1) {
                            logger.info(bill.toString());
                            bill.setToAccount(1);
                            bill.setDescription("微信充值"+ getYuan( bill.getAmount()).toString()+"元成功");
                            bill.setUpdateTime(new Date());
                            bill.setThirdBillId(transaction_id);
                            billAction.notifySuccess(bill, 0);
                            walletAction.changeWechatUsedAccounts(bill.getUserId(),bill.getBak1(),openId);
                            logger.info("回调");
                            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

                        } else {
                            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                            logger.info("订单已处理");
                        }
                    }

                } else {
                    String out_trade_no = (String) packageParams.get("out_trade_no"); //商户订单号
                    String transaction_id = (String) packageParams.get("transaction_id"); //微信支付订单号
                    CommonBill bill = commonServiceProvider.getBillService().selectById(out_trade_no);
                    bill.setDescription(bill.getDescription() + "失败：报文为空");
                    bill.setThirdBillId(transaction_id);
                    bill.setUpdateTime(new Date());
                    commonServiceProvider.getBillService().updateById(bill);
                    logger.info("支付失败,错误信息：#0", packageParams.get("err_code"));
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                }


            } else {
                String out_trade_no = (String) packageParams.get("out_trade_no"); //商户订单号
                String transaction_id = (String) packageParams.get("transaction_id"); //微信支付订单号
                CommonBill bill = commonServiceProvider.getBillService().selectById(out_trade_no);
                bill.setDescription(bill.getDescription() + "失败：微信签名验证失败");
                bill.setThirdBillId(transaction_id);
                bill.setUpdateTime(new Date());
                commonServiceProvider.getBillService().updateById(bill);
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
                logger.info("通知签名验证失败");
            }
            BufferedOutputStream out = new BufferedOutputStream(
                    response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    /*@PostMapping("/text")
    public void aliOauthCallBack(HttpServletRequest request) throws  Exception{
        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        logger.info("&&&&&^^^^^^^^^^^^^^%%%%%%%%%%%%%%%%%%%%$$$$$$$$$$$$$$$$$@@@@@@@@@@@@@@@@@@@@@@@");
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //支付宝用户号
        String app_id = new String(request.getParameter("app_id").getBytes("ISO-8859-1"),"UTF-8");
        logger.info(app_id + "\n");
        //获取第三方登录授权
        String alipay_app_auth = new String(request.getParameter("source").getBytes("ISO-8859-1"),"UTF-8");
        logger.info(alipay_app_auth + "\n");
        //第三方授权code
        String app_auth_code = new String(request.getParameter("app_auth_code").getBytes("ISO-8859-1"),"UTF-8");//获的第三方登录用户授权app_auth_code
        logger.info(app_auth_code + "\n");

        //使用auth_code换取接口access_token及用户userId
        //AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","应用APPID",privateKey,"json","UTF-8",publicKey,"RSA2");//正常环境下的网关
        AlipayClient alipayClient = new DefaultAlipayClient(openUrl,appId,privateKey,"json","UTF-8",aliPublicKey,"RSA2");
        AlipayOpenAuthTokenAppRequest requestLogin1 = new AlipayOpenAuthTokenAppRequest();
        requestLogin1.setBizContent("{" +
                "\"grant_type\":\"authorization_code\"," +
                "\"code\":\""+ app_auth_code +"\"" +
                "}");
        //第三方授权
        AlipayOpenAuthTokenAppResponse responseToken = alipayClient.execute(requestLogin1);
        if(responseToken.isSuccess()){
            logger.info("<br/>调用成功" + "\n");
            logger.info(responseToken.getAuthAppId() + "\n");
            logger.info(responseToken.getAppAuthToken() + "\n");
            logger.info(responseToken.getUserId() + "\n");
        } else {
            logger.info("调用失败" + "\n");
        }
    }*/
}