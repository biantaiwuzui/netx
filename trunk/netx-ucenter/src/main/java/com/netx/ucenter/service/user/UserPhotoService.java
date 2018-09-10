package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.router.dto.bean.UserPhotosResponseDto;
import com.netx.common.user.dto.photo.ExchangePhotoPositionRequestDto;
import com.netx.common.user.dto.photo.InsertUserPhotoRequestDto;
import com.netx.common.user.model.UserPhotoImg;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.ucenter.model.user.UserPhoto;
import com.netx.ucenter.mapper.user.UserPhotoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户照片表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserPhotoService extends ServiceImpl<UserPhotoMapper, UserPhoto>{

    @Autowired
    AddImgUrlPreUtil addImgUrlPreUtil;

    public List<UserPhoto> selectUserPhoto(List<String> userIdList, StringBuilder field) throws Exception{
        Wrapper<UserPhoto> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect(field.toString()).orderBy("user_id").eq("position", 1);
        wrapper.in("user_id", userIdList);
        return selectList(wrapper);
    }

    public Boolean deletePhotoByUserId(String id,String userId){
        Wrapper<UserPhoto> userPhotoWrapper = new EntityWrapper<UserPhoto>();
        userPhotoWrapper.where("user_id = {0} and id={1}", userId,id);
        return delete(userPhotoWrapper);
    }

    public Integer countPhotoByUserId(String userId) throws Exception{
        Wrapper<UserPhoto> userPhotoWrapper = new EntityWrapper<UserPhoto>();
        userPhotoWrapper.where("user_id = {0}", userId);
        return this.selectCount(userPhotoWrapper);
    }

    public void delPhotoByUserId(String userId) throws Exception{
        Wrapper<UserPhoto> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        this.delete(wrapper);
    }

    public Integer getMaxPosition() throws Exception{
        return (Integer)selectObj(new EntityWrapper<UserPhoto>().setSqlSelect("max(position)"));
    }

    public Boolean insertPhoto(String userId,String url,Integer position){
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setCreateUserId(userId);
        userPhoto.setUserId(userId);
        userPhoto.setUrl(url);
        userPhoto.setPosition(position);
        return insert(userPhoto);
    }

    public UserPhoto selectPhotoByUserIdAndPosition(String userId,Integer position){
        return this.selectOne(getWrapperByUserIdAndPosition(userId,position));
    }

    public List<UserPhoto> getUserPhotosGreaterPosition(String userId,Integer position){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("user_id = {0} and position > {1}", userId, position);
        wrapper.orderBy("position");
        return selectList(wrapper);
    }
    
    public List<UserPhoto> selectPhotoListByUserId(String userId,boolean isOwnHead) throws Exception{
        Wrapper<UserPhoto> wrapper = new EntityWrapper<UserPhoto>();
        wrapper.where("user_id = {0}", userId).setSqlSelect("id, concat('"+addImgUrlPreUtil.getUserImgPre()+"',url) as url, position").orderBy("position");
        if(isOwnHead){
            wrapper.and("position!={0}",1);
        }
        return selectList(wrapper);
    }

    private Wrapper<UserPhoto> getWrapperByUserIdAndPosition(String userId,Integer position){
        Wrapper<UserPhoto> wrapper = new EntityWrapper<UserPhoto>();
        wrapper.where("user_id = {0} and position = {1}", userId, position);
        return wrapper;
    }

    public Boolean updatePhotoPositionById(UserPhoto userPhoto,Integer position){
        return update(userPhoto,getWrapperByUserIdAndPosition(userPhoto.getUserId(),position));
    }
    
    public boolean changeHeadImg(String userId, String url) throws Exception {
        Wrapper<UserPhoto> wrapper = new EntityWrapper<UserPhoto>();
        wrapper.where("user_id = {0} and position = 1", userId);
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setUrl(url);
        return update(userPhoto, wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean addHeadImg(String userId,String url){
        UserPhoto userPhoto=new UserPhoto();
        userPhoto.setUserId(userId);
        userPhoto.setUrl(url);
        userPhoto.setPosition(1);
        userPhoto.setCreateUserId(userId);
        return this.insert(userPhoto);
    }

    public String getPhotoUrl(String userId,Integer position){
        Wrapper wrapper = getWrapperByUserIdAndPosition(userId, position);
        wrapper.setSqlSelect("url");
        return (String) this.selectObj(wrapper);
    }
    
    public String selectHeadImg(String userId) throws Exception{
        if (StringUtils.isEmpty(userId))return null;
        Wrapper<UserPhoto> userPhotoWrapper = new EntityWrapper<>();
        Map map = new HashMap();
        map.put("user_id",userId);
        map.put("position",1);
        return addImgUrlPreUtil.getUserImgPre((String) this.selectObj(userPhotoWrapper.setSqlSelect("url").allEq(map)));
    }

    public List<UserPhoto> createUserPhotos(List<String> ids){
        Wrapper<UserPhoto> userPhotoWrapper = new EntityWrapper<>();
        userPhotoWrapper.in("user_id",ids);
        userPhotoWrapper.where("position = 1");
        return this.selectList(userPhotoWrapper);
    }
}
