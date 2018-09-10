package com.netx.ucenter.mapper.common;

import com.netx.common.vo.common.TagQueryCateResponse;
import com.netx.common.vo.common.TagsQueryRequestDto;
import com.netx.ucenter.model.common.CommonTags;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
public interface CommonTagsMapper extends BaseMapper<CommonTags> {
	List<CommonTags> selectPrivate();
    int updateToPublic(@Param("typeCate") String typeCate);
    int updateToPrivate(@Param("typeCate") String typeCate);

    List<TagQueryCateResponse> selectTypeList(@Param("request") TagsQueryRequestDto request);
}