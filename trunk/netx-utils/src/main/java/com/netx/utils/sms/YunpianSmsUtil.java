package com.netx.utils.sms;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送信息模块
 * Created by spark on 2015/12/2.
 */
public class YunpianSmsUtil {
    private final static Log logger = LogFactory.getLog(YunpianSmsUtil.class);

    private final static String VM_CRM_API_KEY = "f9bbd4162a7e63b0f2c603032e1ccc5a";

    private final static String VM_NORMAL_API_KEY = "74e8f4b0695050b4255543bd2ef0d821";

    /**
     * 服务http地址Scala
     */
    private final static String BASE_URI = "http://yunpian.com";
    /**
     * 服务版本号
     */
    private final static String VERSION = "v1";
    /**
     * 编码格式
     */
    private final static String ENCODING = "UTF-8";
    /**
     * 查账户信息的http地址
     */
    private final static String URI_GET_USER_INFO = BASE_URI + "/" + VERSION + "/user/get.json";
    /**
     * 通用发送接口的http地址
     */
    private final static String URI_SEND_SMS = BASE_URI + "/" + VERSION + "/sms/send.json";
    /**
     * 模板发送接口的http地址
     */
    private final static String URI_TPL_SEND_SMS = BASE_URI + "/" + VERSION + "/sms/tpl_send.json";

    /**
     * 获取回复短信的http地址
     */
    private final static String URI_TPL_REPLY_SMS = BASE_URI + "/" + VERSION + "/sms/pull_reply.json";

    private final static String URI_VOICE_SMS=BASE_URI + "/" + VERSION +"/voice/send.json";

    private final static String VOICE_PHONE_NUMBER="02558875270";

    /**
     * 取账户信息
     *
     * @return json格式字符串
     * @throws java.io.IOException
     */
    public static String getUserInfo(String apikey) throws IOException {
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(URI_GET_USER_INFO + "?apikey=" + apikey);
        HttpMethodParams param = method.getParams();
        param.setContentCharset(ENCODING);
        client.executeMethod(method);
        return method.getResponseBodyAsString();
    }

    /**
     * 发短信
     *
     * @param text   　短信内容
     * @param mobile 　接受的手机号
     * @return json格式字符串 {"code":0,"msg":"OK","result":{"count":1,"fee":1,"sid":904481551}}
     * @throws IOException
     */
    private static String sendSms(String apiKey, String text, String mobile) throws IOException {
        HttpClient client = new HttpClient();
        NameValuePair[] nameValuePairs = new NameValuePair[3];
        nameValuePairs[0] = new NameValuePair("apikey", apiKey);
        nameValuePairs[1] = new NameValuePair("text", text);
        nameValuePairs[2] = new NameValuePair("mobile", mobile);
        PostMethod method = new PostMethod(URI_SEND_SMS);
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        method.setRequestBody(nameValuePairs);
        HttpMethodParams param = method.getParams();
        param.setContentCharset(ENCODING);
        client.executeMethod(method);
        return method.getResponseBodyAsString();
    }

    public static boolean sendNormalSms(String content, String mobile) throws IOException {
        String result= sendSms(VM_NORMAL_API_KEY, content, mobile);
        return getSendStatus(result,content);
    }

    public static boolean sendNormalSms(String apiKey, String content, String mobile) throws IOException {
        String result= sendSms(apiKey, content, mobile);
        return getSendStatus(result,content);
    }

