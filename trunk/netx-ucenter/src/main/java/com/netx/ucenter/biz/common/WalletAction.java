package com.netx.ucenter.biz.common;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.netx.common.common.utils.WeChatPayUtil;
import com.netx.common.pay.PayInfoHolder;
import com.netx.common.pay.WeChatInfo;
import com.netx.common.pay.WeChatResponse;
import com.netx.common.vo.common.*;
import com.netx.ucenter.model.common.CommonBill;
import com.netx.ucenter.model.common.CommonWallet;
import com.netx.ucenter.model.user.UserPayAccount;
import com.netx.ucenter.service.common.BillService;
import com.netx.ucenter.service.common.WalletService;
import com.netx.ucenter.service.user.UserPayAccountService;
import com.netx.utils.money.Money;
import com.netx.utils.uri.HttpUtil;
import com.netx.utils.xml.XMLUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.text.MessageFormat;
import java.util.*;

/**
 * Create by wongloong on 17-9-17
 */
@Service
public class WalletAction{
    private Logger logger = LoggerFactory.getLogger("WalletAction");

    @Autowired
    WalletService walletService;
    @Autowired
    BillAction billAction;
    @Autowired
    private BillService billService;
    @Autowired
    private PayInfoHolder payInfoHolder;
    @Autowired
    private UserPayAccountService userPayAccountService;

