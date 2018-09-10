package com.netx.worth.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.utils.DistrictUtil;
import com.netx.worth.mapper.ActiveLogMapper;
import com.netx.worth.model.ActiveLog;

@Service
public class ActiveLogService extends ServiceImpl<ActiveLogMapper, ActiveLog> {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	/**得到附近的
	 * @return */
	public List<ActiveLog> getNearList(String userId, BigDecimal lon, BigDecimal lat, Double length, Page<ActiveLog> page) {
		//TODO 距离WRAPPER
		//EntityWrapper<ActiveLog> entityWrapper = DistrictUtil.buildEntityWrapper("geohash", DistrictUtil.getHashAdjcent(lat, lon, length));
		EntityWrapper<ActiveLog> entityWrapper = new EntityWrapper<>();//暂时逻辑
		if (StringUtils.hasText(userId)) {
			entityWrapper.ne("user_id", userId);
		}
		Page<ActiveLog> selectPage = selectPage(page, entityWrapper);
		return selectPage.getRecords();
	}

	public ActiveLog selectById(String id){
		EntityWrapper<ActiveLog> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("deleted=0 and id={0}",id);
		return selectOne(entityWrapper);
	}
}