    /**
     * 通过模板发送短信
     *
     * @param apikey    apikey
     * @param tpl_id    　模板id
     * @param tpl_value 　模板变量值
     * @param mobile    　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String tplSendSms(String apikey, long tpl_id, String tpl_value, String mobile) throws IOException {
        HttpClient client = new HttpClient();
        NameValuePair[] nameValuePairs = new NameValuePair[4];
        nameValuePairs[0] = new NameValuePair("apikey", apikey);
        nameValuePairs[1] = new NameValuePair("tpl_id", String.valueOf(tpl_id));
        nameValuePairs[2] = new NameValuePair("tpl_value", tpl_value);
        nameValuePairs[3] = new NameValuePair("mobile", mobile);
        PostMethod method = new PostMethod(URI_TPL_SEND_SMS);
        method.setRequestBody(nameValuePairs);
        HttpMethodParams param = method.getParams();
        param.setContentCharset(ENCODING);
        client.executeMethod(method);
        return method.getResponseBodyAsString();
    }


    /**
     * 发送普通的短信 通用
     * @param mobile   要发送的手机号码
     * @param tplValue 模板的参数值 根据模板的规格组装如：感谢您注册#company#，您的验证码是#code#
     *                 则参数组装成 tplValue="#company#=" + 内容 + "&#code#=" + 内容 + ""
     * @param tplId    模板 id
     * @return
     */
    public static boolean sendNormalMessage(String mobile, String tplValue, long tplId) {
        try {
            String result = tplSendSms(VM_NORMAL_API_KEY, tplId, tplValue, mobile);
            return getSendStatus(result,tplId+","+tplValue);
        } catch (IOException e) {
            logger.error("发送短信异常!" + e);
        }
        return false;
    }

    /**
     * 通知类短信，属于营销类
     * @param mobile
     * @param tplValue
     * @param tplId
     * @return
     */
    public static double sendNoticeMessage(String mobile, String tplValue, long tplId) {
        try {
            String result = tplSendSms(VM_CRM_API_KEY, tplId, tplValue, mobile);
            return getSendNum(result);
        } catch (IOException e) {
            logger.error("发送短信异常!" + e);
        }
        return -1;
    }

    /**
     * 发送营销类短信 通用
     * @deprecated
     * @param mobile   要发送的手机号码
     * @param tplValue 模板的参数值 如模板为 亲爱的用户，#company#发福利啦，#text#，感谢你对#company#的支持，祝购物愉快
     *                 则参数组装为: tpl_value = "#company#=" + 内容 + "&#text#=" + 内容 + "";
     * @param tplId    模板 id
     * @return
     */
    public static Boolean sendCrmMessage(String mobile, String tplValue, long tplId) {
        try {
            String result = tplSendSms(VM_CRM_API_KEY, tplId, tplValue, mobile);
            return getSendStatus(result,tplId+","+tplValue);
        } catch (IOException e) {
            logger.error("发送短信异常!" + e);
        }
        return false;
    }

    /**
     * 发送营销类短信 返回值是String
     *
     * @param mobile   要发送的手机号码
     * @param tplValue 模板的参数值 如模板为 亲爱的用户，#company#发福利啦，#text#，感谢你对#company#的支持，祝购物愉快
     *                 则参数组装为: tpl_value = "#company#=" + 内容 + "&#text#=" + 内容 + "";
     * @param tplId    模板 id
     * @return
     */
    public static String sendCrm(String mobile, String tplValue, long tplId) {
        try {
            String result = tplSendSms(VM_CRM_API_KEY, tplId, tplValue, mobile);
            return result;
        } catch (IOException e) {
            logger.error("发送短信异常!" + e);
        }
        return "";
    }

    public static boolean sendVoice(String mobile, String code) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", YunpianSmsUtil.VM_NORMAL_API_KEY);
        params.put("mobile", mobile);
        params.put("code", code);

