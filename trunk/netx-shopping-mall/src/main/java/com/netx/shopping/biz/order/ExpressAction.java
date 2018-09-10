package com.netx.shopping.biz.order;

import com.netx.common.express.AliyunExpressService;

import com.netx.shopping.model.order.SellerExpress;
import com.netx.shopping.service.order.ExpressService;
import com.netx.shopping.vo.ExpressResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-01-30
 */
@Service
public class ExpressAction {
    @Autowired
    AliyunExpressService aliyunExpressService;

    @Autowired
    ExpressService expressService;

    private Logger logger = LoggerFactory.getLogger(ExpressAction.class);

    public List<ExpressResponse> getList(){
        List<SellerExpress> list = expressService.getExpressList();
        List<ExpressResponse> result = new ArrayList<>();
        list.forEach(express -> {
            result.add(createExpressResponse(express.getName(),express.getType()));
        });
        return result;
    }

    private ExpressResponse createExpressResponse(String name,String type){
        ExpressResponse expressResponse = new ExpressResponse();
        expressResponse.setName(name);
        expressResponse.setType(type);
        return expressResponse;
    }

    public void add() throws Exception{
        List<SellerExpress> list = aliyunExpressService.getExpressBusiness(SellerExpress.class);
        list.forEach(express -> {
            try{
                SellerExpress temp = selectByName(express.getName());
                if(temp==null){
                    expressService.insert(express);
                }else{
                    express.setId(temp.getId());
                    express.setCreateTime(temp.getCreateTime());
                    expressService.updateById(express);
                }
            }catch(Exception e){
                logger.error(express.getName()+"插入或更新失败，error："+e.getMessage(),e);
            }
        });
    }

    private SellerExpress selectByName(String name){
        return expressService.selectByName(name);
    }
}
