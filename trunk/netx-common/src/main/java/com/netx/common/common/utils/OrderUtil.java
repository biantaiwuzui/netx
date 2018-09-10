package com.netx.common.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 订单工具类
 * </p>
 *
 * @author wj.liu
 * @since 2017-08-29
 */
public class OrderUtil {


	/**
	 * 获取订单号，规则: 当前时间yyyyMMddHHmmss+5位随机数。
	 * @return
	 */
	public static String getOrderNumber(){
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentTime = format.format(new Date());
		String randomNum = getRandomInt(10000, 99999)+"";
		return currentTime+randomNum;
	}

	/**
	 * 获取min - max 之间的随机数
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomInt(int min, int max){
		int result = 0;
		result = (int) (min + Math.random() * (max - min));
		return result;
	}

}
