package com.netx.fuse.proxy;

import com.netx.common.vo.business.*;
import com.netx.fuse.client.shoppingmall.OrderClientAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderProxy {

    @Autowired
    OrderClientAction orderClient;

//    public GetSellerGoodsQuantityResponseVo getSellerGoodsQuantity(CommonUserIdRequestDto request){
//        Result result=orderClient.getlastMonthOdersAmount(request);
//        if(result.getCode()==0){
//            System.out.println("跨域成功----------------------");
//            JSONObject jsonObject= JSON.parseObject(JSONObject.toJSONString(result.getObject()));
//            GetSellerGoodsQuantityResponseVo responseVo=JSONObject.parseObject(jsonObject.toJSONString(),GetSellerGoodsQuantityResponseVo.class);
//            return responseVo;
//        }
//        System.out.println("获取失败了-------------------");
//        return null;
//    }

    public GetlastMonthOdersAmountResponseVo getlastMonthOrdersAmount(String userId){
        CommonUserIdRequestDto requestDto=new CommonUserIdRequestDto();
        requestDto.setUserId(userId);
        GetlastMonthOdersAmountResponseVo result=orderClient.getlastMonthOdersAmount(requestDto.getUserId());
        if(result != null){
            return result;
        }
        return null;
    }

    public GetEveryMonthOrderAmountResponseDto getEveryMonthOdersAmount(String userId, Date start){
        GetEveryMonthOrderAmountRequestDto requestDto =new GetEveryMonthOrderAmountRequestDto();
        requestDto.setUserId(userId);
        requestDto.setStart(start);
        GetEveryMonthOrderAmountResponseDto result=orderClient.getEveryMonthOdersAmount(requestDto);
        System.out.println(result);
        if(result != null){
            return result;
        }
        return null;
    }

}
