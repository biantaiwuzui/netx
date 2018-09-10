package com.netx.ucenter.biz.user;

import com.netx.common.router.dto.bean.UserPhotosResponseDto;
import com.netx.common.user.dto.photo.ExchangePhotoPositionRequestDto;
import com.netx.common.user.dto.photo.InsertUserPhotoRequestDto;
import com.netx.common.user.model.UserPhotoImg;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.ucenter.model.user.UserPhoto;
import com.netx.ucenter.service.user.UserPhotoService;
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
public class UserPhotoAction{

    @Autowired
    AddImgUrlPreUtil addImgUrlPreUtil;
    @Autowired
    UserPhotoService userPhotoService;

    public UserPhotoService getUserPhotoService() {
        return userPhotoService;
    }

    @Transactional
    public List<UserPhoto> insertUserPhoto(InsertUserPhotoRequestDto request) throws Exception {
        Integer maxPosition = userPhotoService.getMaxPosition();
        maxPosition = maxPosition != null ? maxPosition : 0;
        String userId = request.getUserId();
        List<UserPhoto> list = new ArrayList<>();
        for(String photoUrl : request.getPhotoUrlList()) {
            UserPhoto userPhoto = new UserPhoto();
            userPhoto.setCreateUserId(userId);
            userPhoto.setUserId(userId);
            userPhoto.setUrl(photoUrl);
            userPhoto.setPosition(++maxPosition);
            if(!userPhotoService.insert(userPhoto)){
                throw new RuntimeException("插入照片"+photoUrl+", 出现异常");
            }
            userPhoto.setUrl(addImgUrlPreUtil.getUserImgPre(userPhoto.getUrl()));
            list.add(userPhoto);
        }
        return list;
    }

    @Transactional
    public boolean exchangePhotoPosition(ExchangePhotoPositionRequestDto request) throws Exception{
        //查询两张图片是否存在
        UserPhoto photoA = userPhotoService.selectPhotoByUserIdAndPosition(request.getUserId(),request.getAfterPosition());
        if(photoA==null){
            throw new RuntimeException("交换位置中，有图片不存在");
        }
        UserPhoto photoB = userPhotoService.selectPhotoByUserIdAndPosition(request.getUserId(),request.getBeforePosition());
        if(photoB==null){
            throw new RuntimeException("交换位置中，有图片不存在");
        }
        photoA.setPosition(request.getBeforePosition());
        photoB.setPosition(request.getAfterPosition());
        photoA.setUpdateUserId(request.getUserId());
        photoB.setUpdateUserId(request.getUserId());
        return userPhotoService.updateById(photoA) && userPhotoService.updateById(photoB);
        /*//更新需要交换位置的图片
        UserPhoto beforePhoto = new UserPhoto();
        beforePhoto.setUpdateUser(request.getUserId());
        beforePhoto.setPosition(request.getAfterPosition());
        if(!userPhotoService.updatePhotoPositionById(beforePhoto, request.getBeforePosition())){
            throw new RuntimeException("交换图片位置失败，图片可能不存在");
        }
        beforePhoto.setPosition(request.getBeforePosition());

        //更新被交换位置的图片
        UserPhoto afterPhoto = new UserPhoto();
        afterPhoto.setUpdateUser(request.getUserId());
        afterPhoto.setPosition(request.getBeforePosition());
        Wrapper<UserPhoto> afterWrapper = new EntityWrapper<UserPhoto>();
        afterWrapper.where("userId = {0} and position = {1}", request.getUserId(), request.getAfterPosition());
        if(!update(afterPhoto, afterWrapper)){
            throw new RuntimeException("交换图片位置失败，图片可能不存在");
        }
        return true;*/
    }

    @Transactional
    public boolean deletePhotoById(String photoId) throws Exception{
        //查询出需要删除照片的对象
        UserPhoto deletePhoto =  userPhotoService.selectById(photoId);
        if(deletePhoto.getPosition() == 1){ throw new RuntimeException("用户头像不能删除，只能删除用户照片"); }
        if(deletePhoto != null) {
            Integer startPosition = deletePhoto.getPosition();//获取位置
            String userId = deletePhoto.getUserId();//获取用户id
            if (!userPhotoService.deleteById(photoId)) {
                return false;
            }
            List<UserPhoto> list = userPhotoService.getUserPhotosGreaterPosition(userId, startPosition);
            if (!list.isEmpty()) {//如果列表为空，则不做重排操作
                for (int i = 0; i < list.size(); i++) {
                    UserPhoto userPhoto = list.get(i);
                    userPhoto.setPosition(userPhoto.getPosition() - 1);
                }
                if (!userPhotoService.updateBatchById(list)) {
                    throw new RuntimeException("删除异常");
                }
            }
        }
        return true;
    }

    /**
     *
     * @param userId
     * @param url
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public String addHeadImg(String userId,String url){
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(url))return "头像为空";
        return userPhotoService.addHeadImg(userId,url)?"":"添加头像异常";
    }

    public String selectHeadImg(String userId){
        if (StringUtils.isEmpty(userId))return null;
        return addImgUrlPreUtil.getUserImgPre(userPhotoService.getPhotoUrl(userId,1));
    }

    public List<String> selectHeadImgs(List<String> ids) throws Exception{
        List<String> headImgList=new ArrayList<>();
        List<UserPhoto> resultList=userPhotoService.createUserPhotos(ids);
        int i;
        String headImg;
        for (String id:ids){
            for (i=0;i<resultList.size();i++){
                if(resultList.get(i).getUserId().equals(id)){
                    headImgList.add(addImgUrlPreUtil.getUserImgPre(resultList.get(i).getUrl()));
                    break;
                }
            }
            if(i==resultList.size()){
                headImgList.add(null);
            }
        }
        return headImgList;
    }

    public Map<String,String> selectHeadImgMap(List<String> ids) throws Exception{
        List<UserPhoto> resultList= userPhotoService.createUserPhotos(ids);
        Map<String,String> map = new HashMap<>();
        for(UserPhoto photo : resultList){
            map.put(photo.getUserId(),addImgUrlPreUtil.getUserImgPre(photo.getUrl()));
        }
        return map;
    }

    public UserPhotosResponseDto selectUserPhotos(String userId) throws Exception{
        List<UserPhoto> userPhotoList = userPhotoService.selectPhotoListByUserId(userId,false);
        UserPhotosResponseDto userPhotosResponseDto = new UserPhotosResponseDto();
        if(userPhotoList.size()!=0){
            userPhotosResponseDto.setHeadImgUrl(userPhotoList.remove(0).getUrl());
        }
        userPhotosResponseDto.setImgUrls(userPhotosToUserPhotoImgs(userPhotoList));
        return userPhotosResponseDto;
    }

    private List<UserPhotoImg> userPhotosToUserPhotoImgs(List<UserPhoto> photos){
        List<UserPhotoImg> photoImgs = new ArrayList<>();
        if(photos!=null){
            photos.forEach(userPhoto -> {
                photoImgs.add(UserPhotoToUserPhotoImg(userPhoto));
            });
        }
        return photoImgs;
    }

    private UserPhotoImg UserPhotoToUserPhotoImg(UserPhoto userPhoto){
        UserPhotoImg userPhotoImg = new UserPhotoImg();
        userPhotoImg.setId(userPhoto.getId());
        userPhotoImg.setPosition(userPhoto.getPosition());
        userPhotoImg.setUrl(userPhoto.getUrl());
        return userPhotoImg;
    }
}