        HttpClient client = new HttpClient();
        NameValuePair[] nameValuePairs = new NameValuePair[4];
        nameValuePairs[0] = new NameValuePair("apikey", YunpianSmsUtil.VM_NORMAL_API_KEY);
        nameValuePairs[1] = new NameValuePair("mobile", mobile);
        nameValuePairs[2] = new NameValuePair("code", code);
        nameValuePairs[3] = new NameValuePair("display_num", YunpianSmsUtil.VOICE_PHONE_NUMBER);
        PostMethod method = new PostMethod(URI_VOICE_SMS);
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        method.setRequestBody(nameValuePairs);
        HttpMethodParams param = method.getParams();
        param.setContentCharset(ENCODING);
        client.executeMethod(method);
        String result=method.getResponseBodyAsString();
        return getSendStatus(result,code);
    }


    public static String getReplyMessage(String apikey, int pageSize) throws IOException {
        HttpClient client = new HttpClient();
        NameValuePair[] nameValuePairs = new NameValuePair[2];
        nameValuePairs[0] = new NameValuePair("apikey", apikey);
        nameValuePairs[1] = new NameValuePair("page_size", String.valueOf(pageSize));
        PostMethod method = new PostMethod(URI_TPL_REPLY_SMS);
        method.setRequestBody(nameValuePairs);
        HttpMethodParams param = method.getParams();
        param.setContentCharset(ENCODING);
        client.executeMethod(method);
        return method.getResponseBodyAsString();
    }


    public static double getSendNum(String result){
        try {
            JSONObject.parse(result);
            Map<String, Object> resultMap = (Map)JSONObject.parse(result);//GsonJson.parseJson(result, Map.class);
            double code = (double) resultMap.get("code");
            if (code == 0) {
                if(null != resultMap.get("result") ){
                    if(null != ((Map<String, Object>)resultMap.get("result")).get("count")){
                        return ((Double)((Map<String, Object>)resultMap.get("result")).get("count"));
                    }}else {
                    logger.error("发送短信返回格式异常,result="+result);
                }
            } else {
                logger.error("结果返回值:" + result);
            }
        } catch (Exception e) {
            logger.error("获取发送状态出错!" + e);
        }
        return -1;
    }

    public static boolean getSendStatus(String result,String text) {
        try {
            Map<String, Object> resultMap = (Map)JSONObject.parse(result);//GsonJson.parseJson(result, Map.class);
            int code = (int) resultMap.get("code");
            if (code == 0) {
                return true;
            } else {
                logger.error("云片结果返回值:" + result+","+text);
                return false;
            }
        } catch (Exception e) {
            logger.error("获取发送状态出错!" + e);
        }
        return false;
    }

    // 测试
    public static void main(String[] args) throws IOException {
        //System.out.println(YunpianSmsUtil.sendNormalSms("【新棱镜】验证码：134567，您申请了找回密码，此短信验证码10分钟内有效，请勿转发他人。","18927598405"));
        /*String resp = createSmsTemplate("74e8f4b0695050b4255543bd2ef0d821", "【新棱镜】#userName#，亲，新春大酬宾，送您一张满200减20的优惠券，请注意查收哦！");
        Map<String, Object> resultMap = (Map)JSONObject.parse(resp);
        if("0".equals(resultMap.get("code"))) {
            Map<String, String> data = (Map<String, String>) resultMap.get("template");
            System.out.println("templateId：" + data.get("tpl_id"));
        }*/

        String apiKey = "b2fd26e01d9d020483117db8c882c6e0";
        System.out.println(sendSms(apiKey, "【濠舟互联网平台】您的验证码是123456。如非本人操作，请忽略本短信", "18927598405"));
    }


    public static String createSmsTemplate(String apikey, String tpl_content) throws IOException {
        HttpClient client = new HttpClient();
        NameValuePair[] nameValuePairs = new NameValuePair[4];
        nameValuePairs[0] = new NameValuePair("apikey", apikey);
        nameValuePairs[1] = new NameValuePair("tpl_content", String.valueOf(tpl_content));
        nameValuePairs[2] = new NameValuePair("notify_type", "0");
        PostMethod method = new PostMethod("https://sms.yunpian.com/v1/tpl/add.json");
        method.setRequestBody(nameValuePairs);
        HttpMethodParams param = method.getParams();
        param.setContentCharset(ENCODING);
        try {
            client.executeMethod(method);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return method.getResponseBodyAsString();
    }
}
