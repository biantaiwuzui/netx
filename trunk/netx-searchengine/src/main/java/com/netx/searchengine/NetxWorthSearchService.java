package com.netx.searchengine;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NetxWorthSearchService {
    private Logger logger = LoggerFactory.getLogger(NetxWorthSearchService.class);

    /**
     * 距离排序处于排序的等级【用于取距离结果】
     */
    protected Integer level;

    @Value("${netx.worth.elasticsearch.host}")
    private String host;
    @Value("${netx.worth.elasticsearch.port}")
    private Integer port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    protected TransportClient client = null;
    private TransportAddress transportAddress = null;

    //获取连接
    protected TransportClient getClient() {
        if(client==null){
            try {
                //指定集群地址，地址与端口1
                transportAddress = new TransportAddress(InetAddress.getByName(host), port);
            } catch (UnknownHostException e) {
                logger.error(e.getMessage(),e);
                return null;
            }
            logger.info("create TransportClient...");
            //???
            Settings.Builder builder = Settings.builder();
            builder.put("cluster.name", "elasticsearch");
            //指定集群地址，地址与端口2
            client = new PreBuiltTransportClient(builder.build()).addTransportAddresses(transportAddress);
        }
        return client;
    }

    /**
     * 建立连接
     * @return
     */
    /*protected Client getClient() {
        if (client == null) {
            try {
                logger.warn(host+"================="+port);
                Settings.Builder builder = Settings.builder();
                builder.put("cluster.name", "elasticsearch");
*//*                builder.put("thread_pool.bulk.type",  "fixed");
                builder.put("thread_pool.bulk.size" ,5);
                builder.put("thread_pool.bulk.queue_size", 5);
                builder.put("thread_pool.index.type" , "fixed");
                builder.put("thread_pool.index.size" , 5);
                builder.put("thread_pool.index.queue_size" , 10);
                builder.put("thread_pool.search.type",  "fixed");
                builder.put("thread_pool.search.size" ,5);
                builder.put("thread_pool.search.queue_size", 5);*//*
                TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(host), port);
                this.client = new PreBuiltTransportClient(builder.build()).addTransportAddresses(transportAddress);
            }catch (InstantiationError e){
                logger.error("创建TransportAddress失败:"+e.getMessage(),e);
                return null;
            }catch (Exception e){
                logger.error("获取Client失败:"+e.getMessage(),e);
                return null;
            }
        }
        return client;
    }*/

    /**
     * 日期转换
     * @param dateStr
     * @param isLongDate 是否长日期
     * @return
     */
    protected Date StringToDate(String dateStr, Boolean isLongDate){
        dateStr = isLongDate?dateStr.replace("T"," "):dateStr+" 00:00:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        try {
            return format.parse(dateStr);
        }catch (Exception e){
            logger.warn(e.getMessage());
            return null;
        }
    }

    /**
     * boolean类型的string转boolean
     * @param str
     * @return
     */
    protected Boolean strToBoolean(String str){
        if(str==null){
            return null;
        }
        return str.equals("1");
    }

    /**
     * 打印筛选条件
     * @param queryBuilder
     */
    protected void printfSearch(BoolQueryBuilder queryBuilder){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        logger.info(searchSourceBuilder.toString());
    }

    /**
     * 自定义距离排序规则
     * @param searchBuilder
     * @param lon
     * @param lat
     * @param locationKey
     */
    protected void addScriptGeo(SearchRequestBuilder searchBuilder, double lon, double lat, String locationKey,DistanceUnit unit){
        Map<String,Object> map = new HashMap<>();
        map.put("lon",lon);
        map.put("lat",lat);
        Script script = new Script(ScriptType.INLINE,Script.DEFAULT_SCRIPT_LANG,"def d=doc['"+locationKey+"'].arcDistance(params.lat,params.lon)/"+getMETERS(unit)+";return d>=1?Math.round(d):d;",map);
        ScriptSortBuilder scriptSortBuilder = SortBuilders.scriptSort(script, ScriptSortBuilder.ScriptSortType.NUMBER);
        scriptSortBuilder.order(SortOrder.ASC);
        searchBuilder.addSort(scriptSortBuilder);
    }

    /**
     * 获取距离单位转为单位为米
     * @param unit
     * @return
     */
    protected double getMETERS(DistanceUnit unit){
        switch (unit){
            case MILLIMETERS:
                return 0.001;
            case CENTIMETERS:
                return 0.01;
            case INCH:
                return 0.0254;
            case FEET:
                return 0.3048;
            case YARD:
                return 0.9144;
            case METERS:
                return 1;
            case KILOMETERS:
                return 1000.0;
            case MILES:
                return 1609.344;
            case NAUTICALMILES:
                return 1852.0;
        }
        return 1;
    }

    protected Double getDistance(SearchHit searchHit){
        try {
            BigDecimal geoDis = new BigDecimal(searchHit.getSortValues()[level].toString());
            BigDecimal distance = geoDis.setScale(3, BigDecimal.ROUND_HALF_UP);
            return distance.doubleValue();
        }catch (ArrayIndexOutOfBoundsException exception) {
            logger.error("搜索异常:"+searchHit.getSortValues()+";hit:"+searchHit, exception);
            return 0.0;
        }
    }

    protected Boolean checkLocationStr(BigDecimal location){
        return location!=null && location.doubleValue()!=0;
    }

    protected void addTermShoule( BoolQueryBuilder queryBuilders,String key,List list){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        for(Object value : list){
            queryBuilder.should(QueryBuilders.commonTermsQuery(key,value));
        }
        queryBuilders.must(queryBuilder);
    }
}
