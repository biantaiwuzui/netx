package com.netx.shopping.biz.business;

import com.netx.common.common.utils.GenerateQrcodeUtil;
import com.netx.common.redis.util.RedisUtils;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.vo.business.*;
import com.netx.shopping.biz.marketing.RecordingHistoryAction;
import com.netx.shopping.biz.marketing.RelationshipAction;
import com.netx.shopping.enums.PromotionAwardEnum;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.marketing.SellerArea;
import com.netx.shopping.service.business.SellerService;
import com.netx.shopping.service.marketing.RelationshipService;
import com.netx.shopping.service.marketing.SellerAreaService;
import com.netx.shopping.vo.SellerAgentDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 网商-商家表 服务实现类
 * </p>
 * @author wj.liu
 * @since 2017-08-29
 */
@Service("SellerRegisterAction")
@Transactional(rollbackFor = Exception.class)
public class SellerRegisterAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    SellerService sellerService;

    @Autowired
    RelationshipAction relationshipAction;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RecordingHistoryAction recordingHistoryAction;

    @Autowired
    SellerAreaService sellerAreaService;

    @Autowired
    RelationshipService relationshipService;

    public boolean isCanRegisterSeller(RegisterSellerRequestDto request){
        if (StringUtils.isNotEmpty(request.getId())) {
            return true;
        }
        if (sellerService.isCanRegisterSeller(request) != null) {
            return false;
        }
        return true;
    }

    public void addBusinessNum(String id, String city) {
        redisUtils.increateNum("China", id);
        redisUtils.increateNum(city, id);
    }

    public String getCity(String city, String provinceCode) {
        if (city == null || city.isEmpty()) {
            return provinceCode;
        }
        return provinceCode + "-" + city;
    }

    public Integer getJobNum(PromotionAwardEnum awardEnum, String city) {
        return sellerService.getJobNum(awardEnum,city);
    }

    public Seller getSellerByPid(String pid){
        String id = relationshipAction.getRelationPSellerId(pid);
        if (id != null && !id.isEmpty()) {
            return sellerService.selectById(id);
        }
        return null;
    }

    public Integer getGroupNum(Integer groupNo) {
        Integer d = groupNo / 30;
        return (double) groupNo / 30 != d ? d + 1 : d;
    }

    public Boolean checkUpGroup(Integer num) {
        return num > 30 && num % 30 == 1;
    }


    /**
     * 生成与验证八位数的客服代码
     *
     * @return
     */
    public String buildCustomerCode() {
        String customerCode = "";
        do {
            customerCode = createCustomerCode();
        } while (checkCustomerCode(customerCode) != null);//进行客服代码校检
        return customerCode;
    }

    /**
     * 生成一个八位数的客服代码
     *
     * @return
     */
    private String createCustomerCode() {
        StringBuilder str = new StringBuilder();//定义变长字符串
        Random random = new Random();
        //随机生成数字，并添加到字符串
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    /**
     * 客服代码唯一验证
     *
     * @param customerCode
     * @return
     */
    public Seller checkCustomerCode(String customerCode) {
        return sellerService.checkCustomerCode(customerCode);
    }

    public void createBusinessNum(String id, String city) {
        redisUtils.addNum("China", id, 0);
        redisUtils.addNum(city, id, 0);
    }

    public boolean generateSellerQrcode(String sellerId) {
        int width = 300;
        int height = 300;
        String format = "png";
        String suffix = "data:image/png;base64,";
        String type = "\"type\":" + "\"B\"";
        String id = "\"id\":\"";
        String text = "{" + id + sellerId + "\"," + type + "}";
        try {
            String qrcodeBase64Str = GenerateQrcodeUtil.generateQRCodeStream(text, width, height, format);
            Seller seller = new Seller();
            seller.setId(sellerId);
            seller.setSellerQrcode(suffix + qrcodeBase64Str);
            sellerService.updateById(seller);
        } catch (Exception e) {
            e.getMessage();
        }
        return true;
    }

    public Boolean checkArea(String areaName) {
        if (areaName != null && !areaName.isEmpty()) {
            SellerArea area = sellerAreaService.getArea(areaName);
            return area != null;
        }
        return false;
    }

    public Seller getSellerByCreateTime(String userId) {
        return sellerService.getSellerByCreateTime(userId);
    }

    public Seller getAreaFristBusiness(String provinceCode, String areaName) {
        return sellerService.getAreaFristBusiness(provinceCode,areaName);
    }

    /**
     * 定时任务1：发放工资及清除对应字段
     *
     *
     */
    public void operateMonthThing(){
        List<Seller> sellers = sellerService.getSellerList();
        for (Seller seller : sellers) {
            seller.setAchievementMonth(seller.getJob() == 2 ? 3000 : 5000);
            switch (seller.getJob()) {
                case 2:
                    getSalary(seller, 3000);
                    break;
                case 3:
                    getSalary(seller, 5000);
                    break;
                default:
                    seller.setAchievementMonth(0);
            }
            seller.setMonthNum(0);
            seller.setMonthSecondNum(0);
            seller.setMonthThirdNum(0);
            sellerService.updateById(seller);
        }
    }

    private void getSalary(Seller seller, Integer salary) {
        seller.setAchievementMonth(salary);
        seller.setAchievementTotal(seller.getAchievementTotal() + salary);
    }

    /**
     * 业绩总览
     *
     * @param dto
     * @return
     *
     */
    public List<GetSellerAgentResponsetDto> getSellerAgent(GetSellerAgentRequestDto dto){
        List<GetSellerAgentResponsetDto> list = new ArrayList<>();
        List<SellerAgentDto> mapList = sellerService.getMapList(dto.getUserId());
        if (mapList != null && !mapList.isEmpty()) {
            mapList.forEach(map -> {
                list.add(createGetSellerAgentResponsetDto(map));
            });
        }
        return list;
    }

    private GetSellerAgentResponsetDto createGetSellerAgentResponsetDto(SellerAgentDto dto) {
        GetSellerAgentResponsetDto res = new GetSellerAgentResponsetDto();
        res.setAllAchievement(dto.getAchievementTotal());
        res.setAreaName(getCity(dto.getCityCode(), dto.getProvinceCode()));
        res.setAreaRanking(sellerService.selectSellerAndRandNo(dto.getId(), dto.getProvinceCode(), dto.getCityCode()));//区域排名
        res.setCountryRanking(dto.getRownum());//国家排名
        res.setCustomerServiceNum(dto.getSecondNum());
        res.setMonthAchievement(dto.getAchievementMonth());//月业绩
        res.setCustomerServiceGrounp(getGroupNum(dto.getSecondNum()));
        res.setSellerNum(dto.getThirdNum());
        res.setRegisterTime(DateTimestampUtil.getDateStrByDate(dto.getCreateTime()));
        res.setNewAddCustomerServiceNum(dto.getMonthSecondNum());
        res.setNewAddSellerNum(dto.getMonthThirdNum());
        res.setSellerName(dto.getName());
        res.setSellerId(dto.getId());
        return res;
    }

    private GetSellerAgentResponsetDto createGetSellerAgentResponsetDto(Seller seller) {
        GetSellerAgentResponsetDto res = new GetSellerAgentResponsetDto();
        res.setAllAchievement(seller.getAchievementTotal());
        res.setAreaName(getCity(seller.getCityCode(), seller.getProvinceCode()));
        res.setAreaRanking(redisUtils.getNo(getCity(seller.getCityCode(), seller.getProvinceCode()), seller.getId()));//区域排名
        res.setCountryRanking(redisUtils.getNo("China", seller.getId()));//国家排名
        res.setCustomerServiceNum(seller.getSecondNum());
        res.setMonthAchievement(seller.getAchievementMonth());//月业绩
        res.setCustomerServiceGrounp(getGroupNum(seller.getSecondNum()));
        res.setSellerNum(seller.getThirdNum());
        res.setRegisterTime(DateTimestampUtil.getDateByTimestamp1(seller.getCreateTime().getTime() * 1000l));
        res.setNewAddCustomerServiceNum(seller.getMonthSecondNum());
        res.setNewAddSellerNum(seller.getMonthNum());
        res.setSellerName(seller.getName());
        res.setSellerId(seller.getId());
        return res;
    }

    /**
     * 客服代理
     *
     * @param dto
     * @return
     *
     */
    public List<GetCustomerServiceAgentResponsetDto> getCustomerServiceAgent(GetCustomerServiceAgentRequestDto dto){
        List<GetCustomerServiceAgentResponsetDto> res = new ArrayList<>();
        List<String> ids = relationshipService.getIds(dto.getSellerId());
        if (ids != null && !ids.isEmpty()) {
            List<Seller> sellers =sellerService.getSellerList(ids,"(secondNum+thirdNum) desc");
            if (sellers != null && !sellers.isEmpty()) {
                Seller seller = sellers.get(0);
                int businessNum = seller.getSecondNum() + seller.getThirdNum();
                res.add(createGetCustomerServiceAgentResponsetDto(seller, 1));
                int no = 1;
                for (int i = 1; i < sellers.size(); i++) {
                    seller = sellers.get(i);
                    if (seller.getThirdNum() + seller.getSecondNum() != businessNum) {
                        businessNum = seller.getThirdNum() + seller.getSecondNum();
                        no = i + 1;
                    }
                    res.add(createGetCustomerServiceAgentResponsetDto(seller, no));
                }
            }
        }
        return res;
    }

    private GetCustomerServiceAgentResponsetDto createGetCustomerServiceAgentResponsetDto(Seller seller, int no){
        GetCustomerServiceAgentResponsetDto dto = new GetCustomerServiceAgentResponsetDto();
        dto.setAllIndirectCommission(recordingHistoryAction.getDirect(seller.getId(), 1));//累计提成
        dto.setAreaName(getCity(seller.getCityCode(), seller.getProvinceCode()));
        dto.setAreaRanking(no);//排名
        dto.setCustomerServiceGrounp(getGroupNum(seller.getGroupNo()));
        dto.setMonthIndirectCommission(recordingHistoryAction.getDirect(seller.getId(), 2));//月提成
        dto.setNewAddSellerNum(seller.getMonthNum());
        dto.setSurplusDay(getSurplusDay(seller.getEndTime()));//剩余天数
        dto.setDirectCommission(recordingHistoryAction.getDirect(seller.getId(), 0));//提成
        dto.setAllSellerNum(seller.getSecondNum() + seller.getThirdNum());
        dto.setCustomerServicerName(seller.getName());
        dto.setRegisterTime(DateTimestampUtil.getDateByTimestamp1(seller.getCreateTime().getTime() * 1000l));
        dto.setCustomerServicerNum(seller.getSecondNum());
        dto.setSellerId(seller.getId());
        return dto;
    }

    private Long getSurplusDay(int endTime) {
        Long surplusTime = endTime * 1000l - System.currentTimeMillis();
        if (surplusTime >= 0) {
            return surplusTime / (24 * 60 * 60 * 1000);
        }
        return -1l;
    }


    public List<GetSellerResponseDto> getSellerList(GetCustomerServiceAgentRequestDto dto){
        List<GetSellerResponseDto> res = new ArrayList<>();
        List<String> ids = relationshipService.getIds(dto.getSellerId());
        if (ids != null && !ids.isEmpty()) {
            List<Seller> sellers = sellerService.getSellerList(ids);

            for (Seller seller : sellers) {
                res.add(createGetSellerResponseDto(seller));
            }
        }
        return res;
    }

    private GetSellerResponseDto createGetSellerResponseDto(Seller seller){
        GetSellerResponseDto dto = new GetSellerResponseDto();
        dto.setAreaName(getCity(seller.getCityCode(), seller.getProvinceCode()));
        dto.setCommission(recordingHistoryAction.getCommission(seller.getId()));
        dto.setCustomerServicerName(seller.getName());
        dto.setSurplusDay(getSurplusDay(seller.getEndTime()));
        dto.setRegisterTime(DateTimestampUtil.getDateByTimestamp1(seller.getCreateTime().getTime() * 1000l));
        return dto;
    }
}
