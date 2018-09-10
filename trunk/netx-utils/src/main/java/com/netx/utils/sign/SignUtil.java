package com.netx.utils.sign;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Writer;
import java.util.Map;
import java.util.Random;

/**
 * 微信支付加密工具
 * <p/>
 * 基于微信支付文档V3.3.6
 *
 * @author Retina.Ye
 */
public class SignUtil {

    private static Log logger = LogFactory.getLog(SignUtil.class);

    /**
     * Sign签名生成
     * <p/>
     * a.对所有传入参数按照字段名的ASCII码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2...）
     * 拼接成字符串string1，注意：值为空的参数不参与签名；
     * <p/>
     * b.在string1最后拼接上key=Key（商户支付秘钥）得到stringSignTemp字符串，并对stringSignTemp进行md5运算，
     * 再将得到的字符串所有字符转换为大写，得到sign值signValue。
     *
     * @param params 参与签名的参数
     * @param key    商户支付秘钥
     *
     * @return
     */
    public static String getSignValue(Map<String, String> params, String key) {
        // 第一步(a)
        Map<String, String> tmap = MapUtil.order(params);   // ASCII码从小到大排序
        String string1 = MapUtil.mapJoin(tmap, false, false);   // 连接成URL键值对的格式

        // 第二步(b)
        String stringSignTemp = string1 + "&key=" + key;  // 在string1最后拼接上&key=Key
        return DigestUtils.md5Hex(stringSignTemp).toUpperCase();   // 对stringSignTemp进行md5运算，转换为大写
    }

    public static String getSignValueStep2(Map<String, String> params, String packageStr) {
        // 第一步(a)
        Map<String, String> tmap = MapUtil.order(params);   // ASCII码从小到大排序
        String string1 = MapUtil.mapJoin(tmap, false, true);   // 连接成URL键值对的格式

        // 第二步(b)
        String packageParamsString = string1 + "&sign=" + packageStr;  // 在string1最后拼接上&sign=package
        return packageParamsString;
    }

    /**
     * 生成签名（版本2）
     * a.对所有传入参数按照字段名的ASCII码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2...）
     * 拼接成字符串string1，注意：值为空的参数不参与签名；
     * <p/>
     * b.再将得到的字符串进行sha1编码
     *
     * @param params
     *
     * @return
     */
    public static String getSignVersion2(Map<String, String> params) {
        // 第一步(a)
        Map<String, String> tmap = MapUtil.order(params);   // ASCII码从小到大排序
        String string1 = MapUtil.mapJoin(tmap, false, false);   // 连接成URL键值对的格式
        // 第二步(b)
        return DigestUtils.shaHex(string1);
    }
    
    public static String getSignBySHA1(Map<String, String> params, boolean keyLower, boolean valueUrlencode) {
    	// 第一步(a)
    	Map<String, String> tmap = MapUtil.order(params);   // ASCII码从小到大排序
    	String string1 = MapUtil.mapJoin(tmap, keyLower, valueUrlencode);   // 连接成URL键值对的格式
    	// 第二步(b)
    	return DigestUtils.shaHex(string1);
    }
    
    public static String getSignForMqq(Map<String, String> params, String appKey){
    	// 第一步(a)
        Map<String, String> map = MapUtil.orderAll(params);   // ASCII码从小到大排序
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
        	stringBuilder.append(key)
        	.append("=")
        	.append(map.get(key))
        	.append("&");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            //System.out.println("str-->"+stringBuilder.toString());
            try {
            	byte[] byteKey = (appKey+"&").getBytes("UTF-8");
            	// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            	SecretKey secretKey = new SecretKeySpec(byteKey, "HmacSHA1");
            	// 生成一个指定 Mac 算法 的 Mac 对象
            	Mac mac = Mac.getInstance("HmacSHA1");
            	// 用给定密钥初始化 Mac 对象
            	mac.init(secretKey);
            	byte[] byteSrc = stringBuilder.toString().getBytes("UTF-8");
            	// 完成 Mac 操作
            	byte[] dst = mac.doFinal(byteSrc);
            	// Base64
            	return Base64.encode(dst);
			} catch (Exception e) {
				logger.error("手Q支付签名异常", e);
			}
        }
        return "";
    }

    public static String getNonceStr() {
        Random random = new Random();
        return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
    }

    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    public static String toXml(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>").append("\r\n");

        for (Map.Entry<String, String> data : params.entrySet()) {
            if (NumberUtils.isNumber(data.getValue())) {
                sb.append("<").append(data.getKey()).append(">").append(data.getValue()).append("</").append(data.getKey()).append(">").append("\r\n");
                continue;
            }
            sb.append("<").append(data.getKey()).append("><![CDATA[").append(StringUtils.defaultString(data.getValue())).append("]]></").append(data.getKey()).append(">").append("\r\n");
        }

        sb.append("</xml>").append("\r\n");
        return sb.toString();
    }

    public static <T> T fromXml(String xml, Class<T> clazz) {
        xstream.alias("xml", clazz);
        try {
            return (T) xstream.fromXML(new String(xml.getBytes("iso-8859-1"), "utf-8"));
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /** 扩展xstream，使其支持CDATA块 */
    private static XStream xstream = new XStream(new XppDriver() {
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    }
                    else {
                        writer.write(text);
                    }
                }
            };
        }
    });
}