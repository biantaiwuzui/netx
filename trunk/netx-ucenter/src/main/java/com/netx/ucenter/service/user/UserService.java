package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.router.enums.SelectConditionEnum;
import com.netx.common.user.enums.*;
import com.netx.common.user.model.StatData;
import com.netx.ucenter.util.SearchProcessing;
import com.netx.ucenter.vo.request.EditUserRequestDto;
import com.netx.ucenter.mapper.user.UserMapper;
import com.netx.ucenter.model.user.*;
import com.netx.ucenter.vo.response.UserStatData;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    UserMapper userMapper;

    public UserMapper getUserMapper() {
        return userMapper;
    }

    public List<User> selectUser(List<String> selectDataList , SelectConditionEnum selectConditionEnum, StringBuilder field){
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect(field.toString()).orderBy("id");
        wrapper.in(selectConditionEnum.getValue(), selectDataList);
        return selectList(wrapper);
    }

    public String getOneDataByUserId(String userId,String key){
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect(key).eq("id",userId);
        return (String) selectObj(wrapper);
    }

    public Wrapper<User> getUserWrapper(){
        return new EntityWrapper<>();
    }

    public Boolean checkUser(String userId){
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.where("id = {0} and is_login = 1",userId);
        return selectCount(wrapper)>0;
    }

    public List<User> getAllUserList(){
        return this.selectList(new EntityWrapper<User>().isNotNull("user_number"));
    }

    public User getUserLikeUserNumber(String userNumber) throws Exception{
        Wrapper wrapper = new EntityWrapper<User>();
        wrapper.like("user_number",userNumber);
        return selectOne(wrapper);
    }

    public Boolean updateDayDay(){
        return userMapper.updateDayNum();
    }

    public List<User> getUsersByNoIds(String ids) throws Exception{
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("id not in ({0})", ids);
        return this.selectList(wrapper);
    }

    public User selectMobileByuserId(String userId){
        Wrapper<User> wrapper=new EntityWrapper();
        wrapper.setSqlSelect("mobile");
        wrapper.where("id={0}",userId);
        return this.selectOne(wrapper);
    }

    public User getUserById(String UserId){
        Wrapper<User> wrapper=new EntityWrapper();
        wrapper.where ( "id={0}",UserId );
        return this.selectOne(wrapper);
    }

    public List<User> getNoLastLogin(Date lastTime){
        Wrapper<User> wrapper = new EntityWrapper<User>();
        wrapper.setSqlSelect("id","score");
        wrapper.where("last_login_at < {0}", lastTime);
        return this.selectList(wrapper);
    }

    public User getUserByUserNumberOrMobile(String userNumber,String mobile){
        Wrapper wrapper = new EntityWrapper();
        wrapper.andNew("user_number={0} or mobile={1}",userNumber,mobile);
        return this.selectOne(wrapper);
    }

    public User queryUserFriendsByUserNumber(String userId, String userNumber){
        return userMapper.queryUserFriendsByUserNumber(userId,userNumber);
    }

    @Transactional(readOnly = true)
    public List<User> selectUserByMobiles(String condition,int type,Page<User> userPage){
        Wrapper<User> wrapper = new EntityWrapper<>();
        if(condition!=null && !condition.isEmpty()){
            if(type==1 || type==2) {
                String column = type == 2 ? "mobile" : "user_number";
                wrapper.like(column, condition );
            }else{
                wrapper.where("mobile like '%"+condition+"%' or user_number like '%"+condition+"%'").andNew("deleted = 0");
            }
        }
        return pageToList(userPage,wrapper);
    }

    /**
     *
     * @param ids
     * @param isTime -1:true，其他false
     * @return
     */
    public List<User> getUserByIdsAndToken(List<String> ids,Boolean isTime){
        Wrapper wrapper = new EntityWrapper<>();
        if(checkList(ids)){
            if(isTime){
                return selectList(wrapper);
            }
        }else{
            wrapper.in("id",ids);
        }
        return this.selectList(wrapper);
    }

    private List<User> pageToList(Page<User> page,Wrapper<User> wrapper){
        if(page!=null){
            page = this.selectPage(page,wrapper);
            return page.getRecords();
        }else{
            return this.selectList(wrapper);
        }
    }

    public Integer checkNumber(String number){
        return this.selectCount(getUserNumberWrapper(number));
    }

    public List<User> getUsersByIds(List<String> ids,Page page){
        Wrapper<User> userWrapper = new EntityWrapper();
        userWrapper.in("id", ids).orderBy("id");
        if(page==null){
            return this.selectList(userWrapper);
        }
        return selectPage(page,userWrapper).getRecords();
    }

    /**
     *通过用户id列表获取用户
     * @param ids
     * @return
     */
    public List<User> getUsersByIds(List<String> ids){
        Wrapper<User> userWrapper = new EntityWrapper();
        userWrapper.in("id", ids).orderBy("id");
        return selectList(userWrapper);
    }

    public List<String> getUserIdByBeanDataAndIds(String nickName,String sex,Integer minAge,Integer maxAge){
        Wrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id");
        wrapper.where("deleted = 0");
        if(!StringUtils.isEmpty(nickName)){
            wrapper.like("nickname", nickName);
        }
        if(!StringUtils.isEmpty(sex)){
            wrapper.like("sex", sex);
        }
        if(minAge!=null && maxAge !=null){
            ageBetWeen(wrapper,minAge,maxAge);
        }
        return this.selectObjs(wrapper);
    }

    private Wrapper ageBetWeen(Wrapper wrapper,Integer minAge,Integer maxAge){
        Boolean minFlag = minAge==0;
        Boolean maxFlag = maxAge==0;
        if(minFlag){
            if(!maxFlag){
                //特定年龄以下
                wrapper.and("birthday>={0}",updateYear(maxAge));
            }
        }else {
            if(maxFlag){
                //特定年龄以上
                wrapper.and("birthday<={0}",updateYear(minAge));
            }else {
                //年龄段
                wrapper.between("birthday",updateYear(maxAge),updateYear(minAge));
            }
        }
        return wrapper;
    }

    private Date updateYear(Integer year){
        Date date = new Date();
        date.setYear(date.getYear()-year);
        return date;
    }

    public Page<User> selectAdministratorUserPage(Boolean isAdmin,Page<User> page){
        Wrapper<User> wrapper = new EntityWrapper<User>();
        wrapper.eq("is_admin_user", isAdmin?1:0);
        return selectPage(page,wrapper);
    }

    public User getUserByOnlyContidion(String value,String value2){
        EntityWrapper<User> wrapper = new EntityWrapper<User>();
       // wrapper.eq(contidion, value);
        if(StringUtils.isNotBlank(value2)){
            wrapper.eq("user_number",value2);
        }
        if (StringUtils.isNotBlank(value)){
            wrapper.eq("mobile",value);
        }
        return this.selectOne(wrapper);
    }

    public List<User> getFilterFriendUser(List<String> ids,String credit,String phone,String idCard,String voice) throws Exception{
        Wrapper<User> userWrapper = new EntityWrapper<>();
        if (checkList(ids)){
            return new ArrayList<>();
        }
        if(credit!=null){
            userWrapper.where("credit > {0}",Integer.parseInt(credit));
        }
        if(phone==null){
            userWrapper.and("mobile is null");
        }
        if(idCard==null){
            userWrapper.and("id_number is null");
        }
        if(voice==null){
            userWrapper.and("video is null");
        }
        return this.selectList(userWrapper);
    }

    public User getNearlyUser(String userId) throws Exception{
        Wrapper<User> wrapper = init();
        return this.selectOne(wrapper.where("id = {0}",userId));
    }

    public List<User> getNearlyUserByIds(Collection<?> ids) throws Exception{
        return this.selectList(init(ids));
    }

    public List<String> getNearlyByIds(Collection<?> ids) throws Exception{
        Wrapper<User> wrapper = init(ids);
        wrapper.setSqlSelect("id");
        return (List<String>)(List)this.selectObjs(wrapper);
    }
    
    public List<User> selectUserBySex(List<String> ids,String sex,boolean type) throws Exception{
        Wrapper<User> wrapper = new EntityWrapper<>();
        if(sex!=null){
            wrapper.eq("sex",sex);
        }
        if(!checkList(ids)){
            wrapper.in("id",ids);
        }
        if(type){
            wrapper.eq("nearly_setting",0);
        }
        return this.selectList(wrapper);
    }
    
    public User getUserByUserNumber(String userNumber){
        return this.selectOne(getUserNumberWrapper(userNumber));
    }

    private Wrapper getUserNumberWrapper(String userNumber){
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.where("user_number = {0}",userNumber);
        return wrapper;
    }

    public List<User> selectUsersByWangMing(Page page,WangMingEnum wangMingEnum){
        //组装sql
        Wrapper<User> userWrapper = new EntityWrapper<User>();
        //排序
        switch(wangMingEnum){//TODO 当身价等相同时，根据距离最短来排列，缺少距离判断
            case VALUE:
                userWrapper.orderBy("value", false);
                break;
            case INCOME:
                userWrapper.orderBy("income", false);
                break;
            case CONTRIBUTION:
                userWrapper.orderBy("contribution", false);
                break;
            case CREDIT:
                userWrapper.orderBy("credit", false);
                break;
            case SCORE:
                userWrapper.orderBy("score", false);
                break;
        }
        page = selectPage(page, userWrapper);
        return page.getRecords();
    }

    public Boolean addManagement(User user,Integer lockVersion){
        Wrapper<User> wrapper = new EntityWrapper<User>();
        wrapper.where("id = {0} AND lock_version = {1}", user.getId(), lockVersion);
        return update(user, wrapper);
    }
    public List<String> getAllUserId(){
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id");
        return (List<String>)(List)this.selectObjs(wrapper);
    }

    public List<User> pageUser(String userNumber,Page page,List<String> blackUserIds,Integer operateType){
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.like("user_number", userNumber).orderBy("user_number");
        wrapper.where("user_number is not null and deleted=0");
        //黑名单操作
        if(operateType == SystemBlackStatusEnum.DEFRIEND.getValue()) {
            //如果黑名单没有任何用户，则返回 null
            if(blackUserIds.isEmpty()){
                return null;
            }
            wrapper.in("id", blackUserIds);
        }
        //白名单操作
        if(operateType == SystemBlackStatusEnum.RELEASE.getValue()) {
            wrapper.notIn("id", blackUserIds);
        }
        page = this.selectPage(page, wrapper);
        return page.getRecords();
    }

    private boolean checkList(List list){
        return list == null || list.isEmpty();
    }

    private Wrapper<User> init(Collection<?> ids){
        Wrapper<User> wrapper = init();
        if (ids!=null){
            wrapper.in("id",ids);
        }
        return wrapper;
    }

    private Wrapper<User> init(){
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("nearly_setting",0);
        return wrapper;
    }

    public Page<User> queryUserList(String nickName,String userNumber,String mobile,Page page){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("user_number is not null and deleted=0");
        if(nickName!=null){
            wrapper.like("nickname",nickName);
        }
        if(userNumber!=null){
            wrapper.like("user_number",userNumber);
        }
        if(mobile!=null){
            wrapper.like("mobile",mobile);
        }
        return selectPage(page,wrapper);
    }

    /**
     * 查询用户信用
     * @param userId
     * @return
     */
    public Integer getCreditByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        if(userId != null){
            wrapper.setSqlSelect("credit").where("id = {0} AND deleted = 0", userId);
            return (Integer) this.selectObj(wrapper);
        }
        return null;
    }

    /**
     * 查找用户网信值是否存在，通过userId，credit的值范围
     * @param userId
     * @param credit
     * @return
     */
    public int selectCreditByUserIdAndCredit(String userId,int credit){
        Wrapper wrapper=new EntityWrapper();
        wrapper.where("id={0} and credit>={1}",userId,credit);
        return this.selectCount(wrapper);
    }

    /**
     * 查询用户信用和昵称
     * @param userId
     * @return
     */
    public Map<String, Object> getCreditAndNicknameByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        if(userId != null){
            wrapper.setSqlSelect("credit, nickname").where("id = {0} AND deleted = 0", userId);
            return this.selectMap(wrapper);
        }
        return null;
    }

    /**
     * 根据userId获取网号
     * @param userId
     * @return
     */
    public String getUserNumberByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("user_number").where("id = {0} AND deleted = 0", userId);
        return (String)selectObj(wrapper);
    }

    /**
     * 根据网号获取userId
     * @param userNumber
     * @return
     */
    public String getUserIdByUserNumber(String userNumber){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id").where("user_number = {0} AND deleted = 0", userNumber);
        return (String)selectObj(wrapper);
    }

    /**
     * 更具网号,电话号码,姓名模糊查询并返回用户id集
     * @param
     * @return
     */
    public List<String> getUserIdByNumberOrNicknameOrMobile(String userNumber, String mobile, String nickname){
        SearchProcessing searchProcessing = new SearchProcessing();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id").where("deleted = 0");
        if (StringUtils.isNotBlank(userNumber)) {
            wrapper.where("user_number = {0}", userNumber);
        }
        if (StringUtils.isNotBlank(mobile)){
            wrapper.and("mobile = {0}", mobile);
        }
        if (StringUtils.isNotBlank(nickname)){
            wrapper.like("nickname", searchProcessing.SearchProcessing(nickname));
        }
        return selectObjs(wrapper);
    }

    /**
     * 根据网号获取userIds
     * @param userNumber
     * @return
     */
    public List<String> getUserIdsByUserNumbers(List<String> userNumber){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id").in("user_number", userNumber).where("deleted = 0");
        return selectObjs(wrapper);
    }

    /**
     * 根据手机号获取用户
     * @param mobile
     * @return
     */
    public User getUserByMobile(String mobile){
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.where("mobile = {0} AND deleted = 0", mobile);
        return selectOne(wrapper);
    }

    public int countUserStat(String[] userIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id",userIds);
        wrapper.isNotNull("real_name");
        wrapper.isNotNull("id_number");
        wrapper.isNotNull("mobile");
        return selectCount(wrapper);
    }

    public User queryEarlyUser(){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.orderBy("create_time");
        return selectOne(wrapper);
    }

    public String getNickNameById(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("nickName").where("id = {0} AND deleted = 0", userId);
        return (String)selectObj(wrapper);
    }

    /**
     * 更新数据库,修改用户信息,ById
     * @param EditUserRequestDto
     * @return
     */
    public Boolean editUserById(EditUserRequestDto EditUserRequestDto){
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.where("id = {0} and deleted = 0", EditUserRequestDto.getId());
        Boolean flagLogin;
        Boolean flagAdminUser;
        String flagSex;
        User user=new User();
        user.setNickname(EditUserRequestDto.getNickname().trim());
        user.setUserNumber(EditUserRequestDto.getUserNumber().trim());
        user.setRealName(EditUserRequestDto.getRealName().trim());
        if (EditUserRequestDto.getSex()==1){
            flagSex="男";
        }
        else {
            flagSex="女";
        }
        user.setSex(flagSex);
        if (EditUserRequestDto.getLogin()==1){
            flagLogin=true;
        }
        else {
            flagLogin=false;
        }
        user.setLogin(flagLogin);
        if (EditUserRequestDto.getAdminUser()==1){
            flagAdminUser=true;
        }
        else {
            flagAdminUser=false;
        }
        user.setAdminUser(flagAdminUser);
        user.setMobile(EditUserRequestDto.getMobile().trim());
        user.setRole(EditUserRequestDto.getRole().trim());
        user.setEducationLabel(EditUserRequestDto.getEducationLabel().trim());
        user.setProfessionLabel(EditUserRequestDto.getProfessionLabel().trim());
        user.setInterestLabel(EditUserRequestDto.getInterestLabel());
        user.setLv(EditUserRequestDto.getLv());
        return this.update(user, wrapper);
    }

    public int queryRealUser(List<String> userIds){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("deleted=0");
        wrapper.in("id",userIds);
        wrapper.isNotNull("real_name");
        wrapper.isNotNull("mobile");
        wrapper.isNotNull("id_number");
        return selectCount(wrapper);
    }

    public List<User> queryRealUserList(List<String> userIds){
        Wrapper<User> wrapper=new EntityWrapper<>();
        wrapper.where("deleted=0");
        wrapper.in("id",userIds);
        wrapper.isNotNull("real_name");
        wrapper.isNotNull("mobile");
        wrapper.isNotNull("id_number");
        return this.selectList(wrapper);
    }

    //大赛排行榜
    public List<UserStatData> getUserStatData(){
        return userMapper.getUserStatData();
    }

    //大赛排行榜
    public List<StatData> getSuggestStatData(){
        return userMapper.getSuggestStat();
    }

    // 网信详情 - 根据 userId 获取用户昵称，信用值，性别，年龄
    public User getUserInfoByUserId(String userId) {
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("nickname, credit, sex, birthday");
        wrapper.where("id = {0}", userId);
        return selectOne(wrapper);
    }

    //网信信用值排行
    public Integer getCreditRankByUserId(String userId){ return userMapper.getCreditRankByUserId(userId);}


    public Integer selectNums(){
        EntityWrapper<User> wrapper = new EntityWrapper();
        return selectCount(wrapper);

    }
}