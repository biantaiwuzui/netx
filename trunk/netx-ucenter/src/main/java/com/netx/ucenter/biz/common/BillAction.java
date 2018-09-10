package com.netx.ucenter.biz.common;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.utils.WeChatPayUtil;
import com.netx.common.common.utils.CommonXMLUtil;
import com.netx.common.pay.PayInfoHolder;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.vo.common.*;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.common.CommonBill;
import com.netx.ucenter.model.common.CommonWallet;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.common.BillService;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.utils.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Create by wongloong on 17-9-17
 */
@Service
public class BillAction{
    private Logger logger = LoggerFactory.getLogger(BillAction.class);

    @Autowired
    private PayInfoHolder payInfoHolder;

    @Autowired
    private WalletAction walletAction;
    @Autowired
    private WalletFrozenAction walletFrozenAction;
    @Autowired
    private BillService billService;
    @Autowired
    private UserAction userAction;
    @Autowired
    private CommonServiceProvider commonServiceProvider;

    private Integer getAge(Date birthday){
        return ComputeAgeUtils.getAgeByBirthday(birthday);
    }

    public List<CommonBill> getPage(String userId,BillQueryRequestDto request) throws Exception{
        Page page = new Page(request.getCurrent(),request.getSize());
        return billService.getPage(page,0,userId,request.getStartTime(),request.getEndTime(),request.getType());
    }

    public List<BillListResponseDto> getPages(String userId,BillQueryRequestDto request) throws Exception{
        List<BillListResponseDto> list = new ArrayList<>();
        List<CommonBill> lists = getPage(userId,request);
        String id=null;
        if (lists!=null && lists.size() != 0) {
            BigDecimal amount=null;
            for (int i = 0; i < lists.size(); i++) {
                BillListResponseDto dto=new BillListResponseDto();
                BeanUtils.copyProperties(lists.get(i), dto);
                dto.setType(lists.get(i).getType());
                amount =new BigDecimal (Money.getMoneyString(lists.get(i).getTradeType()==0?-Math.abs(lists.get(i).getAmount()):Math.abs(lists.get(i).getAmount())).trim());
                dto.setAmount(amount);
                list.add(dto);
                User user = userAction.getUserService().selectById(lists.get(i).getUserId());
                id=list.get(i).getId();
                if(user!=null){
                    list.get(i).setAge(getAge(user.getBirthday()));
                    list.get(i).setLv(user.getLv());
                    list.get(i).setNickname(user.getNickname());
                    list.get(i).setSex(user.getSex());
                    list.get(i).setCredit(user.getCredit());
                }
                list.get(i).setId(id);
            }
        }
        return list;
    }

    /**
     *
     * @param bill
     * @param tradType 充值时对于平台是支出，tradType填0，反之提现则为1
     */
    @Transactional(rollbackFor = Exception.class)
    public void notifySuccess(CommonBill bill,int tradType){
        CommonBill requestDto = notifySuccessFly(bill,tradType);
        if(bill.getToAccount()==1) {//确定充值成功才转账
            if (tradType == 0) {
                this.updateWallet(requestDto, -1);
                this.updateWallet(bill, 1);
            } else {
                this.updateWallet(requestDto, 1);
                this.updateWallet(bill, -1);
            }
            requestDto.setToAccount(1);
            billService.updateById(requestDto);
        }
        billService.updateById(bill);
    }

    @Transactional(rollbackFor = Exception.class)
    CommonBill notifySuccessFly(CommonBill bill,int tradType){

        CommonBill requestDto=new CommonBill();
        requestDto.setAmount(bill.getAmount());
        requestDto.setDescription(bill.getDescription());
        requestDto.setPayChannel(bill.getPayChannel());
        requestDto.setUserId("999");
        requestDto.setToAccount(0);//这里还不确定是否充值成功
        requestDto.setType(0);
        requestDto.setTradeType(tradType);
        requestDto.setCreateTime(new Date());
        billService.insert(requestDto);
        return requestDto;
    }


