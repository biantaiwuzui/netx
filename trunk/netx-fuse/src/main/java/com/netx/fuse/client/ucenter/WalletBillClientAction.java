package com.netx.fuse.client.ucenter;

import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.common.vo.common.BillListResponseDto;
import com.netx.common.vo.common.BillQueryRequestDto;
import com.netx.common.vo.common.SearchMonthBillCountRequestDto;
import com.netx.fuse.biz.ucenter.WallerFrozenFuseAction;
import com.netx.ucenter.biz.common.BillAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletBillClientAction {

    private Logger logger = LoggerFactory.getLogger(WalletBillClientAction.class);

    @Autowired
    BillAction billAction;

    @Autowired
    WallerFrozenFuseAction wallerFrozenFuseAction;


    public BillAction getBillAction() {
        return billAction;
    }

    /**
     *
     * @param userId   付款人用户id
     * @param requestDto
     * @return
     */
    public Boolean addBill(String userId, BillAddRequestDto requestDto){
        try {
            wallerFrozenFuseAction.add(userId,requestDto);
            return true;
        } catch (Exception e) {
            logger.error("添加交易流水失败:"+e.getMessage(), e);
        }
        return false;
    }

    public List<BigDecimal> searchMonthBillCount(SearchMonthBillCountRequestDto requestDto) {
        try {
            List<BigDecimal> recommendCounts = billAction.searchMonthBillCounts(requestDto.getRecommendUserIds());
            if (recommendCounts != null && recommendCounts.size() > 0) {
                return recommendCounts;
            }
            throw new RuntimeException("查询保荐人每个用户的交易额返回为空");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public List<BillListResponseDto> queryList(String userId,BillQueryRequestDto request) {
        try {
            return billAction.getPages(userId,request);
        } catch (Exception e) {
            logger.error("查询交易流水异常", e);
        }
        return null;
    }
}
