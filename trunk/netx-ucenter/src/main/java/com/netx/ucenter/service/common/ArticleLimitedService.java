package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonArticleLimitMapper;
import com.netx.ucenter.model.common.CommonArticleLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class ArticleLimitedService extends ServiceImpl<CommonArticleLimitMapper,CommonArticleLimit>{
    private Logger logger = LoggerFactory.getLogger(ArticleLimitedService.class);

    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonArticleLimit>().where("user_id={0}",userId));
    }

    public Boolean insertWzCommonArticleLimited(CommonArticleLimit wzCommonArticleLimited) throws Exception{
        wzCommonArticleLimited.setDate(new Date());
        wzCommonArticleLimited.setDeleted(0);
        wzCommonArticleLimited.setCreateTime(new Date());
        return this.insert(wzCommonArticleLimited);
    }

    public CommonArticleLimit selectOneByUserId(String userId){
        Wrapper wrapper = new EntityWrapper<CommonArticleLimit>();
        wrapper.where("user_id={0}",userId);
        return selectOne(wrapper);
    }

    public Long countByStartToEnd(Long start,Long end){
        Wrapper wrapper = new EntityWrapper<CommonArticleLimit>();
        wrapper.setSqlSelect("COUNT(id)");
        wrapper.between("create_time",start,end);
        return (Long) selectObj(wrapper);
    }

    public List<CommonArticleLimit> selectArticleLimitedList(Integer current,Integer size){
        //分页查询
        Page<CommonArticleLimit> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        EntityWrapper<CommonArticleLimit> wrapper=new EntityWrapper<>();
        wrapper.orderBy("date",false);
        Page pageResult=selectPage(page,wrapper);
        return pageResult.getRecords();
    }
}
