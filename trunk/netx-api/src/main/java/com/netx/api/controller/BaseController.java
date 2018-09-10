package com.netx.api.controller;

import com.netx.api.component.UserTokenState;
import com.netx.api.security.TokenHelper;
import com.netx.common.redis.model.UserGeo;
import com.netx.utils.json.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BaseController {

	@Autowired
	protected TokenHelper tokenHelper;

	protected JsonResult getResult(boolean success,String msg) {
		if(success) {
			return JsonResult.success();
		}else {
			return JsonResult.fail(msg);
		}
	}

	protected JsonResult getResult(boolean success,String successMsg,String failMsg) {
		if(success) {
			return JsonResult.success(successMsg);
		}else {
			return JsonResult.fail(failMsg);
		}
	}

	protected JsonResult successResult(Map map){
		JsonResult jsonResult = JsonResult.success();
		jsonResult.setResult(map);
		return jsonResult;
	}

	/**
	 * 获取token里的userId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected String getUserId(HttpServletRequest request) throws Exception{
		String authToken = tokenHelper.getToken(request);
		if(authToken==null){
			throw new RuntimeException("请先授权登录");
		}
		String userId = tokenHelper.getUsernameFromToken(authToken);
		refreshToken(userId,request);
		return userId;
	}

	/**
	 * token时间延长
	 * @param token
	 * @param request
	 */
	private void refreshToken(String token,HttpServletRequest request){
		Device device = DeviceUtils.getCurrentDevice(request);
		if(token!=null){
			int expiresIn = tokenHelper.getExpiredIn(device);
			new UserTokenState(token, expiresIn);
		}
	}

	/**
	 * userId优先级返回
	 * @param userId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected String getUserId(String userId,HttpServletRequest request) throws Exception{
		if(userId==null || userId.length()==0){
			return getUserId(request);
		}
		return userId;
	}

	protected void checkGeo(Double lon,Double lat) throws Exception{
		if(lon<-180 || lon>180){
			throw new RuntimeException("输入的经度不合法(-180,180)");
		}
		if(lat<-90 || lat>90){
			throw new RuntimeException("输入的纬度不合法(-90,90)");
		}
	}

	/**
	 * 获取用户id及经纬度
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected UserGeo getGeoFromRequest(HttpServletRequest request) throws Exception{
		UserGeo userGeo = new UserGeo();
		userGeo.setLat(getLat(request));
		userGeo.setLon(getLon(request));
		userGeo.setUserId(getUserId(request));
		return userGeo;
	}

	/**获得当前用户id*/
	public String getUserIdInWorth(HttpServletRequest request){
		String userId;
		try{
			userId = getUserId(request);
			if(StringUtils.isEmpty(userId)){
				throw new RuntimeException ("用户id不能为空");
			}
		}catch (Exception e){
			throw new RuntimeException ("用户id不能为空");
		}
		return userId;
	}

	protected Double getLon(HttpServletRequest request){
		return parseDouble(request.getParameter("lon"));
	}

	protected Double getLat(HttpServletRequest request){
		return parseDouble(request.getParameter("lat"));
	}

	private Double parseDouble(String value) {
		Double result = 0.0;
		if(!StringUtils.isEmpty(value)){
			try {
				result = new Double(value);
			} catch (Exception e) {
			}
		}
		return result;
	}
}
