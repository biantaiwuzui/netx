package com.netx.ucenter.biz.user;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.redis.model.UserGeoRadius;
import com.netx.common.user.constant.ScoreConstant;
import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.user.dto.common.CommonUserBaseInfoDto;
import com.netx.common.user.dto.divideManagement.OperateDivideManagementRequestDto;
import com.netx.common.user.dto.divideManagement.SelectAdminstratorListResponseDto;
import com.netx.common.user.dto.divideManagement.SelectUserByUserNumberResponseDto;
import com.netx.common.user.dto.information.SelectUserListByDetailRequestDto;
import com.netx.common.user.dto.information.UpdateUserBaseInfoRequest;
import com.netx.common.user.dto.information.UpdateUserSettingRequestDto;
import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.dto.user.UserInfoAndHeadWatch;
import com.netx.common.user.dto.wangMing.AddScoreRecordRequestDto;
import com.netx.common.user.dto.wangMing.SelectWangMingListBaseResponseDto;
import com.netx.common.user.dto.wangMing.SelectWangMingListRequestDto;
import com.netx.common.user.enums.*;
import com.netx.common.user.model.RedisUser;
import com.netx.common.user.model.StatData;
import com.netx.common.user.model.UserInfo;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.GetUserListRequestDto;
import com.netx.common.vo.common.StatDataDto;
import com.netx.common.vo.common.UpdateUserRequestDto;
import com.netx.common.vo.common.UserBean;
import com.netx.searchengine.model.UserSearchResponse;
import com.netx.searchengine.query.UserFriendSearchQuery;
import com.netx.searchengine.service.FriendSearchService;
import com.netx.ucenter.biz.common.AliyunPictureAction;
import com.netx.ucenter.biz.common.MessagePushAction;
import com.netx.ucenter.biz.common.WalletAction;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.ucenter.model.user.*;
import com.netx.ucenter.service.friend.FriendsService;
import com.netx.ucenter.service.user.*;
import com.netx.ucenter.util.UserNumberGenerator;
import com.netx.ucenter.vo.request.*;
import com.netx.ucenter.vo.response.SelectUserSettingResponseDto;
import com.netx.ucenter.vo.response.UserStatData;
import com.netx.utils.DistrictUtil;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import org.apache.commons.codec.digest.DigestUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserAction {
    //==================== 公用属性 Start ===================
    private Logger logger = LoggerFactory.getLogger(UserAction.class);
    @Autowired
    private UserService userService;
    @Autowired
    private LoginHistoryService loginHistoryService; //登录记录服务
    @Autowired
    private UserProfileService userProfileService;//用户详情服务
    @Autowired
    private UserProfileAction userProfileAction;
    @Autowired
    private UserPhotoAction userPhotoAction;//照片服务
    @Autowired
    private UserPhotoService userPhotoService;
    @Autowired
    private UserEducationService userEducationService;//用户教育服务
    @Autowired
    private WalletAction walletAction;//钱包服务
    @Autowired
    private UserScoreAction userScoreAction;//用户信用服务
    @Autowired
    private UserVerifyService userVerifyService;
    @Autowired
    private FriendsService friendsService;
    @Autowired
    private UserWatchAction userWatchAction;
    @Autowired
    private MessagePushAction messagePushAction;//极光服务
    @Autowired
    private ScoreAction scoreAction;
    //@Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserOauthAction userOauthAction;

    @Autowired
    private AliyunPictureAction aliyunPictureAction;

    @Autowired
    private FriendSearchService friendSearchService;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    private RedisCache redisCache;

    private Object userLock = new Object();

    @Autowired
    public UserAction(RedisInfoHolder redisInfoHolder) {
        encoder = new BCryptPasswordEncoder();
        logger.info("userAction构造注入服务");
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
    }

    public void setToken(String userId, String token) {
        //该条记录的类型userToken,
        RedisKeyName redisKeyName = new RedisKeyName("userToken", RedisTypeEnum.HSET_TYPE, null);
        //记录名,加用户id,用户token组成缓存
        redisCache.hSet(redisKeyName.getUserKey(), userId, token);
    }

    public String getToken(String userId) {
        RedisKeyName redisKeyName = new RedisKeyName("userToken", RedisTypeEnum.HSET_TYPE, null);
        return (String) redisCache.hGet(redisKeyName.getUserKey(), userId);
    }

    public User updateGeo(User user, Double lon, Double lat, Boolean isUpdate) {
        if (user != null) {
            user.setLat(new BigDecimal(lat));
            user.setLon(new BigDecimal(lon));
            user.setLastActiveAt(new Date());
            if (isUpdate) {
                userProfileAction.updateUserProfileScore(user);
            }
        }
        return user;
    }

    public UserService getUserService() {
        return userService;
    }

    public Integer count() {
        return userService.selectCount(userService.getUserWrapper());
    }

    /**
     * 用户乐观锁操作(更新用户表时，需使用)
     * 为用户添加乐观锁，并返回User对象
     *
     * @param userId 用户id
     * @return user 用户对象
     */
    public User lockVersionOperation(String userId) {
        User user = userService.selectById(userId);
        if (user.getLockVersion() == null) throw new RuntimeException("用户不合法");
        return user;
    }

    /**
     * 添加或调整分工角色（私有）
     *
     * @param user 用户
     * @param list 角色列表
     * @return
     */
    private User operateUserRole(User user, List<UserRoleEnum> list) {
        if (list == null) {//说明此用户被暂停所有权限
            user.setRole("0");
            return user;
        }
        String role = "";
        for (UserRoleEnum userRoleEnum : list) {
            role = role + "," + userRoleEnum.getValue();
        }
        role = role.substring(1);//将第一个逗号删掉
        //System.out.println("角色：" + role);
        user.setRole(role);
        return user;
    }



    public boolean updateUserBaseInfo(UpdateUserBaseInfoRequest request) throws Exception {
        User user = lockVersionOperation(request.getUserId());
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getSex() != null) {
            user.setSex(request.getSex());
        }
        if (request.getBirthday() != null) user.setBirthday(request.getBirthday());
        user.setUpdateUserId(request.getUserId());
        return userProfileAction.updateUserProfileScore(user);
    }

    public Boolean updateUserById(User user) {
        Boolean b=userService.updateById(user);
        updateRedis(queryUserById(user.getId()));
        return b;
    }

    private void updateRedis(User user) {
        RedisKeyName redisKeyName = new RedisKeyName("userInfo", RedisTypeEnum.OBJECT_TYPE, user.getId());
        redisCache.put(redisKeyName.getUserKey(), user);
    }

    public Boolean updateUserByIds(List<User> list) {
        Boolean b=userService.updateBatchById(list);
        list.forEach(user -> {
            updateRedis(queryUserById(user.getId()));
        });
        return b;
    }

    public User checkUserSameName(String nickname) {
        return userService.getUserByOnlyContidion(nickname, "nickname");
    }

    //------ 分工管理 Start ------
    public List<SelectAdminstratorListResponseDto> selectAdministratorList(CommonListDto request) throws Exception {
        Page<User> page = new Page<>(request.getCurrentPage(), request.getSize());
        List<User> userList = userService.selectAdministratorUserPage(true, page).getRecords();
        List<SelectAdminstratorListResponseDto> list = new ArrayList<>();
        for (User user : userList) {
            SelectAdminstratorListResponseDto dto = VoPoConverter.copyProperties(user, SelectAdminstratorListResponseDto.class);
            dto.setUserId(user.getId());
            String identify = userVerifyService.selectUserIdentity(user.getId(), VerifyTypeEnum.IDCARD_TYPE.getValue(), VerifyStatusEnum.PASSING_AUTHENTICATION.getValue());
            if (identify != null) {
                String[] identifyData = identify.split(",");
                dto.setRealName(identifyData[0]);
            }
            list.add(dto);
        }
        return list;
    }

    public SelectUserByUserNumberResponseDto selectUserByUserNumber(String userNumber) throws Exception {
        User user = userService.getUserByUserNumber(userNumber);
        String identity = userVerifyService.selectUserIdentity(user.getId(), VerifyTypeEnum.IDCARD_TYPE.getValue(), VerifyStatusEnum.PASSING_AUTHENTICATION.getValue());
        String realName = null;
        if (identity != null) {
            realName = identity.split(",")[0];
        }
        SelectUserByUserNumberResponseDto response = VoPoConverter.copyProperties(user, SelectUserByUserNumberResponseDto.class);
        response.setUserId(user.getId());
        response.setRealName(realName);
        return response;
    }

    public boolean addManagement(OperateDivideManagementRequestDto request) {
        //这里不能使用 lockVersionOperation 私有方法，updateById 无法令 setAdminUser 生效（可能是框架bug）
        //删除管理员同上，均需要手动进行乐观锁操作
        int lockVersion = userService.selectById(request.getUserId()).getLockVersion();
        User user = new User();
        user.setLockVersion(lockVersion + 1);
        user.setAdminUser(true);
        user = operateUserRole(user, request.getList());
        user.setApprovalTime(new Date());
        return userService.addManagement(user, lockVersion);
    }

    public boolean trimManagement(OperateDivideManagementRequestDto request) {
        User user = lockVersionOperation(request.getUserId());
        user.setUpdateUserId(request.getUserId());
        user = operateUserRole(user, request.getList());
        return updateUserById(user);
    }

    public boolean deleteManagement(List<String> userIdList) {
        int size = userIdList.size();
        for (int i = 0; i < size; i++) {
            String userId = userIdList.get(i);
            User user = new User();
            user.setId(userId);
            user.setUpdateUserId(userId);
            user.setAdminUser(false);
            user.setRole("0");
            updateUserById(user);
        }
        return true;
    }
    //------ 分工管理 End ------

    @Transactional
    public List<SelectWangMingListBaseResponseDto> selectWangMingList(SelectWangMingListRequestDto request) throws Exception {
        Page<User> page = new Page<>(request.getCommonListDto().getCurrentPage(), request.getCommonListDto().getSize());
        List<User> poList = userService.selectUsersByWangMing(page, request.getWangMingEnum());

        //组装 response
        List<SelectWangMingListBaseResponseDto> response = new ArrayList<SelectWangMingListBaseResponseDto>();
        //UserGeo userGeo = null;
        for (User user : poList) {
            String userId = user.getId();
            String headImgUrl = userPhotoService.selectHeadImg(userId);
            String tag = userProfileService.getUserTag(userId, "disposition");
            SelectWangMingListBaseResponseDto vo = VoPoConverter.copyProperties(user, SelectWangMingListBaseResponseDto.class);
            vo.setUserId(userId);
            vo.setHeadImgUrl(headImgUrl);
            vo.setTag(tag);
            vo.setLon(user.getLon().doubleValue());
            vo.setLat(user.getLat().doubleValue());
            response.add(vo);
        }
        return response;
    }

    @Transactional
    public List<UserSynopsisData> searchUserListByDetail(SelectUserListByDetailRequestDto request, String userId, Double lon, Double lat) throws Exception {
        UserFriendSearchQuery query = VoPoConverter.copyProperties(request, UserFriendSearchQuery.class);
        query.setPage(request.getPage().getCurrentPage(), request.getPage().getSize());
        query.setExcludeUserId(userId);
        if (request.getRadius() != null) {
            query.setMaxDistance(request.getRadius().doubleValue());
        }
        if (lat != null && lon != null) {
            query.setCenterGeoPoint(new GeoPoint(lat, lon));
        }
        List<UserSynopsisData> list = new ArrayList<>();
        List<UserSearchResponse> responseList = friendSearchService.queryFriends(query);
        if (responseList.size() > 0) {
            responseList.forEach(userSearchResponse -> {
                        list.add(SearchResponseToSynopsisData(userSearchResponse, userId));
                    }
            );
        }
        return list;
    }

    private UserSynopsisData SearchResponseToSynopsisData(UserSearchResponse response, String userId) {
        UserSynopsisData userSynopsisData = VoPoConverter.copyProperties(response, UserSynopsisData.class);
        userSynopsisData.setWatch(userWatchAction.getIsWatch(userId, response.getId()));
        userSynopsisData.setHeadImgUrl(addImgUrlPreUtil.getUserImgPre(userSynopsisData.getHeadImgUrl()));
        return userSynopsisData;
    }

    public List<CommonUserBaseInfoDto> selectUserBaseInfoByUserId(List<String> userIdList) throws Exception {
        if (userIdList.isEmpty()) {
            return null;
        }
        List<User> userList = userService.getUsersByIds(userIdList, null);
        List<CommonUserBaseInfoDto> list = new ArrayList<CommonUserBaseInfoDto>();//新列表，记录返回值
        if (userList != null && !userList.isEmpty()) {
            Map<String, CommonUserBaseInfoDto> map = createCommonUserBaseInfoDtoMap(userList, userPhotoAction.selectHeadImgMap(userIdList));
            userIdList.forEach(userId -> {
                CommonUserBaseInfoDto dto = (CommonUserBaseInfoDto) getMapValue(map, userId);
                if (dto != null) {
                    list.add(dto);
                }
            });
        }
        return list;
    }

    private Map<String, CommonUserBaseInfoDto> createCommonUserBaseInfoDtoMap(List<User> users, Map<String, String> headImgs) {
        Map<String, CommonUserBaseInfoDto> map = new HashMap<>();
        users.forEach(user -> {
            try {
                map.put(user.getId(), createCommonUserBaseInfoDto(user, (String) getMapValue(headImgs, user.getId())));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
        return map;
    }

    private Object getMapValue(Map map, String key) {
        return map.containsKey(key) ? map.get(key) : null;
    }

    private CommonUserBaseInfoDto createCommonUserBaseInfoDto(User user, String headImg) throws Exception {
        CommonUserBaseInfoDto commonUserBaseInfoDto = VoPoConverter.copyProperties(user, CommonUserBaseInfoDto.class);
        commonUserBaseInfoDto.setUserId(user.getId());
        commonUserBaseInfoDto.setHeadImgUrl(headImg);
        commonUserBaseInfoDto.setMobile(hideMobile(user.getMobile()));
        commonUserBaseInfoDto.setIdNumber(toStatus(userVerifyService.getVerifyState(user.getId(), 1)));
        commonUserBaseInfoDto.setVideo(toStatus(userVerifyService.getVerifyState(user.getId(), 2)));
        commonUserBaseInfoDto.setCar(toStatus(userVerifyService.getVerifyState(user.getId(), 3)));
        commonUserBaseInfoDto.setHouse(toStatus(userVerifyService.getVerifyState(user.getId(), 4)));
        commonUserBaseInfoDto.setDegree(toStatus(userVerifyService.getVerifyState(user.getId(), 5)));
        return commonUserBaseInfoDto;
    }

    public UserBean changeUserByVerify(String userId) throws Exception {
        User user = userService.selectById(userId);
        UserBean userBean = new UserBean();
        if (user != null) {
            changeUserBean(userBean, user);
        }
        return userBean;
    }

    private void changeUserBean(UserBean userBean, User user) throws Exception {
        userBean.setId(user.getId());
        userBean.setBirthday(user.getBirthday());
        userBean.setUserNumber(user.getUserNumber());
        userBean.setSex(user.getSex());
        userBean.setMobile(hideMobile(user.getMobile()));
        userBean.setNickname(user.getNickname());
        userBean.setName(user.getRealName());
        userBean.setIdNumber(getStatus(user.getIdNumber(), user.getId(), 1));
        userBean.setVideo(getStatus(user.getVideo(), user.getId(), 2));
        userBean.setCar(getStatus(user.getCar(), user.getId(), 3));
        userBean.setHouse(getStatus(user.getHouse(), user.getId(), 4));
        userBean.setDegree(getStatus(user.getDegree(), user.getId(), 5));
    }

    private String getStatus(String verify, String id, Integer type) throws Exception {
        if (verify != null) {
            return verify;
        }
        return toStatus(userVerifyService.getVerifyState(id, type));
    }

    public User changeUserByVerify(User user) throws Exception {
        user.setIdNumber(toStatus(userVerifyService.getVerifyState(user.getId(), 1)));
        user.setVideo(toStatus(userVerifyService.getVerifyState(user.getId(), 2)));
        user.setCar(toStatus(userVerifyService.getVerifyState(user.getId(), 3)));
        user.setHouse(toStatus(userVerifyService.getVerifyState(user.getId(), 4)));
        user.setDegree(toStatus(userVerifyService.getVerifyState(user.getId(), 5)));
        return user;
    }

    private String hideMobile(String mobile) {
        if (mobile == null || mobile.isEmpty()) {
            return null;
        }
        return mobile.substring(0, 3) + "********";
    }

    private String toStatus(Integer status) {
        if (status != null) {
            switch (status) {
                case 0:
                    return "待审核";
                case 1:
                    return "已认证";
                case 2:
                    return "认证没通过";
            }
        }
        return null;
    }

    public boolean updateUserSetting(UpdateUserSettingRequestDto request) {
        User user=new User();
        user.setId(request.getUserId());
        boolean flag_giftSetting = (request.getGiftSettingEnum() != null);
        boolean flag_invitationSetting = (request.getInvitationSettingEnum() != null);
        boolean flag_articleSetting = (request.getArticleSettingEnum() != null);
        boolean flag_nearlySetting = request.getNearlySettingEnum() != null;
        if (flag_giftSetting) {
            user.setGiftSetting(request.getGiftSettingEnum().getValue());
        }
        if (flag_invitationSetting) {
            user.setInvitationSetting(request.getInvitationSettingEnum().getValue());
        }
        if (flag_articleSetting) {
            user.setArticleSetting(request.getArticleSettingEnum().getValue());
        }
        if (flag_nearlySetting) {
            user.setNearlySetting(request.getNearlySettingEnum().getValue());
        }
        if (request.getVoiceSetting() != null) {
            user.setVoiceSetting(request.getVoiceSetting() ? 1 : 0);
        }
        if (request.getShockSetting() != null) {
            user.setShockSetting(request.getShockSetting() ? 1 : 0);
        }
        return updateUserById(user);
    }

    public SelectUserSettingResponseDto selectUserSetting(String userId) {
        User user = userService.selectById(userId);
        if (user == null) return null;
        return VoPoConverter.copyProperties(user, SelectUserSettingResponseDto.class);
    }

    public Boolean verifyUserSetting(String fromUserId, String toUserId, Integer wzType) throws Exception {
        // 获取用户的设置
        SelectUserSettingResponseDto selectUserSettingResponseDto = selectUserSetting(toUserId);
        Integer giftSetting = selectUserSettingResponseDto.getGiftSetting();
        Integer invitationSetting = selectUserSettingResponseDto.getInvitationSetting();

        // 记录判断值，拒绝的值不用记录，因为初始化为 false
        boolean flag_gift = false; // 记录是否可以接受礼物
        boolean flag_invitation = false; // 记录是否可以接受邀请
        boolean flag_gift_friends = (giftSetting == GiftSettingEnum.ACCEPT_FRIENDS.getValue());
        boolean flag_gift_watch_to = (giftSetting == GiftSettingEnum.ACCEPT_WATCH_TO.getValue());
        boolean flag_invitation_friends = (invitationSetting == InvitationSettingEnum.ACCEPT_FRIENDS.getValue());
        boolean flag_invitation_watch_to = (invitationSetting == InvitationSettingEnum.ACCEPT_WATCH_TO.getValue());

        // 礼物
        if (WzTypeEnum.GIFT.getValue() == wzType) {
            if (flag_gift_friends) {
                flag_gift = friendsService.checkFriendOne(fromUserId, toUserId);
            }
            if (flag_gift_watch_to) {
                flag_gift = (userWatchAction.checkWatch(toUserId, fromUserId) != null);
            }
            return flag_gift;
        }

        // 邀请
        if (WzTypeEnum.INVITATION.getValue() == wzType) {
            if (flag_invitation_friends) {
                flag_invitation = friendsService.checkFriendOne(fromUserId, toUserId);
            }
            if (flag_invitation_watch_to) {
                flag_invitation = (userWatchAction.checkWatch(toUserId, fromUserId) != null);
            }
            return flag_invitation;
        }
        return false;
    }

    public boolean lockUser(String userId, boolean isLock) throws Exception {
        User user = userService.selectById(userId);
        if (user == null) {
            throw new RuntimeException("此用户不存在");
        }
        user.setLock(isLock);
        return updateUserById(user);
    }

    /*
     * userVo类转user实体类
     * @param 输入userVo（AddWzUserLoginRequest）
     * @return user
     */
    private User userVoToUserEntity(AddWzUserLoginRequest userVo) {
        User user = new User();
        user.setMobile(userVo.getMobile());
        user.setNickname(userVo.getNickName());
        user.setLon(userVo.getLon());
        user.setLat(userVo.getLat());
        user.setLastActiveAt(new Date());
        user.setSex(userVo.getSex());
        return user;
    }

    /*
     * 生成网号
     * @param 输入网号（long长整型）
     * @return {true:含有,false:不含有}
     */
    @Transactional(rollbackFor = Exception.class)
    String buildNumber() {
        String id = "";
        //生成网号并进行网号校检
        do {
            //随机生成网号
            //1001010110为最小网号，9989979969为最大网号(8988969860=9989979969-1001010110+1)
            id = 1001010110l + (long) (Math.random() * 8988969860l) + "";
        } while (checkNumber(id));//进行网号校检
        return id;
    }

    /*
     * 网号校检
     * @param 输入网号（long长整型）
     * @return {true:含有,false:不含有}
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean checkNumber(String id) {
        UserNumberGenerator userNumberGenerator = new UserNumberGenerator();
        int i, j;
        //检测网号是否唯一
        if (checkOnly(id)) {
            return true;
        }
        //检测网号是否有幸运数段
        if (userNumberGenerator.checkLuck(id)) {
            return true;
        }
        String checkNum;
        //检测网号是否有重复数段或顺序数段或是否存在循环重复数段
        for (i = 3; i < userNumberGenerator.getNumberLen(); i++) {
            for (j = 0; j + i <= userNumberGenerator.getNumberLen(); j++) {
                checkNum = id.substring(j, i + j);
                //检测网号有没有重复数段或顺序数段
                if (userNumberGenerator.checkOrder(checkNum, i))
                    return true;
                //检测网号是否存在循环重复数段
                //if((numStr.substring(i+j,numberLen).indexOf(checkNum)!=-1)) return true;
                if ((id.indexOf(checkNum, i + j) != -1))
                    return true;
            }
        }
        return false;
    }

    /*
     * 检测网号是否唯一
     * @param 输入网号（long长整型）
     * @return {true:含有,false:不含有}
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean checkOnly(String num) {
        int count = userService.checkNumber(num);
        return count == 0 ? false : true;
    }

    /**
     * 密码加密
     *
     * @param salt
     * @param password
     * @return
     */
    public String md5Password(String salt, String password) {
        return DigestUtils.md5Hex(salt + password);
    }

    /**
     * 添加用户信息
     *
     * @param userVo
     * @return user
     */
    public User addUser(AddWzUserLoginRequest userVo) throws Exception {
        User user = userVoToUserEntity(userVo);
        user = addUserOtherData(user);
        user.setLock(false);
        //user.setPassword(md5Password(user.getSalt(),userVo.getPassWord()));
        user.setPassword(encoder.encode(userVo.getPassWord()));
        user.setBirthday(userVo.getBirthday());
        return userService.insert(user) ? user : null;
    }

    /**
     * 添加用户固定基本信息
     *
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public User addUserOtherData(User user) {
        user.setUserNumber(buildNumber());
        user.setLoginDays(0);
        user.setCredit(100);
        user.setLockVersion(0);
        user.setScore(BigDecimal.ZERO);
        user.setLastCompletePercent(BigDecimal.ZERO);
        user.setUserProfileScore(0);
        user.setCreateTime(new Date());
        return user;
    }

    /**
     * 添加用户详情
     *
     * @param userId
     * @return true 插入成功 false 插入失败
     */
    public boolean addUserProfileByUserId(String userId) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userId);
        userProfile.setCreateUserId(userId);
        return userProfileService.insert(userProfile);
    }

    /**
     * @param userVo
     * @return true 注册成功 false 注册失败
     */
    @Transactional(readOnly = false)
    public User register(AddWzUserLoginRequest userVo) throws Exception {
        User user = selectUserByMobile(userVo.getMobile(), null);
        if (user != null) {
            user.setMobile(null);
            return user;
        }
        return successRegister(addUser(userVo));
    }

    /**
     * 成功注册后的操作
     *
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public User successRegister(User user) {
        if (user != null) {
            user.setCreateUserId(user.getId());
            user.setUpdateUserId(user.getId());
            String password = messagePushAction.addUser(user.getId());
            if (password != null) {
                user.setJmessagePassword(password);
                user.setRegJMessage(true);//极光注册成功后修改此字段
            }
            if (userService.updateById(user) && addUserProfileByUserId(user.getId())) {
                userProfileAction.updateUserProfileScore(user.getId());//用户资料计分
                walletAction.queryWalletByUserId(user.getId());
            } else {
                user.setId(null);
            }
        }
        return user;
    }

    public User fristOauthUser(OauthLoginRequetDto oauthLoginRequetDto) {
        synchronized (userLock) {
            return fristOauthUserInner(oauthLoginRequetDto);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    User fristOauthUserInner(OauthLoginRequetDto oauthLoginRequetDto) {
        UserOauth userOauth = userOauthAction.getUserOauthService().queryOauth(oauthLoginRequetDto.getOauthId(), 1);
        User user = null;
        if (userOauth != null) {
            if (StringUtils.isEmpty(oauthLoginRequetDto.getUserId())) {
                user = userService.getUserByMobile(oauthLoginRequetDto.getMobile());
                if (user == null) {
                    user = addOauthUser(oauthLoginRequetDto.getMobile(), oauthLoginRequetDto.getNickName(),
                            toSex(userOauth.getOauthType(), oauthLoginRequetDto.getSex()), oauthLoginRequetDto.getLon(), oauthLoginRequetDto.getLat());
                    if (user != null) {
                        try {
                            userPhotoAction.addHeadImg(user.getId(), changeOthersUrl(oauthLoginRequetDto.getHeadImg()));
                        } catch (Exception e) {
                            logger.info("头像获取失败");
                        }
                    }
                }
                if (user != null) {
                    userOauthAction.updateOauth(user.getId(), userOauth);
                    userProfileAction.updateUserProfileScore(user.getId());
                    //授权登录成功后，添加微信支付账号
                    if (userOauth.getOauthType().equals(1)) {
                        walletAction.changeWechatUsedAccount(user.getId(), oauthLoginRequetDto.getNickName(), userOauth.getOpenId());
                    }
                }
            } else {
                user = userService.selectById(oauthLoginRequetDto.getUserId());
            }
        }

        return user;
//        User user = null;
//        if(userOauth!=null){
//            if(StringUtils.isEmpty(oauthLoginRequetDto.getUserId())){
//                user = userService.getUserByMobile(oauthLoginRequetDto.getMobile());
//                if(user == null) {
//                    user = addOauthUser(oauthLoginRequetDto.getMobile(), oauthLoginRequetDto.getNickName(),
//                            toSex(userOauth.getOauthType(), oauthLoginRequetDto.getSex()), oauthLoginRequetDto.getLon(), oauthLoginRequetDto.getLat());
//                    if(user != null){
//                        userOauthAction.updateOauth(user.getId(),userOauth);
//                        try {
//                            userPhotoAction.addHeadImg(user.getId(),changeOthersUrl(oauthLoginRequetDto.getHeadImg()));
//                        }catch (Exception e){
//                            logger.info("头像获取失败");
//                        }
//                        userProfileAction.updateUserProfileScore(user.getId());
//                    }
//                }else {
//                    userOauthAction.updateOauth(user.getId(),userOauth);
//                }
//            }else{
//                user = userService.selectById(oauthLoginRequetDto.getUserId());
//            }
//        }
//        return user;
    }

    private String toSex(int type, String sex) {
        if (type == 1 || type == 3) {
            return sex.equals("2") ? "女" : "男";
        }
        if (type == 2) {
            return sex.equals("F") ? "女" : "男";
        }
        return sex;
    }

    @Transactional(rollbackFor = Exception.class)
    public User queryOauthUser(String nickName, String sex, BigDecimal lon, BigDecimal lat, String opendId, int type, String headImg) {
        UserOauth userOauth = userOauthAction.checkOtherUser(opendId, type);
        User user;
        //第一次授权
        if (userOauth == null) {
            userOauth = userOauthAction.addOauthService(null, opendId, type, 1);
            if (userOauth == null) {
                return null;
            }
        }
        if (StringUtils.isEmpty(userOauth.getUserId())) {
            user = addOauthUser(null, nickName, sex, lon, lat);
            if (user != null) {
                userOauthAction.updateOauth(user.getId(), userOauth);
                try {
                    userPhotoAction.addHeadImg(user.getId(), changeOthersUrl(headImg));
                } catch (Exception e) {
                    logger.info("头像获取失败");
                }
            }
        } else {
            user = userService.selectById(userOauth.getUserId());
        }
        return user;
    }

    /**
     * 将第三方URL转入到本地URL中
     *
     * @param url
     * @return
     */
    private String changeOthersUrl(String url) throws Exception {
        InputStream is = aliyunPictureAction.downloadNetPicture(url);
        return is == null ? null : aliyunPictureAction.uploadNetPicture(is, AliyunBucketType.UserBucket);
    }

    /**
     * 生成第三登陆的本平台用户
     *
     * @param mobile
     * @param nickName
     * @param sex
     * @param lon
     * @param lat
     * @return user
     */
    @Transactional(rollbackFor = Exception.class)
    public User addOauthUser(String mobile, String nickName, String sex, BigDecimal lon, BigDecimal lat) {
        logger.info("addOauthUser");
        User user = new User();
        user = addUserOtherData(user);
        user.setSex(sex);
        user.setNickname(nickName);
        user.setMobile(mobile);
        user.setLastActiveAt(new Date());
        user.setLat(lat);
        user.setLon(lon);
        if (userService.insert(user)) {
            return successRegister(user);
        }
        return null;
    }

    /**
     * 添加登录记录
     *
     * @param userId
     * @param time
     * @return
     */
    public boolean addLoginHistory(String userId, Date time, BigDecimal lon, BigDecimal lat) {
        UserLoginHistory loginHistory = new UserLoginHistory();
        loginHistory.setUserId(userId);
        loginHistory.setCreateUserId(userId);
        loginHistory.setLoginAt(time);
        loginHistory.setLat(lat);
        loginHistory.setLon(lon);
        return loginHistoryService.insert(loginHistory);
    }

    /**
     * 判断day与curryDay的关系
     *
     * @param day      需判断的时间
     * @param curryDay 当前时间
     * @return 0 系统错误,1 昨今关系,2 从前关系,3 将来关系
     */
    public int judgeTimeRelationship(long day, long curryDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(sdf.format(curryDay));
            if (day < date.getTime()) {
                if (day >= (date.getTime() - 24 * 60 * 60 * 1000)) {
                    return 1;//昨今关系
                } else {
                    return 2;//从前关系
                }
            }
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return 0;//系统错误
        }
        return 3;//将来关系
    }

    @Transactional(rollbackFor = Exception.class)
    public User calculateScoreByDay(int day, User user) {
        AddScoreRecordRequestDto addScoreRecordRequestDto = getAddScoreRecord(user);
        if (day >= 30) addScoreRecordRequestDto.setCode(5);
        else if (day >= 11) addScoreRecordRequestDto.setCode(4);
        else if (day >= 5) addScoreRecordRequestDto.setCode(3);
        else if (day >= 1) addScoreRecordRequestDto.setCode(2);
        else return user;
        userScoreAction.addScoreRecord(addScoreRecordRequestDto, user);
        user.setScore(user.getScore().add(ScoreConstant.CODE_MAP_SCORE.get(addScoreRecordRequestDto.getCode())));
        return user;
    }

    private AddScoreRecordRequestDto getAddScoreRecord(User user) {
        AddScoreRecordRequestDto addScoreRecordRequestDto = new AddScoreRecordRequestDto();
        addScoreRecordRequestDto.setUserId(user.getId());
        addScoreRecordRequestDto.setRelatableId(user.getId());
        addScoreRecordRequestDto.setRelatableType("User");
        return addScoreRecordRequestDto;
    }

    private boolean checkLock(Boolean lock) {
        return lock == null || !lock;
    }

    /**
     * 登录成功进行的操作
     *
     * @param user
     * @param time
     * @return null 系统异常,
     */
    @Transactional(rollbackFor = Exception.class)
    public User successLogin(User user, Date time, BigDecimal lon, BigDecimal lat) {
        //判断用户是否被封
        if (checkLock(user.getLock())) {
            //添加登录记录是否成功
            if (addLoginHistory(user.getId(), time, lon, lat)) {
                //判断是否为第一次登录
                if (user.getLoginDays() != 0) {
                    //判断是否为连续登录天数
                    switch (judgeTimeRelationship(user.getLastLoginAt().getTime(), time.getTime())) {
                        case 0://系统异常
                            return null;
                        case 1://进行连续登录操作
                            user.setLoginDays(user.getLoginDays() + 1);
                            break;
                        case 2://连续登录重置
                            user.setLoginDays(1);
                            break;
                    }
                } else {
                    user.setLoginDays(1);
                }
                user.setLogin(true);
                if (lon.doubleValue() != 0 && lat.doubleValue() != 0) {
                    user.setLon(lon);
                    user.setLat(lat);
                }
                user.setLastActiveAt(time);
                user.setLastLoginAt(time);
                user.setUpdateTime(time);
                //将没有极光注册成功的重新注册到极光
                if (user.getRegJMessage() == null || !user.getRegJMessage()) {
                    String password = messagePushAction.addUser(user.getId());
                    if (password != null) {
                        user.setJmessagePassword(password);
                        user.setRegJMessage(true);//极光注册成功后修改此字段
                    }
                }
                //修改用户信息里的所有时间
                if (!updateUserById(user)) {
                    user.setId(null);
                }
                user = calculateScoreByDay(user.getLoginDays(), user);
            } else {
                user.setId(null);
            }
        }
        return user;
    }

    /**
     * 用户普通登录
     *
     * @param request
     * @return （user.id=null 系统异常，user=null 登录失败）除此之外 表示以获取全部用户信息
     */
    @Transactional(readOnly = false)
    public User loginUser(WzUserLoginRequest request) throws Exception {
        Date time = new Date();
        User user = userService.getUserByUserNumberOrMobile(request.getAccount(), request.getAccount());
        if(user==null){
            if(request.getAccount().matches("1[3-9]\\d{9}")){
                request.setAccount("0086"+request.getAccount());
                user = userService.getUserByUserNumberOrMobile(request.getAccount(), request.getAccount());
            }
        }
        if (user != null) {
            if (!StringUtils.isEmpty(user.getPassword())) {
                //加密密码与登录密码比对
                if (encoder.matches(request.getPassword(), user.getPassword())) {
                    //进行登录成功的操作
                    logger.info("登录成功");
                    return successLogin(user, time, request.getLon(), request.getLat());
                } else {
                    return null;
                }
            }
        }
        return user;
    }

    /**
     * 后台登录
     *
     * @param request
     * @return
     */
    public String loginAdmin(WzUserLoginRequest request) throws Exception {
        User user = userService.selectById(request.getAccount());
        if (user != null) {
            if (user.getAdminUser() && encoder.matches(request.getPassword(), user.getAdminPassword())) {
                user.setLoginBackend(true);
                updateUserById(user);
                return null;//验证成功
            }
            return "验证失败！非管理员或密码错误";//不是管理员或密码不正确
        }
        return "验证失败！用户不存在";
    }

    /**
     * 后台登录
     *
     * @param userNumber
     * @param password
     * @return
     * @throws Exception
     */
    public String loginAdmin(String userNumber, String password) throws Exception {
        User user = getUserByUserNumber(userNumber);
        Date date = new Date();
        if (user != null) {
            if (user.getAdminUser() && encoder.matches(password, user.getAdminPassword())) {
                user.setLastActiveAt(date);
                user.setLoginBackend(true);
                updateUserById(user);
                return null;//验证成功
            }
            return "验证失败！非管理员或密码错误";//不是管理员或密码不正确
        }
        return "验证失败！用户不存在";
    }

    /**
     * 检验密码是否重复
     *
     * @param password
     * @param newPassword
     * @return true 相同 false 不同
     */
    public boolean checkPWD(String password, String newPassword) {
        return (!(password == null || password.isEmpty()) && newPassword.equals(password)) ? true : false;
    }


    public boolean checkPwd(WzPassWordBaseRequest request) throws Exception {
        User user = userService.selectById(request.getUserId());
        if (user == null) {
            return false;
        }
        //request.setPassword(md5Password(user.getSalt(),request.getPassword()));
        switch (request.getType()) {
            case USER_TYPE:
                //检测密码是否与管理员密码重复
                if (encoder.matches(request.getPassword(), user.getPassword())) {
                    return true;
                }
                break;
            case PAY_TYPE:
                //检测密码是否与管理员密码重复
                if (encoder.matches(request.getPassword(), user.getPayPassword())) {
                    return true;
                }
                break;
            case ADMIN_TYPE:
                //检测是否有管理员权限
                if (user.getAdminUser() && encoder.matches(request.getPassword(), user.getAdminPassword())) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * 密码修改
     *
     * @param request
     * @return 用户信息;
     */
    public String updatePWD(WzPassWordRequest request) throws Exception {
        Date time = new Date();
        User user = userService.selectById(request.getUserId());
        if (user == null) {
            return "账号不存在";
        }
        //String password=md5Password(user.getSalt(),);
        switch (request.getType()) {
            case USER_TYPE:
                //检测密码是否与管理员密码重复
                if (user.getAdminUser() && encoder.matches(request.getPassword(), user.getAdminPassword())) {
                    return "登录密码与管理员密码不能一样";
                }
                //检测密码是否与支付密码重复
                if (encoder.matches(request.getPassword(), user.getPayPassword())) {
                    return "登录密码与支付密码不能一样";
                }
                user.setPassword(encoder.encode(request.getPassword()));
                break;
            case PAY_TYPE:
                //检测密码是否与管理员密码重复
                if (user.getAdminUser() && encoder.matches(request.getPassword(), user.getAdminPassword())) {
                    return "管理员密码与支付密码不能一样";
                }
                //检测密码是否与登录密码重复
                if (encoder.matches(request.getPassword(), user.getPassword())) {
                    return "登录密码与支付密码不能一样";
                }
                user.setPayPassword(encoder.encode(request.getPassword()));
                break;
            case ADMIN_TYPE:
                //检测是否有管理员密码
                if (user.getAdminPassword() != null && !encoder.matches(request.getOldPassword(), user.getAdminPassword())) {
                    return "旧密码错误";
                }
            case ADMIN_FORGET_TYPE:
                //检测是否有管理员权限
                if (!user.getAdminUser()) {
                    return "你不是管理员，不能修改管理员密码";
                }
                //检测密码是否重复
                if (encoder.matches(request.getPassword(), user.getPayPassword())) {
                    return "管理员密码与支付密码不能一样";
                }
                if (encoder.matches(request.getPassword(), user.getPassword())) {
                    return "登录密码与管理员密码不能一样";
                }
                user.setAdminPassword(encoder.encode(request.getPassword()));
                break;
        }
        user.setUpdateTime(time);
        user.setUpdateUserId(request.getUserId());
        return updateUserById(user) ? null : "密码修改失败";
    }

    public User selectUserByMobile(String mobile, String userNumber) {
        if (userService.getUserByOnlyContidion(mobile, userNumber) != null) {
            return userService.getUserByOnlyContidion(mobile, userNumber);
        }
        return null;
    }

    @Transactional(readOnly = true)
    public UserSynopsisData queryUserFriendsByUserNumber(String userId, String userNumber) throws Exception {
        /*User user = getUserByUserNumber(userNumber);
        if(user!=null){
            if(friendsService.checkFriend(userId,user.getId())){
                return userToUserSynopsisData(user,null,null);
            }
        }*/
        User user = userService.queryUserFriendsByUserNumber(userId, userNumber);
        if (user != null) {
            return userToUserSynopsisData(user, null, null, null);
        }
        return new UserSynopsisData();
    }

    public List<User> selectUserByMobiles(String condition, int type, Page<User> userPage) {
        return userService.selectUserByMobiles(condition, type, userPage);
    }


    public List<UserSynopsisData> selectUserByMobiles(String condition, int type, Page<User> userPage, Double lon, Double lat, String userId) throws Exception {
        List<User> users = selectUserByMobiles(condition, type, userPage);
        List<UserSynopsisData> list = new ArrayList<>();
        users.forEach(user -> {
            UserSynopsisData userSynopsisData = userToUserSynopsisData(user, lon, lat, userId);
            if (userSynopsisData.getId() != null) {
                list.add(userSynopsisData);
            }
        });
        return list;
    }


    public UserSynopsisData getUserSynopsisData(String userId, String id) throws Exception {
        User user = userService.selectById(id);
        logger.info("用户Id：" + id + "的经纬度为：（lat=" + user.getLat() + ",lon=" + user.getLon() + "）");
        return getUserSynopsisData(userId, user.getLon().doubleValue(), user.getLat().doubleValue(), id);
    }


    public UserSynopsisData getUserSynopsisData(String userId, Double lon, Double lat, String toUserId) throws Exception {
        User user = userService.selectById(userId);
        return userToUserSynopsisData(user, lon, lat, toUserId);
    }


    public List<UserSynopsisData> getUserSynopsisDataList(List<String> ids, Page page, String userId, Double lon, Double lat) {
        return UserListToUserSynopsisDataList(userService.getUsersByIds(ids, page), userId, lon, lat);
    }

    /**
     * 检查用户手机号码是否为空
     * @param userId
     * @return
     */
    public int checkUserMobile(String userId) {
       int result;
       String strMobile=userService.selectMobileByuserId(userId).getMobile()+"";
       if (!strMobile.equals("")){
           result=1;
       }else{
           result=0;
       }
       return result;
    }

    /**
     * 判断网信值是否大于或等于所传的值，通过userId，credit
     * @param userId
     * @param credit
     * @return
     */
    public int checkUserCreditByUserIdAndCredit(String userId,int credit){
        int result;
        if (userService.selectCreditByUserIdAndCredit(userId,credit)==1){
            result=1;
        }else{
            result=0;
        }
        return result;
    }

    private List<UserSynopsisData> redisUserAndUserSynopsisDataList(List<User> users) {
        List<UserSynopsisData> userSynopsisDataTemp = new ArrayList<>();
        User user;
        for (int i = 0; i < users.size(); i++) {
            user = users.get(i);
            UserSynopsisData userSynopsisData = VoPoConverter.copyProperties(user, UserSynopsisData.class);
            userSynopsisDataTemp.add(userSynopsisData);
        }
        return userSynopsisDataTemp;
    }

    private BigDecimal doubleToBigDecimal(Double d) {
        return d == null ? null : new BigDecimal(d);
    }

    /**
     * UserList转UserSynopsisDataList
     * 建议用户不要重复
     *
     * @param users
     * @return
     */
    public List<UserSynopsisData> UserListToUserSynopsisDataList(List<User> users, String userId, Double lon, Double lat) {
        List<UserSynopsisData> userSynopsisDataList = new ArrayList<>();
        if (users.size() != 0) {
            if ((lon == null || lat == null) & !StringUtils.isEmpty(userId)) {
                User user = userService.selectById(userId);
                lon = user.getLon().doubleValue();
                lat = user.getLat().doubleValue();
            }
            UserSynopsisData userSynopsisData;
            for (User user : users) {
                logger.info("中心用户id：" + userId);
                userSynopsisData = userToUserSynopsisData(user, lon, lat, userId);
                if (userSynopsisData.getId() != null) {
                    userSynopsisDataList.add(userSynopsisData);
                }
            }
        }
        return userSynopsisDataList;
    }

    public Map<String, Object> getUsersAndHeadImg(List<String> userIds) {
        List<User> list = userService.getUsersByIds(userIds, null);
        Map<String, Object> map = new HashMap<>();
        if (list.size() > 0) {
            list.forEach(user -> {
                UserInfoAndHeadImg userInfoAndHeadImg = getUserInfoAndHeadImg(user);
                if (userInfoAndHeadImg != null) {
                    map.put(user.getId(), userInfoAndHeadImg);
                }
            });
        }
        return map;
    }

    public UserInfoAndHeadImg getUserInfoAndHeadImg(String userId) {
        User user = userService.selectById(userId);
        return getUserInfoAndHeadImg(user);
    }

    public UserInfoAndHeadWatch getUserInfoAndHeadWatch(String toUserId, String fromUserId) {
        User user = userService.selectById(fromUserId);
        if (user == null) {
            return null;
        }
        UserInfoAndHeadWatch userInfoAndHeadWatch = VoPoConverter.copyProperties(user, UserInfoAndHeadWatch.class);
        try {
            userInfoAndHeadWatch.setHeadImgUrl(userPhotoAction.selectHeadImg(user.getId()));
        } catch (Exception e) {
            logger.warn("用户id：" + user.getId() + "获取头像失败");
        }
        userInfoAndHeadWatch.setIsWatch(userWatchAction.getIsWatch(fromUserId, toUserId));
        return userInfoAndHeadWatch;
    }

//    public UserSynopsisData userSynopsisData(String userId){
//        User user = userService.selectById(userId);
//        return userSynopsisData;
//    }


    public UserInfoAndHeadImg getUserInfoAndHeadImg(User user) {
        if (user == null) {
            return null;
        }
        UserInfoAndHeadImg userInfoAndHeadImg = VoPoConverter.copyProperties(user, UserInfoAndHeadImg.class);
        try {
            userInfoAndHeadImg.setHeadImgUrl(userPhotoAction.selectHeadImg(user.getId()));
        } catch (Exception e) {
            logger.warn("用户id：" + user.getId() + "获取头像失败");
        }
        return userInfoAndHeadImg;
    }

    public Map<String, UserSynopsisData> getUserSynopsisDataMap(List<String> ids, Double lon, Double lat, String userId) throws Exception {
        List<User> userList = userService.getUsersByIds(ids, null);
        Map<String, UserSynopsisData> dataMap = new HashMap<>();
        userList.forEach(user -> {
            UserSynopsisData userSynopsisData = userToUserSynopsisData(user, lon, lat, userId);
            if (userSynopsisData.getId() != null) {
                dataMap.put(user.getId(), userSynopsisData);
            }
        });
        return dataMap;
    }

    public UserSynopsisData getSystemUser() {
        UserSynopsisData userSynopsisData = new UserSynopsisData();
        userSynopsisData.setNickName("系统");
        userSynopsisData.setId("999");
        return userSynopsisData;
    }

    public List<UserSynopsisData> getUserSynopsisDataList(List<String> ids, Double lon, Double lat, String userId) throws Exception {
        List<User> userList = userService.getUsersByIds(ids, null);
        List<UserSynopsisData> list = new ArrayList<>();
        if (userList != null && userList.size() > 0) {
            userList.forEach(user -> {
                UserSynopsisData userSynopsisData = userToUserSynopsisData(user, lon, lat, userId);
                if (userSynopsisData.getId() != null) {
                    list.add(userSynopsisData);
                }
            });
        }
        return list;
    }

    /**
     * Redis用户
     *
     * @param user
     * @return
     */
    private UserSynopsisData userToUserSynopsisData(User user, Double lon, Double lat, String userId) {
        if (user == null) return new UserSynopsisData();
        Double distance = null;
        if (user.getLat() != null && user.getLon() != null) {
            distance = DistrictUtil.calcDistance(lat, lon, user.getLat().doubleValue(), user.getLon().doubleValue());
        }
        logger.info("中心的经纬度为：(lat=" + lat + ",lon=" + lon + ")，与用户id：" + user.getId() + "的经纬度：lat=" + user.getLat() + ",lon=" + user.getLon() + ")的距离为：" + distance);
        return userAndRedisTo(user, distance, userId);
    }

    public UserSynopsisData userAndRedisTo(User user, Double redius, String userId) {
        UserSynopsisData userSynopsisData = VoPoConverter.copyProperties(user, UserSynopsisData.class);
        userSynopsisData.setDistance(redius);
        try {
            userSynopsisData.setHeadImgUrl(userPhotoService.selectHeadImg(user.getId()));
        } catch (Exception e) {
            logger.warn("头像获取失败：" + e.getMessage());
        }
        try {
            UserProfile userProfile = userProfileService.getUserProfileByUserId(user.getId());
            userSynopsisData.setTag(userProfile.getDisposition());
            userSynopsisData.setAddress(userProfile.getAddress());
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        userSynopsisData.setWatch(userWatchAction.getIsWatch(userId, userSynopsisData.getId()));
        return userSynopsisData;
    }

    public List<String> getOnlineUserIds(List<String> ids, long time) throws Exception {
        List<User> userList = userService.getUserByIdsAndToken(ids, time == -1);
        List<String> newIds = new ArrayList<>();
        userList.forEach(user -> {
            if (time == -1 || checkOnline(user.getLastActiveAt().getTime(), time)) {
                newIds.add(user.getId());
            }
        });
        return newIds;
    }

    private boolean checkList(List list) {
        return list == null || list.isEmpty();
    }

    private boolean checkOnline(Long lastActiveAt, long time) {
        if (lastActiveAt != null) {
            return lastActiveAt >= time;
        }
        return false;
        //return redisUser.getLastActiveAt()>=time && redisUser.getLastActiveAt()>=redisUser.getExpiredAt();
    }


    public List<UserSynopsisData> getOnlineUser(List<String> ids, long time) {
        List<User> userList = userService.getUserByIdsAndToken(ids, time == -1);
        if (userList.isEmpty()) return null;
        List<UserSynopsisData> userSynopsisData = new ArrayList<>();
        RedisUser redisUser;
        int size = userList.size();
        for (int i = size - 1; i >= 0; i--) {
            User user = userList.get(i);
            if (time == -1 || checkOnline(user.getLastActiveAt().getTime(), time)) {
                userSynopsisData.add(userAndRedisTo(user, null, null));
                continue;
            }
            userList.remove(i);
        }
        return userSynopsisData;
    }

    public List<UserGeoRadius> screenNearlyUserGeo(List<UserGeoRadius> userGeos) throws Exception {
        int size = userGeos.size();
        User user;
        for (int i = size - 1; i >= 0; i--) {
            user = getNearlyUser(userGeos.get(i).getUserId());
            if (user == null) {
                userGeos.remove(i);
            }
        }
        return userGeos;
    }


    public User getNearlyUser(String userId) throws Exception {
        return userService.getNearlyUser(userId);
    }


    public List<User> getNearlyUserByIds(Collection<?> ids) throws Exception {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<User>();
        }
        return userService.getNearlyUserByIds(ids);
    }

    public List<String> getNearlyByIds(Collection<?> ids) throws Exception {
        return userService.getNearlyByIds(ids);
    }


    public Map<String, UserInfo> selectUserInfoBySex(String sex) throws Exception {
        List<User> list = userService.selectUserBySex(null, sex, false);
        ;
        Map<String, UserInfo> map = new HashMap<>();
        list.forEach(user -> {
            map.put(user.getId(), toUserInfo(user));
        });
        return map;
    }

    private UserInfo toUserInfo(User user) {
        return VoPoConverter.copyProperties(user, UserInfo.class);
    }

    public Boolean checkUserOnline(String userId, Long time) throws Exception {
        User user = userService.selectById(userId);
        if (user != null) {
            if (user.getLastActiveAt().getTime() + OnLineTimeEnum.ON_LINE_TIME_ENUM.getTime() > time) {//半个小时的离线时间
                return true;
            }
        }
        return false;
    }


    public User getUserByUserNumber(String userNumber) {
        return userService.getUserByUserNumber(userNumber);
    }

    public void test() {
        List<User> list = userService.getAllUserList();
        Date time = new Date();
        list.forEach(user -> {
            user.setLastLoginAt(time);
            user.setCreateUserId(user.getId());
            try {
                String password = messagePushAction.addUser(user.getId());
                if (password != null) {
                    user.setRegJMessage(true);
                    user.setJmessagePassword(password);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            user.setLat(new BigDecimal(22));
            user.setLon(new BigDecimal(113));
            userService.updateById(user);
            RedisKeyName redisKeyName = new RedisKeyName("userInfo", RedisTypeEnum.OBJECT_TYPE, user.getId());
            redisCache.put(redisKeyName.getUserKey(), user);
        });
    }

    public void testProfile() {
        List<User> list = userService.getAllUserList();
        list.forEach(user -> {
            userProfileAction.updateUserProfileScore(user);
        });
    }

    public void deleteUser(String userId) throws Exception {
        userService.deleteById(userId);
        messagePushAction.deleteUser(userId);
        userProfileService.delUserProfile(userId);
        loginHistoryService.deleteByUserId(userId);
    }

    public List<String> getAllUserId() {
        return userService.getAllUserId();
    }

    public void checkUserId(String userId) throws Exception {
        if (userId == null) {
            throw new RuntimeException("用户id不能为空");
        }
        User user = userService.selectById(userId);
        if (user == null) {
            throw new RuntimeException("此用户（" + userId + "）不存在");
        }
        if (!user.getRegJMessage()) {
            String password = messagePushAction.addUser(userId);
            if (password != null) {
                user.setRegJMessage(true);
                user.setJmessagePassword(password);
                updateUserById(user);
            }
        }
    }

    public boolean addMessageUser(String userId, String password) {
        User user = userService.selectById(userId);
        if (user != null && !user.getRegJMessage()) {
            user.setJmessagePassword(password);
            user.setRegJMessage(true);
            return updateUserById(user);
        }
        return false;
    }

    public Map getUserListAndCount(GetUserListRequestDto request) {
        Map map = new HashMap();
        Page<User> page = new Page<User>(request.getCurrentPage(), request.getSize());
        List<User> userList = userService.queryUserList(request.getNickname(), request.getUserNumber(), request.getMobile(), page).getRecords();
        map.put("userList", userList);
        map.put("count", page.getTotal());
        return map;
    }

    public boolean updateUser(UpdateUserRequestDto request) {
        User user = userService.selectById(request.getId());
        if (user != null) {
            VoPoConverter.copyProperties(request, user);
            user.setLockVersion(user.getLockVersion() == null ? 1 : user.getLockVersion() + 1);
            return updateUserById(user);
        }

        return false;
    }

    public User queryUser(String userId) {
        RedisKeyName redisKeyName = new RedisKeyName("userInfo", RedisTypeEnum.OBJECT_TYPE, userId);
       // String a=redisKeyName.getUserKey();
        User user = (User) redisCache.get(redisKeyName.getUserKey());
        if (user == null) {
            user = userService.selectById(userId);
            if (user != null) {
                redisCache.put(redisKeyName.getUserKey(), user);
            } else {
                throw new RuntimeException("用户不存在");
            }
        }
        //redisCache.remove(redisKeyName.getUserKey());
        return user;
    }

    public User queryUserById(String userId) {
        User user = userService.selectById(userId);
        return user;
    }

    public UserStatData queryUserStatData(String userId) {
        User user = queryUser(userId);
        return queryUserStatData(user);
    }

    public UserStatData queryUserStatData(User user) {
        UserStatData userStatData = VoPoConverter.copyProperties(user, UserStatData.class);
        userStatData.setHeadImg(userPhotoAction.selectHeadImg(user.getId()));
        return userStatData;
    }

    public UserStatData queryStatDataDto(StatDataDto statDataDto) {
        UserStatData userStatData = VoPoConverter.copyProperties(statDataDto, UserStatData.class);
        return userStatData;
    }

    public Map<String, UserStatData> queryUserStatData() {
        List<UserStatData> userStatDataList = userService.getUserStatData();
        List<User> users = userService.getAllUserList();
        Map<String, UserStatData> map = new HashMap<>();
        userStatDataList.forEach(userStatData -> {
            userStatData.setHeadImg(addImgUrlPreUtil.getUserImgPre(userStatData.getHeadImg()));
            map.put(userStatData.getId(), userStatData);
        });
        return map;
    }

    public UserStatData queryEarlyUser() {
        User user = userService.queryEarlyUser();
        return VoPoConverter.copyProperties(user, UserStatData.class);
    }

    /**
     * boss
     * 更新数据库,修改用户信息,ById,更新缓存
     *
     * @param editUserRequestDto
     * @return
     */
    @Transactional
    public Boolean editUserById(EditUserRequestDto editUserRequestDto) {
        User user = queryUser(editUserRequestDto.getId());
        if (user==null){
           return false;
        }
       // User user1=new User();

        VoPoConverter.copyProperties(editUserRequestDto,user);
        if (editUserRequestDto.getSex()==1){
            user.setSex("男");
        }
        else {
            user.setSex("女");
        }
//        User user=new User();
//        userService.selectById(editUserRequestDto.getId())
        return updateUserById(user);
    }

    //大赛排行榜
    public List<UserStatData> getUserStatData(){
        return userService.getUserStatData();
    }

    public List<StatData> getSuggestStatData(){
        return userService.getSuggestStatData();
    }

    public User selectMobileByuserId(String userId) {
        return userService.selectMobileByuserId(userId);
    }
}