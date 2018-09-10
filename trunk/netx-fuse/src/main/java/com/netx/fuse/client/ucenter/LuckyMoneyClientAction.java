package com.netx.fuse.client.ucenter;

import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.LuckyMoneyResponseDto;
import com.netx.common.vo.common.LuckMoneyQueryDto;
import com.netx.ucenter.biz.common.LuckyMoneyAction;
import com.netx.ucenter.model.common.CommonLuckyMoney;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LuckyMoneyClientAction {

    private Logger logger = LoggerFactory.getLogger(LuckyMoneyClientAction.class);

    @Autowired
    LuckyMoneyAction luckyMoneyAction;

    public List<LuckyMoneyResponseDto> query(LuckMoneyQueryDto request){
        try {
            List<CommonLuckyMoney> list = luckyMoneyAction.query(request);
            if(list!=null || list.size()>0){
                return VoPoConverter.copyList(list,LuckyMoneyResponseDto.class);
            }
        } catch (Exception e) {
            logger.error("获取红包设置出错", e);
        }
        return new ArrayList<>();
    }

    public Boolean updateLuckMoneySet(){
        try{
            boolean res=luckyMoneyAction.updateLuckMoneySet();
            if (res) {
                return true;
            }else{
                logger.warn("500","更新失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return false;
    }
}
