package com.netx.common.express;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netx.common.common.utils.HttpUtils;
import com.netx.common.user.util.DateTimestampUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 阿里云物流服务类
 * @author 黎子安
 * @date 2017/10/23
 */
@Service
public class AliyunExpressService {

    @Value("${aliyunExpress.appcode}")
    private String appcode;

    @Value("${aliyunExpress.host}")
    private String host;

    private Logger logger = LoggerFactory.getLogger(AliyunExpressService.class);

    /**
     * 获取物流信息
     * @param number
     * @param type
     */
    public String getExpressData(String number,String type){
        if(number != null){
            Map<String, String> querys = new HashMap<>();
            querys.put("number", number);
            querys.put("type", type==null?"auto":type);
            try {
                String jsonObject = getResultJson(sendHttpResponse(querys, "query"));
                return fromatDate(JSONObject.parseObject(getJSONObject(jsonObject).get("result").toString()).get("list").toString());
            } catch (Exception e) {
                logger.warn(e.getMessage(),e);
            }
        }
        return null;
    }

    /**
     * 获取物流信息【日期格式化】
     * @param number
     * @param type
     * @return
     */
    public String getExpressDataFormatDate(String number,String type){
        String result = getExpressData(number, type);
        return result==null?null:fromatDate(result);
    }

    /**
     * 获取物流信息List
     * @param number
     * @param type
     * @return
     */
    public List<Map<String,String>> getExpressDataList(String number,String type){
        String result = getExpressData(number,type);
        List<Map<String,String>> mapList = new ArrayList<>();
        if(result!=null){
            mapList= (List<Map<String,String>>)(List)JSONArray.parseArray(result,Map.class);
        }
        return mapList;
    }

    /**
     * 获取物流信息List【日期格式化】
     * @param number
     * @param type
     * @return
     */
    public List<Map<String,String>> getExpressDataLists(String number,String type){
        List<Map<String,String>> mapList = getExpressDataList(number,type);
        if(mapList!=null && mapList.size()>0){
            formatDateToMapList(mapList);
        }
        return mapList;
    }

    private void formatDateToMapList(List<Map<String,String>> mapList){
        int size = mapList.size();
        Date date;
        String time;
        for (int i = 0; i <size ; i++) {
            time = mapList.get(i).remove("time");
            if(StringUtils.isNotBlank(time)){
                date = DateTimestampUtil.stringToDate(time,"yyyy-M-dd HH:mm:ss");
                if(date!=null){
                    mapList.get(i).put("date",numToString(date.getMonth()+1)+"-"+numToString(date.getDate()));
                    mapList.get(i).put("time",numToString(date.getHours())+":"+numToString(date.getMinutes()));
                }
            }
        }
    }

    public List<Map<String,String>> jsonToExpressDateList(String json){
        List<Map<String,String>> mapList = new ArrayList<>();
        if(json!=null){
            mapList= (List<Map<String,String>>)(List)JSONArray.parseArray(json,Map.class);
        }
        return mapList;
    }

    /**
     * 格式化
     * @param json
     * @return
     */
    public List<Map<String,String>> jsonToExpressDateLists(String json){
        List<Map<String,String>> mapList = jsonToExpressDateList(json);
        formatDateToMapList(mapList);
        return mapList;
    }

    /**
     * 日期格式化
     * @param json
     * @return
     */
    private String fromatDate(String json){
        String frist = "\"time\": \"";
        String end = "\"";
        Pattern pattern = Pattern.compile(frist+"(\\d+-\\d+-\\d+ \\d+:\\d+:\\d+)"+end);
        Matcher matcher = pattern.matcher(json);
        String time;
        Date date;
        while (matcher.find()){
            try {
                time = matcher.group(1);
                date = DateTimestampUtil.stringToDate(time,"yyyy-M-dd HH:mm:ss");
                if(date!=null){
                    json=json.replace(frist+time+end,frist+numToString(date.getMonth()+1)+"-"+numToString(date.getDate())+" "+numToString(date.getHours())+":"+numToString(date.getMinutes())+end);
                }
            }catch (Exception e){
                logger.warn(e.getMessage());
            }
        }
        return json;
    }

    private String numToString(Integer num){
        return (num>9?"":"0")+num;
    }

    private JSONObject getJSONObject(String json){
        return json!=null?JSON.parseObject(json):null;
    }

    private HttpResponse sendHttpResponse(Map<String,String> querys,String type) throws Exception{
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        HttpResponse response = HttpUtils.doGet(host, "/express/"+type,  headers, querys);
        return response;
    }

    private String getResultJson(HttpResponse httpResponse){
        String json = null;
        try {
            HttpEntity httpEntity = httpResponse.getEntity();
            json = new String(EntityUtils.toByteArray(httpEntity),"UTF-8");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return json;
    }

    /**
     * 获取快递公司
     */
    public <T>List<T> getExpressBusiness(Class<T> className){
        try {
            HttpResponse response = sendHttpResponse(new HashMap<String, String>(), "type");
            //获取response的body
            return (List<T>)(List) JSONArray.parseArray(getJSONObject(getResultJson(response)).get("result").toString(),className);
        } catch (Exception e) {
           logger.error(e.getMessage(),e);
        }
        return new ArrayList<>();
    }

}