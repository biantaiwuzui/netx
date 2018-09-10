package com.netx.shopping.biz.merchantcenter;


import com.netx.common.express.AliyunExpressService;
import com.netx.shopping.biz.BaseAction;
import com.netx.shopping.model.merchantcenter.MerchantExpress;
import com.netx.shopping.service.merchantcenter.MerchantExpressService;
import com.netx.shopping.vo.ExpressResponse;
import com.netx.shopping.vo.QueryExpressRequestDto;
import com.netx.shopping.vo.UpdateExpressRequestDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 网商-快递公司名单表 服务action
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class MerchantExpressAction extends BaseAction{

    private Logger logger = LoggerFactory.getLogger(MerchantExpressAction.class);

    @Autowired
    private AliyunExpressService aliyunExpressService;

    @Autowired
    MerchantExpressService expressService;

    public MerchantExpressService getExpressService() {
        return expressService;
    }

    public List<ExpressResponse> getList(){
        List<MerchantExpress> list = expressService.getExpressList();
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

    /**
     *
     */
    public void add(){
        List<MerchantExpress> list = aliyunExpressService.getExpressBusiness(MerchantExpress.class);
        list.forEach(express -> {
            try{
                MerchantExpress temp = selectByName(express.getName());
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

    /**
     * 根据快递名查询快递公司
     * @param name
     * @return
     */
    private MerchantExpress selectByName(String name){
        return expressService.selectByName(name);
    }

    /**
     * 分页查询快递公司-boss
     * @param request
     * @return
     */
    public List<MerchantExpress> queryMerchantExpressList(QueryExpressRequestDto request){
        if(StringUtils.isNotBlank(request.getName())){
            request.setName(searchProcessing(request.getName()));
        }
        return expressService.queryMerchantExpressList(request);
    }

    /**
     * 修改快递热门度-boss
     * @param id
     * @param hot
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateHot(String id, Integer hot){
        MerchantExpress merchantExpress = expressService.selectById(id);
        if(merchantExpress != null){
            merchantExpress.setHot(hot);
            return expressService.updateById(merchantExpress);
        }
        return false;
    }

    /**
     * 批量修改快递热门度-boss
     * @param requestDtos
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateHots(List<UpdateExpressRequestDto> requestDtos){
        for(UpdateExpressRequestDto requestDto:requestDtos){
            if(!updateHot(requestDto.getId(), requestDto.getHot())){
                return false;
            }
        }
        return true;
    }

    /**
     * 禁用/解除禁用-boss
     * 快递公司
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean disable(String id){
        MerchantExpress merchantExpress = expressService.selectById(id);
        if(merchantExpress != null){
            if(merchantExpress.getDeleted() == 0){
                merchantExpress.setDeleted(1);
            }else {
                merchantExpress.setDeleted(0);
            }
            return expressService.updateById(merchantExpress);
        }
        return false;
    }
}
