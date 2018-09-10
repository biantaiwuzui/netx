package com.netx.ucenter.biz.common;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.netx.common.vo.common.*;
import com.netx.ucenter.model.common.CommonTags;
import com.netx.ucenter.service.common.TagsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Create by wongloong on 17-9-1
 */
@Service
public class TagsAction {
    private Logger logger = LoggerFactory.getLogger(TagsAction.class);

    @Autowired
    private TagsService tagsService;

    public TagsService getTagsService() {
        return tagsService;
    }

    public void deleteTag(String userId) throws Exception {
        tagsService.deleteTag(userId);
    }

    public Boolean deleteById(String id) throws Exception {
        return tagsService.deleteById(id);
    }

    private void createWzCommonTags(CommonTags tags, String value, Integer type, String typeCate, String createUser, Boolean cate) {
        tags.setCreateUserId(createUser);
        tags.setValue(value);
        tags.setType(type);
        tags.setTypeCate(typeCate);
        tags.setCatePrivate(cate ? 0 : 1);
    }

    public Boolean saveOrUpdate(TagsAddRequestDto request) throws Exception {
        CommonTags tags = new CommonTags();
        Boolean flag = request.getCreateUser().equals("0");
        if (!StringUtils.isEmpty(request.getId())) {
            //标签id
            tags = tagsService.selectById(request.getId());
            if (tags == null) {
                throw new RuntimeException("此标签不存在");
            }
        }
        else {
            //根据当前用户id，判断用户是否已创建相同的标签
            if (!flag) {
                List<CommonTags> tagsList = tagsService.getWzCommonTagsList(request.getValue(), request.getCreateUser(), request.getTypeCate());
                if (null != tagsList && !tagsList.isEmpty()) {//不允许添加公用标签已存在的值
                    //throw new Exception("当前标签值已存在");
                    return null;
                }
            }
        }
        createWzCommonTags(tags, request.getValue(), request.getType(), request.getTypeCate(), request.getCreateUser(), flag);
        try {
            tags.setPy(PinyinHelper.getShortPinyin(tags.getValue()).substring(0, 1).toUpperCase());
        } catch (PinyinException e) {
            logger.warn("拼音转换出错", e);
        }
        return tagsService.insertOrUpdate(tags);
    }

    public List<CommonTags> queryList(TagsQueryRequestDto request) throws Exception {
        return tagsService.queryList(request.getCreateUser(), request.getType(), request.getTypeCate());
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean changeTagsScope(TagsScopeChangeRequestDto request) {
        if (request.getIds().length > 0) {
            List<CommonTags> wzCommonTagsList = tagsService.selectBatchIds(Arrays.asList(request.getIds()));
            wzCommonTagsList.forEach(tag -> {
                if (request.getToPublic() == 0) {//降级
                    tag.setDeleted(1);
                    tag.setUpdateTime(new Date());
                    tagsService.updateById(tag);
                } else {//升级
                    tag.setCreateUserId("0");
                    tag.setId(null);
                    tag.setCreateTime(new Date());
                    tagsService.insert(tag);
                }
            });
        }
        return true;
    }

    /**
     * lcx
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTags(TagsUpdateRequestDto request) {
        CommonTags commonTags = tagsService.selectById(request.getId());
        commonTags.setValue(request.getValue());
        commonTags.setTypeCate(request.getTypeCate());
        commonTags.setUpdateUserId("admin");//TODO lcx
        return tagsService.insertOrUpdate(commonTags);
    }

    public String selectByIds(String ids) {
        if (ids.indexOf(",") > 0) {//多个id
            String[] idsList = ids.split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for (String id : Arrays.asList(idsList)) {
                CommonTags tag = tagsService.selectById(id);
                if (tag.getDeleted().intValue() == 0) {
                    stringBuffer.append(tag.getValue());
                    stringBuffer.append(",");
                }
            }
            return stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1);
        } else {
            CommonTags tag = tagsService.selectById(ids);
            if (tag.getDeleted().intValue() == 0) {
                return tag.getValue();
            } else {
                return "";
            }
        }
    }
}
