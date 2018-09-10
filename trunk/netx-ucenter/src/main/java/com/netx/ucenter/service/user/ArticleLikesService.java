package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.user.UserArticleLikesMapper;
import com.netx.ucenter.model.user.UserArticleLikes;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 资讯点赞表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-10-24
 */
@Service
public class ArticleLikesService extends ServiceImpl<UserArticleLikesMapper, UserArticleLikes>{

    public int selectLikesNumber(String articleId){
        Wrapper<UserArticleLikes> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id").where("article_id = {0} and is_like = {1}", articleId, true);
        return selectCount(wrapper);
    }

    public boolean getLike(String userId,String articleId){
        Wrapper<UserArticleLikes> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} and article_id = {1}", userId, articleId);
        wrapper.setSqlSelect("is_like");
        Integer flag = (Integer) selectObj(wrapper);
        return flag!=null && flag==1;
    }

    public UserArticleLikes getArticleLikesByUserIdAndArticleId(String userId, String articleId){
        Wrapper<UserArticleLikes> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} and article_id = {1}", userId, articleId);
        return this.selectOne(wrapper);
    }

    public List<Object> selectArticleIdByUserId(String userId) throws Exception{
        Wrapper<UserArticleLikes> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("article_id").where("user_id = {0} and is_like = {1}", userId, true);
        return selectObjs(wrapper);
    }

    public Boolean deleteByArticleId(String articleId){
        Wrapper<UserArticleLikes> articleLikesWrapper = new EntityWrapper<>();
        articleLikesWrapper.where("article_id = {0}", articleId);
        return delete(articleLikesWrapper);
    }

    public boolean deleteArticleByUserId(String userId,List<String> articleIds){
        Wrapper<UserArticleLikes> articleLikesWrapper = new EntityWrapper<>();
        articleLikesWrapper.where("deleted=0");
        if(articleIds!=null && articleIds.size()>0){
            articleLikesWrapper.in("article_id",articleIds);
        }
        articleLikesWrapper.orNew("user_id={0}",userId);
        return delete(articleLikesWrapper);
    }

}
