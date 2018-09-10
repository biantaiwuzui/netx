package com.netx.shopping.biz.product;

import com.netx.common.vo.business.AddKindRequestDto;
import com.netx.shopping.model.product.SellerKind;
import com.netx.shopping.service.product.KindService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 网商-商品分类表 服务实现类
 * </p>
 *
 * @author wj.liu
 * @since 2017-08-29
 */
@Service("kindAction")
@Transactional(rollbackFor = Exception.class)
public class KindAction{

    @Autowired
    KindService kindService;

    private Logger logger = LoggerFactory.getLogger(KindAction.class);

    
    public String getKindName(String kindId){
        try {
            if (kindId != null) {
                SellerKind kind = kindService.selectById(kindId);
                return kind == null ? "" : kind.getName();
            }
        } catch(Exception e){
            logger.error(e.getMessage(),e);
        }
        return "";
    }

    
    public SellerKind addOrUpdate(AddKindRequestDto request){
        SellerKind kind= new SellerKind();
        BeanUtils.copyProperties(request,kind);
        if(StringUtils.isNotBlank(request.getId())){
            kind.setUpdateUserId(request.getUserId());
        }else{
            kind.setDeleted(0);
            kind.setCreateUserId(request.getUserId());
        }
        Boolean res=kindService.insertOrUpdate(kind);
        if (res==false)
        {
            return null;
        }
        return kind;
    }

    
    public List<SellerKind> list(String userId){
        return kindService.list(userId);
    }

    
    public boolean delete(String id){
        return kindService.delete(id);
    }

}
