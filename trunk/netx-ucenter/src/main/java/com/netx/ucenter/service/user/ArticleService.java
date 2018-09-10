package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.common.enums.WorthTypeEnum;
import com.netx.common.user.dto.article.ArticleCreateTimeDto;
import com.netx.ucenter.mapper.user.ArticleMapper;
import com.netx.ucenter.model.user.Article;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资讯表（图文、音视） 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-09-14
 */
@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private ArticleMapper articleMapper;

    public ArticleMapper getArticleMapper() {
        return articleMapper;
    }

    public List<Article> getArticleList(Integer articleType, String title, String userId) throws Exception {
        EntityWrapper<Article> wrapper = new EntityWrapper<Article>();
        wrapper.where("id is not null and deleted=0");
        if (articleType != null) {
            wrapper.and("article_type={0}", articleType);
        }
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(userId)) {
            wrapper.and("user_id={0}", userId);
        }
        return selectList(wrapper);
    }

    public List<Article> getArticlePage(Page page) throws Exception {
        Wrapper wrapper = new EntityWrapper();
        return selectPage(page, wrapper).getRecords();
    }

    public Article getArticleByUserId(String id, String userId) {
        Wrapper<Article> wrapper = new EntityWrapper<>();
        wrapper.where("user_id ={0} and deleted=0", userId);
        if (!StringUtils.isEmpty(id)) {
            wrapper.and("id = {0}", id);
        }
        wrapper.orderBy("create_time");
        return this.selectOne(wrapper);
    }
    /**
     * 检测是否存在同样的内容
     * @param content
     * @return
     */
    public boolean whetherTheSameContentExists(String content){
        EntityWrapper<Article> articleEntityWrapper=new EntityWrapper<>();
        articleEntityWrapper.where("content={0}",content);
        if(selectCount(articleEntityWrapper)>0) {
            return true;
        }
        return false;
    }
    public Page<Article> getAllArticle(Page page, String title, Integer statusCode, Integer advertorialType,String userId) {
        EntityWrapper<Article> wrapper = new EntityWrapper<>();
        wrapper.where("deleted = 0");
        if (statusCode != null) {
            wrapper.and("status_code = {0}", statusCode);
        }
        if (advertorialType != null) {
            wrapper.and("advertorial_type = {0}", advertorialType);
        }
        if (StringUtils.isNotBlank(userId)){
            wrapper.and("user_id={0}",userId);
        }
        if (StringUtils.isNotBlank(title)) {
            wrapper.like("title", title);
        }
        wrapper.orderBy("create_time",false);
        return this.selectPage(page, wrapper);
    }

    public Boolean deleteByUserId(String Id) {
        Article article=new Article();
        Wrapper<Article> wrapper = new EntityWrapper<>();
        article.setDeleted(1);
        wrapper.eq("id", Id);
        return this.update(article, wrapper);
    }

    public List<String> queryArticleIdsByUserId(String userId) {
        Wrapper<Article> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.setSqlSelect("id");
        return (List<String>) (List) selectObjs(wrapper);
    }

    public List<Article> getArticlesByUserId(Page page, String userId, Boolean isArticleType, Boolean isAsc, String publishUserId) throws Exception {
        Wrapper<Article> articleWrapper = new EntityWrapper<Article>();
        articleWrapper.where("user_id = {0} and deleted=0", publishUserId).orderBy("create_time" + isAscStr(isAsc));
        if (isArticleType != null) {
            articleWrapper.and("advertorial_type = {0}", isArticleType ? 1 : 0);
        }
        if (!userId.equals(publishUserId)) {
            articleWrapper.and("is_lock = {0}", false);
        }
        return selectPage(page, articleWrapper).getRecords();
    }

    public List<Article> getArticleListByIds(List<Object> ids, Boolean isLock) throws Exception {
        Wrapper<Article> wrapper = selectArticleListWrapper(isLock);
        wrapper.in("id", ids);
        return selectList(wrapper);
    }

    public List<Article> getArticleListById(List<String> ids, Page page, Boolean articleType) throws Exception {
        Wrapper<Article> wrapper = new EntityWrapper<>();
        wrapper.in("id", ids);
        if (articleType != null) {
            wrapper.and("advertorial_type = {0}", articleType ? 1 : 0);
        }
        wrapper.and("deleted=0");
        if (page == null) {
            return selectList(wrapper);
        }
        return selectPage(page, wrapper).getRecords();
    }

    public Integer selectArticleCountByUserId(String userId, Boolean articleType, Boolean isLock) throws Exception {
        Wrapper<Article> wrapper = new EntityWrapper<Article>();
        wrapper.where("user_id = {0} and deleted=0", userId);//是否封禁，不能被封禁
        //wrapper.setSqlSelect("id");
        if (articleType != null) {
            wrapper.and("advertorial_type = {0}", articleType ? 1 : 0);
        }
        if (isLock != null) {
            wrapper.and("is_lock = {0}", isLock);
        }
        return selectCount(wrapper);
    }

    public List<Article> getArticlePage(Page page, Integer top, Long time, Boolean isLock) throws Exception {
        Wrapper<Article> topWrapper = new EntityWrapper<Article>();
        topWrapper.where("top = {0} and top_expired_at > {1} and is_Lock = {2} and deleted=0", top, time, isLock);
        return selectPage(page, topWrapper).getRecords();
    }

    public List<Article> getArticleList(Integer statusCode, Boolean isLock, List<String> ids) throws Exception {
        EntityWrapper<Article> wrapper = new EntityWrapper<>();
        //查找待审核被封禁的并且选择的
        wrapper.where("status_code={0} and is_lock={1} and deleted=0", statusCode, isLock).in("id", ids);
        return this.selectList(wrapper);
    }

    public Page selectPageByStatusCode(Page page, Integer statusCode) throws Exception {
        EntityWrapper<Article> wrapper = new EntityWrapper<Article>();
        wrapper.where("status_code={0} and deleted=0", statusCode);
        return selectPage(page, wrapper);
    }

    private String isAscStr(Boolean isAsc) {
        return isAsc ? "" : " desc";
    }

    /**
     * 生成查询咨讯列表 wrapper
     * 被封禁的咨讯无法查出来
     * 里面设置好了 wrapper 的 setSqlSelect 和 where
     *
     * @param isLock 是否封禁
     * @return
     */
    private Wrapper<Article> selectArticleListWrapper(Boolean isLock) {
        Wrapper<Article> wrapper = new EntityWrapper<>();
        wrapper.where("is_lock = {0} and deleted=0", isLock);
        return wrapper;
    }

    public List<Map<String, Object>> queryAritcleStat() {
        Wrapper<Article> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("sum(hits) as hits,user_id as userId");
        wrapper.where("deleted=0");
        wrapper.orderBy("hits");
        wrapper.groupBy("userId");
        return selectMaps(wrapper);
    }

    public List<String> queryIdsByUserId(String userId) {
        Wrapper<Article> wrapper = new EntityWrapper<>();
        wrapper.where("deleted=0 and user_id = {0} and deleted=0", userId);
        wrapper.setSqlSelect("id");
        return (List<String>) (List) selectObjs(wrapper);
    }

    /*根据userId查询图文总数  @梓*/
    public Integer getArticleCountByUserId(String userId) {
        Wrapper<Article> wrapper = new EntityWrapper<Article>();
        wrapper.where("user_id = {0} and deleted=0", userId);
        return selectCount(wrapper);
    }

    /**
     * 根据userId查询文章创建时间，返回标题，用户Id，还有创建时间
     *
     * @param userId
     * @return
     */
    public List<Article> selectArticleCreateTimeByUserId(String userId) {
        Wrapper<Article> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("user_id as userId,title,create_time as createTime");
        wrapper.where("user_id = {0}", userId);
        wrapper.orderBy("create_time");
        return selectList(wrapper);
    }

    public boolean updateStatusbyUserIdAndStatus(String articleId,int status){
        Article article=new Article();
        article.setId(articleId);
        article.setStatusCode(status);
        return this.updateById(article);
    }
    /**
     * 根据articleId，修改update_user_id
     * @param adminId
     * @param articleId
     * @return
     */
    public boolean updateUpdateUserIdByArticleId(String adminId,String articleId){
        Article article=new Article();
        article.setId(articleId);
        article.setUpdateUserId(adminId);
        return this.updateById(article);
    }

    /**
     * 返回比赛相关图文
     * @param matchId
     * @param page
     * @return
     */
    public Page<Article> selectArticlePageByMatchId(String matchId, Page<Article> page) {
        EntityWrapper<Article> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("worth_type={0}", WorthTypeEnum.MATCH_TYPE.getName())
                .and()
                .like("worth_ids", matchId);
        return selectPage(page, entityWrapper);
    }
}
