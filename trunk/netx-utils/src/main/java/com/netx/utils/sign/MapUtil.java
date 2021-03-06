package com.netx.utils.sign;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author Retina.Ye
 */
public class MapUtil {

    /**
     * Map key 排序
     * <p/>
     * 值为空的不参与签名
     *
     * @param map
     *
     * @return
     */
    public static Map<String, String> order(Map<String, String> map) {
        HashMap<String, String> tempMap = new LinkedHashMap<String, String>();
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());

        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

        for (Map.Entry<String, String> item : infoIds) {
            if (StringUtils.isBlank(item.getValue())) {
                continue;
            }
            tempMap.put(item.getKey(), item.getValue());
        }
        return tempMap;
    }
    
    public static Map<String, String> orderAll(Map<String, String> map) {
    	HashMap<String, String> tempMap = new LinkedHashMap<String, String>();
    	List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
    	
    	Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
    	
    	for (Map.Entry<String, String> item : infoIds) {
    		tempMap.put(item.getKey(), item.getValue());
    	}
    	return tempMap;
    }


    /**
     * 转换对象为map
     *
     * @param object
     * @param ignore
     *
     * @return
     */
    public static Map<String, String> objectToMap(Object object, String... ignore) {
        Map<String, String> tempMap = new LinkedHashMap<String, String>();
        for (Field f : object.getClass().getDeclaredFields()) {
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            boolean ig = false;
            if (ignore != null && ignore.length > 0) {
                for (String i : ignore) {
                    if (i.equals(f.getName())) {
                        ig = true;
                        break;
                    }
                }
            }
            if (ig) {
                continue;
            }
            else {
                Object o = null;
                try {
                    o = f.get(object);
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                tempMap.put(f.getName(), o == null ? "" : o.toString());
            }
        }
        return tempMap;
    }

    /**
     * url 参数串连
     *
     * @param map
     * @param keyLower       key是否要转成小写
     * @param valueUrlencode value是否要URLEncoding
     *
     * @return
     */
    public static String mapJoin(Map<String, String> map, boolean keyLower, boolean valueUrlencode) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            if (map.get(key) != null && !"".equals(map.get(key))) {
                try {
                    stringBuilder.append(keyLower ? key.toLowerCase() : key)
                            .append("=")
                            .append(valueUrlencode ? URLEncoder.encode(map.get(key), "utf-8") : map.get(key))
                            .append("&");
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    /**
     * 简单 xml 转换为 Map
     *
     *
     * @return
     */
    public static Map<String, String> xmlToMap(String xml) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
            Element element = document.getDocumentElement();
            NodeList nodeList = element.getChildNodes();
            Map<String, String> map = new LinkedHashMap<String, String>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);
                map.put(e.getNodeName(), e.getTextContent());
            }
            return map;
        }
        catch (DOMException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
