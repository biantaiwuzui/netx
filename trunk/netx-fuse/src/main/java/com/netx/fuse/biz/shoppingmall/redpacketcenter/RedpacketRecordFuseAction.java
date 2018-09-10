package com.netx.fuse.biz.shoppingmall.redpacketcenter;

import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.vo.business.RedpacketRecordDto;
import com.netx.common.vo.business.RedpacketRecordResponseDto;
import com.netx.common.vo.business.SeeRedpacketRecordDto;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.shopping.biz.redpacketcenter.RedpacketRecordAction;
import com.netx.shopping.biz.redpacketcenter.RedpacketSendAction;
import com.netx.shopping.model.redpacketcenter.RedpacketRecord;
import com.netx.shopping.model.redpacketcenter.RedpacketSend;
import com.netx.shopping.service.redpacketcenter.RedpacketRecordService;
import com.netx.shopping.service.redpacketcenter.RedpacketSendService;
import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RedpacketRecordFuseAction {

    @Autowired
    RedpacketSendService redpacketSendService;
    @Autowired
    RedpacketRecordService redpacketRecordService;
    @Autowired
    UserClientProxy userClientProxy;
    @Autowired
    RedpacketRecordAction redpacketRecordAction;
    @Autowired
    RedpacketSendAction redpacketSendAction;
    @Autowired
    RedpacketSendFuseAction redpacketSendFuseAction;

    public RedpacketRecordResponseDto see(SeeRedpacketRecordDto dto) throws Exception{
        RedpacketSend redpacketSend= redpacketSendService.selectById(dto.getId());
        if (redpacketSend==null){
            throw new Exception("红包不存在");
        }
        RedpacketRecordResponseDto result=new RedpacketRecordResponseDto();
        BeanUtils.copyProperties(redpacketSend,result);
        result.setAmount(new BigDecimal(Money.getMoneyString(redpacketSend.getAmount())));
        List<RedpacketRecord> redpacketRecordList = redpacketRecordService.getRedpacketRecordList(dto.getId());
        List<RedpacketRecordDto> RedpacketRecordDtoList = new ArrayList<>();
        BigDecimal max=new BigDecimal(0);//最大红包金额
        BigDecimal min=new BigDecimal(0);//最小红包金额
        if (redpacketRecordList.size() > 0) {
            for (RedpacketRecord redpacketRecord : redpacketRecordList) {
                if (!StringUtils.isEmpty(redpacketRecord.getUserId())) {
                    RedpacketRecordDto redpacketRecordDto = new RedpacketRecordDto();
                    BeanUtils.copyProperties(redpacketRecord, redpacketRecordDto);
                    redpacketRecordDto.setAmount(new BigDecimal(Money.getMoneyString(redpacketRecord.getAmount())));
                    //获取用户信息
                    List<String> userIds=new ArrayList<>();
                    userIds.add(redpacketRecord.getUserId());
                    Map<String,UserSynopsisData> userSynopsisDataMap=userClientProxy.selectUserMapByIds(userIds);
                    UserSynopsisData userSynopsisData= userSynopsisDataMap.get(redpacketRecord.getUserId());
                    if (userSynopsisData!=null){
                        redpacketRecordDto = redpacketRecordAction.setUserInfo(redpacketRecordDto, userSynopsisData);
                    }
                    RedpacketRecordDtoList.add(redpacketRecordDto);
                }
            }
            max=new BigDecimal(Money.getMoneyString(redpacketRecordList.get(0).getAmount()));
            min=new BigDecimal(Money.getMoneyString(redpacketRecordList.get(redpacketRecordList.size()-1).getAmount()));
        }
        result.setAllAmount(redpacketSendAction.getLastDayAmount());
        Long start = DateTimestampUtil.getStartOrEndOfTimestamp(new Date().getTime(), 1) ;
        int allNum= redpacketSendFuseAction.getRedpacketAllNum();
        if (start>redpacketSend.getCreateTime().getTime()){
            result.setRedpacketNO(allNum);
        }else{
            result.setRedpacketNO(redpacketRecordService.getRedpacketRecordList(new Date(start),new Date(redpacketSend.getCreateTime().getTime())).size());
        }
        result.setRedpacketAllNum(allNum);
        result.setMaxAmount(max);
        result.setMinAmount(min);
        RedpacketRecord redpacketRecord=redpacketRecordService.getRedpacketRecord(dto.getUserId(),dto.getId());
        //用户抢红包情况
        if(!redpacketSendAction.judgeReceive(dto.getUserId())){
            result.setRobSituation("您还不能抢红包哦！");
            result.setAmount(new BigDecimal(0));
        }else if (redpacketSendAction.judgeRobRedpacket(dto.getUserId(),dto.getId())==null){
            result.setRobSituation("您没有抢过这个红包哦！");
            result.setAmount(new BigDecimal(0));
        } else if(new BigDecimal(Money.getMoneyString(redpacketRecord.getAmount())).compareTo(new BigDecimal(0))==0){
            result.setRobSituation("您没有抢到哦！加油！");
            result.setAmount(new BigDecimal(0));
        }else{
            result.setRobSituation("恭喜您抢到"+Money.getMoneyString(redpacketRecord.getAmount())+"元!");
            result.setAmount(new BigDecimal(Money.getMoneyString(redpacketRecord.getAmount())));
        }
        result.setRedpacketNum(redpacketRecordService.getRedpacketRecordList(dto.getId()).size());
        result.setUserRedpacketRecord(RedpacketRecordDtoList);
        return result;
    }


    public List<RedpacketRecordDto> seeRedpacketToUser(String userId){
        //获取用户信息
        List<String> userIds=new ArrayList<>();
        userIds.add(userId);
        Map<String, UserSynopsisData> userSynopsisDataMap = userClientProxy.selectUserMapByIds(userIds);
        UserSynopsisData userSynopsisData = userSynopsisDataMap.get(userId);
        List<RedpacketRecord> redpacketRecords = redpacketRecordService.getSellerRedpacketRecordByUserIdAndAmount(userId);
        List<RedpacketRecordDto> res=new ArrayList<>();
        for (RedpacketRecord redpacketRecord : redpacketRecords) {
            RedpacketRecordDto redpacketRecordDto = new RedpacketRecordDto();
            redpacketRecordDto.setAmount(new BigDecimal(Money.getMoneyString(redpacketRecord.getAmount())));
            redpacketRecordDto.setCreateTime(redpacketRecord.getCreateTime());
            if (userSynopsisData != null){
                redpacketRecordDto = redpacketRecordAction.setUserInfo(redpacketRecordDto,userSynopsisData);
            }
            res.add(redpacketRecordDto);
        }
        return res;
    }

}
