package com.netx.ucenter.biz.user;

import com.netx.common.user.dto.information.UpdateUserProfileRequest;
import com.netx.common.user.dto.wangMing.AddScoreRecordRequestDto;
import com.netx.common.user.enums.UserInformationScoreEnum;
import com.netx.common.user.enums.VerifyStatusEnum;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.UpdateUserDetailsRequestDto;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserProfile;
import com.netx.ucenter.service.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户详情表
凡是标签，均以字符形式存，以逗号分隔 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserProfileAction{

    private Logger logger = LoggerFactory.getLogger(UserProfileAction.class);

    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserAction userAction;
    @Autowired
    private UserPhotoService userPhotoService;
    @Autowired
    private UserVerifyService userVerifyService;
    @Autowired
    private UserEducationAction userEducationAction;
    @Autowired
    private UserProfessionAction userProfessionAction;
    @Autowired
    private UserInterestAction userInterestAction;
    @Autowired
    private UserScoreAction userScoreAction;
    @Autowired
    private ScoreAction scoreAction;

    public List<UserProfile> selectUserProfileByUserId(List<String> userIdList) throws Exception {
        Map<String,UserProfile> userProfileMap = listUserProfileToMap(userProfileService.selectUserProfileByUserId(userIdList));
        List<UserProfile> list = new ArrayList<>();
        for(String userId:userIdList){
            if(userProfileMap.containsKey(userId)){
                list.add(userProfileMap.get(userId));
            }
        }
        if(userIdList.size() != list.size()) throw new RuntimeException("用户id列表含有不存在的用户id");
        return list;
    }

    private Map<String,UserProfile> listUserProfileToMap(List<UserProfile> userProfileList){
        Map<String,UserProfile> map = new HashMap<>();
        userProfileList.forEach(userProfile -> {
            map.put(userProfile.getUserId(),userProfile);
        });
        return map;
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserProfile(UpdateUserProfileRequest request){
        UserProfile userProfile = new UserProfile();
        userProfile.setUpdateUserId(request.getUserId());
        //常驻必选项，必须有内容
        if(StringUtils.hasText(request.getOftenIn())) userProfile.setOftenIn(request.getOftenIn());
        //家乡必选项，必须有内容
        if(StringUtils.hasText(request.getHomeTown())) userProfile.setHomeTown(request.getHomeTown());
        if(request.getAlreadyTo() != null) userProfile.setAlreadyTo(request.getAlreadyTo());
        if(request.getWantTo() != null) userProfile.setWantTo(request.getWantTo());
        if(request.getAddress() != null) userProfile.setAddress(request.getAddress());
        if(request.getIntroduce() != null) userProfile.setIntroduce(request.getIntroduce());
        //性格必选项，必须有内容
        if(StringUtils.hasText(request.getDisposition())) userProfile.setDisposition(request.getDisposition());
        if(request.getAppearance() != null) userProfile.setAppearance(request.getAppearance());
        if(request.getIncome() != null) userProfile.setIncome(request.getIncome());
        if(request.getMaxIncome() != null ) userProfile.setMaxIncome(request.getMaxIncome());
        if(request.getEmotion() != null) userProfile.setEmotion(request.getEmotion());
        if(request.getHeight() != null) userProfile.setHeight(request.getHeight());
        if(request.getWeight() != null) userProfile.setWeight(request.getWeight());
        if(request.getNation() != null) userProfile.setNation(request.getNation());
        if(request.getAnimalSigns() != null) userProfile.setAnimalSigns(request.getAnimalSigns());
        if(request.getStarSign() != null) userProfile.setStarSign(request.getStarSign());
        if(request.getBloodType() != null) userProfile.setBloodType(request.getBloodType());
        if(request.getDescription() != null) userProfile.setDescription(request.getDescription());
        boolean flag = userProfileService.updateUserProfile(request.getUserId(),userProfile);
        if(flag){
            updateUserProfileScore(request.getUserId());
        }
        return flag;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserProfileScore(String userId){
        User user = userAction.getUserService().selectById(userId);
        return updateUserProfileScore(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserProfileScore(User user){
        UserProfile userProfile = userProfileService.getUserProfileByUserId(user.getId());
        //需要加的分数总和
        int score = calculateUser(user) + calculateProfile(userProfile) + calculatePhoto(user.getId()) + calculateVerify(user.getId()) + calculateEducation(user.getId()) + calculateProfession(user.getId()) + calculateInterest(user.getId());
        //之前现在总的分(有重复)-加过的分=这次需要加的分,这次加的分要放进redis相加
        int yu = score-user.getUserProfileScore();
        //更新用户表积分
        user = updateScoreByUserProfileScore(score,user);
        user.setUpdateUserId(user.getId());
        boolean flag = userAction.updateUserById(user);
        if(flag){
            scoreAction.addScore(user.getId(),yu);
        }
        return flag;
    }

    //用户基本资料
    public int calculateUser(User user){
        int score = 0;
        //昵称
        if(!StringUtils.isEmpty(user.getNickname())){
            score += UserInformationScoreEnum.NICKNAME.getScore();
        }
        //性别
        if(!StringUtils.isEmpty(user.getSex())){
            score += UserInformationScoreEnum.SEX.getScore();
        }
        //生日与年龄
        if(!StringUtils.isEmpty(user.getBirthday())){
            Integer age = ComputeAgeUtils.getAgeByBirthday(user.getBirthday());
            if (age > 0 && age <= 150) {
                score += UserInformationScoreEnum.AGE.getScore() + UserInformationScoreEnum.BIRTHDAY.getScore();
            }
        }
        if(!StringUtils.isEmpty(user.getMobile())){
            score += UserInformationScoreEnum.PHONE.getScore();
        }
        if(!StringUtils.isEmpty(user.getEducationLabel())){
            score += UserInformationScoreEnum.EDUCATION_LABEL.getScore();
        }
        if(!StringUtils.isEmpty(user.getProfessionLabel())){
            score += UserInformationScoreEnum.PROFESSION_LABEL.getScore();
        }
        if(!StringUtils.isEmpty(user.getInterestLabel())){
            score += UserInformationScoreEnum.INTEREST_LABEL.getScore();
        }
        return score;
    }

    public int calculateProfile(UserProfile userProfile){
        int score = 0;
        if(!StringUtils.isEmpty(userProfile.getOftenIn())){
            score+=UserInformationScoreEnum.OFTEN_IN.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getHomeTown())){
            score+=UserInformationScoreEnum.HOMETOWN.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getDisposition())){
            score+=UserInformationScoreEnum.DISPOSITION.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getIntroduce())){
            score+=UserInformationScoreEnum.INTRODUCE.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getDescription())){
            score+=UserInformationScoreEnum.DESCRIPTION.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getAddress())){
            score+=UserInformationScoreEnum.ADDRESS.getScore();
        }
        if(userProfile.getHeight()!=null && userProfile.getHeight()>0){
            score+=UserInformationScoreEnum.HEIGHT.getScore();
        }
        if(userProfile.getWeight()!=null && userProfile.getWeight()>0){
            score+=UserInformationScoreEnum.WEIGHT.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getAppearance())){
            score+=UserInformationScoreEnum.APPEARANCE.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getAlreadyTo())){
            score+=UserInformationScoreEnum.ALREADY_TO.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getWantTo())){
            score+=UserInformationScoreEnum.WANT_TO.getScore();
        }
        if(userProfile.getIncome()!=null && userProfile.getIncome()>=0){
            score+=UserInformationScoreEnum.INCOME.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getEmotion())){
            score+=UserInformationScoreEnum.EMOTION.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getNation())){
            score+=UserInformationScoreEnum.NATION.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getAnimalSigns())){
            score+=UserInformationScoreEnum.ANIMAL_SIGNS.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getStarSign())){
            score+=UserInformationScoreEnum.STAR_SIGN.getScore();
        }
        if(!StringUtils.isEmpty(userProfile.getBloodType())){
            score+=UserInformationScoreEnum.BLOOD_TYPE.getScore();
        }
        return score;
    }

    public int calculatePhoto(String userId){
        try {
            Integer count = userPhotoService.countPhotoByUserId(userId);
            if(count!=null && count>1){
                return UserInformationScoreEnum.PHOTO.getScore();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return 0;
    }

    public int calculateVerify(String userId){
        List<Map<String,Object>> list = userVerifyService.countUserIdentities(userId,VerifyStatusEnum.PASSING_AUTHENTICATION.getValue());
        int score = 0;
        if(list.size()>0){
            int[] verifyCount = new int[5];
            list.forEach(map->{
                Integer i = (Integer)map.get("type");
                if(i!=null){
                    verifyCount[i-1]=checkNull((Integer) map.get("num"));
                }
            });
            if(verifyCount[0] > 0) {score += UserInformationScoreEnum.IDCARD.getScore();}
            if(verifyCount[1] > 0) {score += UserInformationScoreEnum.VIDEO.getScore();}
            if(verifyCount[2] > 0) {score += UserInformationScoreEnum.CAR.getScore();}
            if(verifyCount[3] > 0) {score += UserInformationScoreEnum.HOUSE.getScore();}
            if(verifyCount[4] > 0) {score += UserInformationScoreEnum.DEGREE.getScore();}
        }
        return score;
    }

    private int checkNull(Integer num){
        return num==null?0:num;
    }

    public int calculateEducation(String userId){
        Integer count = userEducationAction.getUserEducationService().countEducationByUserId(userId);
        return count==null || count<1?0:UserInformationScoreEnum.EDUCATION_SCHOOL.getScore()+UserInformationScoreEnum.EDUCATION_DEGREE.getScore();
    }

    public int calculateProfession(String userId){
        Integer count = userProfessionAction.getProfessionService().countProfessionByUserId(userId);
        return count==null || count<1?0:UserInformationScoreEnum.PROFESSION_COMPANY.getScore()+UserInformationScoreEnum.PROFESSION_TOPPROFESSION.getScore();
    }

    public int calculateInterest(String userId){
        Integer count = userInterestAction.getUserInterestService().countInterestByUserId(userId);
        return count==null || count<1?0:UserInformationScoreEnum.INTEREST_TYPE.getScore()+UserInformationScoreEnum.INTEREST_DETAIL.getScore();
    }
    //------------ 私有方法 ----------------
    /**
     * 1、必选项（7项）、照片、自我介绍、图文详情（一共10项）
     */
 /*   private Integer calculateRequired(User user, UserProfile userProfile, Integer photoCount){
        Integer score = 0;
        //必选项，照片，自我介绍，图文详情(共10项)
        if(StringUtils.hasText(user.getNickname())) {score += UserInformationScoreEnum.NICKNAME.getScore();}
        if(StringUtils.hasText(user.getSex())) {score += UserInformationScoreEnum.SEX.getScore();}
        if(user.getBirthday() != null) {//必须先判断数字型的数据是否为空，否则可能会出异常
            Integer age = computeAgeUtils.getAgeByBirthday(user.getBirthday());
            if (age > 0 && age <= 150) {
                score += UserInformationScoreEnum.AGE.getScore();
                score += UserInformationScoreEnum.BIRTHDAY.getScore();
            }
        }
        if(StringUtils.hasText(userProfile.getOftenIn())) {score += UserInformationScoreEnum.OFTEN_IN.getScore();}
        if(StringUtils.hasText(userProfile.getHomeTown())) {score += UserInformationScoreEnum.HOMETOWN.getScore();}
        if(StringUtils.hasText(userProfile.getDisposition())) {score += UserInformationScoreEnum.DISPOSITION.getScore();}
        if(photoCount > 0) {score += UserInformationScoreEnum.PHOTO.getScore();}
        if(StringUtils.hasText(userProfile.getIntroduce())) {score += UserInformationScoreEnum.INTRODUCE.getScore();}
        if(StringUtils.hasText(userProfile.getDescription())) {score += UserInformationScoreEnum.DESCRIPTION.getScore();}
        return score;
    }
    *//**
     * 2、计算认证部分（共6项，包括手机）
     * @param verifyCount 认证成功数组，存储存在认证成功的数量
     *//*
    private Integer calculateVerify(User user, Integer[] verifyCount){
        Integer score = 0;
        //认证部分（包含手机，注意手机认证不信息不在认证表）（共6项）
        if(StringUtils.hasText(user.getMobile())) {score += UserInformationScoreEnum.PHONE.getScore();}
        if(verifyCount[1] > 0) {score += UserInformationScoreEnum.IDCARD.getScore();}
        if(verifyCount[2] > 0) {score += UserInformationScoreEnum.VIDEO.getScore();}
        if(verifyCount[3] > 0) {score += UserInformationScoreEnum.CAR.getScore();}
        if(verifyCount[4] > 0) {score += UserInformationScoreEnum.HOUSE.getScore();}
        if(verifyCount[5] > 0) {score += UserInformationScoreEnum.DEGREE.getScore();}
        return score;
    }
    *//**
     * 3、计算综合项：文化教育、工作经历、兴趣爱好(每个共3项，一共9项)
     *//*
    private Integer calculateComprehensive(SelectUserEducationResponse education, SelectUserProfessionResponse profession, SelectUserInterestResponse interest){
        Integer score = 0;
        //综合部分(共9项)
        if(StringUtils.hasText(education.getEducationLabel())) {score += UserInformationScoreEnum.EDUCATION_LABEL.getScore(); }
        if(education.getList().size() > 0) {//判断教育详情列表是否存在数据
            SelectUserEducationDetailResponse educationDetailResponse = education.getList().get(0);
            if(educationDetailResponse!=null){
                if (StringUtils.hasText(educationDetailResponse.getSchool())) {
                    score += UserInformationScoreEnum.EDUCATION_SCHOOL.getScore();
                }
                if (StringUtils.hasText(educationDetailResponse.getDegree())) {
                    score += UserInformationScoreEnum.EDUCATION_DEGREE.getScore();
                }
            }
        }
        if(StringUtils.hasText(profession.getProfessionLabel())) {score += UserInformationScoreEnum.PROFESSION_LABEL.getScore();}
        if(profession.getList().size() > 0) {
            SelectUserProfessionDetailResponse selectUserProfessionDetailResponse = profession.getList().get(0);
            if(selectUserProfessionDetailResponse!=null){
                if (StringUtils.hasText(selectUserProfessionDetailResponse.getCompany())) {
                    score += UserInformationScoreEnum.PROFESSION_COMPANY.getScore();
                }
                if (StringUtils.hasText(selectUserProfessionDetailResponse.getTopProfession())) {
                    score += UserInformationScoreEnum.PROFESSION_TOPPROFESSION.getScore();
                }
            }
        }
        if(StringUtils.hasText(interest.getInterestLabel())) {score += UserInformationScoreEnum.INTEREST_LABEL.getScore();}
        if(interest.getList().size() > 0) {
            SelectUserInterestDetailResponse response = interest.getList().get(0);
            if(response!=null){
                if (StringUtils.hasText(interest.getList().get(0).getInterestType())) {
                    score += UserInformationScoreEnum.INTEREST_TYPE.getScore();
                }
                if (StringUtils.hasText(interest.getList().get(0).getInterestDetail())) {
                    score += UserInformationScoreEnum.INTEREST_DETAIL.getScore();
                }
            }
        }
        return score;
    }
    *//**
     * 4、计算特殊项：地址、身高、外貌、体重(共4项)
     *//*
    private Integer calculateParticular(UserProfile userProfile){
        Integer score = 0;
        //特殊项：地址、身高、外貌、体重(共4项)
        if(StringUtils.hasText(userProfile.getAddress())) {score += UserInformationScoreEnum.ADDRESS.getScore();}
        if(userProfile.getHeight() > 0) {score += UserInformationScoreEnum.HEIGHT.getScore();}
        if(StringUtils.hasText(userProfile.getAppearance())) {score += UserInformationScoreEnum.APPEARANCE.getScore();}
        if(userProfile.getWeight() > 0) {score += UserInformationScoreEnum.WEIGHT.getScore();}
        return score;
    }
    *//**
     * 5、计算其余部分（8项）
     *//*
    private Integer calculateResidue(UserProfile userProfile){
        Integer score = 0;
        //其余部分(8项)
        if(StringUtils.hasText(userProfile.getAlreadyTo())) {score += UserInformationScoreEnum.ALREADY_TO.getScore();}
        if(StringUtils.hasText(userProfile.getWantTo())) {score += UserInformationScoreEnum.WANT_TO.getScore();}
        if(userProfile.getIncome() != 0) {score += UserInformationScoreEnum.INCOME.getScore();}
        if(StringUtils.hasText(userProfile.getEmotion())) {score += UserInformationScoreEnum.EMOTION.getScore();}
        if(StringUtils.hasText(userProfile.getNation())) {score += UserInformationScoreEnum.NATION.getScore();}
        if(StringUtils.hasText(userProfile.getAnimalSigns())) {score += UserInformationScoreEnum.ANIMAL_SIGNS.getScore();}
        if(StringUtils.hasText(userProfile.getStarSign())) {score += UserInformationScoreEnum.STAR_SIGN.getScore();}
        if(StringUtils.hasText(userProfile.getBloodType())) {score += UserInformationScoreEnum.BLOOD_TYPE.getScore();}
        return score;
    }
*/
    private Integer completePercent(BigDecimal bigDecimal){
        return bigDecimal==null?0:(int)(bigDecimal.doubleValue()*100);
    }

    /**
     * 根据用户资料分数更新积分
     * @param score 需要更新的资料分数
     * @param user 查询出的User对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    User updateScoreByUserProfileScore(Integer score, User user){
        Integer lastComplete = completePercent(user.getLastCompletePercent());
        user.setUserProfileScore(score);
        if(score<100){
            int now = (int)Math.ceil(score/10);
            int last = (int)Math.ceil(lastComplete/10);
            if(now-last>0){
                user.setLastCompletePercent(new BigDecimal((double)score/100).setScale(2,BigDecimal.ROUND_DOWN));
                AddScoreRecordRequestDto addScoreRecordRequestDto = new AddScoreRecordRequestDto();
                addScoreRecordRequestDto.setUserId(user.getId());
                addScoreRecordRequestDto.setRelatableId(user.getId());
                addScoreRecordRequestDto.setRelatableType("User");
                addScoreRecordRequestDto.setCode(1);
                for(int j=last; j<now; j++){//有多少个 10% 的积分差，就运行多少次添加积分
                    userScoreAction.addScoreRecord(addScoreRecordRequestDto);
                }
            }
        }
        return user;
    }

    /**
     * 根据用户id查询用户单一基本信息
     * @param userId
     * @return
     */
    public String getUserTag(String userId,String type){
        return userProfileService.getUserTag(userId, type);
    }

    public UserProfile getUserProfileByUserId(String userId) throws Exception{
        return userProfileService.getUserProfileByUserId(userId);
    }

    public void delUserProfile(String userId) throws Exception{
        userProfileService.delUserProfile(userId);//正常删除
        userScoreAction.delScoreRecord(userId);//删除积分
        userProfessionAction.deleteByUserId(userId);//删除工作经历
        userInterestAction.deleteByUserId(userId);//删除爱好
        userEducationAction.deleteByUserId(userId);//删除教育
    }

    public Boolean updateUserDetails(UpdateUserDetailsRequestDto updateUserDetailsRequestDto){
        UserProfile userProfile=new UserProfile();
        VoPoConverter.copyProperties(updateUserDetailsRequestDto,userProfile);
        return userProfileService.updateUserProfile(updateUserDetailsRequestDto.getUserId(),userProfile);
    }
    //===================  黎子安 End  =====================
}
