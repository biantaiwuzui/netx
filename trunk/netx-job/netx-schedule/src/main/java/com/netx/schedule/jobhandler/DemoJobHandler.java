package com.netx.schedule.jobhandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.ucenter.biz.user.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 任务Handler示例（Bean模式）
 *
 * 开发步骤：
 * 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHandler(value="DemoJobHandler")
@Component
public class DemoJobHandler extends IJobHandler {

	@Autowired
	UserAction userAction;
	@Override
	public ReturnT<String> execute(String json) throws Exception {
		JSONObject jsonObject = JSON.parseObject(json);
		System.out.println(jsonObject);
		XxlJobLogger.log(new Date()+"："+"Hello World.");
		test(userAction.getAllUserId());
		return SUCCESS;
	}

	private void test(List<String> list){
		BufferedWriter out = null;
		try {
			String content = list!=null?list.toString():"";
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("/data/logs/test/test.txt", true)));
			out.write(new Date()+"："+content+"\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
