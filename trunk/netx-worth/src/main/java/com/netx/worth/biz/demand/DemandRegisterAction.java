package com.netx.worth.biz.demand;

import java.util.Date;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.wz.dto.demand.DemandRegisterDto;
import com.netx.utils.money.Money;
import com.netx.worth.enums.DemandOrderStatus;
import com.netx.worth.enums.DemandRegisterStatus;
import com.netx.worth.enums.DemandStatus;
import com.netx.worth.model.Demand;
import com.netx.worth.model.DemandOrder;
import com.netx.worth.service.WorthServiceprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.netx.worth.model.DemandRegister;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 需求报名表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-26
 */
@Service
public class DemandRegisterAction{

    @Autowired
    WorthServiceprovider worthServiceprovider;

    @Autowired
    DemandOrderAction demandOrderAction;

    @Transactional(rollbackFor = Exception.class)
    public String acceptRegister(String id,String userId){
        DemandRegister demandRegister = worthServiceprovider.getDemandRegisterService().selectById(id);
        if(demandRegister==null){
            return "申请单已不存在";
        }
        if(demandRegister.getStatus().equals(DemandRegisterStatus.SUCCESS.status)){
            return "该申请已接受" ;
        }
        Demand demand = worthServiceprovider.getDemandService().selectById(demandRegister.getDemandId());
        if(demand==null){
            return "该需求可能已取消";
        }
        if(!userId.equals(demand.getUserId())){
            return "你非发布者，不能接受申请";
        }
        int count = worthServiceprovider.getDemandOrderService ().getDemandOrderCountByDemandIdAndUserId ( demandRegister.getDemandId (),demandRegister.getUserId ());
        if (count != 0) {
            return  "该申请已接受" ;
        }
        //验证需求人数
        count = worthServiceprovider.getDemandOrderService ().getDemandOrderCountByDemandId (
                demandRegister.getDemandId (),
                DemandOrderStatus.ACCEPT.status,
                DemandOrderStatus.CONFIRM.status,
                DemandOrderStatus.START.status,
                DemandOrderStatus.REFUNDMENT.status
        );
        if (demand.getAmount ()<=count) {
            return   "需求入选人数已满，不能再接受申请" ;
        }
        DemandOrder demandOrder = new DemandOrder();
        if(demandOrderAction.addDemandOrder(demandOrder,demand.getId(),demandRegister.getUserId(),userId)){
            demandRegister.setStatus(DemandRegisterStatus.SUCCESS.status);
            demandRegister.setUpdateUserId(userId);
            demandRegister.setDemandOrderId(demandOrder.getId());
            if(worthServiceprovider.getDemandRegisterService().updateById(demandRegister)){
                if(count+1==demand.getAmount ()){
                    count = count+1;
                    demand.setStatus(DemandStatus.START.status);
                    worthServiceprovider.getDemandService().updateById(demand);
                }
                //返回count的字符串类型,方便判断以及发送推送
                return count+",success";
            }
        }
        return "接受申请失败";
    }

    /**吧DemandPublishDto赋给Demand对象**/
    public DemandRegister DemandRegisterDtoToDemandRegister(DemandRegisterDto demandRegisterDto) {
        DemandRegister demandRegister = new DemandRegister ();
        demandRegister.setDemandId ( demandRegisterDto.getDemandId () );
        demandRegister.setUserId ( demandRegisterDto.getUserId () );
        if(demandRegisterDto.getStartAt()!=null){
            demandRegister.setStartAt ( new Date ( demandRegisterDto.getStartAt() ) );
        }
        if(demandRegisterDto.getEndAt ()!=null){
            demandRegister.setEndAt ( new Date ( demandRegisterDto.getEndAt () ) );
        }
        demandRegister.setUnit ( demandRegisterDto.getUnit () );
        demandRegister.setAbout ( demandRegisterDto.getAbout () );
        demandRegister.setDescription ( demandRegisterDto.getDescription () );
        if(demandRegisterDto.getWage ()!=null){
            demandRegister.setWage (new Money (demandRegisterDto.getWage ()).getCent ());
        }
        demandRegister.setAddress ( demandRegisterDto.getAddress () );
        demandRegister.setLat ( demandRegisterDto.getLat () );
        demandRegister.setLon ( demandRegisterDto.getLon () );
        demandRegister.setAnonymity ( demandRegisterDto.getAnonymity () );
        demandRegister.setId ( demandRegisterDto.getId () );
        return demandRegister;
    }

}
