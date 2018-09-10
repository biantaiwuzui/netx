package com.netx.shopping.biz.redpacketcenter;


import com.netx.common.user.util.DateTimestampUtil;
import com.netx.shopping.model.ordercenter.constants.OrderStatusEnum;
import com.netx.shopping.model.ordercenter.MerchantOrderInfo;
import com.netx.shopping.model.redpacketcenter.RedpacketPool;
import com.netx.shopping.model.redpacketcenter.RedpacketRecord;
import com.netx.shopping.model.redpacketcenter.RedpacketSend;
import com.netx.shopping.service.ordercenter.MerchantOrderInfoService;
import com.netx.shopping.service.redpacketcenter.RedpacketPoolService;
import com.netx.shopping.service.redpacketcenter.RedpacketRecordService;
import com.netx.shopping.service.redpacketcenter.RedpacketSendService;
import com.netx.utils.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * <p>
 * 网商-红包发放表 前端控制器
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@Service("newRedpacketSendAction")
public class RedpacketSendAction {

    private Logger logger = LoggerFactory.getLogger(RedpacketSendAction.class);


    @Autowired
    RedpacketSendService redpacketSendService;

    @Autowired
    MerchantOrderInfoService merchatOrderInfoService;

    @Autowired
    RedpacketRecordService redpacketRecordService;

    @Autowired
    RedpacketPoolService redpacketPoolService;

