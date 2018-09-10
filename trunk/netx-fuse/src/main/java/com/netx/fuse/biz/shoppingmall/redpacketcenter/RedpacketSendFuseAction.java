package com.netx.fuse.biz.shoppingmall.redpacketcenter;

import com.netx.common.common.enums.AuthorEmailEnum;
import com.netx.common.common.enums.JobEnum;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.vo.business.GetRedpacketInfoResponseDto;
import com.netx.common.vo.business.LuckyMoneyResponseDto;
import com.netx.common.vo.business.ReceiveRedPacketRequestDto;
import com.netx.common.vo.business.UpadteRedpacketPoolRequestDto;
import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.common.vo.common.BillStatisticsRequestDto;
import com.netx.common.vo.common.LuckMoneyQueryDto;
import com.netx.fuse.biz.job.JobFuseAction;
import com.netx.fuse.client.ucenter.LuckyMoneyClientAction;
import com.netx.fuse.client.ucenter.WalletBillClientAction;
import com.netx.fuse.proxy.WalletBillProxy;
import com.netx.shopping.biz.redpacketcenter.RedpacketPoolAction;
import com.netx.shopping.biz.redpacketcenter.RedpacketSendAction;
import com.netx.shopping.enums.RedpacketPoolRecordSourceEnum;
import com.netx.shopping.enums.RedpacketPoolRecordWayEnum;
import com.netx.shopping.model.redpacketcenter.RedpacketRecord;
import com.netx.shopping.model.redpacketcenter.RedpacketSend;
import com.netx.shopping.service.redpacketcenter.RedpacketSendService;
import com.netx.ucenter.biz.common.MessagePushAction;
import com.netx.utils.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RedpacketSendFuseAction {

    private Logger logger = LoggerFactory.getLogger(RedpacketSendAction.class);

    @Autowired
    LuckyMoneyClientAction luckyMoneyClientAction;
    @Autowired
    WalletBillProxy walletBillProxy;
    @Autowired
    RedpacketSendService redpacketSendService;
    @Autowired
    RedpacketSendAction redpacketSendAction;
    @Autowired
    WalletBillClientAction walletBillClientAction;
    @Autowired
    RedpacketPoolAction redpacketPoolAction;
    @Autowired
    JobFuseAction jobFuseAction;
    @Autowired
    MessagePushAction messagePushAction;

    /**
     * 查询红包设置 time1和time2二选一
     * @param status 状态 1.已审核 2.等待审核,3.明天生效
     * @param timeStamp 时间戳
     * @param stringTime String时间，如 22:00
     * @return
     */
    public List<LuckyMoneyResponseDto> getLuckMoneySet(Integer status, Long timeStamp, String stringTime){
        LuckMoneyQueryDto luckMoneyQueryDto = new LuckMoneyQueryDto();
        if (stringTime==null) {
            luckMoneyQueryDto.setTime(DateTimestampUtil.getNowByHM(timeStamp));
        }else {
            luckMoneyQueryDto.setTime(stringTime);
        }
        luckMoneyQueryDto.setStatus(status);
        List<LuckyMoneyResponseDto> list = luckyMoneyClientAction.query(luckMoneyQueryDto);
        return list;
    }

    /**
     * 获取下个能够发放的红包
     * @return
     * @throws
     */
    public LuckyMoneyResponseDto getNextRedpacket(){
        List<LuckyMoneyResponseDto> list=this.getLuckMoneySet(1,new Date().getTime(),null);
        LuckyMoneyResponseDto dto= list.size() == 0 ? null : list.get(0);
        return dto;
    }

    /**
     * 获取今天红包发放个数
     */

    public Integer getRedpacketAllNum(){
        return this.getLuckMoneySet(1,0l,"00:00").size();
    }

    public List<BigDecimal> getAllConsumptionAndCurrencyConsumption(String userId) throws Exception{
        List<BigDecimal> result=new ArrayList<>();
        //用户累计消费总额
        BillStatisticsRequestDto billQueryRequestDto = new BillStatisticsRequestDto();
        billQueryRequestDto.setPayChannel(3);
        billQueryRequestDto.setQueryType(0);
        billQueryRequestDto.setUserId(userId);
        BigDecimal change=walletBillProxy.queryBillAmountList(billQueryRequestDto);//todo
        change = change == null ? new BigDecimal(0) : change;
        logger.info(change+"************change");

        result.add(change);
        return result;
    }

    public BigDecimal receive(ReceiveRedPacketRequestDto requestDto) throws Exception{
        RedpacketSend redpacketSend = redpacketSendService.selectById(requestDto.getRedpacketSendId());
        int num = redpacketSend.getRedpacketNum();
        BigDecimal amount;//抢到的红包金额
        if (num > 0) {
            //判断是否有抢红包资格
            if (!redpacketSendAction.judgeReceive(requestDto.getUserId())) {
                throw new Exception("你暂未通过网商完成过交易，暂时不能抢红包哦！加油！");
            }
            if (redpacketSendAction.judgeRobRedpacket(requestDto.getUserId(), requestDto.getRedpacketSendId()) != null) {
                throw new Exception("你已抢过这个红包!");
            }
            //判断用户身份
            //最大订单者或最早订单者
            if (redpacketSendAction.judgeWhetherMaxOrderUser(requestDto.getUserId()) || redpacketSendAction.judgeWhetherEarlyOrderUser(requestDto.getUserId())) {
                if (redpacketSendAction.getUserTodayRobNum(requestDto.getUserId()) > 0) {
                    redpacketSendAction.insertRobNoneRedpacketRecord(requestDto.getUserId(),requestDto.getRedpacketSendId());
                    return null;
                }
            } else {
                if (redpacketSendAction.getUserTodayRobNum(requestDto.getUserId()) > 1) {
                    redpacketSendAction.insertRobNoneRedpacketRecord(requestDto.getUserId(),requestDto.getRedpacketSendId());
                    return null;
                }
            }
            //用户首次领取红包
            if (redpacketSendAction.judgeWhetherFirstRob(requestDto.getUserId())) {
                BigDecimal allConsumption=getAllConsumptionAndCurrencyConsumption(requestDto.getUserId()).get(0);
                BigDecimal percent=new BigDecimal(0);
                if (allConsumption.compareTo(new BigDecimal(100))!=1){
                    percent=new BigDecimal(0.02);
                }else  if(allConsumption.compareTo(new BigDecimal(100))==1&&allConsumption.compareTo(new BigDecimal(1000))!=1){
                    percent=new BigDecimal(0.01);
                }else if(allConsumption.compareTo(new BigDecimal(1000))==1&&allConsumption.compareTo(new BigDecimal(5000))!=1){
                    percent=new BigDecimal(0.005);
                }else if(allConsumption.compareTo(new BigDecimal(5000))==1){
                    percent=new BigDecimal(0.001);
                }
                amount = allConsumption.multiply(percent).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                amount = getRedpacketAmount(requestDto.getRedpacketSendId(), requestDto.getUserId()).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            //判断是否大于红包剩余金额
            if (amount.compareTo(new BigDecimal(Money.getMoneyString(redpacketSend.getSurplusAmount())))==1){
                amount=new BigDecimal(Money.getMoneyString(redpacketSend.getSurplusAmount()==null?0:redpacketSend.getSurplusAmount()));
            }
            //插入红包记录表
            redpacketSendAction.insertReapacketRecord(amount,requestDto.getRedpacketSendId(),requestDto.getUserId());
            //更新剩余红包金额
            BigDecimal surplusAmount = new BigDecimal(Money.getMoneyString(redpacketSend.getSurplusAmount()==null?0:redpacketSend.getSurplusAmount())).subtract(amount);
            redpacketSend.setSurplusAmount(new Money(surplusAmount).getCent());
            redpacketSend.setRedpacketNum(num - 1);
            redpacketSendService.updateById(redpacketSend);
            //发钱给用户
            try {
                this.grant(requestDto.getUserId(), amount);
            } catch (Exception e) {
                logger.error("红包发放失败" + e.getMessage(), e);
            }
            int newRedpacketNum = redpacketSendService.selectById(requestDto.getRedpacketSendId()).getRedpacketNum();
            if (newRedpacketNum == 0) {
                //红包抢完
                //生成下个红包
                if (this.getNextRedpacket() != null) {
                    this.send();
                }
            }
        }else {
            redpacketSendAction.insertRobNoneRedpacketRecord(requestDto.getUserId(),requestDto.getRedpacketSendId());
            amount=null;
        }
        return amount;
    }

    /**
     * 发钱给用户
     */
    public void grant(String userId,BigDecimal amount){
        //发放金额给用户
        BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
        billAddRequestDto.setAmount(amount);
        billAddRequestDto.setToUserId(userId);
        billAddRequestDto.setDescription("领取网值红包");
        billAddRequestDto.setPayChannel(4);
        walletBillClientAction.addBill("999",billAddRequestDto);
        //红包池减少金额，增加记录
        UpadteRedpacketPoolRequestDto upDto = new UpadteRedpacketPoolRequestDto();
        upDto.setWay(RedpacketPoolRecordWayEnum.EXPEND.getCode());
        upDto.setSourceId(userId);
        upDto.setAmount(amount);
        upDto.setSource(RedpacketPoolRecordSourceEnum.REDPACKET_SEND.getCode());
        redpacketPoolAction.upadteRedpacketPool(upDto);
    }

    public boolean send() throws Exception{
        BigDecimal allAmount = redpacketSendAction.getLastDayAmount();
        int allNum = redpacketSendAction.getQualificationNum();
        if (allAmount == null) {
            throw new Exception("昨日红包金额为零");
        }
        LuckyMoneyResponseDto dto=this.getNextRedpacket();
        if (dto==null){
            throw  new Exception("红包已发放完！");
        }
        RedpacketSend r=redpacketSendAction.getTodayLastRedpacket();
        BigDecimal lastSurplusAmount=r==null?new BigDecimal(0):new BigDecimal(Money.getMoneyString(r.getSurplusAmount()==null?0:r.getSurplusAmount()));
        //分好红包加入红包发放表
        BigDecimal amount = dto.getSendCount().divide(new BigDecimal(100)).multiply(allAmount).add(lastSurplusAmount);
        BigDecimal num0 = dto.getSendPeople().divide(new BigDecimal(100)).multiply(new BigDecimal(allNum));
        int num = (int) Math.floor(num0.doubleValue());
        logger.info(num + "======" + allNum);
        RedpacketSend redpacketSend = new RedpacketSend();
        redpacketSend.setAmount(new Money(amount).getCent());
        redpacketSend.setNum(allNum);
        redpacketSend.setRedpacketNum(num);
        redpacketSend.setSendTime(dto.getSendTime());
        redpacketSend.setSurplusAmount(new Money(amount).getCent());
        redpacketSendService.insert(redpacketSend);
        long timeStamp = DateTimestampUtil.getTimestampByHM(String.valueOf(dto.getSendTime()));
        //推送给所有用户,
        try {
            Boolean b = jobFuseAction.addJob(JobEnum.PUSH_RED_PACKET_INFORMATION_JOB,redpacketSend.getId(),redpacketSend.getId(),"网值发红包啦，赶快来抢啊！",DateTimestampUtil.getDateByTimestamp(timeStamp),AuthorEmailEnum.ZI_AN);
            logger.warn("红包信息推送给所用用户定时任务"+(b?"成功":"失败"));
            //TODO 定时任务
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        // 定时器,若下个红包发放时间半小时后红包未发放完则生成最终名单并把余额加入下个红包
        timeStamp += 60 * 30 * 1000;
        Date date = DateTimestampUtil.getDateByTimestamp(timeStamp);
        try {
            Boolean flag = jobFuseAction.addJob(JobEnum.CHECK_IS_SEND_FINISH_JOB,redpacketSend.getId(),redpacketSend.getId(), "红包发放："+redpacketSend.getId(),date, AuthorEmailEnum.ZI_AN);
            logger.warn("下个红包发放时间到而此红包未发放完则把余额加入下个红包定时任务"+(flag?"成功":"失败"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }

    public void PushRedPacketInformation(){
        messagePushAction.buildPushObjectForAll("网值发红包啦，赶快来抢啊！");
    }

    /**
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal getRedpacketAmount(String redpacketSendId,String userId) throws Exception{
        RedpacketSend redpacketSend=redpacketSendService.selectById(redpacketSendId);
        //分配的红包
        BigDecimal amount;
        if (redpacketSendAction.judgeWhetherMaxOrderUser(userId)){
            amount=new BigDecimal(Money.getMoneyString(redpacketSend.getSurplusAmount()==null?0:redpacketSend.getSurplusAmount())).multiply(new BigDecimal(0.15));
        }else if(redpacketSendAction.judgeWhetherEarlyOrderUser(userId)){
            amount=new BigDecimal(Money.getMoneyString(redpacketSend.getSurplusAmount()==null?0:redpacketSend.getSurplusAmount())).multiply(new BigDecimal(0.1));
        }else {
            amount=redpacketSendAction.getRedpacketAmount(new BigDecimal(Money.getMoneyString(redpacketSend.getAmount()==null?0:redpacketSend.getAmount())),new BigDecimal(Money.getMoneyString(redpacketSend.getSurplusAmount()==null?0:redpacketSend.getSurplusAmount())));
        }
        //用户累计消费总额
        BigDecimal  allConsumption=getAllConsumptionAndCurrencyConsumption(userId).get(0);
        //网币消费
        BigDecimal currencyConsumption=getAllConsumptionAndCurrencyConsumption(userId).get(1);
        //用户累计抢红包金额
        BigDecimal userRobRedpacketAmount= redpacketSendAction.getUserRobRedpacketAmount(userId);
        //用户累计消费总额减去用户累计抢红包金额
        BigDecimal amountDifference=allConsumption.subtract(userRobRedpacketAmount);
        logger.info(amountDifference+"*************amountDifference");
        //红包上限
        BigDecimal redpacketUpperLimitAmount=(currencyConsumption.divide(allConsumption ,2, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(0.1))).multiply(amountDifference).divide(new BigDecimal(1000));
        BigDecimal theoryAmount=new BigDecimal(0);
        if (amount.compareTo(redpacketUpperLimitAmount)!=1){
            theoryAmount=amount;
        }else{
            theoryAmount=redpacketUpperLimitAmount;
        }
        BigDecimal finalAmount;
        if (theoryAmount.compareTo(amountDifference)!=1){
            finalAmount=theoryAmount;
        }else{
            finalAmount=amountDifference;
        }
        return finalAmount;
    }

    public GetRedpacketInfoResponseDto getRedpacketInfo(String userId){
        //获取今日开始时间
        Long start = DateTimestampUtil.getStartOrEndOfTimestamp(new Date().getTime(), 1);
        GetRedpacketInfoResponseDto result = new GetRedpacketInfoResponseDto();
        List<RedpacketSend> list = redpacketSendService.getRedpacketSendList(new Date(start));
        if (list != null && list.size()>0){
            RedpacketSend fristRedPacket = list.remove(0);
            BeanUtils.copyProperties(fristRedPacket,result);
            result.setAmount(new BigDecimal(Money.getMoneyString(fristRedPacket.getAmount())));
            //上个红包
            RedpacketSend redpacketSend=redpacketSendService.getRedpacketSend(new Date(fristRedPacket.getCreateTime().getTime()),"<");
            String lastRedpacketSendId=null;
            if (redpacketSend!=null){
                lastRedpacketSendId=redpacketSend.getId();
            }
            //已抢红包总金额
            BigDecimal robAmount=new BigDecimal(0);
            int allNum=this.getRedpacketAllNum();
            for(RedpacketSend sellerRedpacketSend:list){
                robAmount = robAmount.add(new BigDecimal(Money.getMoneyString(sellerRedpacketSend.getAmount()==null?0:sellerRedpacketSend.getAmount())).subtract(new BigDecimal(Money.getMoneyString(sellerRedpacketSend.getSurplusAmount()==null?0:sellerRedpacketSend.getSurplusAmount()))));
            }
            if (this.getNextRedpacket()==null&&redpacketSendAction.judgeIsRobFinish(fristRedPacket.getId())){
                //今日红包发放完
                GetRedpacketInfoResponseDto res=new GetRedpacketInfoResponseDto();
                res.setLastRedpacketSendId(fristRedPacket.getId());
                res.setAllAmount(redpacketSendAction.getLastDayAmount());
                res.setRobAmount(robAmount.add(new BigDecimal(Money.getMoneyString(fristRedPacket.getAmount()==null?0:fristRedPacket.getAmount())).subtract(new BigDecimal(Money.getMoneyString(fristRedPacket.getSurplusAmount()==null?0:fristRedPacket.getSurplusAmount())))));
                res.setRedpacketAllNum(allNum);
                return res;
            }
            result.setRobAmount(robAmount);
            result.setAllAmount(redpacketSendAction.getLastDayAmount());
            if(fristRedPacket.getSendTime() != null) {
                Long sendTime = fristRedPacket.getSendTime().getTime();
                result.setSendTime(sendTime);
                result.setSendMoneyTime(60*30*1000+sendTime);
                if (sendTime > System.currentTimeMillis()){
                    result.setRobFinishStatus(0);
                }else {
                    if (result.getRobFinishStatus() == null){
                        result.setRobFinishStatus(2);
                    }else {
                        if (result.getRobFinishStatus() !=1 ){
                            result.setRobFinishStatus(2);
                        }
                    }
                }
            }
            result.setRedpacketNO(redpacketSendAction.getRedpacketNO());
            result.setRedpacketAllNum(allNum);
            result.setLastRedpacketSendId(lastRedpacketSendId);
            //用户抢红包状态 1未抢 2抢到了 3.用户没抢到红包
            RedpacketRecord redpacketRecord=redpacketSendAction.judgeRobRedpacket(userId,fristRedPacket.getId());
            if (redpacketRecord==null){
                result.setUserRobStatus(1);
            }else {
                if (new BigDecimal(Money.getMoneyString(redpacketRecord.getAmount()==null?0:redpacketRecord.getAmount())).compareTo(new BigDecimal(0))==1) {
                    result.setUserRobStatus(2);
                    result.setUserRobAmount(new BigDecimal(Money.getMoneyString(redpacketRecord.getAmount()==null?0:redpacketRecord.getAmount())));
                }else {
                    result.setUserRobStatus(3);
                }
            }
            //红包状态：0.还未能抢红包 1.红包未抢完 2.红包时间已过
            if (!redpacketSendAction.judgeIsRobFinish(fristRedPacket.getId())){
                result.setRobFinishStatus(1);
            }
        }
        return result;
    }

    /**
     * 检查是否发放完红包，若否则发放下个红包，用于定时器
     * @param
     * @return
     */

    public boolean checkIsSendFinish(String id) throws Exception{
        boolean res=true;
        RedpacketSend redpacketSend=redpacketSendService.selectById(id);
        if (redpacketSend!=null) {
            if (redpacketSend.getRedpacketNum() != 0) {
                if (this.getNextRedpacket() != null) {
                    this.send();
                }
            }
        }
        return res;
    }

}
