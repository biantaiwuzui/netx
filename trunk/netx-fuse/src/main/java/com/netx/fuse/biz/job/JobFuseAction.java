package com.netx.fuse.biz.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netx.common.common.enums.AuthorEmailEnum;
import com.netx.common.common.enums.JobEnum;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.fuse.client.job.JobClient;
import com.netx.ucenter.biz.common.JobHistoryAction;
import com.netx.ucenter.model.common.JobHistory;
import com.netx.ucenter.service.common.CommonServiceProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobFuseAction {

    private Logger logger = LoggerFactory.getLogger(JobFuseAction.class);

    @Autowired
    private JobHistoryAction jobHistoryAction;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    @Autowired
    private JobClient jobClient;

    /**
     * 获取定时任务
     * @param jobEnum 定时任务的执行器
     * @param typeId 事件id
     * @param typeName 事件名
     * @param param 定时任务参数【json格式】,单参不用json
     * @return
     */
    public JobHistory queryJobHistory(JobEnum jobEnum, String typeId, String typeName, String param){
        return commonServiceProvider.getJobHistoryService().queryJob(jobEnum, typeId,typeName,param);
    }

    /**
     * 添加定时任务
     * @param jobEnum 定时任务的执行器
     * @param param 定时任务参数【json格式】,单参不用json
     * @param typeId 事件id
     * @param typeName 事件名
     * @param executeTime 执行时间
     * @param authorEmailEnum 管理员
     * @return
     */
    public Boolean addJob(JobEnum jobEnum, String param,String typeId, String typeName,Date executeTime,AuthorEmailEnum authorEmailEnum){
        return addJob(jobEnum, param, typeId, typeName, DateTimestampUtil.getCron(executeTime), authorEmailEnum);
    }

    /**
     * 添加定时任务
     * @param jobEnum 定时任务的执行器
     * @param param 定时任务参数
     * @param typeId 事件id
     * @param typeName 事件名
     * @param executeTime 执行时间
     * @param authorEmailEnum 管理员
     * @return
     */
    public Boolean addJob(JobEnum jobEnum, Object param,String typeId, String typeName,Date executeTime,AuthorEmailEnum authorEmailEnum){
        String json = param==null?null:JSON.toJSONString(param);
        return addJob(jobEnum,json , typeId, typeName, DateTimestampUtil.getCron(executeTime), authorEmailEnum);
    }

    /**
     * 添加定时任务
     * @param jobEnum 定时任务的执行器
     * @param param 定时任务参数【json格式】,单参不用json
     * @param typeId 事件id
     * @param typeName 事件名
     * @param cron cron的表达式
     * @param authorEmailEnum 管理员
     * @return
     */
    public Boolean addJob(JobEnum jobEnum, String param,String typeId, String typeName,String cron,AuthorEmailEnum authorEmailEnum){
        if(queryJobHistory(jobEnum, typeId, typeName, param)==null){
            try {
                HttpResponse httpResponse = jobClient.add(createJobMap(jobEnum, param, typeName, cron, authorEmailEnum));
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject = getJSONObject(getResultJson(httpResponse));
                    if(jsonObject.getInteger("code")==200){
                        jobHistoryAction.addJobHistory(jsonObject.getInteger("content"),param,typeId,typeName,jobEnum.getName());
                        return true;
                    }
                    System.out.println(jsonObject);
                }

            }catch (Exception e){
                System.out.println("error:"+e.getMessage());
                logger.error(e.getMessage(),e);
            }
        }
        return false;
    }

    private Map<String,String> createJobMap(JobEnum jobEnum,String param, String typeName,String cron,AuthorEmailEnum authorEmailEnum){
        if(authorEmailEnum==null){
            authorEmailEnum = AuthorEmailEnum.ZI_AN;
        }
        Map<String,String> map = new HashMap<>();
        map.put("jobGroup","1");
        map.put("jobDesc",typeName);
        map.put("executorRouteStrategy","FIRST");
        map.put("jobCron", cron);
        map.put("glueType","BEAN");
        map.put("executorParam",param);
        map.put("executorHandler",jobEnum.getName());
        map.put("executorBlockStrategy","SERIAL_EXECUTION");
        map.put("executorFailStrategy","FAIL_ALARM");
        map.put("author",authorEmailEnum.getName());
        map.put("alarmEmail",authorEmailEnum.getEmail());
        map.put("glueRemark","GLUE代码初始化");
        return map;
    }

    /**
     * 删除定时任务
     * @param jobEnum 定时任务的执行器
     * @param typeId 事件id
     * @param typeName 事件名
     * @param param 定时任务参数
     * @return
     */
    public Boolean removeJob(JobEnum jobEnum,String typeId,String typeName,Object param){
        String json = param==null?null:JSON.toJSONString(param);
        return removeJob(jobEnum, typeId, typeName, json);
    }

    /**
     * 删除定时任务
     * @param jobEnum 定时任务的执行器
     * @param typeId 事件id
     * @param typeName 事件名
     * @param param 定时任务参数【json格式】,单参不用json
     * @return
     */
    public Boolean removeJob(JobEnum jobEnum,String typeId,String typeName,String param){
        if(jobEnum!=null){
            JobHistory jobHistory = queryJobHistory(jobEnum, typeId,typeName,param);
            if(jobHistory!=null){
                Map<String,String> map = new HashMap<>();
                map.put("id",jobHistory.getJobId()+"");
                try {
                    if(getHttpResponse(jobClient.remove(map))){
                        jobHistory.setDeleted(1);
                        return commonServiceProvider.getJobHistoryService().updateById(jobHistory);
                    }
                }catch (Exception e){
                    logger.error(e.getMessage()+"删除定时任务失败",e);
                }
            }
        }
        return false;
    }
    
    /* 批量删除定时任务 **/
    public Boolean BatchRemoveJob(String typeId){
        if(StringUtils.isNotBlank(typeId)){
            List<JobHistory> jobHistories = commonServiceProvider.getJobHistoryService().batchDeleteMeetingJob(typeId);
            if (jobHistories != null && jobHistories.size()>0) {
                for(JobHistory jobHistory : jobHistories){
                    Map<String,String> map = new HashMap<>();
                    map.put("id",jobHistory.getJobId()+"");
                    try {
                        if(getHttpResponse(jobClient.remove(map))){
                            jobHistory.setDeleted(1);
                            commonServiceProvider.getJobHistoryService().updateById(jobHistory);
                        }
                    }catch (Exception e){
                        logger.error(e.getMessage()+jobHistory.getTypeName()+":删除定时任务失败",e);
                    }
                }
                return true;
            }
        }
        return null;
    }

    /**
     * 暂停定时任务
     * @param jobEnum 定时任务的执行器
     * @param typeId 事件id
     * @param typeName 事件名
     * @param param 定时任务参数【json格式】,单参不用json
     * @return
     */
    public Boolean pauseJob(JobEnum jobEnum,String typeId,String typeName,String param){
        if(jobEnum!=null){
            JobHistory jobHistory = queryJobHistory(jobEnum, typeId,typeName,param);
            if(jobHistory!=null){
                Map<String,String> map = new HashMap<>();
                map.put("id",jobHistory.getJobId()+"");
                try {
                    if(getHttpResponse(jobClient.pause(map))){
                        return commonServiceProvider.getJobHistoryService().deleteById(jobHistory.getId());
                    }
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
            }
        }
        return false;
    }

    /**
     * 恢复定时任务
     * @param jobEnum 定时任务的执行器
     * @param typeId 事件id
     * @param typeName 事件名
     * @param param 定时任务参数【json格式】,单参不用json
     * @return
     */
    public Boolean resumeJob(JobEnum jobEnum,String typeId,String typeName,String param){
        if(jobEnum!=null){
            JobHistory jobHistory = queryJobHistory(jobEnum, typeId,typeName,param);
            if(jobHistory!=null){
                Map<String,String> map = new HashMap<>();
                map.put("id",jobHistory.getJobId()+"");
                try {
                    if(getHttpResponse(jobClient.resume(map))){
                        return commonServiceProvider.getJobHistoryService().deleteById(jobHistory.getId());
                    }
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
            }
        }
        return false;
    }

    private Boolean getHttpResponse(HttpResponse response){
        return response.getStatusLine().getStatusCode()==200;
    }

    private String getResultJson(HttpResponse httpResponse){
        String json = null;
        try {
            HttpEntity httpEntity = httpResponse.getEntity();
            json = new String(EntityUtils.toByteArray(httpEntity),"UTF-8");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return json;
    }

    private JSONObject getJSONObject(String json){
        return json!=null? JSON.parseObject(json):null;
    }


}
