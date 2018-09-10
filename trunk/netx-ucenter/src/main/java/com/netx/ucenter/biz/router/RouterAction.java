package com.netx.ucenter.biz.router;

import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.redis.service.GeoService;
import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.router.dto.bean.UserPhotosResponseDto;
import com.netx.common.router.dto.select.*;
import com.netx.common.router.enums.ModelEnum;
import com.netx.common.router.enums.SelectConditionEnum;
import com.netx.common.router.enums.SelectFieldEnum;
import com.netx.common.user.model.RedisUser;
import com.netx.common.user.model.UserInfo;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.wz.util.CommonUtil;
import com.netx.searchengine.query.UserFriendSearchQuery;
import com.netx.ucenter.biz.common.GroupAction;
import com.netx.ucenter.biz.common.MessagePushAction;
import com.netx.ucenter.biz.common.TagsAction;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import com.netx.ucenter.biz.user.*;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserPhoto;
import com.netx.ucenter.model.user.UserProfile;
import com.netx.ucenter.service.common.*;
import com.netx.ucenter.service.user.*;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class RouterAction{
    @Autowired
    private UserAction userAction;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserPhotoAction userPhotoAction;
    @Autowired
    private GeoService geoService;
    @Autowired
    private UserVerifyCreditAction userVerifyCreditAction;
    @Autowired
    private UserVerifyAction userVerifyAction;
    @Autowired
    private UserWatchAction userWatchAction;
    @Autowired
    private UserOauthService userOauthService;
    @Autowired

    private UserIncomeService userIncomeService;
    @Autowired
    private UserContributionService userContributionService;
    @Autowired
    private UserBlacklistService userBlacklistService;
    @Autowired
    private MessagePushService messagePushService;
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private ArbitrationService arbitrationService;
    @Autowired
    private CostService costService;
    @Autowired
    private DepositBillService depositBillService;
    @Autowired
    private WalletFrozenService walletFrozenService;
    @Autowired
    private TagsAction tagsAction;
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private SensitiveService sensitiveService;
    @Autowired
    private SensitiveSuggestService sensitiveSuggestService;
    @Autowired
    private ReceivablesOrderService receivablesOrderService;
    @Autowired
    private OtherSetService otherSetService;
    @Autowired
    private LuckyMoneyService luckyMoneyService;
    @Autowired
    private GroupAction groupAction;
    @Autowired
    private ExamineFinanceService examineFinanceService;
    @Autowired
    private ArbitrationResultService arbitrationResultService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ArticleAction articleAction;
    @Autowired
    private BillService billService;
    @Autowired
    private ArticleLimitedService articleLimitedService;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;
    @Autowired
    private RedisInfoHolder redisInfoHolder;
    @Autowired
    private MessagePushAction messagePushAction;
    @Autowired
    private CommonServiceProvider commonServiceProvider;

    private Logger logger = LoggerFactory.getLogger(RouterAction.class);

    //============== 黎子安 start ======================

  /*
    public Boolean checkDeleteUser(String userId) throws Exception {
        return businessProxy.checkUpHaveCompleteGoodsOrder(userId) && clientProxy.checkNetx(userId);
    }
*/

    private RedisCache redisCache;

    private void client(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
    }

    public Boolean deleteUser(String userId){
        User user = userAction.getUserService().selectById(userId);
        try {
            client();
            //删除用户在redis里的用户数据
            RedisKeyName redisKeyName = new RedisKeyName("userInfo", RedisTypeEnum.OBJECT_TYPE,userId);
            redisCache.remove(redisKeyName.getUserKey());
        }catch (Exception e){
            logger.error("redis信息清除失败："+e.getMessage(),e);
        }try {
            userAction.deleteUser(userId);//删除用户基本信息
            /**更新替换删除*/
            //userAction.deleteUserUpdate(user);
            userVerifyCreditAction.deleteUserVerifyCreditByUserId(user);//删除用户与身份证、信用绑定
            userVerifyAction.deleteVerify(userId,user.getMobile());//删除认证信息
            userWatchAction.deleteWatch(userId);//删除关注以及好友关系
            userPhotoAction.getUserPhotoService().delPhotoByUserId(userId);//删除用户照片
            userOauthService.delOauth(userId);//删除第三方信息
            userIncomeService.deleteByUserId(userId);//删除收益记录
            userContributionService.deleteByUserId(userId);//删除贡献流水记录
            userBlacklistService.deleteByUserId(userId);//删除黑名单
            articleAction.deleteByUserId(userId);//删除咨讯
            messagePushAction.delete(userId);//删除极光信息
        }catch (Exception e){
            logger.error("用户信息清除失败："+e.getMessage(),e);
        }try {
            cleanCommond(userId);
        }catch (Exception e) {
            logger.error("通用信息信息清除失败："+e.getMessage(),e);
        }
        return true;
    }

    public void cleanCommond(String userId) throws Exception{
        walletFrozenService.deleteWalletFrozen(userId);
        tagsAction.deleteTag(userId);
        sensitiveSuggestService.delByUserId(userId);
        sensitiveService.delByUserId(userId);
        receivablesOrderService.delByUserId(userId);
        otherSetService.delByUserId(userId);
        luckyMoneyService.delByUserId(userId);
        groupAction.delByUserId(userId);
        suggestionService.delByUserId(userId);
        arbitrationService.delByUserId(userId);
        examineFinanceService.delByUserId(userId);
        evaluateService.delByUserId(userId);
        depositBillService.delByUserId(userId);
        costService.delByUserId(userId);
        billService.delByUserId(userId);
        articleLimitedService.delByUserId(userId);
        areaService.delByUserId(userId);
        arbitrationResultService.delByUserId(userId);
        commonServiceProvider.getWzCommonImHistoryService().deleteImHistory(userId);
    }



    //------ 用户 ------
    /**
     * 按用户id查询音频数、图文数
     * @param userId
     * @return SelectUcenterArticleNumByUserResponseDto
     */
    public SelectUcenterArticleNumByUserResponseDto articleNumByUserId(String userId) throws Exception{
        SelectUcenterArticleNumByUserResponseDto selectUcenterArticleNumByUserResponseDto = new SelectUcenterArticleNumByUserResponseDto();
        selectUcenterArticleNumByUserResponseDto.setImageWriteNum(articleAction.selectArticleCountByUserId(userId,null));
        //selectUcenterArticleNumByUserResponseDto.setVedioNum(articleAction.selectArticleCountByUserId(userId,ArticleTypeEnum.AUDIO_VIDEO));
        selectUcenterArticleNumByUserResponseDto.setVedioNum(0);
        return selectUcenterArticleNumByUserResponseDto;
    }

    /**
     * 用户基本信息
     * @param userId
     * @return
     */
    public SelectUserBeanResponseDto selectUserBeanByUserId(String userId,String fromUserId) throws Exception{
        User user = userAction.getUserService().selectById(userId);
        return selectUserBeanByUserId(user, fromUserId);
    }

    public SelectUserBeanResponseDto selectUserBeanByUserId(User user,String fromUserId) throws Exception{
        SelectUserBeanResponseDto selectUserBeanResponseDto = new SelectUserBeanResponseDto();
        if(user!=null){
            VoPoConverter.copyProperties(user, selectUserBeanResponseDto);
            selectUserBeanResponseDto.setAge(ComputeAgeUtils.getAgeByBirthday(user.getBirthday()));

            UserProfile userProfile = userProfileService.getUserProfileByUserId(user.getId());
            if(userProfile!=null){
                selectUserBeanResponseDto.setOftenIn(userProfile.getOftenIn());
                selectUserBeanResponseDto.setTag(userProfile.getDisposition());
            }
            UserPhotosResponseDto userPhotosResponseDto = userPhotoAction.selectUserPhotos(user.getId());
            selectUserBeanResponseDto.setHeadImgUrl(userPhotosResponseDto.getHeadImgUrl());
            selectUserBeanResponseDto.setImgUrls(userPhotosResponseDto.getImgUrls());
            selectUserBeanResponseDto.setWatch(userWatchAction.getIsWatch(fromUserId,user.getId()));
        }
        return selectUserBeanResponseDto;
    }

    public List<UserInfo> searchGetUserInfo(Double lon,Double lat,Double redaius,Long online,String sex) throws Exception{

        UserFriendSearchQuery userFriendSearchQuery = addUserFriendSearchQuery(new UserFriendSearchQuery(),lon,lat,redaius,online,sex);

        List<UserInfo> infoList = new ArrayList<>();
        //距离和性别
        List<User> userList = getListUserByIds(lon,lat,redaius,sex);
        boolean flag = online==null;
        //在线
        RedisUser redisUser;
        for(User user:userList) {
            if (flag || checkOnline(user.getLastActiveAt().getTime(),online)) {
                infoList.add(toUserInfo(user));
            }
        }
        return infoList;
    }

    private UserFriendSearchQuery addUserFriendSearchQuery(UserFriendSearchQuery userFriendSearchQuery,Double lon,Double lat,Double redaius,Long online,String sex){
        userFriendSearchQuery.setSex(sex);
        userFriendSearchQuery.setOnlineTime(online);
        userFriendSearchQuery.setCenterGeoPoint(new GeoPoint(lat,lon));
        userFriendSearchQuery.setMaxDistance(redaius);
        return userFriendSearchQuery;
    }

    
    public Map<String,UserInfo> searchGetUserInfoMap(Double lon,Double lat,Double redaius,Long online,String sex) throws Exception{
        Map<String,UserInfo> map = new HashMap<>();
        //距离和性别
        List<User> userList = getListUserByIds(lon,lat,redaius,sex);
        boolean flag = online==null;
        //在线
        for(User user:userList) {
            if (flag || checkOnline(user.getLastActiveAt().getTime(),online)) {
                map.put(user.getId(),toUserInfo(user));
            }
        }
        return map;
    }

    private Boolean checkOnline(Long lastActiveAt,Long online) throws Exception{
        if(online == -1){
            return true;
        }
        if(lastActiveAt==null){
            return false;
        }
        return lastActiveAt>=online;
    }

    private List<User> getListUserByIds(Double lon,Double lat,Double redaius,String sex) throws Exception{
        List<String> ids = new ArrayList<>();
        //距离和性别
        if(redaius!=null){
            ids = geoService.nearListUserIds(lon,lat,redaius);
        }
        return userAction.getUserService().selectUserBySex(ids,sex,redaius!=null);
    }

    private boolean checkWish(SelectWishDetailDataResponseDto wish,Integer state,String title,String tag){
        if(state!=null){
            if(wish.getStatus()!=state){
                return false;
            }
        }
        if(title!=null){
            if(!wish.getTitle().equals(title)){
                return false;
            }
        }
        if(tag!=null){
            if(!CommonUtil.labelsAllMatch(tag,wish.getWishLabel())){
                return false;
            }
        }
        return true;
    }

    private UserInfo toUserInfo(User user){
        return VoPoConverter.copyProperties(user,UserInfo.class);
    }

    private boolean checkList(List list){
        return list==null || list.isEmpty();
    }

    private List<String> getIds(Set<String> ids){
        return new ArrayList<>(ids);
    }

    //============== 黎子安 end ======================

    //============== 李卓 start ======================

    
    public List<UserInfoResponseDto> selectUserInfoList(List<String> selectDataList, SelectConditionEnum selectConditionEnum, List<SelectFieldEnum> selectFieldEnumList) throws Exception{
        if(selectDataList.isEmpty()) return null;
        if(selectConditionEnum == null) return null;
        if(selectFieldEnumList.isEmpty()) return null;
        StringBuilder[] fields = selectFieldHandler(selectFieldEnumList, selectConditionEnum);
        List<UserInfoResponseDto> list = selectHandler(selectDataList, selectConditionEnum, fields);
        if(selectDataList.size() != list.size()) throw new RuntimeException("入参数据里含有不存在或不合法数据");
        return list;
    }

    
    public UserInfoResponseDto selectUserInfo(String selectData, SelectConditionEnum selectConditionEnum, List<SelectFieldEnum> selectFieldEnumList) throws Exception{
        if(!StringUtils.hasText(selectData)) return null;
        if(selectConditionEnum == null) return null;
        if(selectFieldEnumList.isEmpty()) return null;
        StringBuilder[] fields = selectFieldHandler(selectFieldEnumList, selectConditionEnum);
        List<String> selectDataList = new ArrayList<>();
        selectDataList.add(selectData);
        List<UserInfoResponseDto> list = selectHandler(selectDataList, selectConditionEnum, fields);
        if(list.isEmpty()){ throw new RuntimeException("入参数据里含有不存在或不合法数据"); }
        return list.get(0);
    }

    //------ 私有 ------

    /**
     * 处理查询字段列表
     * 将列表的枚举转化为相应的值，并存储为 StringBuilder，多个值以逗号隔开
     * @param selectFieldEnumList
     * @param selectConditionEnum
     * @return StringBuilder[]，下标为0：用户表字段枚举值，下标为1：用户详情表字段枚举值，下标为2：用户照片表字段枚举值
     */
    private StringBuilder[] selectFieldHandler(List<SelectFieldEnum> selectFieldEnumList, SelectConditionEnum selectConditionEnum){
        //用户表的 id 必须查，这是查询其他表的通行证
        StringBuilder[] stringBuilder = {new StringBuilder(), new StringBuilder(), new StringBuilder()};
        if(selectConditionEnum == SelectConditionEnum.USER_ID){
            stringBuilder[0].append("id,");
        }else{
            stringBuilder[0].append("id,"+selectConditionEnum.getValue()+",");
        }
        ModelEnum[] modelEnums = ModelEnum.values();
        int enumsLength = modelEnums.length;
        int listLength = selectFieldEnumList.size();
        for (int i = 0; i < listLength; i++) {//遍历定制查询字段
            for(int j = 0; j < enumsLength; j++) {//遍历model枚举
                if (selectFieldEnumList.get(i).getModel() == modelEnums[j].getModel()) {
                    stringBuilder[j].append(selectFieldEnumList.get(i).getValue());
                    stringBuilder[j].append(",");
                    break;
                }
            }
        }
        for(int i = 0; i < enumsLength; i++){
            int length = stringBuilder[i].length();
            if(length != 0){
                stringBuilder[i].setLength(length - 1);//删除最后的逗号
            }
        }

        /*System.out.println("------ 查询字段处理 ------");
        for(int i=0; i<stringBuilder.length; i++)
            System.out.println(i+":"+stringBuilder[i]);*/

        return stringBuilder;
    }

    /**
     * 查询处理
     * @param selectDataList 查询数据
     * @param selectConditionEnum 查询条件
     * @param fields 查询字段
     * @return 获取定制的用户信息
     */
    private List<UserInfoResponseDto> selectHandler(List<String> selectDataList, SelectConditionEnum selectConditionEnum, StringBuilder[] fields) throws Exception{
        List<UserInfoResponseDto> list = new ArrayList<>();
        if(fields.length != 0){
            int selectDataListLength = selectDataList.size();
            List<User> userList = new ArrayList<>();
            List<UserProfile> userProfileList = new ArrayList<>();
            List<UserPhoto> userPhotoList = new ArrayList<>();
            //查询用户数据（必须），因为需要用户表的id或者网号或者手机号
            userList = userAction.getUserService().selectUser(selectDataList, selectConditionEnum, fields[0]);
            int subqueryDataLength = userList.size();
            //获取用户id列表、用户网号列表、用户手机号列表
            List<List<String>> whereDataList = this.getWhereDataList(userList, subqueryDataLength);
            if(whereDataList == null) throw new RuntimeException("入参数据里含有不存在或不合法数据");
            List<String> userIdList = whereDataList.get(0);
            List<String> userNumberList = whereDataList.get(1);
            List<String> mobileList = whereDataList.get(2);
            if(fields[1].length() != 0){
                userProfileList = userProfileService.selectUserProfile(userIdList, fields[1]);
            }
            if(fields[2].length() != 0){
                userPhotoList = userPhotoAction.getUserPhotoService().selectUserPhoto(userIdList, fields[2]);
            }

            /*System.out.println("------------------ 数据测试：userList, userProfileList, userPhotoList ---------------------");
            System.out.println("->userList");
            for(user user : userList)
                System.out.println(user);
            System.out.println("->userProfile");
            for(UserProfile userProfile : userProfileList)
                System.out.println(userProfile);
            System.out.println("->userPhoto");
            for(UserPhoto userPhoto : userPhotoList)
                System.out.println(userPhoto);*/

            //根据条件选择相应的子查询的条件值
            List<String> subqueryDataList = new ArrayList<>();
            switch(selectConditionEnum){
                case USER_ID:
                    subqueryDataList = userIdList;
                    break;
                case USER_NUMBER:
                    subqueryDataList = userNumberList;
                    break;
                case MOBILE:
                    subqueryDataList = mobileList;
                    break;
            }
            //处理子查询结果
            list = this.handleSubqueryData(subqueryDataList, selectDataList, userList, userProfileList, userPhotoList, selectDataListLength, subqueryDataLength);
        }
        return list;
    }

    /**
     * 处理子查询数据，组装成需要查询到的数据列表
     * @param subqueryDataList 子查询出来的用户id列表
     * @param selectDataList 调用者传递过来的条件数据列表
     * @param userList 用户信息列表
     * @param userProfileList 用户详情信息列表
     * @param userPhotoList 用户头像信息列表
     * @param selectDataListLength 调用者数据列表长度
     * @param subqueryDataLength 子查询数据列表长度
     * @return
     */
    private List<UserInfoResponseDto> handleSubqueryData(List<String> subqueryDataList, List<String> selectDataList, List<User> userList, List<UserProfile> userProfileList, List<UserPhoto> userPhotoList, int selectDataListLength, int subqueryDataLength){
        if(subqueryDataList.isEmpty()){ return null; }
        if(selectDataList.isEmpty()){ return null; }
        boolean flag_user = userList.isEmpty();
        boolean flag_userProfile = userProfileList.isEmpty();
        boolean flag_userPhoto = userPhotoList.isEmpty();
        if(flag_user && flag_userProfile && flag_userPhoto){ return null; }
        List<UserInfoResponseDto> list = new ArrayList<>();
        User user;
        UserProfile userProfile;
        UserPhoto userPhoto;

        /*System.out.println("---------------- 处理子查询数据 ----------------");
        System.out.println("需要的数据长度："+selectDataListLength);
        System.out.println("子查询的数据长度："+subqueryDataLength);
        System.out.println("条件值列表："+selectDataList);
        System.out.println("子查询的条件值列表："+subqueryDataList);*/

        for(int i=0; i<selectDataListLength; i++){//需要查询的数据
            for(int j=0; j<subqueryDataLength; j++){//子查询的数据
                if(subqueryDataList.get(j).equals(selectDataList.get(i))) {
                    user = flag_user ? null : userList.get(j);
                    userProfile = flag_userProfile ? null : userProfileList.get(j);
                    userPhoto = flag_userPhoto ? null : userPhotoList.get(j);

                    /* System.out.println("------ 测试循环信息 ------");
                    System.out.println("i:"+i+",j:"+j);
                    System.out.println(user);
                    System.out.println(userProfile);
                    System.out.println(userPhoto);*/

                    UserInfoResponseDto userInfoResponseDto = translateUserInfoResponseDto(user, userProfile, userPhoto);
                    list.add(userInfoResponseDto);
                }
            }
        }
        return list;
    }

    /**
     * 根据用户信息列表获取用户id列表、网号列表、手机号列表
     * 若传参 userList 没有相应数据，则为 null
     * @param userList
     * @param length
     * @return 嵌套列表，0：用户id列表    1：网名列表  2：手机号列表
     */
    private List<List<String>> getWhereDataList(List<User> userList, int length){
        if(userList.isEmpty()) return null;
        List<List<String>> list = new ArrayList<>();
        List<String> userIdList = new ArrayList<>();
        List<String> userNumberList = new ArrayList<>();
        List<String> userMobileList = new ArrayList<>();
        User user;
        for(int i=0; i < length; i++){
            user = userList.get(i);
            userIdList.add(user.getId());
            userNumberList.add(user.getUserNumber());
            userMobileList.add(user.getMobile());
        }
        list.add(userIdList);
        list.add(userNumberList);
        list.add(userMobileList);
        return list;
    }

    /**
     * 将各种信息转化为 UserInfoResponseDto 对象
     * @param user
     * @param userProfile
     * @param userPhoto
     * @return
     */
    private UserInfoResponseDto translateUserInfoResponseDto(User user, UserProfile userProfile, UserPhoto userPhoto){
        if(user == null && userProfile == null && userPhoto == null){ return null; }
        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto();
        if(user != null) {
            //====== 1、用户表 ======
            userInfoResponseDto.setUserId(user.getId());
            //------ 基本信息 ------
            userInfoResponseDto.setUserNumber(user.getUserNumber());
            userInfoResponseDto.setNickname(user.getNickname());
            userInfoResponseDto.setSex(user.getSex());
            Integer age = ComputeAgeUtils.getAgeByBirthday(user.getBirthday());
            userInfoResponseDto.setAge(age);
            userInfoResponseDto.setLv(user.getLv());
            userInfoResponseDto.setUserProfileScore(user.getUserProfileScore());
            //------ 认证信息 ------
            userInfoResponseDto.setMobile(user.getMobile());
            /*userInfoResponseDto.setIdNumber(user.getIdNumber());
            userInfoResponseDto.setVideo(user.getVideo());
            userInfoResponseDto.setCar(user.getCar());
            userInfoResponseDto.setHouse(user.getHouse());
            userInfoResponseDto.setDegree(user.getDegree());*/
            //------ 综合信息 ------
            /*userInfoResponseDto.setEducationLabel(user.getEducationLabel());
            userInfoResponseDto.setProfessionLabel(user.getProfessionLabel());
            userInfoResponseDto.setInterestLabel(user.getInterestLabel());*/
            //------ 网名信息 ------
            userInfoResponseDto.setScore(user.getScore());
            userInfoResponseDto.setCredit(user.getCredit());
            userInfoResponseDto.setValue(user.getValue());
            userInfoResponseDto.setIncome(user.getIncome());
            userInfoResponseDto.setContribution(user.getContribution());
            //------ 其他信息 ------
            userInfoResponseDto.setLockVersion(user.getLockVersion());
            //userInfoResponseDto.setLoginDays(user.getLoginDays());
            userInfoResponseDto.setGiftSetting(user.getGiftSetting());
            userInfoResponseDto.setInvitationSetting(user.getInvitationSetting());
            userInfoResponseDto.setRole(user.getRole());
        }
        if(userProfile != null) {
            //========== 2、用户详情表 ==========
            userInfoResponseDto.setOftenIn(userProfile.getOftenIn());
            userInfoResponseDto.setDisposition(userProfile.getDisposition());
        }
        if(userPhoto != null) {
            //========== 3、用户照片表 ==========
            userInfoResponseDto.setHeadImgUrl(addImgUrlPreUtil.getUserImgPre(userPhoto.getUrl()));
        }
        return userInfoResponseDto;
    }
}