    public void modifyAccount(String userId, WalletModifyAccountRequestDto request) throws Exception {
        if(StringUtils.isNotEmpty( request.getAliAccountId())){
            userPayAccountService.modifyAccount(userId,request.getAliAccountId());
        }
        if(StringUtils.isNotEmpty(request.getWechatAccountId())){
            userPayAccountService.modifyAccount(userId,request.getWechatAccountId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void pay(String userId, String bizId, String toUserId,long amount,Integer type,String description,Integer payChannel){
        CommonBill commonBill = new CommonBill();
        commonBill.setAmount(amount);
        commonBill.setType(type);
        commonBill.setDescription(description);
        commonBill.setTradeType(0);
        commonBill.setBak1(toUserId);
        commonBill.setPayChannel(payChannel);
        commonBill.setThirdBillId(bizId);
        payOne(userId,-amount,commonBill);
        commonBill.setId(null);
        commonBill.setTradeType(1);
        commonBill.setBak1(userId);
        payOne(toUserId,amount,commonBill);
    }

    @Transactional(rollbackFor = Exception.class)
    void payOne(String userId,long amount,CommonBill commonBill){
        CommonWallet commonWallet = queryWalletByUserId(userId);
        commonWallet.setTotalAmount(commonWallet.getTotalAmount()+amount);
        walletService.updateById(commonWallet);
        commonBill.setCreateUserId(userId);
        commonBill.setWallerId(commonWallet.getId());
        commonBill.setTotalAmount(commonWallet.getTotalAmount());
        billService.insert(commonBill);
    }

    public String modifyAliAccount(String userId,WalletModifyAliAccountRequestDto request) throws Exception {
        return this.modifyAccount(userId, request.getAccountId());
    }

    /**
     * 修改提现账号
     * @param userId
     * @param accountId 可以是支付宝列表id, 也可以是微信账号列表id
     * @return
     * @throws Exception
     */
    private String modifyAccount(String userId , String accountId) throws  Exception{
        if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(accountId)) {
            queryWalletByUserId(userId);
            return userPayAccountService.modifyAccount(userId,accountId);
        }
        return "修改提现账号传参异常";
    }

    public String modifyWechatAccount(String userId,WalletModifyWechatAccountRequestDto request) throws Exception {
        return this .modifyAccount(userId, request.getAccountId());
    }

    public WalletFindAccountResponseVo findAccount(String userId) throws Exception {
        CommonWallet wzCommonWallet = queryWalletByUserId(userId);
        WalletFindAccountResponseVo vo=new WalletFindAccountResponseVo();
        vo.setUserId(userId);
        vo.setAliAccount( userPayAccountService.getBindAccount(userId,2));//获取支付宝的绑定账号
        vo.setWechatAccount(userPayAccountService.getBindAccount(userId,1));//获取微信的绑定账号
        Map<String,String> map=new HashMap<>();
        List<UserPayAccount> aliAccounts=userPayAccountService.getCommonAccounts(userId,2);
        for (UserPayAccount account:aliAccounts){
            map.put(account.getAccountDisplay(),account.getId());// key=显示的账号  value：主键id
        }
        vo.setUsedAliAccount(map);
        map=new HashMap<>();
        List<UserPayAccount> wechatAccounts=userPayAccountService.getCommonAccounts(userId,1);
        for (UserPayAccount account:wechatAccounts){
            map.put(account.getAccountDisplay(),account.getId());// key=显示的账号  value：主键id
        }
        vo.setUsedWechatAccount(map);
        return vo;
    }

    /**
     * 查询用户钱包【不存在则开通用户钱包】
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonWallet queryWalletByUserId(String userId){
        CommonWallet wzCommonWallet = walletService.selectByUserId(userId);
        if (wzCommonWallet == null) {
            wzCommonWallet = new CommonWallet();
            wzCommonWallet.setCreateTime(new Date());
            wzCommonWallet.setUserId(userId);
            wzCommonWallet.setTotalAmount(0l);
            wzCommonWallet.setVsn(0);
            if(!walletService.insert(wzCommonWallet)){
                throw new RuntimeException("开通钱包失败");
            }
        }
        return wzCommonWallet;
    }

    /**
     * 提现
     * @param userId
     * @param requestDto
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public String withdraw(String userId,WalletWithdrawRequestDto requestDto,CommonWallet wallet) throws Exception{
        UserPayAccount account = userPayAccountService.selectById(requestDto.getAccountId());//提现绑定的账号
        Boolean flag = requestDto.getType() == 0;
        if(account==null){
            return flag?"请先设置支付宝提现账号":"未设置微信提现账号";
        }
        if(flag){
            if(account.getAccountType()!=2){
                return "提现账号有误";
            }
            return aliWithdraw(userId, requestDto,account);
        }else{
            if(account.getAccountType()!=1){
                return "提现账号有误";
            }
            return weChatWithdraw(userId, requestDto,account,wallet);
        }
    }

    /**
     * 支付宝提现
     * @param userId
     * @param requestDto
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = RuntimeException.class)
    String aliWithdraw(String userId,WalletWithdrawRequestDto requestDto,UserPayAccount account){
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(payInfoHolder.getOpenUrl(), payInfoHolder.getAppId(), payInfoHolder.getPrivateKey(), "json", "utf-8", payInfoHolder.getAliPublicKey(), "RSA2");
            AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
            CommonBill bill = new CommonBill();
            bill.setCreateTime(new Date());
            bill.setPayChannel(0);//支付宝
            bill.setTradeType(0);//支出
            bill.setAmount(getCent(requestDto.getAmount()));
            bill.setDescription(new MessageFormat("支付宝账号:{0}从平台提现{1}元").format(new String[]{account.getAccountDisplay(),requestDto.getAmount().toString()}));
            bill.setUserId(userId);
            bill.setToAccount(0);
            bill.setType(0);
            Boolean billInsertFlag = billService.insert(bill);
            logger.info(billInsertFlag.toString());
            request.setBizContent("{" +
                    "    \"out_biz_no\":\"" + bill.getId() + "\"," +
                    "    \"payee_type\":\"" + "ALIPAY_USERID" + "\"," +
                    "    \"payee_account\":\"" + account.getAccountIdentity() + "\"," +
                    "    \"amount\":\"" + requestDto.getAmount().toString() + "\"," +
                    "    \"payer_show_name\":\"网值提现\"," +
                    // "    \"payee_real_name\":\"网值\"," +
                    "    \"remark\":\"\"," +
                    "  }");
            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                bill.setToAccount(1);//已到帐
                billAction.notifySuccess(bill,1);
                logger.info("转账成功");
                return null;
            } else {
                bill.setDescription(bill.getDescription()+"失败");
                billService.updateById(bill);
                logger.info("转账失败");
                logger.warn("转账异常,订单id:#0,errMsg:#1", bill.getId(), response.getSubMsg());
                return response.getSubMsg();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 微信提现
     * @param userId
     * @param requestDto
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    String weChatWithdraw(String userId,WalletWithdrawRequestDto requestDto,UserPayAccount account,CommonWallet wallet) throws Exception{
        String result = "提现失败";
        if(requestDto.getCent()<100){
            return result+"，原因：最低提现1元";
        }
        if(requestDto.getCent()>2000000){
            return result+"，原因：每次提现不能超过20000元";
        }
        CommonBill bill = new CommonBill();
        bill.setCreateTime(new Date());
        bill.setPayChannel(1);//微信
        bill.setTradeType(0);//支出
        bill.setAmount(requestDto.getCent());
        bill.setDescription(new MessageFormat("微信账号昵称为:{0}从平台提现{1}元").format(new String[]{account.getAccountDisplay(),requestDto.getAmount().toString()}));
        bill.setUserId(userId);
        bill.setToAccount(0);
        bill.setType(0);
        billService.insert(bill);

        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("mch_appid", payInfoHolder.getWechatAppId());
        parameters.put("mchid", payInfoHolder.getWechatMchId());
        parameters.put("nonce_str", WeChatPayUtil.CreateNoncestr());
        parameters.put("partner_trade_no", bill.getId());
        parameters.put("openid", account.getAccountIdentity()); //订单id
        parameters.put("check_name", "NO_CHECK");
        parameters.put("amount", requestDto.getCent());
        parameters.put("desc", "网值平台提现");
        parameters.put("spbill_create_ip", payInfoHolder.getHost());

        String sign = WeChatPayUtil.createSign("UTF-8", parameters, payInfoHolder.getWechatApiKey());
        parameters.put("sign", sign);
        //封装请求参数结束
        String requestXML = WeChatPayUtil.getRequestXml(parameters);
        //指定读取证书格式为PKCS12
        ClassLoader classLoader = getClass().getClassLoader();
        char[] wechatCharArray = payInfoHolder.getWechatMchId().toCharArray();
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //指定PKCS12的密码(商户ID)
        keyStore.load(classLoader.getResourceAsStream("cert/apiclient_cert.p12"),wechatCharArray );
        SSLConnectionSocketFactory sslsf=HttpUtil.getSSLConnectionSocketFactory(keyStore,wechatCharArray);
        StringEntity stringEntity = getStringEntity(requestXML);
        WeChatInfo weChatInfo = HttpUtil.doPostWeChat(payInfoHolder.getWechatWithdrawUrl(),sslsf,stringEntity,WeChatInfo.class);
        if(weChatInfo!=null){
            if(weChatInfo.getReturn_code().equals("SUCCESS")){
                if(weChatInfo.getResult_code().equals("SUCCESS")){
                    bill.setToAccount(1);//已到帐
                    bill.setDescription(bill.getDescription()+"成功");
                    bill.setThirdBillId(weChatInfo.getPayment_no());
                    billAction.notifySuccess(bill,1);
                    return null;
                }else {
                    result="提现失败，原因："+weChatInfo.getErr_code_des();
                    bill.setDescription(bill.getDescription()+"失败");
                    billService.updateById(bill);
                }
            }
        }
        return result;
        /*
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, wechatCharArray).build();
        //指定TLS版本
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, new String[]{"TLSv1"}, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        //设置httpclient的SSLSocketFactory
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            HttpPost httpget = new HttpPost(payInfoHolder.getWechatWithdrawUrl());
            //请求的xml需转码为iso8859-1编码，否则易出现签名错误或红包上的文字显示有误
            StringEntity stringEntity = new StringEntity(new String(requestXML.getBytes(), "ISO8859-1"));//param参数，可以为"key1=value1&key2=value2"的一串字符串
            //stringEntity.setContentType("application/x-www-form-urlencoded");
            httpget.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpget.setEntity(stringEntity);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    //获取微信提现返回参数
                    WeChatInfo weChatInfo = getWeChatInfo(entity);
                    logger.info(new Date()+":"+weChatInfo);
                    if(weChatInfo!=null){
                        if(weChatInfo.getReturn_code().equals("SUCCESS")){
                            if(weChatInfo.getResult_code().equals("SUCCESS")){
                                bill.setToAccount(1);//已到帐
                                billAction.notifySuccess(bill,1);
                                result=null;
                            }else {
                                result="提现失败，原因："+weChatInfo.getErr_code_des();
                            }
                        }
                    }
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        }finally {
            httpclient.close();
        }
        return result;*/
    }

    private StringEntity getStringEntity(String requestXML) throws Exception{
        return new StringEntity(new String(requestXML.getBytes(), "ISO8859-1"));
    }

    /**
     * 解析微信提现返回信息
     * @param entity
     * @return
     */
    private WeChatInfo getWeChatInfo(HttpEntity entity) throws Exception{
        logger.info("----------------------------------------");
        logger.info("Response content length: " + entity.getContentLength());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String text;
        while ((text = bufferedReader.readLine()) != null) {
            stringBuilder.append(text);
        }
        //获取微信提现返回参数
        return XMLUtil.fromXML(stringBuilder.toString(),WeChatInfo.class);
    }

    public void modifyPassword(String userId,WalletModifyPasswordRequestDto request) throws Exception {
        CommonWallet wzCommonWallet = queryWalletByUserId( userId );
        if (StringUtils.isEmpty(wzCommonWallet.getId())) {
            throw new Exception("开通钱包系统异常");
        }
        wzCommonWallet.setPassword(DigestUtils.md5DigestAsHex(request.getNewPassword().getBytes()));
        walletService.updateById(wzCommonWallet);
    }

    public BigDecimal getAmount(String userId) {
        CommonWallet wzCommonWallet = queryWalletByUserId(userId);
        return getMoney(wzCommonWallet.getTotalAmount());
    }

    public ThirdPayResponse notifyAliClient(String userId,WalletRechargeRequestDto requestDto) throws Exception {
/*        //判断平台用户钱包是否足够金额
        boolean r=this.checkIsEnough("999",requestDto.getPayAmount());
        if(!r) {
            throw new Exception("平台余额不足，充值失败");
        }*/
        BigDecimal commonBill = billService.countThisDayOutcome(userId,1);
        BigDecimal billCount = commonBill.add(requestDto.getPayAmount ().multiply ( BigDecimal.valueOf ( 100 ) ));
        BigDecimal count = new BigDecimal ( 10000000 );
        if( billCount.compareTo ( count ) > 0 ){
            throw new RuntimeException ( "今日充值金额已达或与此次提现金额相加超出10万元,今日不能再充值." );
        }
        ThirdPayResponse thirdPayResponse = new ThirdPayResponse();
        String result = "";
        AlipayClient alipayClient = new DefaultAlipayClient(payInfoHolder.getOpenUrl(), payInfoHolder.getAppId(), payInfoHolder.getPrivateKey(), "json", "utf-8", payInfoHolder.getAliPublicKey(), "RSA2");
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("网值支付宝充值" + requestDto.getPayAmount() + "元");
        model.setSubject("网值充值");
//        String id = UUID.randomUUID().toString();
        String billId = addBillAndReturnBillId(userId,requestDto, 1);
        model.setOutTradeNo(billId);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(requestDto.getPayAmount().toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(payInfoHolder.getNotifyUrl());
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        //就是orderString 可以直接给客户端请求，无需再做处理。
        logger.info(response.getBody());
        result = response.getBody();
        thirdPayResponse.setOrderStr(result);
        return thirdPayResponse;
    }

    public boolean checkIsEnough(String userId,BigDecimal amount){
        CommonWallet wzCommonWallet= queryWalletByUserId(userId);
        if (wzCommonWallet.getTotalAmount()<getCent(amount)){
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> notifyWechatClient(String userId,WalletRechargeRequestDto requestDto, HttpServletRequest request){
/*        //判断平台用户钱包是否足够金额
        boolean r=this.checkIsEnough("999",requestDto.getPayAmount());
        if(!r) {
            throw new Exception("平台余额不足，充值失败");
        }*/
        BigDecimal commonBill = billService.countThisDayOutcome(userId,1);
        BigDecimal billCount = commonBill.add (requestDto.getPayAmount ().multiply ( BigDecimal.valueOf ( 100 ) ));
        BigDecimal count = new BigDecimal ( 10000000 );
        if( billCount.compareTo ( count )> 0 ) {
            throw new RuntimeException ( "今日充值金额已达或与此次充值金额相加超出10万元,今日不能再充值." );
        }
        String billId = addBillAndReturnBillId(userId,requestDto, 0);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        String appid = payInfoHolder.getWechatAppId();
        String mch_id = payInfoHolder.getWechatMchId();
        parameters.put("appid", appid);//应用ID
        parameters.put("mch_id", mch_id);//商户号
        parameters.put("nonce_str", WeChatPayUtil.CreateNoncestr());//随机字符串
        parameters.put("body", "网值微信充值");//商品描述
        parameters.put("out_trade_no", billId); //订单id
        parameters.put("fee_type", "CNY");
        long cent = new Money(requestDto.getPayAmount()).getCent();
        if(cent<=0){
            resultMap.put("msg","付款金额错误");
            resultMap.put("code","500");
            return resultMap;
        }
        parameters.put("notify_url", payInfoHolder.getWechatNotifyUrl());
        parameters.put("trade_type", "APP");
        parameters.put("total_fee", (int)cent);
        parameters.put("spbill_create_ip", WeChatPayUtil.toIpAddr(request));
        //设置签名
        String sign = WeChatPayUtil.createSign("UTF-8", parameters, payInfoHolder.getWechatApiKey());
        parameters.put("sign", sign);
        //封装请求参数结束
        String requestXML = WeChatPayUtil.getRequestXml(parameters);
        //调用统一下单接口
        logger.info("请求信息："+parameters);
        String result = WeChatPayUtil.httpsRequest(payInfoHolder.getWechatOrderUrl(), "POST", requestXML);
        logger.info("\n" + result+"======");
            /**统一下单接口返回正常的prepay_id，再按签名规范重新生成签名后，将数据传输给APP。参与签名的字段名为appId，partnerId，prepayId，nonceStr，timeStamp，package。注意：package的值格式为Sign=WXPay**/
            //Map<String, String> map = CommonXMLUtil.doXMLParse(result);
        WeChatResponse weChatResponse = XMLUtil.fromXML(result,WeChatResponse.class);
        logger.info(weChatResponse+"*********");
        if(weChatResponse.getReturn_code()!=null && !weChatResponse.getReturn_code().equals("SUCCESS")){
            throw new RuntimeException(weChatResponse.getReturn_msg());
        }
        SortedMap<Object, Object> parameterMap2 = new TreeMap<Object, Object>();
        parameterMap2.put("appid", appid);
        parameterMap2.put("partnerid", mch_id);
        parameterMap2.put("prepayid", weChatResponse.getPrepay_id());
        parameterMap2.put("package", "Sign=WXPay");
        parameterMap2.put("noncestr", WeChatPayUtil.CreateNoncestr());
        //本来生成的时间戳是13位，但是ios必须是10位，所以截取了一下
        long time = System.currentTimeMillis()/1000;
        parameterMap2.put("timestamp", time+"");
        String sign2 = WeChatPayUtil.createSign("UTF-8", parameterMap2, payInfoHolder.getWechatApiKey());
        parameterMap2.put("sign", sign2);
        resultMap.put("code", "200");
        resultMap.put("msg", parameterMap2);
        logger.info(resultMap+"//*****//");
        return resultMap;
    }

    @Transactional(rollbackFor = Exception.class)
    String addBillAndReturnBillId(String userId,WalletRechargeRequestDto requestDto, int payType) {
        CommonBill bill = new CommonBill();
        bill.setUserId(userId);
        bill.setTradeType(1);
        bill.setAmount(getCent(requestDto.getPayAmount()));
        bill.setDescription(getDescription(payType)+requestDto.getPayAmount().toString()+"元");
        bill.setCreateTime(new Date());
        bill.setToAccount(0);//初始化未到帐
        bill.setPayChannel(payType);
        bill.setType(0);
        bill.setBak1(requestDto.getNickName());
        billService.insert(bill);
        return bill.getId();
    }

    private String getDescription(int type) {
        return type==0?"支付宝充值":"微信充值";
    }

    public boolean changeAliUsedAccounts(String userId,String aliBuyerUserId, String aliAccount) throws Exception {
        queryWalletByUserId(userId);
        if(!StringUtils.isEmpty(aliAccount) && !StringUtils.isEmpty(aliBuyerUserId)) {
            String accountId= userPayAccountService.addAccount(userId,aliAccount,aliBuyerUserId,2);
            //如果当前没有绑定的支付宝账号,就绑定刚刚加入的账号id
            if(userPayAccountService.getBindAccount(userId,2)==null){
                userPayAccountService.modifyAccount(userId,accountId);
            }
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean changeWechatUsedAccounts(String userId, String nickName, String openId){
        queryWalletByUserId(userId);
        return changeWechatUsedAccount(userId, nickName, openId);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean changeWechatUsedAccount(String userId, String nickName, String openId){
        if(StringUtils.isNotBlank(nickName) && StringUtils.isNotBlank(openId)){
            String accountId= userPayAccountService.addAccount(userId,nickName,openId,1);
            //如果当前没有绑定的支付宝账号,就绑定刚刚加入的账号id
            if(userPayAccountService.getBindAccount(userId,1)==null){
                userPayAccountService.modifyAccount(userId,accountId);
            }
            return true;
        }
        return false;
    }

    public boolean boolIsAccountBindTrue(String userId, int type) throws Exception {
        queryWalletByUserId(userId);
        UserPayAccount account = userPayAccountService.get(userId,type);
        if(account!=null){
            return true;
        }
        return false;
    }

    private BigDecimal getMoney(Long cent){
        return new BigDecimal(Money.getMoneyString(cent));
    }

    private Long getCent(BigDecimal bigDecimal){
        return new Money(bigDecimal).getCent();
    }

   /* @Override
    public String aliOauthRequest(String userId) throws Exception {
        String result="";
        //指定证书的格式
        KeyStore keyStore=KeyStore.getInstance("PKCS12");
        //读取指定位置的证书
        FileInputStream inputStream=new FileInputStream(new File("k:/apiclient_cert.p12"));
        try {
            //指定证书的密码（商户id）
            keyStore.load(inputStream,"1491515532".toCharArray());
        }  catch (Exception e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
        }
        SSLContext sslContext=SSLContexts.custom().
                loadKeyMaterial(keyStore,"1491515532".toCharArray()).build();
        SSLConnectionSocketFactory factory=new SSLConnectionSocketFactory(sslContext,new String[]{"TLSv1"},null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        //指定httpclient的socketfactory
        CloseableHttpClient httpClient=HttpClients.custom().
                setSSLSocketFactory(factory).build();
        try{
            //new HttpPost("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=APPID&scope=SCOPE&redirect_uri=ENCODED_URL");
            //TODO redirect_uri http://localhost:18080/test
            HttpPost httpPost=this.setPostUrl("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm",appId,"http://112.74.200.88:18080/test");
            httpPost.addHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
            CloseableHttpResponse response=httpClient.execute(httpPost);
            HttpEntity entity=response.getEntity();
            System.out.println("------------------------------");
            if(entity!=null){
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
                System.out.println("context-length"+entity.getContentLength());
                StringBuffer buffer=new StringBuffer();
                String text="";
                while((text=bufferedReader.readLine())!=null){
                    buffer.append(text);
                }
                System.out.println(buffer.toString());
                result=buffer.toString();
                EntityUtils.consume(entity);
            }
            response.close();
        }finally {
            if(httpClient!=null){
                httpClient.close();
            }
        }
        return result;
    }

    private HttpPost setPostUrl(String requestUrl,String appId,String redirect_uri){
        StringBuffer buffer=new StringBuffer(requestUrl);
        try {
            buffer.append("?app_id=").append(appId).append("&scope=auth_userinfo&redirect_uri=").append(URLEncoder.encode(redirect_uri,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new HttpPost(buffer.toString());
    }*/

}