    /**
     * 更新钱包
     * @param bill 交易流水
     * @param i -1支出，1收入
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateWallet(CommonBill bill,int i){
        //0支出，1收入
        CommonWallet wzCommonWallet = walletAction.queryWalletByUserId(bill.getUserId());
        if (null != wzCommonWallet && !StringUtils.isEmpty(wzCommonWallet.getId())) {
            wzCommonWallet.setTotalAmount(wzCommonWallet.getTotalAmount()+i*bill.getAmount());
            commonServiceProvider.getWalletService().updateById(wzCommonWallet);
            bill.setTotalAmount(wzCommonWallet.getTotalAmount());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoRunWithdrawBill() {
        List<CommonBill> billList = billService.getWzCommonBillList(0,0,0,null);
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", payInfoHolder.getAppId(), payInfoHolder.getPrivateKey(), "json", "utf-8", payInfoHolder.getAliPublicKey(), "RSA2");
        if (null != billList && !billList.isEmpty()) {
            for (CommonBill bill : billList) {
                AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
                request.setBizContent("{" +
                        "\"out_biz_no\":\"" + bill.getId() + "\"" +
                        "  }");
                AlipayFundTransOrderQueryResponse response = null;
                try {
                    response = alipayClient.execute(request);
                } catch (AlipayApiException e) {
                    logger.error("第三方提现跑批异常", e);
                }
                if (response.isSuccess()) {//查询状态返回成功
                    bill.setToAccount(1);//设置流水已到账
                    CommonWallet wzCommonWallet = walletAction.queryWalletByUserId(bill.getUserId());
                    wzCommonWallet.setTotalAmount(wzCommonWallet.getTotalAmount()-bill.getAmount());
                    bill.setUpdateTime(new Date());
                    billService.updateById(bill);
                } else {
                    logger.warn("第三方提现跑批失败订单号:#0,返回订单状态:#1", bill.getId(), response.getErrorCode());
                    if (response.getStatus().equals("FAIL")) {//处理失败
                        if (response.getErrorCode().equals("SYSTEM_ERROR")) {//系统异常
                            logger.warn("支付宝提现系统异常");
                        } else if (response.getErrorCode().equals("ILLEGAL_ACCOUNT_STATUS_EXCEPTION")) {
                            //请检查一下账户状态是否正常，如果账户不正常请联系支付宝小二。
                            // 联系方式：https://support.open.alipay.com/alipay/support/index.htm
                            logger.warn("支付宝提现跑批查询失败,请检查一下账户状态是否正常，如果账户不正常请联系支付宝小二");
                        } else {//其余关闭订单
                            bill.setDeleted(1);
                            billService.updateById(bill);
                        }
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoRunAliRechargeBill() {
        List<CommonBill> bills = billService.getWzCommonBillList(1,0,0,new Date(System.currentTimeMillis()- 60 * 30 * 1000));
        if (null != bills && !bills.isEmpty()) {
            for (CommonBill bill : bills) {
//                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "app_id", "your private_key", "json", "GBK", "alipay_public_key", "RSA2");
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", payInfoHolder.getAppId(),payInfoHolder.getPrivateKey(), "json", "GBK", payInfoHolder.getAliPublicKey(), "RSA2");
                AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
                request.setBizContent("{" +
                        "\"out_trade_no\":\"" + bill.getId() + "\"" +
                        "  }");
                AlipayTradeQueryResponse response = null;
                try {
                    response = alipayClient.execute(request);
                } catch (AlipayApiException e) {
                    e.printStackTrace();
                }
                if (response.isSuccess()) {
                    if (response.getTradeStatus().equals("TRADE_CLOSED")) {//交易关闭
                        bill.setDeleted(1);
                    } else if (response.getTradeStatus().equals("WAIT_BUYER_PAY")) {//等待付款

                    } else if (response.getTradeStatus().equals("TRADE_SUCCESS")) {//交易成功
                        modifySuccess(bill);
                    }
                    billService.updateById(bill);
                } else {
                    logger.warn("查询支付宝订单状态失败,#0", response.getSubMsg());
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoRunWechatRechargeBill() {
        List<CommonBill> bills = billService.getWzCommonBillList(1,1,0,new Date(System.currentTimeMillis()- 60 * 30 * 1000));
        if (null != bills && !bills.isEmpty()) {
            for (CommonBill bill : bills) {
                SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
                parameters.put("appid", payInfoHolder.getWechatAppId());
                parameters.put("mch_id", payInfoHolder.getWechatMchId());
                parameters.put("nonce_str", WeChatPayUtil.CreateNoncestr());
                parameters.put("out_trade_no", bill.getId()); //订单id
                //设置签名
                String sign = WeChatPayUtil.createSign("UTF-8", parameters, payInfoHolder.getWechatApiKey());
                parameters.put("sign", sign);
                //封装请求参数结束
                String requestXML = WeChatPayUtil.getRequestXml(parameters);
                //调用统一下单接口
                String result = WeChatPayUtil.httpsRequest(payInfoHolder.getWechatQueryOrderUrl(), "POST", requestXML);
                logger.info("\n" + result);
                try {
                    Map<String, String> map = CommonXMLUtil.doXMLParse(result);
                    String returnCode = map.get("return_code");
                    if (StringUtils.isEmpty(returnCode) && returnCode.equals("SUCCESS")) {
                        String appid = map.get("appid");
                        String mchId = map.get("mch_id");
                        String tradeState = map.get("trade_state");
                        if (appid.equals(payInfoHolder.getWechatAppId()) && mchId.equals(payInfoHolder.getWechatMchId())) {
                            String total = map.get("total_fee");//=bill.getAmount()*100
                            if (tradeState.equals("SUCCESS")) {
                                if (bill.getAmount()== Long.parseLong(total)) {
                                    modifySuccess(bill);
                                }
                            } else {
                                bill.setDeleted(1);
                            }
                            billService.updateById(bill);
                        }
                    }
                } catch (Exception e) {
                    logger.error("微信查询订单跑批异常", e);
                }

            }
        }
    }

    public List<BigDecimal> searchMonthBillCounts(List<String> recommendUserIds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //获取上个月1号0点
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Long start = cal.getTime().getTime();
        //获取本月1号零时时间戳
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.DATE, 1);//设置为1号,当前日期既为本月第一天
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        Long end = cal2.getTime().getTime();
        List<BigDecimal> lists = new ArrayList<>();
        for (String userId : recommendUserIds) {
            BigDecimal monthCount = getYuan( billService.getSumAmountByUserId(userId,start,end));
            if (monthCount != null) {
                lists.add(monthCount);
            }else{
                lists.add(BigDecimal.ZERO);
            }
        }
        return lists;
    }

    public BigDecimal statisticBill(BillStatisticsRequestDto dto) {
        return  getYuan(billService.statisticBill(dto));
    }

    public BigDecimal getYuan(BigDecimal cent){
        return cent.divide(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
    }

    public Map<String, BigDecimal> statisticBills(BillsStatisticsRequestDto dto) {
        BillStatisticsRequestDto requestDto = new BillStatisticsRequestDto();
        BeanUtils.copyProperties(dto,requestDto);
        Map<String, BigDecimal> result = new HashMap<>();
        requestDto.setType(requestDto.getType());
        List<Long> currentDate = getCurrentDate();
        requestDto.setStartTime(currentDate.get(0));
        requestDto.setEndTime(currentDate.get(1));
        requestDto.setQueryType(0);
        BigDecimal currentExpense = statisticBill(requestDto);
        requestDto.setQueryType(1);
        BigDecimal currentIncome = statisticBill(requestDto);
        result.put("dayDisbursement",currentExpense);
        result.put("dayIncome",currentIncome);

        List<Long> weekDate = getWeekDate();
        requestDto.setStartTime(weekDate.get(0));
        requestDto.setEndTime(weekDate.get(1));
        requestDto.setQueryType(0);
        BigDecimal weekExpense = statisticBill(requestDto);
        requestDto.setQueryType(1);
        BigDecimal weekIncome = statisticBill(requestDto);
        result.put("weekDisbursement",weekExpense);
        result.put("weekIncome",weekIncome);

        List<Long> monthDate = getMonthDate();
        requestDto.setStartTime(monthDate.get(0));
        requestDto.setEndTime(monthDate.get(1));
        requestDto.setQueryType(0);
        BigDecimal monthExpense = statisticBill(requestDto);
        requestDto.setQueryType(1);
        BigDecimal monthIncome = statisticBill(requestDto);
        result.put("monthDisbursement",monthExpense);
        result.put("monthIncome",monthIncome);

        List<Long> yearDate = getYearDate();
        requestDto.setStartTime(yearDate.get(0));
        requestDto.setEndTime(yearDate.get(1));
        requestDto.setQueryType(0);
        BigDecimal yearExpense = statisticBill(requestDto);
        requestDto.setQueryType(1);
        BigDecimal yearIncome = statisticBill(requestDto);
        result.put("yearDisbursement",yearExpense);
        result.put("yearIncome",yearIncome);

        requestDto.setStartTime(null);
        requestDto.setEndTime(null);
        requestDto.setQueryType(0);
        BigDecimal totalExpense = statisticBill(requestDto);
        requestDto.setQueryType(1);
        BigDecimal totalIncome = statisticBill(requestDto);
        result.put("totalDisbursement",totalExpense);
        result.put("totalIncome",totalIncome);
        return result;
    }

    private List<Long> getCurrentDate(){
        List<Long> list = new ArrayList<Long>();

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        Date beginOfDate = calendar1.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        Date endOfDate = calendar2.getTime();

        list.add(beginOfDate.getTime());
        list.add(endOfDate.getTime());
        return list;
    }

    private List<Long> getWeekDate() {
        List<Long> list = new ArrayList<Long>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);

        Calendar calendar2 = Calendar.getInstance();
        //这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        calendar2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        calendar2.add(Calendar.WEEK_OF_YEAR, 1);
        calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);

        list.add(calendar1.getTime().getTime());
        list.add(calendar2.getTime().getTime());
        return list;
    }

    private List<Long> getMonthDate(){
        List<Long> list = new ArrayList<Long>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, 0);
        calendar1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);

        //获取当前月最后一天
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        list.add(calendar1.getTime().getTime());
        list.add(calendar2.getTime().getTime());
        return list;
    }

    private List<Long> getYearDate(){
        List<Long> list = new ArrayList<Long>();
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.clear();
        calendar1.set(Calendar.YEAR, currentYear);
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.clear();
        calendar2.set(Calendar.YEAR, currentYear);
        calendar2.roll(Calendar.DAY_OF_YEAR, -1);
        calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);

        list.add(calendar1.getTime().getTime());
        list.add(calendar2.getTime().getTime());
        return list;
    }

    private void modifySuccess(CommonBill bill) {
        bill.setToAccount(1);
        CommonWallet wzCommonWallet = walletAction.queryWalletByUserId(bill.getUserId());
        wzCommonWallet.setTotalAmount(wzCommonWallet.getTotalAmount()+bill.getAmount());
        commonServiceProvider.getWalletService().updateById(wzCommonWallet);
    }

    public BigDecimal countThisDayOutcome(String userId,Integer i){
       return billService.countThisDayOutcome ( userId, i );
    }
}
