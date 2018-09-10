package com.netx.fuse.biz.worth;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.netx.common.user.dto.common.CommonUserBaseInfoDto;
import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.fuse.client.ucenter.WalletBillClientAction;
import com.netx.fuse.client.ucenter.WangMingClientAction;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.worth.biz.common.RefundAction;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.biz.settlement.SettlementAmountAction;
import com.netx.worth.biz.settlement.SettlementCreditAction;
import com.netx.worth.biz.settlement.SettlementLogAction;
import com.netx.worth.model.Settlement;
import com.netx.worth.model.SettlementAmount;
import com.netx.worth.model.SettlementCredit;
import com.netx.worth.util.ArithUtil;

@Service
public class SettlementFuseAction {
	
    /**
     * 收入
     */
    public static final int IN = 1;
    /**
     * 支出
     */
    public static final int OUT = 0;

	@Autowired
	private SettlementAction settlementAction;
	@Autowired
	private SettlementCreditAction settlementCreditAction;
	@Autowired
	private WalletBillClientAction walletBillClientAction;
	@Autowired
	private WangMingClientAction wangMingClientAction;
	@Autowired
	private SettlementAmountAction settlementAmountAction;
	@Autowired
	private RefundAction refundAction;
	@Autowired
	private SettlementLogAction settlementLogAction;

	@Autowired
	private UserClientProxy userClientProxy;

	public void settlementCredit(Settlement settlement, SettlementCredit settlementCredit) {
		settlementLogAction.creatCreditLog(settlement.getId(), settlementCredit.getId(), settlementCredit.getUserId(),
				settlementCredit.getCredit());
		CommonUserBaseInfoDto user = userClientProxy.getUser(settlementCredit.getUserId());
		Map request = new HashMap();
		request.put("credit", settlementCredit.getCredit());
		request.put("lockVersion", user.getLockVersion());
		request.put("relatableId", settlementCredit.getId());
		request.put("relatableType", "SettlementCredit");
		request.put("userId", settlementCredit.getUserId());
		wangMingClientAction.addCreditRecord(request);
	}

	public void settlementAmount(Settlement settlement, SettlementAmount settlementAmount) {
		settlementLogAction.createAmountLog(settlement.getId(), settlementAmount.getId(), settlementAmount.getUserId(),
				settlementAmount.getAmount());
		BigDecimal amount = BigDecimal.valueOf(settlementAmount.getAmount());
		Integer tradeType = null;
		Map request = new HashMap();
		tradeType = amount.doubleValue() >= 0 ? this.IN : this.OUT;
		amount = ArithUtil.abs(amount.divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN));
		BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
		billAddRequestDto.setAmount(amount);
		billAddRequestDto.setDescription("SettlementAmount | " + settlementAmount.getId());
		billAddRequestDto.setPayChannel(3);
		billAddRequestDto.setType(tradeType);
		walletBillClientAction.addBill("999",billAddRequestDto);
	}

	public void settlementRightNow(Settlement settlement) {
		List<SettlementAmount> settlementAmounts = settlementAmountAction.selectBySettlementId(settlement.getId());
		settlementAmounts.forEach(settlementAmount -> {
			settlementAmount(settlement, settlementAmount);
		});

		List<SettlementCredit> settlementCredits = settlementCreditAction
				.selectBySettlementIdAndCan(settlement.getId());
		settlementCredits.forEach(settlementCredit -> {
			settlementCredit(settlement, settlementCredit);
		});
	}

	@Transactional(rollbackFor = Exception.class)
	public void start() {
		List<Settlement> list = settlementAction.getAvailableSettlement();
		list.forEach(settlement -> {
			if (settlement.getCan()) {// 能结算的立即结算
				settlementRightNow(settlement);
			} else {// 不能结算的标记一下再结算
				if (refundAction.isUnProcess(settlement.getRelatableType(), settlement.getRelatableId()))
					return;// 如果还有没处理的退款，那直接退出。
				settlementAction.makeCanSettlement(settlement);
				settlementRightNow(settlement);
			}
			settlementAction.finish(settlement);
		});
	}
}
