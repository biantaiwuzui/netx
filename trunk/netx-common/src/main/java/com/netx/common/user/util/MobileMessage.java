package com.netx.common.user.util;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.netx.common.user.enums.MobileSmsCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.util.List;
import java.util.Map;

/**
 * @author 老肥猪
 * @since 2018-8-14
 */
@Component
public class MobileMessage {

    @Autowired
    private NewMobileInfoHolder newMobileInfoHolder;

    //默认模板
    private String SmsTemplateCode = "SMS_124250001";

    private Logger logger = LoggerFactory.getLogger(MobileMessage.class);

    public void setSmsTemplateCode(String smsTemplateCode){
        SmsTemplateCode = smsTemplateCode;
    }


    /**
     * 发送短信
     * @param tel 电话号码
     * @param code 验证码
     * @return
     */
    public int send(String tel, String code) {
        //短信模板的内容
        String json = "{code:'" + code + "'}";
        SmsTemplateCode = MobileSmsCode.MS_MOBILE_CODE.code(tel);
        return internationalSendMessage(tel, json);
    }

    /**
     * 新的发送短信的方法，支持国际（个人）
     * @param tel
     * @param json
     * @return
     */
    private int internationalSendMessage(String tel,String json){
        // 设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        // 初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-shenzhen", newMobileInfoHolder.getAppkey(), newMobileInfoHolder.getSecret());
        try {
            DefaultProfile.addEndpoint("cn-shenzhen", "cn-shenzhen", newMobileInfoHolder.getProduct(), newMobileInfoHolder.getDomain());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        // 组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        // 使用post提交
        request.setMethod(MethodType.POST);
        // 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
        request.setPhoneNumbers(tel);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName(newMobileInfoHolder.getSignName());
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(SmsTemplateCode);
        // 对应json
        request.setTemplateParam(json);
        // 请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ServerException e) {
            logger.error(e.getMessage(),e);
            return -1;
        } catch (ClientException e) {
            logger.error(e.getMessage(),e);
            return -1;
        }
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")){
            return 1;
        }
        logger.info("短信业务发生错误，错误码为："+sendSmsResponse.getCode());
        return -1;
    }

    /**
     *  发送短信（群发）
     * @param tels
     * @param jsons
     * @param signNames
     * @return
     */
    private int internationalSendMessageBatch(String tels,String jsons,String signNames){
        // 设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        // 初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-shenzhen", newMobileInfoHolder.getAppkey(), newMobileInfoHolder.getSecret());
        try {
            DefaultProfile.addEndpoint("cn-shenzhen", "cn-shenzhen", newMobileInfoHolder.getProduct(), newMobileInfoHolder.getDomain());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendBatchSmsRequest request = new SendBatchSmsRequest();
        request.setPhoneNumberJson(tels);
        request.setSignNameJson(signNames);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(SmsTemplateCode);
        //必填:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParamJson(jsons);
        SendBatchSmsResponse sendSmsResponse= null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ServerException e) {
            logger.error(e.getMessage(),e);
            return -1;
        } catch (ClientException e) {
            logger.error(e.getMessage(),e);
            return -1;
        }
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")){
            return 1;
        }
        logger.info("短信业务发生错误，错误码为："+sendSmsResponse.getCode());
        return -1;
    }
    /**
     * 发送短信
     * @param tel 电话号码
     * @param map 模版内容
     * @return
     */
    public int send(String tel, Map<String,Object> map, MobileSmsCode mobileSmsCode) {
        this.SmsTemplateCode = mobileSmsCode.code(tel);
        return internationalSendMessage(tel, JSON.toJSONString(map));
    }
    /**
     * 发送短信群发
     * @param tels 电话号码
     * @param signNames 没有改变就传null
     * @param listMap 模版内容
     * @return
     */
    public int sends(List<String> tels, List<String> signNames, List<Map<String,Object>> listMap, MobileSmsCode mobileSmsCode) {
        if(tels==null&&tels.size()==0){
            return -1;
        }
        if(signNames==null||signNames.size()==0){
            for (int i=0;i<tels.size();i++){
                signNames.add(newMobileInfoHolder.getSignName());
            }
        }
        this.SmsTemplateCode = mobileSmsCode.code(tels.get(0));
        return internationalSendMessageBatch(JSON.toJSONString(tels), JSON.toJSONString(listMap),JSON.toJSONString(signNames));
    }

//    /**
//     * 老的发送短信方法
//     * @param tel
//     * @param json
//     * @return
//     */
//    private int sendMessage(String tel,String json){
//        TaobaoClient client=null;
//        //实例化请求
//        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
//        //公共回传参数
//        req.setExtend("123456");
//        //短信类型
//        req.setSmsType("normal");
//        //短信签名
//        req.setSmsFreeSignName(mobileInfoHolder.getSignName());
//        //短信模版变量
//        req.setSmsParamString(json);
//        //手机号码
//        req.setRecNum(tel);
//        //设置模板
//        req.setSmsTemplateCode(SmsTemplateCode);
//        logger.info(req.getSmsFreeSignName());
//        try {
//            //实例化响应
//            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
//            logger.info(rsp.getBody());
//            return 1;
//        } catch (Exception e) {
//            logger.error(e.getMessage(),e);
//            return -1;
//        }
//    }

}
