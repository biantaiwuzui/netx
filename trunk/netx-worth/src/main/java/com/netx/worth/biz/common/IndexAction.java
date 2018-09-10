package com.netx.worth.biz.common;

import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.common.enums.WorthIndexTypeEnum;
import com.netx.common.common.enums.WorthTypeEnum;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.wz.dto.common.WorthIndexResponseDto;
import com.netx.common.wz.dto.common.WorthNewlyHistory;
import com.netx.searchengine.SearchServiceProvider;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.model.WorthSearchResponse;
import com.netx.searchengine.query.WorthSearchQuery;
import com.netx.searchengine.service.WorthSearchService;
import com.netx.utils.DistrictUtil;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import com.netx.worth.biz.match.MatchCreateAction;
import com.netx.worth.service.WorthServiceprovider;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndexAction {
    private Logger logger = LoggerFactory.getLogger(IndexAction.class);

    @Autowired
    private SearchServiceProvider searchServiceProvider;

    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    @Autowired
    private WorthServiceprovider worthServiceprovider;
    @Autowired
    private MatchCreateAction matchCreateAction;

    private RedisCache redisCache;

    private RedisCache clientRedis() {
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
        return redisCache;
    }

    public List<WorthIndexResponseDto> searchWorth(WorthTypeEnum typeEnum, String title, Integer sort, Integer current, Integer size, Double lon, Double lat) {
        WorthSearchQuery worthSearchQuery = new WorthSearchQuery();
        worthSearchQuery.setCenterGeoPoint(new GeoPoint(lat, lon));
        worthSearchQuery.setPage(current, size);
        worthSearchQuery.setTitle(title);
        switch (sort) {
            case 1:
                //添加排序规则到查询类
                worthSearchQuery.addLastAscQuery(new LastAscQuery("publishTime", false));
                //可多添加几个排序规则，但推荐在最终执行语句中添加
                return getWorthList(worthSearchQuery, "credit", typeEnum);
            case 2:
                return getWorthList(worthSearchQuery, "credit", typeEnum);
            case 3:
                worthSearchQuery.addLastAscQuery(new LastAscQuery("isHoldCredit", false));
                return getWorthList(worthSearchQuery, "credit", typeEnum);
            case 4:
                worthSearchQuery.addLastAscQuery(new LastAscQuery("credit", false));
                return getWorthList(worthSearchQuery, null, typeEnum);
            default:
                return getWorthList(worthSearchQuery, "dealsTotal", typeEnum);
        }
    }

    public List<WorthIndexResponseDto> queryIndex(WorthIndexTypeEnum worthIndexTypeEnum, Integer current, Integer size, Double lon, Double lat) {
        WorthSearchQuery worthSearchQuery = new WorthSearchQuery();
        worthSearchQuery.setCenterGeoPoint(new GeoPoint(lat, lon));
        worthSearchQuery.setPage(current, size);
        if (worthIndexTypeEnum != null) {
            switch (worthIndexTypeEnum) {
                case Credit_Type:
                    return getWorthList(worthSearchQuery, "credit", null);
                case Hold_Credit_Type:
                    return getWorthList(worthSearchQuery, "isHoldCredit", null);
                case Newly_Publish_Type:
                    //加这个东西主要是不想被过滤
                    return getWorthList(worthSearchQuery, "publishTime", WorthTypeEnum.OTHER_TYPE);
                case Watch_Type:
                    return getWorthList(worthSearchQuery, "hit", null);
                case Recommend_Type:
                    return getWorthList(worthSearchQuery, "dealsTotal", null);
                case For_You_Type:
                    return getWorthList(worthSearchQuery, "endTime", null);
                case Supply_Type:
                    return getWorthList(worthSearchQuery, "credit", WorthTypeEnum.SKILL_TYPE);
                case Only_You_Type:
                    return getWorthList(worthSearchQuery, "credit", WorthTypeEnum.DEMAND_TYPE);
                case Shape_Happy_Type:
                    return getWorthList(worthSearchQuery, "credit", WorthTypeEnum.MEETING_TYPE);
                case Disconnection_Type:
                    return getWorthList(worthSearchQuery, "credit", WorthTypeEnum.WISH_TYPE);
                case Top_Match_Event:
                    return getWorthList(worthSearchQuery, "regCount", WorthTypeEnum.MATCH_TYPE);

            }
        } else {
            return getWorthList(worthSearchQuery, null, null);
        }
        return new ArrayList<>();
    }

    public void addHistory(WorthTypeEnum typeEnum, String userId, String id) {
        WorthNewlyHistory history = new WorthNewlyHistory();
        history.setWorthTypeEnum(typeEnum);
        history.setUserId(userId);
        history.setId(id);
        RedisKeyName redisKeyName = new RedisKeyName("worthHistory", RedisTypeEnum.OBJECT_TYPE, userId);
        clientRedis().put(redisKeyName.getCommonKey(), history);
    }

    public WorthIndexResponseDto getHistory(String userId, Double lon, Double lat) {
        RedisKeyName redisKeyName = new RedisKeyName("worthHistory", RedisTypeEnum.OBJECT_TYPE, userId);
        WorthNewlyHistory history = (WorthNewlyHistory) clientRedis().get(redisKeyName.getCommonKey());
        WorthIndexResponseDto worthIndexResponseDto = null;
        if (history != null) {
            WorthSearchResponse worthSearchResponse = searchServiceProvider.getWorthSearchService().queryWorthById(history.getId(), history.getWorthTypeEnum().getName());
            if (worthSearchResponse != null) {
                worthIndexResponseDto = createWorthIndexResponseDto(worthSearchResponse);
                if (worthSearchResponse.getLat() != null || worthIndexResponseDto.getLon() != null) {
                    worthIndexResponseDto.setDistance(DistrictUtil.calcDistance(worthSearchResponse.getLat().doubleValue(), worthIndexResponseDto.getLon().doubleValue(), lat, lon));
                }
            }
        }
        return worthIndexResponseDto;
    }

    private List<WorthIndexResponseDto> getWorthList(WorthSearchQuery worthSearchQuery, String key, WorthTypeEnum typeEnum) {
        //添加排序查询规则
        List<String> worthList = new ArrayList<>();
        worthList.add("Meeting");
        worthList.add("Skill");
        worthList.add("Demand");
        worthList.add("Wish");
        worthList.add("Match");
        if (typeEnum != null) {
            if (worthList.contains(typeEnum.getName())) {
                worthSearchQuery.addFristAscQueries(new LastAscQuery("status", true));
                worthSearchQuery.setType(typeEnum.getName());
            }
        }
        if (key != null) {
            worthSearchQuery.addLastAscQuery(new LastAscQuery(key, false));
        }else {
            worthSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
        }
        //选择查询类目，写到153行里面去了
//        if (typeEnum != null) {
//            worthSearchQuery.setType(typeEnum.getName());
//        }
        //创建返回dto集合
        List<WorthIndexResponseDto> result = new ArrayList<>();
        //将查询类设置好。进行详细查询，排序执行语句
        List<WorthSearchResponse> list = null;
        //判断是否title是否为空
        if(StringUtils.isNotBlank(worthSearchQuery.getTitle())) {
            //判断是否包含字母，防止重复
            if(worthSearchQuery.getTitle().matches(".*[a-zA-z].*")) {
                list = new ArrayList<WorthSearchResponse>();
                worthSearchQuery.setTitle(worthSearchQuery.getTitle().toUpperCase());
                List<WorthSearchResponse> listToU = searchServiceProvider.getWorthSearchService().queryWorth(worthSearchQuery, key);
                //小写
                worthSearchQuery.setTitle(worthSearchQuery.getTitle().toLowerCase());
                List<WorthSearchResponse> listToL = searchServiceProvider.getWorthSearchService().queryWorth(worthSearchQuery, key);
                list.addAll(listToU);
                list.addAll(listToL);
            }else{
                list = searchServiceProvider.getWorthSearchService().queryWorth(worthSearchQuery, key);
            }
        }else{
            list = searchServiceProvider.getWorthSearchService().queryWorth(worthSearchQuery, key);
        }


        //添加具体（最终）dto集合
        if (typeEnum==null){
            if (list != null && list.size() > 0) {
                list.forEach(worthSearchResponse -> {
                    if (worthSearchResponse.getWorthType().equals("Meeting")){
                        if (worthSearchResponse.getStatus()!=6){
                            result.add(createWorthIndexResponseDto(worthSearchResponse));
                        }
                    }else if(worthSearchResponse.getWorthType().equals("Wish")){
                        if (worthSearchResponse.getStatus()==4||worthSearchResponse.getStatus()==6||worthSearchResponse.getStatus()==7){
                            result.add(createWorthIndexResponseDto(worthSearchResponse));
                        }
                    }else if(worthSearchResponse.getWorthType().equals("Skill")){
                        if (worthSearchResponse.getStatus()==1||worthSearchResponse.getStatus()==3){
                            result.add(createWorthIndexResponseDto(worthSearchResponse));
                        }
                    }else if (worthSearchResponse.getWorthType().equals("Demand")){
                        if (worthSearchResponse.getStatus()==1||worthSearchResponse.getStatus()==3){
                            result.add(createWorthIndexResponseDto(worthSearchResponse));
                        }
                    }else if (worthSearchResponse.getWorthType().equals("Match")){
                        if (worthSearchResponse.getStatus()>2&&worthSearchResponse.getStatus()<=8){
                            result.add(createWorthMatchIndexResponseDto(worthSearchResponse));
                        }
                    }
                });
            }
        } else if (typeEnum.getName().equals("Meeting")){
            List list1=new ArrayList();
            List list2=new ArrayList();
            List list3=new ArrayList();
            if (list != null && list.size() > 0) {
                list.forEach(worthSearchResponse -> {
                    if (worthSearchResponse.getStatus()==0){
                        result.add(createWorthIndexResponseDto(worthSearchResponse));
                    }
                    if (worthSearchResponse.getStatus()==5){
                        list1.add(createWorthIndexResponseDto(worthSearchResponse));
                    }
                    if (worthSearchResponse.getStatus()==1)
                    {
                        list2.add(createWorthIndexResponseDto(worthSearchResponse));
                    }
                    if (worthSearchResponse.getStatus()==4){
                        list3.add(createWorthIndexResponseDto(worthSearchResponse));
                    }
                });
                result.addAll(list1);
                result.addAll(list2);
                result.addAll(list3);
            }
        }else if(typeEnum.getName().equals("Match")){
            if (list != null && list.size() > 0) {
                list.forEach(worthSearchResponse -> {
                    result.add(createWorthMatchIndexResponseDto(worthSearchResponse));
                });
            }
        }else {
            if (list != null && list.size() > 0) {
                list.forEach(worthSearchResponse -> {
                    result.add(createWorthIndexResponseDto(worthSearchResponse));
                });
            }
        }
        return result;
    }

    private WorthIndexResponseDto createWorthIndexResponseDto(WorthSearchResponse searchResponse) {
        WorthIndexResponseDto worthIndexResponseDto = VoPoConverter.copyProperties(searchResponse, WorthIndexResponseDto.class);
        //  worthIndexResponseDto.setCount(worthServiceprovider.getDemandRegisterService().getRegCount());
        worthIndexResponseDto.setImages(addImgUrlPreUtil.addImgUrlPres(searchResponse.getImages(), AliyunBucketType.ActivityBucket));
        worthIndexResponseDto.setHeadImg(addImgUrlPreUtil.getUserImgPre(searchResponse.getHeadImg()));
        return worthIndexResponseDto;
    }
    private WorthIndexResponseDto createWorthMatchIndexResponseDto(WorthSearchResponse searchResponse) {
        WorthIndexResponseDto worthIndexResponseDto = VoPoConverter.copyProperties(searchResponse, WorthIndexResponseDto.class);
        if(searchResponse.getMatchImageUrl()!=null) {
            if(searchResponse.getMatchImageUrl().size()>0) {
                worthIndexResponseDto.setMatchImageUrl(matchCreateAction.pictures(searchResponse.getMatchImageUrl().get(0)));
            }
        }
        return worthIndexResponseDto;
    }
}
