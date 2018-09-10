package com.netx.ucenter.biz.user;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.common.user.model.StatData;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.biz.router.StatAction;
import com.netx.ucenter.model.user.UserSuggest;
import com.netx.ucenter.service.user.UserService;
import com.netx.ucenter.service.user.UserSuggestService;
import com.netx.ucenter.util.ListToString;
import com.netx.ucenter.util.TupleToList;
import com.netx.ucenter.vo.request.AddScoreInBossRequestDto;
import com.netx.ucenter.vo.response.AddSuggestPassDto;
import com.netx.ucenter.vo.response.ExamineSuggestDto;
import com.netx.ucenter.vo.request.QuerySuggestRequestDto;
import com.netx.ucenter.vo.response.SuggestionsDto;
import com.netx.ucenter.vo.response.UserStatData;
import com.netx.utils.cache.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserSuggestAction {

    private Logger logger = LoggerFactory.getLogger(UserSuggestAction.class);

    @Autowired
    WzCommonImHistoryAction wzCommonImHistoryAction;

    @Autowired
    private UserSuggestService userSuggestService;

    @Autowired
    private UserAction userAction;
    @Autowired
    ScoreAction scoreAction;

    @Autowired
    private StatAction statAction;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisInfoHolder redisInfoHolder;
    @Autowired
    UserScoreAction userScoreAction;

    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    private RedisCache redisCache;

    private RedisCache clientRedis() {
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
        return redisCache;
    }

    /**
     * 添加建议
     *
     * @param userId
     * @param suggest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean add(String userId, String suggest) {
        UserSuggest userSuggest = new UserSuggest();
        userSuggest.setUserId(userId);
        userSuggest.setSuggest(suggest);
        return userSuggestService.insert(userSuggest);
    }

    /**
     * 查询建议排行榜
     *
     * @return
     */
    public Map<String, Object> querySuggestStat(String fromUserId, int start, int end) {
        Set<Tuple> set = clientRedis().zrevrangeWithScores("SuggestStat", 0, -1);
        Map<String, Object> result = new HashMap<>();
        //如果redis取到了数据
        if (set.size() > 0) {
            result.put("list", TupleToList.tupleToList(set, fromUserId, result, start, end));
        }
        else {
            long startTime = System.nanoTime();
            //获取用户详情+url+分数
            List<UserStatData> userStatDatas = userAction.getUserStatData();
            //List<User> userList = userAction.getUserService().getAllUserList();
            List<StatData> StatSuggests = userAction.getSuggestStatData();
            Map<String,Integer> baseMap=new HashMap<>();
            for (int j=0;j<StatSuggests.size();j++){
                baseMap.put(StatSuggests.get(j).getId(),StatSuggests.get(j).getNum());
            }
            if (userStatDatas != null && userStatDatas.size() > 0) {
                userStatDatas.forEach(userStatData -> {
                   // UserStatData userStatData = userAction.queryUserStatData(user);
                    userStatData.setHeadImg(addImgUrlPreUtil.getUserImgPre(userStatData.getHeadImg()));
                   // userStatData.setNum(new BigDecimal(userSuggestService.querySuggestStat(userStatData.getId())));
                    if (baseMap.get(userStatData.getId())!=null){
                        userStatData.setNum(new BigDecimal(baseMap.get(userStatData.getId())));
                    }
                    else {
                        userStatData.setNum(BigDecimal.ZERO);
                    }
                    clientRedis().zaddOne("SuggestStat", userStatData.getNum().doubleValue(), ListToString.objectToJson(userStatData), 600);
                });
            }
            Set<Tuple> set1 = clientRedis().zrevrangeWithScores("SuggestStat", 0, -1);
            if (set1.size() > 0) {
                result.put("list", TupleToList.tupleToList(set1, fromUserId, result, start, end));
            }
            //获取结束时间
            long endTime = System.nanoTime();
            System.out.println("SuggestStat排行榜遍历sql程序运行时间： " + (endTime - startTime) + "ns"+(endTime - startTime)/1000000000+"s");
        }
        return result;
    }

    /***
     *用户建议列表
     *
     */
    public Map<String, Object> userSuggest(QuerySuggestRequestDto suggestDto) {
        int check;
        List<SuggestionsDto> suggestionsDtoList = new ArrayList<>();
        /*用户建议信息**/
        Page<UserSuggest> page = new Page<>();
        page.setCurrent(suggestDto.getCurrentPage());
        page.setSize(suggestDto.getSize());
        Map<String, Object> map = new HashMap<>();
        User userCheck = new User();
        if (suggestDto.getMobile() != null) {
            userCheck = userService.getUserByMobile(suggestDto.getMobile());
        } else {
            if (!suggestDto.getUserNumber().equals("")) {
                userCheck = userService.getUserByUserNumber(suggestDto.getUserNumber());
            }
        }
        if (userCheck != null) {
            List<UserSuggest> suggestList = userSuggestService.tables(suggestDto, userCheck.getId(), page).getRecords();
            if (suggestList != null && !suggestList.isEmpty()) {
                for (UserSuggest userSuggest : suggestList) {
                    User user = userService.selectById(userSuggest.getUserId());
                    if (user == null) {
                        return null;
                    }
                    suggestionsDtoList.add(userMeger(user, userSuggest));
                }
                map.put("list", suggestionsDtoList);
            } else {
                map.put("list", null);
            }
        } else {
            map.put("list", null);
        }
        map.put("total", page.getTotal());
        map.put("pageSize", page.getPages());
        return map;
    }

    private SuggestionsDto userMeger(User user, UserSuggest userSuggest) {
        SuggestionsDto suggestionsDto = new SuggestionsDto();
        if (user != null && userSuggest != null) {
            VoPoConverter.copyProperties(user, suggestionsDto);
            VoPoConverter.copyProperties(userSuggest, suggestionsDto);
        }
        return suggestionsDto;
    }

    /**
     * 审批用户建议
     */
    @Transactional
    public Boolean suggest(ExamineSuggestDto examineSuggestDto, String auditUserName) throws Exception {
        if (examineSuggestDto.getEffective() == 1) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
            String sd = sdf.format(examineSuggestDto.getCreateTime());// 时间戳转换成时间
            // user=new User();
            //user.setId(examineSuggestDto.getUserId());
            //建议通过显示给用户
            wzCommonImHistoryAction.add("999", examineSuggestDto.getUserId(), "您于" + sd + "的建议已通过:" + examineSuggestDto.getResult(), examineSuggestDto.getId(), MessageTypeEnum.USER_TYPE, null, null);
            //userScoreAction.updateUserScore(user,15);
            scoreAction.addScore(examineSuggestDto.getUserId(), StatScoreEnum.SS_SUGGEST_PASS);

            return userSuggestService.suggest(examineSuggestDto, auditUserName);
            //更新redis用户总结分
//            RedisKeyName redisKeyName = new RedisKeyName("userScore", RedisTypeEnum.ZSET_TYPE,null);
//            redisCache.zincrby(redisKeyName.getUserKey(),examineSuggestDto.getUserId(), StatScoreEnum.SS_SUGGEST_PASS.score());
        } else if (examineSuggestDto.getEffective() == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
            String sd = sdf.format(examineSuggestDto.getCreateTime());// 时间戳转换成时间
            //建议不通过显示给用户
            wzCommonImHistoryAction.add("999", examineSuggestDto.getUserId(), "您于" + sd + "的建议不通过:" + examineSuggestDto.getResult(), examineSuggestDto.getId(), MessageTypeEnum.USER_TYPE, null, null);
            return userSuggestService.suggest(examineSuggestDto, auditUserName);
        }
        //更新redis用户总积分
//        RedisKeyName redisKeyName = new RedisKeyName("userScore", RedisTypeEnum.ZSET_TYPE,null);
//        redisCache.zincrby(redisKeyName.getUserKey(),examineSuggestDto.getUserId(), StatScoreEnum.SS_SUGGEST_PASS.score());
        scoreAction.addScore(examineSuggestDto.getUserId(), StatScoreEnum.SS_SUGGEST_PASS);
        return userSuggestService.suggest(examineSuggestDto, auditUserName);
    }

    @Transactional
    public Boolean addSuggestPass(AddSuggestPassDto addSuggestPassDto, String adminId) {
        User user = userAction.selectUserByMobile(addSuggestPassDto.getMobile(), addSuggestPassDto.getUserNumber());
        if (user == null) {
            return false;
        }
        Boolean b = userSuggestService.addSuggestPass(addSuggestPassDto, adminId, user.getId());
        scoreAction.addScore(user.getId(), StatScoreEnum.SS_SUGGEST_PASS);
        return b;
    }

    @Transactional
    public Boolean addScoreInBoss(AddScoreInBossRequestDto addScoreInBossRequestDto) {
        User user = userAction.selectUserByMobile(addScoreInBossRequestDto.getMoblie(), null);
        if (user == null) {
            return false;
        }
        scoreAction.addScore(user.getId(), StatScoreEnum.SS_SUGGEST_PASS);
        return true;
    }

    @Transactional
    public Boolean checkPassword(AddScoreInBossRequestDto addScoreInBossRequestDto){
        String password="houtaiguanliyuan11";
        if (addScoreInBossRequestDto.getPassword().equals(password)){
            return true;
        }else{
            return false;
        }
    }
}