    /**
     * 判断是否有抢红包资格
     */
    public boolean judgeReceive(String userId){
        if (merchatOrderInfoService.getProductOrderCountByUserIdAndTime(userId,new Date(DateTimestampUtil.getStartOrEndOfTimestamp(new Date().getTime(), 1)))<1) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否抢到红包
     * @return
     */
    public boolean judgeWhetherRobSuccess(String userId,String redpacketSendId){
        if (redpacketRecordService.getRedpacketRecord(userId,redpacketSendId,"a")!=null){
            return true;
        }
        return false;
    }


    /**
     *判断是否抢过红包
     */
    public RedpacketRecord judgeRobRedpacket(String userId, String redpacketSendId){
        return redpacketRecordService.getRedpacketRecord(userId,redpacketSendId);
    }



    public void insertRobNoneRedpacketRecord(String userId,String redpacketSendId){
        RedpacketRecord redpacketRecord=new RedpacketRecord();
        redpacketRecord.setUserId(userId);
        redpacketRecord.setRedpacketSendId(redpacketSendId);
        redpacketRecordService.insert(redpacketRecord);
    }

    /**
     * 插入红包记录表
     * @param amount
     * @param redpacketSendId
     * @param userId
     */
    public void insertReapacketRecord(BigDecimal amount, String redpacketSendId, String userId){
        RedpacketRecord redpacketRecord = new RedpacketRecord();
        redpacketRecord.setAmount(new Money(amount).getCent());
        redpacketRecord.setRedpacketSendId(redpacketSendId);
        redpacketRecord.setUserId(userId);
        redpacketRecordService.insert(redpacketRecord);
    }

    /**
     * 判断用户注册以来是否第一次抢红包
     * @param userId
     * @return
     */
    public boolean judgeWhetherFirstRob(String userId){
        if (redpacketRecordService.getRedpacketRecord(userId)==null){
            return true;
        }
        return false;
    }

    /**
     * 获取用户今日抢到红包次数
     * @param userId
     * @return
     */
    public Integer getUserTodayRobNum(String userId){
        Long start=DateTimestampUtil.getStartOrEndOfTimestamp(System.currentTimeMillis(),1);
        Long end =DateTimestampUtil.getStartOrEndOfTimestamp(System.currentTimeMillis(),2);
        return redpacketRecordService.getRedpacketRecordCount(userId,new Date(start),new Date(end));
    }

    /**
     * 获取今日最后一个已发放红包
     * @return
     */
    public RedpacketSend getTodayLastRedpacket(){
        Long start = DateTimestampUtil.getStartOrEndOfTimestamp(new Date().getTime(), 1) ;
        return redpacketSendService.getRedpacketSend(new Date(start),">");
    }

    /**
     * 判断红包是否抢完
     * @param id 红包id
     * @return
     */
    public boolean judgeIsRobFinish(String id){
        RedpacketSend redpacketSend=redpacketSendService.selectById(id);
        if (redpacketSend.getNum()==0 || new Date().getTime()-DateTimestampUtil.getTimestampByHM(String.valueOf(redpacketSend.getSendTime()))>30*60*1000){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取现在发放的是第几个红包
     */
    public Integer getRedpacketNO(){
        //获取今日开始时间
        Long start = DateTimestampUtil.getStartOrEndOfTimestamp(new Date().getTime(), 1);
        Integer res=redpacketSendService.countRedpackSend(new Date(start),">");
        return res;
    }

    /**
     * 获取分到的红包金额
     * @param allAmount 红包总额
     * @param surplusAmount
     * @return
     */
    public BigDecimal getRedpacketAmount(BigDecimal allAmount,BigDecimal surplusAmount){
        //红包分配金额上限
        double maxAmount = allAmount.doubleValue() / 10;
        //剩余金额
        double otherAmount = surplusAmount.doubleValue() * 0.75 * 100;
        Random random = new Random();
        double randomNum = random.nextInt((int) (otherAmount));
        logger.info(randomNum+"随机数");
        if (randomNum < 1) {
            return null;
        } else if(randomNum/100>maxAmount){
            logger.info(maxAmount+"********************maxAmount");
            return BigDecimal.valueOf(maxAmount);
        }else {
            logger.info(randomNum/100+"********************a");
            return BigDecimal.valueOf(randomNum / 100);
        }
    }

    /**
     * 判断是否为昨日订单金额最大者
     */
    public boolean judgeWhetherMaxOrderUser(String userId){
        Long startTime = DateTimestampUtil.getStartOrEndOfTimestamp(DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 1, 1), 1);
        Long endTime = DateTimestampUtil.getStartOrEndOfTimestamp(DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 1, 1), 2);
        MerchantOrderInfo productOrder = merchatOrderInfoService.getproductOrder(OrderStatusEnum.OS_FINISH,new Date(startTime),new Date(endTime),"userId");
        if (productOrder !=null){
            if (productOrder.getUserId().equals(userId)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨日下订单最早者
     */
    public boolean judgeWhetherEarlyOrderUser(String userId){
        Long startTime = DateTimestampUtil.getStartOrEndOfTimestamp(DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 1, 1), 1);
        Long endTime = DateTimestampUtil.getStartOrEndOfTimestamp(DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 1, 1), 2);
        MerchantOrderInfo productOrder = merchatOrderInfoService.getproductOrder(OrderStatusEnum.OS_FINISH,new Date(startTime),new Date(endTime));
        if (productOrder !=null){
            if (productOrder.getUserId().equals(userId)){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取用户累计抢红包金额
     * @param userId
     * @return
     */
    public BigDecimal getUserRobRedpacketAmount(String userId){

        return redpacketRecordService.getAmount(userId);
    }


    /**
     * 获取用户累计订单金额
     * @param userId
     * @return
     */
    public long getAllOrderAmount(String userId){
        Long startTime = DateTimestampUtil.getStartOrEndOfTimestamp(DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 1, 1), 1);
        return merchatOrderInfoService.getAllOrderAmount(userId,new Date(startTime));
    }

    /**
     * 检查当前红包是否抢完
     * @return
     */
    public RedpacketSend checkIsSendFinish(){
        Long startTime = DateTimestampUtil.getStartOrEndOfTimestamp(DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 1, 1), 1) ;
        RedpacketSend redpacketSend=redpacketSendService.getRedpacketSend(new Date(startTime),">");
        if (redpacketSend==null){
            return null;
        }
        boolean res=redpacketSend.getRedpacketNum()==0?true:false;
        if (res){
            return null;
        }else {
            return redpacketSend;
        }
    }


    /**
     * 获取今天红包总额
     *
     */
    public BigDecimal getLastDayAmount() {
        RedpacketPool sellerRedpacketPool = redpacketPoolService.get();
        if(sellerRedpacketPool==null || sellerRedpacketPool.getRedpacketAmount()==null){
            return new BigDecimal(0.0);
        }
        return Money.CentToYuan(sellerRedpacketPool.getRedpacketAmount()).getAmount();
    }

    /**
     * 获取领取红包资格人数
     */
    public int getQualificationNum() {
        Long time = DateTimestampUtil.getStartOrEndOfTimestamp(new Date().getTime(), 1);
        int num = merchatOrderInfoService.getProductOrderList(new Date(time),OrderStatusEnum.OS_FINISH).size();
        return num;
    }

    /**
     * 时间戳转换为日期
     */
    private String getCurrentTimeStr(Long time){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(time);
    }
}
