package com.netx.shopping.biz.merchantcenter;

import com.netx.common.common.utils.GenerateQrcodeUtil;
import com.netx.common.redis.util.RedisUtils;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.vo.business.GetCustomerServiceAgentResponsetDto;
import com.netx.common.vo.business.GetSellerAgentRequestDto;
import com.netx.common.vo.business.GetSellerAgentResponsetDto;
import com.netx.common.vo.business.GetSellerResponseDto;
import com.netx.shopping.biz.marketingcenter.MerchantRecordingHistoryAction;
import com.netx.shopping.enums.PromotionAwardEnum;
import com.netx.shopping.model.marketingcenter.MerchantRecordingHistory;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.shopping.vo.RegisterMerchantRequestDto;
import com.netx.shopping.vo.SellerAgentDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MerchantRegisterAction {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private MerchantRecordingHistoryAction merchantRecordingHistoryAction;

    public boolean isCanRegisterMerchant(RegisterMerchantRequestDto request){
        if (merchantService.isCanRegisterMerchant(request) != null) {
            return false;
        }
        return true;
    }

    /**
     * 生成与验证八位数的客服代码
     *
     * @return
     */
    public String buildCustomerServiceCode() {
        String customerServiceCode = "";
        do {
            customerServiceCode = createCustomerServiceCode();
        } while (checkCustomerServiceCode(customerServiceCode) != null);//进行客服代码校检
        return customerServiceCode;
    }

    /**
     * 生成一个八位数的客服代码
     *
     * @return
     */
    private String createCustomerServiceCode() {
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
     * @param customerServiceCode
     * @return
     */
    public Merchant checkCustomerServiceCode(String customerServiceCode) {
        return merchantService.checkCustomerServiceCode(customerServiceCode);
    }

    public void createBusinessNum(String id, String city) {
        redisUtils.addNum("China", id, 0);
        redisUtils.addNum(city, id, 0);
    }

    public String getCity(String city, String provinceCode) {
        if (city == null || city.isEmpty()) {
            return provinceCode;
        }
        return provinceCode + "-" + city;
    }

    public Integer getPosition(PromotionAwardEnum awardEnum, String city) {
        return merchantService.getPosition(awardEnum, city);
    }

    public int getGroupNum(Integer groupNo) {
        int d = groupNo / 30;
        return groupNo%30==0 ? d:d+1;
    }

    public Boolean checkUpGroup(Integer num) {
        return num > 30 && num % 30 == 1;
    }

    /**
     * 定时任务1：发放工资及清除对应字段
     *
     *
     */
    public void operateMonthThing(){
        List<Merchant> merchants = merchantService.getMerchantList();
        for (Merchant merchant : merchants) {
            //发放工资
            merchant.setAchievementMonth(merchant.getPosition() == 2 ? 3000 : 5000);
            switch (merchant.getPosition()) {
                case 2:
                    getSalary(merchant, 3000);
                    break;
                case 3:
                    getSalary(merchant, 5000);
                    break;
                default:
                    merchant.setAchievementMonth(0);
            }
            merchant.setMonthNum(0);
            merchant.setMonthSecondNum(0);
            merchant.setMonthThirdNum(0);
            merchantService.updateById(merchant);
        }
    }

    private void getSalary(Merchant merchant, Integer salary) {
        merchant.setAchievementMonth(salary);
        merchant.setAchievementTotal(merchant.getAchievementTotal() + salary);
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
        List<SellerAgentDto> mapList = merchantService.getMapList(dto.getUserId());
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
        res.setAreaRanking(merchantService.selectMerchantAndRandNo(dto.getId(), dto.getProvinceCode(), dto.getCityCode()));//区域排名
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

    /**
     * 客服代理
     *
     * @param parentMerchantId
     * @return
     *
     */
    public List<GetCustomerServiceAgentResponsetDto> getCustomerServiceAgent(String parentMerchantId){
        List<GetCustomerServiceAgentResponsetDto> res = new ArrayList<>();
        List<Merchant> merchants = merchantService.getMerchantListByParentMerchantId(parentMerchantId);
        if (merchants != null && !merchants.isEmpty()) {
            int no = 1;
            Merchant merchantFrist = merchants.remove(0);
            int businessNum = merchantFrist.getSecondNum() + merchantFrist.getThirdNum();
            res.add(createGetCustomerServiceAgentResponsetDto(merchantFrist, no));
            for (Merchant merchant : merchants) {
                if (merchant.getSecondNum() + merchant.getThirdNum() != businessNum) {
                    no++;
                    businessNum = merchant.getSecondNum() + merchant.getThirdNum();
                }
                res.add(createGetCustomerServiceAgentResponsetDto(merchant, no));
            }
        }
        return res;
    }

    private GetCustomerServiceAgentResponsetDto createGetCustomerServiceAgentResponsetDto(Merchant merchant, int no){
        GetCustomerServiceAgentResponsetDto dto = new GetCustomerServiceAgentResponsetDto();
        dto.setAllIndirectCommission(merchantRecordingHistoryAction.getDirect(merchant.getId(), 1));//累计提成
        dto.setAreaName(getCity(merchant.getCityCode(), merchant.getProvinceCode()));
        dto.setAreaRanking(no);//排名
        dto.setCustomerServiceGrounp(getGroupNum(merchant.getGroupNo()));
        dto.setMonthIndirectCommission(merchantRecordingHistoryAction.getDirect(merchant.getId(), 2));//月提成
        dto.setNewAddSellerNum(merchant.getMonthNum());
        dto.setSurplusDay(getSurplusDay(merchant.getExpireTime().getTime()));//剩余天数
        dto.setDirectCommission(merchantRecordingHistoryAction.getDirect(merchant.getId(), 0));//提成
        dto.setAllSellerNum(merchant.getSecondNum() + merchant.getThirdNum());
        dto.setCustomerServicerName(merchant.getName());
        dto.setRegisterTime(DateTimestampUtil.getDateByTimestamp1(merchant.getCreateTime().getTime()));
        dto.setCustomerServicerNum(merchant.getSecondNum());
        dto.setSellerId(merchant.getId());
        return dto;
    }

    private Long getSurplusDay(long endTime) {
        Long surplusTime = endTime - System.currentTimeMillis();
        if (surplusTime >= 0) {
            return surplusTime / (24 * 60 * 60 * 1000);
        }
        return -1l;
    }

    public List<GetSellerResponseDto> getMerchantList(String parentMerchantId){
        List<GetSellerResponseDto> res = new ArrayList<>();
        List<Merchant> merchants = merchantService.getMerchantListByParentMerchantId(parentMerchantId);
        for (Merchant merchant : merchants) {
            res.add(createGetMerchantResponseDto(merchant));
        }
        return res;
    }
    private GetSellerResponseDto createGetMerchantResponseDto(Merchant merchant){
        GetSellerResponseDto dto = new GetSellerResponseDto();
        dto.setAreaName(getCity(merchant.getCityCode(), merchant.getProvinceCode()));
        dto.setCommission(merchantRecordingHistoryAction.getCommission(merchant.getId()));
        dto.setCustomerServicerName(merchant.getName());
        dto.setSurplusDay(getSurplusDay(merchant.getExpireTime().getTime()));
        dto.setRegisterTime(DateTimestampUtil.getDateByTimestamp1(merchant.getCreateTime().getTime()));
        return dto;
    }

}
