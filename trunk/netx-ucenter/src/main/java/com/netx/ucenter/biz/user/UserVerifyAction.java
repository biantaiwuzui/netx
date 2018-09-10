package com.netx.ucenter.biz.user;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.vo.message.JpushDto;
import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.user.dto.userManagement.*;
import com.netx.common.user.dto.userVerify.*;
import com.netx.common.user.enums.MobileSmsCode;
import com.netx.common.user.enums.VerifyCreditEnum;
import com.netx.common.user.enums.VerifyStatusEnum;
import com.netx.common.user.enums.VerifyTypeEnum;
import com.netx.common.user.util.*;
import com.netx.ucenter.biz.common.MessagePushAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserVerify;
import com.netx.ucenter.model.user.UserVerifyCredit;
import com.netx.ucenter.model.user.UserVerifyResource;
import com.netx.ucenter.service.user.*;
import com.netx.ucenter.vo.request.QueryUserVerifyListRequestDto;
import com.netx.ucenter.vo.request.WzMobileCodeRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 用户认证表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserVerifyAction{

    private Logger logger = LoggerFactory.getLogger(UserVerifyAction.class);

    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;
    @Autowired
    private UserVerifyResourceAction userVerifyResourceAction;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAction userAction;
    @Autowired
    private UserVerificationCodeAction userVerificationCodeAction;
    @Autowired
    private MessagePushAction messagePushAction;
    @Autowired
    private UserVerifyCreditAction userVerifyCreditAction;
    @Autowired
    UserVerifyService userVerifyService;
    @Autowired
    UserVerifyResourceService userVerifyResourceService;
    @Autowired
    UserPhotoAction userPhotoAction;
    @Autowired
    UserProfileAction userProfileAction;
    @Autowired
    MobileMessage mobileMessage;

    private Integer getAge(Date birthday){
        return ComputeAgeUtils.getAgeByBirthday(birthday);
    }

    public Integer getVerifyState(String userId,Integer verifyType) throws Exception{
        return userVerifyService.getVerifyState(userId, verifyType);
    }
    
    public void deleteVerify(String userId,String mobile) throws Exception{
        userVerificationCodeAction.delMobileCode(userId,mobile);
        userVerifyResourceAction.deleteVerfyResource(userId);
        userVerifyService.deleteVerify(userId);
    }

    @Transactional
    public Integer postUserIdCardVerify(PostUserIdCardVerifyRequest request) throws Exception{
        String idCardNumber = request.getIdCardNumber();
        UserVerifyCredit userVerifyCredit = userVerifyCreditAction.selectUserIdNumber(idCardNumber,VerifyCreditEnum.IDNUMBERCREDIT_TYPE);
        if(userVerifyCredit!=null && userVerifyCredit.getUserId()!=null){
            return 2;//该身份证已经被认证
        }
        String userId = request.getUserId();
        String realName = request.getRealName();
        String frontPhotoUrl = request.getFrontPhotoUrl();
        String backPhotoUrl = request.getBackPhotoUrl();
        //插入到认证表
        String userVerifyId = postUserVerify(userId, VerifyTypeEnum.IDCARD_TYPE.getValue(), realName+","+idCardNumber);
        if (userVerifyId == null) return 0;
        //插入到认证资源表
        Boolean flagVerifyResource1 = userVerifyResourceAction.postUserVerifyResource(userId, userVerifyId, frontPhotoUrl, 1);
        Boolean flagVerifyResource2 = userVerifyResourceAction.postUserVerifyResource(userId, userVerifyId, backPhotoUrl, 2);
        return flagVerifyResource1 && flagVerifyResource2 ? 1:0;
    }

    @Transactional
    public boolean postUserVideoVerify(PostUserVideoVerifyRequest request) throws Exception{
        String userId = request.getUserId();
        String videoUrl = request.getVideoUrl();
        //插入到认证表
        String userVerifyId = postUserVerify(userId, VerifyTypeEnum.VIDEO_TYPE.getValue(), null);
        if (userVerifyId == null) return false;
        //插入到认证资源表
        Boolean flagVerifyResource = userVerifyResourceAction.postUserVerifyResource(userId, userVerifyId, videoUrl, 1);
        return flagVerifyResource;
    }

    @Transactional
    public Integer postUserCarVerify(PostUserCarVerifyRequest request) throws Exception{
        String userId = request.getUserId();
        String carBrand = request.getCarBrand();
        if(userVerifyService.checkVerifyByCar(VerifyTypeEnum.CAR_TYPE.getValue(),carBrand)!=null){
            return 2;
        }
        String vehicleLicenseUrl = request.getVehicleLicenseUrl();
        String annualRegistrationUrl = request.getAnnualRegistrationUrl();
        String driverLicenseUrl = request.getDriverLicenseUrl();
        //插入到认证表
        String userVerifyId = postUserVerify(userId, VerifyTypeEnum.CAR_TYPE.getValue(), carBrand);
        if (userVerifyId == null) return 0;
        //插入到认证资源表
        Boolean flagVerifyResource1 = userVerifyResourceAction.postUserVerifyResource(userId, userVerifyId, vehicleLicenseUrl, 1);
        Boolean flagVerifyResource2 = userVerifyResourceAction.postUserVerifyResource(userId, userVerifyId, annualRegistrationUrl, 2);
        Boolean flagVerifyResource3 = userVerifyResourceAction.postUserVerifyResource(userId, userVerifyId, driverLicenseUrl, 3);
        return flagVerifyResource1 && flagVerifyResource2 && flagVerifyResource3?1:0;
    }

    @Transactional
    public boolean postUserHouseVerify(PostUserHouseRequest request) throws Exception{
        String userId = request.getUserId();
        String propertyCertificateUrl = request.getPropertyCertificateUrl();
        //插入到认证表
        String userVerifyId = postUserVerify(userId, VerifyTypeEnum.HOUSE_TYPE.getValue(), null);
        if (userVerifyId == null) return false;
        //插入到认证资源表
        Boolean flagVerifyResource = userVerifyResourceAction.postUserVerifyResource(userId, userVerifyId, propertyCertificateUrl, 1);
        return flagVerifyResource;
    }

    @Transactional
    public boolean postUserDegreeVerify(PostUserDegreeRequest request) throws Exception{
        String userId = request.getUserId();
        String degreeCertificateUrl = request.getDegreeCertificateUrl();
        String graduationCertificateUrl = request.getGraduationCertificateUrl();
        //插入到认证表
        String userVerifyId = postUserVerify(userId, VerifyTypeEnum.DEGREE_TYPE.getValue(), null);
        if (userVerifyId == null) return false;
        //插入到认证资源表
        Boolean flagVerifyResource1 = userVerifyResourceAction.postUserVerifyResource(userId, userVerifyId, degreeCertificateUrl, 1);
        Boolean flagVerifyResource2 = userVerifyResourceAction.postUserVerifyResource(userId, userVerifyId, graduationCertificateUrl, 2);
        return flagVerifyResource1 && flagVerifyResource2;
    }

    @Transactional
    public Map<String, Object> selectUserVerifyList(QueryUserVerifyListRequestDto request) throws Exception{
        Map<String, Object> map = new HashMap<>();
        List<String> ids = new ArrayList<>();
        if(StringUtils.isNotBlank(request.getUserNumber()) || StringUtils.isNotBlank(request.getMobile()) || StringUtils.isNotBlank(request.getNickName())) {
            ids = userService.getUserIdByNumberOrNicknameOrMobile(request.getUserNumber(), request.getMobile(), request.getNickName());
        }
        Page page = new Page(request.getCurrentPage(), request.getSize());
        Page<UserVerify> userVerifyPage = userVerifyService.getUserVerifyByStatus(request.getStatus(), page, ids, request.getVerifyType());
        List<SelectUserVerifyBaseResponse> voList = new ArrayList<>();
        for (UserVerify userVerify : userVerifyPage.getRecords()) {
            SelectUserVerifyBaseResponse base = new SelectUserVerifyBaseResponse();
            VoPoConverter.copyProperties(userVerify, base);
            base.setAdmin(userVerify.getUpdateUserId());
            //赋值用户表的属性
            User user = userService.selectById(userVerify.getUserId());
            if(user != null) {
                base.setNickName(user.getNickname());
                base.setLv(user.getLv());
                base.setSex(user.getSex());
                base.setAge(getAge(user.getBirthday()));
                base.setMobile(user.getMobile());
                base.setUserNumber(user.getUserNumber());
                voList.add(base);
            }
        }
        map.put("list", voList);
        map.put("total", userVerifyPage.getTotal());
        return map;
    }

    public VerifyResponse getVerifyResponse(String userId,VerifyTypeEnum typeEnum){
        VerifyResponse verifyResponse = new VerifyResponse();
        UserVerify userVerify = userVerifyService.queryVerifyByUserId(userId,typeEnum);
        if(userVerify!=null){
            List<UserVerifyResource> poList = userVerifyResourceService.selectVerifyResourceListByUserVerifyId(userVerify.getId());
            List<SelectUserVerifyResourceResponse> voList = UserVerifyResourcesToSelectUserVerifyResourceResponses(poList);
            if(typeEnum == VerifyTypeEnum.IDCARD_TYPE){
                if(!StringUtils.isEmpty(userVerify.getDescription())){
                    String[] message = userVerify.getDescription().split(",",2);
                    if(message.length>1){
                        verifyResponse.setRealName(message[0]);
                        verifyResponse.setVerifyMessage(message[1]);
                    }
                }
            }else {
                verifyResponse.setVerifyMessage(userVerify.getDescription());
            }
            verifyResponse.setId(userVerify.getId());
            verifyResponse.setStatus(userVerify.getStatus());
            verifyResponse.setList(voList);
        }
        return verifyResponse;
    }

    @Transactional
    public SelectUserVerifyResponse selectUserVerify(String userId) throws Exception{
        SelectUserVerifyResponse response = new SelectUserVerifyResponse();
        //获取用户基础内容
        User user = userService.selectById(userId);
        response.setNickName(user.getNickname());
        response.setUserNumber(user.getUserNumber());
        response.setLv(user.getLv());
        response.setSex(user.getSex());
        response.setAge(getAge(user.getBirthday()));
        //获取用户认证内容
        List<UserVerify> userVerifyList = selectVerifyByUserId(userId,true);
        for (UserVerify userVerify : userVerifyList){
            String userVerifyId = userVerify.getId();
            //获取相关认证资源
            List<UserVerifyResource> poList = userVerifyResourceService.selectVerifyResourceListByUserVerifyId(userVerifyId);
            //List<SelectUserVerifyResourceResponse> voList = VoPoConverter.copyList(poList, SelectUserVerifyResourceResponse.class);
            List<SelectUserVerifyResourceResponse> voList = UserVerifyResourcesToSelectUserVerifyResourceResponses(poList);
            //判断认证类型，再进行 set 描述和资源
            if (userVerify.getVerifyType() == VerifyTypeEnum.IDCARD_TYPE.getValue()){
                response.setIdNumber(userVerify.getDescription());
                response.setIdCardResource(voList);
            }
            if (userVerify.getVerifyType() == VerifyTypeEnum.VIDEO_TYPE.getValue()){
                response.setVideoResource(voList);
            }
            if (userVerify.getVerifyType() == VerifyTypeEnum.CAR_TYPE.getValue()) {
                response.setCarResource(voList);
            }
            if (userVerify.getVerifyType() == VerifyTypeEnum.HOUSE_TYPE.getValue()) {
                response.setHouseResource(voList);
            }
            if (userVerify.getVerifyType() == VerifyTypeEnum.DEGREE_TYPE.getValue()) {
                response.setDegreeResource(voList);
            }
        }
        return response;
    }

    private List<SelectUserVerifyResourceResponse> UserVerifyResourcesToSelectUserVerifyResourceResponses(List<UserVerifyResource> userVerifyResources){
        List<SelectUserVerifyResourceResponse> list = new ArrayList<>();
        if(userVerifyResources!=null && userVerifyResources.size()>0){
            userVerifyResources.forEach(verifyResource -> {
                list.add(UserVerifyResourceToSelectUserVerifyResourceResponse(verifyResource));
            });
        }
        return list;
    }

    private SelectUserVerifyResourceResponse UserVerifyResourceToSelectUserVerifyResourceResponse(UserVerifyResource verifyResource){
        SelectUserVerifyResourceResponse selectUserVerifyResourceResponse = new SelectUserVerifyResourceResponse();
        selectUserVerifyResourceResponse.setUserVerifyId(verifyResource.getUserVerifyId());
        selectUserVerifyResourceResponse.setUrl(addImgUrlPreUtil.getUserImgPre(verifyResource.getUrl()));
        selectUserVerifyResourceResponse.setPosition(verifyResource.getPosition());
        return selectUserVerifyResourceResponse;
    }

    private List<UserVerify> selectVerifyByUserId(String userId,Boolean type) throws Exception{
        return userVerifyService.selectVerifyByUserId(userId,type?VerifyStatusEnum.PENDING_AUTHENTICATION.getValue():VerifyStatusEnum.PASSING_AUTHENTICATION.getValue());
    }

    /**
     * 查找用户认证，通过用户Id
     * @param userId
     * @return
     * @throws Exception
     */
    public List<UserVerify> selectUsersVerifyIdByUserId(List<String> userId) throws Exception{
        return userVerifyService.selectUsersVerifyIdByUserId(userId);
    }

    public UserVerifyBeanResponse getUserVerifyBeanResponse(String userId,Boolean type) throws Exception{
        UserVerifyBeanResponse userVerifyBeanResponse = new SelectUserVerifyResponse();
        List<UserVerify> userVerifyList = selectVerifyByUserId(userId,type);
        for (UserVerify userVerify : userVerifyList){
            String userVerifyId = userVerify.getId();
            //获取相关认证资源
            List<UserVerifyResource> poList = userVerifyResourceService.selectVerifyResourceListByUserVerifyId(userVerifyId);
            List<SelectUserVerifyResourceResponse> voList = VoPoConverter.copyList(poList, SelectUserVerifyResourceResponse.class);
            //判断认证类型，再进行 set 描述和资源
            if (userVerify.getVerifyType() == VerifyTypeEnum.IDCARD_TYPE.getValue()){
                userVerifyBeanResponse.setIdNumber(userVerify.getDescription());
                userVerifyBeanResponse.setIdCardResource(voList);
            }
            else if (userVerify.getVerifyType() == VerifyTypeEnum.VIDEO_TYPE.getValue()){
                userVerifyBeanResponse.setVideoResource(voList);
            }
            else if (userVerify.getVerifyType() == VerifyTypeEnum.CAR_TYPE.getValue()) {
                userVerifyBeanResponse.setCarResource(voList);
            }
            else if (userVerify.getVerifyType() == VerifyTypeEnum.HOUSE_TYPE.getValue()) {
                userVerifyBeanResponse.setHouseResource(voList);
            }
            else if (userVerify.getVerifyType() == VerifyTypeEnum.DEGREE_TYPE.getValue()) {
                userVerifyBeanResponse.setDegreeResource(voList);
            }
        }
        return userVerifyBeanResponse;
    }

    @Transactional
    public boolean operateUserVerify(OperateUserVerifyRequest request) throws Exception{
        String updateUserId = request.getUpdateUserId();
        String userId = request.getUserId();
        //存储认证审核数据
        UserVerify userVerify = userVerifyService.selectById(request.getUserVerifyId());
        if(userVerify == null){
            throw new Exception("认证id有误");
        }
        if(!userId.equals(userVerify.getUserId())){
            throw new Exception("用户id不一致");
        }
        if(!userVerify.getStatus().equals(VerifyStatusEnum.PENDING_AUTHENTICATION.getValue())){//必须是待认证状态，才允许操作
            throw new Exception("此认证已经被处理过了");
        }
        userVerify.setReason(request.getReason());
        userVerify.setStatus(request.getStatus());
        boolean flag = true;
        if(request.getStatus().equals(VerifyStatusEnum.REJECTIVE_AUTHENTICATION.getValue())) {//认证拒绝
            flag = this.operateRejectiveStatus(updateUserId, userId, userVerify);
        }else if(request.getStatus() == VerifyStatusEnum.PASSING_AUTHENTICATION.getValue()) {//认证通过
            flag = this.operatePassingStatus(updateUserId, userId, userVerify);
            if(flag){
                userProfileAction.updateUserProfileScore(userId);
            }
        }
        //只返回存储认证审核数据是否成功，认证是否通过只是一个支点，不需要返回此结果
        return flag;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer operateUserMobileVerify(WzMobileCodeRequest request,String userId){
        //返回值：-1:该号码已被其他用户绑定； 0:手机验证失败； 1:验证成功； 2:验证码过期； 3:验证码已经被验证过，请重新获取验证码； 其他:你暂无验证码。
        if(userAction.selectUserByMobile(request.getMobile(),null)!=null){
            return -1;
        }
        int statusCode = userVerificationCodeAction.checkMobileCode(request,userId);
        if(statusCode == 1){//成功后绑定手机
            User user = userService.selectById(userId);
            user.setUpdateUserId(userId);
            user.setMobile(request.getMobile());
            userService.updateById(user);
            userProfileAction.updateUserProfileScore(userId);
        }
        return statusCode;
    }
    
    public Map<String,Object> matchUserVerify(String realName, String mobile, String userNumber, String idNumber)throws Exception {
        User user= userService.getUserByUserNumber(userNumber);
        Map<String,Object> map = new HashMap<>();
        if(user==null){
            throw new RuntimeException("此用户不存在");
        }
        if(user.getRealName()==null){
            throw new RuntimeException("身份未认证");
        }
        if(!user.getRealName().equals(realName)){
            throw new RuntimeException("姓名错误");
        }
        if(!user.getMobile().equals(mobile)){
            throw new RuntimeException("手机号码不正确");
        }
        if(!StringUtils.isEmpty(idNumber)){
            String description = userVerifyService.selectUserIdentity(user.getId(),VerifyTypeEnum.IDCARD_TYPE.getValue(),VerifyStatusEnum.PASSING_AUTHENTICATION.getValue());
            if(StringUtils.isEmpty(description)){
                throw new RuntimeException("未通过身份认证");
            }
            String[] strArray = description.split(",");
            if(strArray.length<2){
                throw new RuntimeException("未通过身份认证");
            }
            if(!strArray[1].equals(idNumber)){
                throw new RuntimeException("身份证不正确");
            }
        }

        map.put("id",user.getId());
        map.put("nickname",user.getNickname());
        map.put("headImg",userPhotoAction.selectHeadImg(user.getId()));
        return map;
    }

    public String selectUserIdentity(String userId) throws Exception{
        return userVerifyService.selectUserIdentity(userId,VerifyTypeEnum.IDCARD_TYPE.getValue(),VerifyStatusEnum.PASSING_AUTHENTICATION.getValue());
    }

    //--------------------- 私有方法 ----------------------
    /**
     * 发送认证请求
     * @param userId
     * @param verifyType 认证类型(1：身份认证, 2：视频认证, 3：车辆认证, 4：房产认证, 5：学历认证)
     * @param description 描述（若没有，传null）
     * @return 认证表id，若返回值为null，说明认证表已存在相应的待认证或已认证信息
     */
    private String postUserVerify(String userId, Integer verifyType, String description) throws Exception{
        //若认证表里不存在此用户的此种类型认证的待认证和认证通过信息，则插入认证请求信息
        if(!haveVerify(verifyType, userId)) {
            UserVerify userVerify = new UserVerify();
            userVerify.setCreateUserId(userId);
            userVerify.setUserId(userId);
            userVerify.setVerifyType(verifyType);
            if (description != null) {
                userVerify.setDescription(description);
            }
            userVerifyService.insert(userVerify);
            return userVerify.getId();
        }
        return null;
    }

    /**
     * 判断是否已有待认证或通过认证的相关认证项目
     * @param verifyType
     * @param userId
     * @return
     */
    private boolean haveVerify(Integer verifyType, String userId) throws Exception{
        int count = userVerifyService.countUserVerifyByUserId(verifyType, userId);
        if(count == 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 拒绝认证并更改
     * @param updateUserId 操作人的用户id
     * @param userId 用户id
     * @param userVerify 用户认证
     * @return true：更新成功，false：更新失败
     * @throws Exception
     */
    private boolean operateRejectiveStatus(String updateUserId, String userId, UserVerify userVerify) throws Exception{
        // 向用户推送认证失败信息，并且说明认证失败原因
        VerifyTypeEnum verifyTypeEnum = VerifyTypeEnum.getEnumByValue(userVerify.getVerifyType());
        User user = userService.selectById(userId);
        if(user != null){
            if(StringUtils.isNotBlank(user.getMobile())){
                sendMessage(userVerify.getCreateTime(),user.getMobile(), verifyTypeEnum.getName(), userVerify.getReason(), false);
            }
        }
//        JpushDto jpushDto = new JpushDto();
//        jpushDto.setType(MessageTypeEnum.USER_TYPE);
//        jpushDto.setTitle("亲爱的用户，对不起，你的" + verifyTypeEnum.getName() + "被拒绝");
//        jpushDto.setUserId(userId);
//        jpushDto.setAlertMsg("拒绝原因：" + userVerify.getReason());
        //messagePushClient.sendByAlias(jpushDto);
//        messagePushAction.sendMessageAlias(jpushDto);
        // 更新认证状态
        userVerify.setUpdateUserId(updateUserId);
        return userVerifyService.updateById(userVerify);
    }

    /**
     * 通过认证并更改
     * @param updateUserId 操作人的用户id
     * @param userId 用户id
     * @param userVerify 用户认证
     * @return true：更新成功，false：更新失败
     * @throws Exception
     */
    private boolean operatePassingStatus(String updateUserId, String userId, UserVerify userVerify) throws Exception {
        //若认证通过，将数据存入用户表，可能存在多条数据，以逗号分隔
        String[] userVerifyIdList = userVerify.getId().split(",");
        int length = userVerifyIdList.length;
        UserVerify userVerifyTemp;
        User user;
        for(int i=0; i<length; i++){
            userVerifyTemp = userVerifyService.selectById(userVerifyIdList[i]);
            if(!userId.equals(userVerifyTemp.getUserId())){//认证信息集的用户id必须相同，才能允许操作
                throw new Exception("认证信息集的所有用户id不一致");
            }
            if(userVerifyTemp.getStatus().equals(VerifyStatusEnum.PENDING_AUTHENTICATION.getValue())){//必须是待认证状态，才允许操作
                throw new Exception("拒绝认证操作，认证集含有已经被处理的认证");
            }
            userVerify.setId(userVerifyIdList[i]);
            userVerify.setUpdateUserId(updateUserId);
            userVerifyService.updateById(userVerify);
            userVerifyTemp.setStatus(userVerify.getStatus());//防止数据出错，将状态更新到更改状态后
            user = userAction.lockVersionOperation(userVerifyTemp.getUserId());
            user.setUpdateUserId(updateUserId);
            operateUserVerifySuccess(user, userVerifyTemp);
        }
        return true;
    }

    /**
     * 根据传递的user和userVerify进行成功认证后的操作
     * @param user  认证成功的用户表
     * @param userVerify    认证成功的认证表
     * @return
     */
    private boolean operateUserVerifySuccess(User user, UserVerify userVerify) throws Exception{
        switch (VerifyTypeEnum.getEnumByValue(userVerify.getVerifyType())) {
            case IDCARD_TYPE:
                //特殊情况，身份认证里面的描述含有姓名+身份证号码，身份证号码需要加密插入到用户表
                String description = userVerify.getDescription();
                String[] str = description.split(",");
                String totalStr = str[1].substring(0, 4) + "**************" ;
                user.setIdNumber(totalStr);
                user.setRealName(str[0]);
                Date birthday = getBirthday(str[1]);
                if(birthday != null){
                    user.setBirthday(birthday);
                }
                userVerifyCreditAction.addOrUpdateVerifyCredit(user.getLockVersion(),user.getId(),str[1],user.getCredit());
                break;
            case VIDEO_TYPE:
                String videoStr = "已上传视频";
                user.setVideo(videoStr);
                break;
            case CAR_TYPE:
                String carStr = userVerify.getDescription();
                carStr = "*"+carStr.substring(1,2) + "**" + carStr.substring(4);
                user.setCar(carStr);
                break;
            case HOUSE_TYPE:
                String houseStr = "已上传房产证明";
                user.setHouse(houseStr);
                break;
            case DEGREE_TYPE:
                String degreeStr = "已上传学历证明";
                user.setDegree(degreeStr);
                break;
        }
        // 向用户推送认证失败信息，并且说明认证失败原因
        VerifyTypeEnum verifyTypeEnum = VerifyTypeEnum.getEnumByValue(userVerify.getVerifyType());
//        JpushDto jpushDto = new JpushDto();
//        jpushDto.setTitle("亲爱的用户，恭喜你！你的" + verifyTypeEnum.getName() + "已通过");
//        jpushDto.setUserId(user.getId());
//        //messagePushClient.sendByAlias(jpushDto);
//        jpushDto.setType(MessageTypeEnum.USER_TYPE);
//        messagePushAction.sendMessageAlias(jpushDto);
        if(StringUtils.isNotBlank(user.getMobile())){
            sendMessage(userVerify.getCreateTime(),user.getMobile(), verifyTypeEnum.getName(), null, true);
        }
        return userService.updateById(user);
    }

    private void sendMessage(Date date,String tel, String type, String reason, Boolean status){
        Map<String, Object> map = new HashMap<>();
        map.put("date", DateTimestampUtil.getDateStrsByDate(date));
        map.put("type", type);
        if(status){
            mobileMessage.send(tel, map, MobileSmsCode.MS_VERIFY_SUCESS);
        }else{
            map.put("reason", reason);
            mobileMessage.send(tel, map, MobileSmsCode.MS_VERIFY_FAIL);
        }
    }

    private Date getBirthday(String idCard) throws Exception{
        if(idCard.length() == 18) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String birthday = idCard.substring(6, 10) + "-" + idCard.substring(10, 12) + "-" + idCard.substring(12, 14);
            return sdf.parse(birthday);
        }
        return null;
    }
}
