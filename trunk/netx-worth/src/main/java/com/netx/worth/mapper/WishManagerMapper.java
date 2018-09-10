package com.netx.worth.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.model.WishManager;

/**
 * <p>
  * 心愿监管人表，监管者的数量按支持者的总数确定。支持人数10人及以下，1名监管者；11~49人，3名；50人及以上，5名 Mapper 接口
 * </p>
 *
 * @author 逍遥游
 * @since 2017-09-18
 */
public interface WishManagerMapper extends BaseMapper<WishManager> {

}