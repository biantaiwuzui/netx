package com.netx.utils.uri;

import com.netx.utils.datastructures.Tuple;
import com.netx.utils.xml.XMLUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger("HttpUtil");

    public static SSLConnectionSocketFactory getSSLConnectionSocketFactory(KeyStore keyStore,char[] wechatCharArray) throws Exception{
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, wechatCharArray).build();
        //指定TLS版本
        return new SSLConnectionSocketFactory(
                sslcontext, new String[]{"TLSv1"}, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }

    public static <T>T doPostWeChat(String url,SSLConnectionSocketFactory sslsf,StringEntity stringEntity,Class<T> tClass){
        try {
            //设置httpclient的SSLSocketFactory
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            HttpPost httpPost = new HttpPost(url);
            //stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.setEntity(stringEntity);
            try {
                CloseableHttpResponse response = httpclient.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                T info = null;
                if(httpEntity!=null){
                    info = getWeChatInfo(response.getEntity(),tClass);
                }
                response.close();
                httpclient.close();
                return info;
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static <T>T getWeChatInfo(HttpEntity entity,Class<T> tClass){
        try {
            logger.info("----------------------------------------");
            logger.info("Response content length: " + entity.getContentLength());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuilder.append(text);
            }
            //获取微信提现返回参数
            return XMLUtil.fromXML(stringBuilder.toString(),tClass);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
