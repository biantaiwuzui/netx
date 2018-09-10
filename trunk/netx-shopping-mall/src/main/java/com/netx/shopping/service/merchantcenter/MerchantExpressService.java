package com.netx.shopping.service.merchantcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.shopping.model.merchantcenter.MerchantExpress;
import com.netx.shopping.mapper.merchantcenter.MerchantExpressMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.vo.QueryExpressRequestDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 网商-快递公司名单表 服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class MerchantExpressService extends ServiceImpl<MerchantExpressMapper, MerchantExpress>{
    /**
     * 根据代号获取物流公司详情
     * @param number
     * @return
     */
    public MerchantExpress getExpress(String number){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.where("number={0}", number);
        return this.selectOne(wrapper);
    }

    /**
     * 根据拼音获取物流公司详情
     * @param type
     * @return
     */
    public MerchantExpress getExpressByType(String type){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.where("type={0}", type);
        return this.selectOne(wrapper);
    }

    public List<MerchantExpress> getExpressList(){
        Wrapper<MerchantExpress> wrapper = new EntityWrapper<>();
        wrapper.orderBy("hot desc,type");
        return this.selectList(wrapper);
    }

    public MerchantExpress selectByName(String name){
        Wrapper<MerchantExpress> wrapper = new EntityWrapper<>();
        wrapper.eq("name",name);
        return this.selectOne(wrapper);
    }

    /**
     * 根据代号获取快递名
     * @param number
     * @return
     */
    public String getNameByNumber(String number){
        Wrapper<MerchantExpress> wrapper = new EntityWrapper<>();
        wrapper.eq("number",number);
        wrapper.setSqlSelect("name");
        return (String) this.selectObj(wrapper);
    }

    public String getNameByType(String type){
        Wrapper<MerchantExpress> wrapper = new EntityWrapper<>();
        wrapper.eq("type",type);
        wrapper.setSqlSelect("name");
        return (String) this.selectObj(wrapper);
    }

    public String getTypeByCode(String number){
        Wrapper<MerchantExpress> wrapper = new EntityWrapper<>();
        wrapper.eq("number",number);
        wrapper.setSqlSelect("type");
        return (String) this.selectObj(wrapper);
    }

    /**
     * 分页查询快递公司
     * @param request
     * @return
     */
    public List<MerchantExpress> queryMerchantExpressList(QueryExpressRequestDto request){
        Page page = new Page();
        page.setSize(request.getSize());
        page.setCurrent(request.getCurrent());
        Wrapper<MerchantExpress> wrapper = new EntityWrapper<>();
        wrapper.where("deleted = 0");
        if(StringUtils.isNotBlank(request.getLetter())){
            wrapper.and("letter = {0}", request.getLetter());
        }
        if(StringUtils.isNotBlank(request.getNumber())){
            wrapper.and("number = {0}", request.getNumber());
        }
        if(StringUtils.isNotBlank(request.getName())){
            wrapper.like("name", request.getName());
        }
        wrapper.orderBy("hot", false);
        return selectPage(page, wrapper).getRecords();
    }
}
