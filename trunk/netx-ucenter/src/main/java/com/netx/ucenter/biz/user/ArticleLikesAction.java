package com.netx.ucenter.biz.user;

import com.netx.common.user.dto.wangMing.AddScoreRecordRequestDto;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserArticleLikes;
import com.netx.ucenter.service.user.ArticleLikesService;
import com.netx.ucenter.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ArticleLikesAction{

    @Autowired
    private ArticleAction articleAction;
    @Autowired
    private UserService userService;
    @Autowired
    private UserScoreAction userScoreAction;
    @Autowired
    private ArticleLikesService articleLikesService;

    @Transactional(rollbackFor = Exception.class)
    public boolean likeArticle(String userId, String articleId){
        UserArticleLikes articleLikes = articleLikesService.getArticleLikesByUserIdAndArticleId(userId, articleId);
        if(articleLikes == null) {//不存在记录
            articleLikes = new UserArticleLikes();
            articleLikes.setArticleId(articleId);
            articleLikes.setUserId(userId);
            articleLikes.setCreateUserId(userId);
            articleLikes.setIsLike(1);
            articleLikesService.insert(articleLikes);
            //进行更新积分操作
            this.updateScoreByArticleLikes(articleLikes);
            return true;
        }else{//存在记录
            if(articleLikes.getIsLike()!=1) {//点赞
                String targetUserId = articleAction.selectUserIdByArticleId(articleId);//获取图文发布者的用户id
                if (targetUserId == null)
                    return false;//操作失败
                articleLikes.setIsLike(1);
            }else{//取消点赞
                articleLikes.setIsLike(0);
            }
            articleLikesService.updateById(articleLikes);
            return true;
        }
    }

    //------ 私有 ------

    /**
     * 根据点赞添加积分
     * @param articleLikes
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateScoreByArticleLikes(UserArticleLikes articleLikes){
        //注意：此 userId 是咨讯的发布者的 userId ，不是点赞人的 userId
        String userId = articleAction.selectUserIdByArticleId(articleLikes.getArticleId());
        User user = userService.selectById(userId);
        Integer currentLikes = user.getCurrentLikes();
        if(currentLikes == null) throw new RuntimeException("此咨讯的发布者不合法");
        currentLikes = currentLikes + 1;
        User u = new User();//用于更新 currentLikes
        u.setId(userId);
        if(currentLikes == 5) {//添加积分
            u.setCurrentLikes(0);
            //添加积分
            AddScoreRecordRequestDto request = new AddScoreRecordRequestDto();
            request.setUserId(userId);
            request.setCode(6);
            request.setRelatableId(articleLikes.getId());
            request.setRelatableType(UserArticleLikes.class.getSimpleName());
            userScoreAction.addScoreRecord(request);
        }else{//不进行添加积分操作
            u.setCurrentLikes(currentLikes);
        }
        return userService.updateById(u);
    }
}
