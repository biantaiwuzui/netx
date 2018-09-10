package com.netx.fuse.biz.ucenter;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.vo.common.BusinessEvaluateQueryRequestDto;
import com.netx.common.vo.common.BusinessEvaluateQueryRequestDto;
import com.netx.fuse.client.shoppingmall.GoodsClientAction;
import com.netx.ucenter.biz.common.EvaluateAction;
import com.netx.ucenter.model.common.CommonEvaluate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluateFuseAction {
    @Autowired
    EvaluateAction evaluateAction;

    @Autowired
    GoodsClientAction goodsClientAction;
    public Map<String,Object> businessQueryPage(BusinessEvaluateQueryRequestDto request) throws Exception {
        List<String> puductIds = goodsClientAction.getGoodsBySellerId(request.getBusinessId());
        if(puductIds!=null && puductIds.size()!=0){
            return evaluateAction.businessQueryPage(request,puductIds);
        }
        return new HashMap<>();
    }
}
