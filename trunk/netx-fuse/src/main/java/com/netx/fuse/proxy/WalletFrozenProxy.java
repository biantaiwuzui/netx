package com.netx.fuse.proxy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.netx.common.vo.common.FrozenOperationRequestDto;
import com.netx.common.vo.common.FrozenQueryRequestDto;
import com.netx.common.vo.common.WzCommonWalletFrozenResponseDto;
import com.netx.fuse.client.ucenter.WalletForzenClientAction;

@Repository
public class WalletFrozenProxy {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    WalletForzenClientAction walletForzenClient;

    public List<WzCommonWalletFrozenResponseDto> queryList(FrozenQueryRequestDto requestDto){
        return walletForzenClient.queryList(requestDto);
        /*if(result.getCode()==0){
            logger.info("queryList 调用成功");
            JSONArray jsonArray=JSON.parseArray(JSONObject.toJSONString(result.getObject()));
            list = JSONObject.parseArray(jsonArray.toJSONString(),WzCommonWalletFrozenResponseDto.class);
            return list;
        }
        logger.info("queryList 调用失败");
        return null;*/
    }

	public boolean pay(FrozenOperationRequestDto requestDto) {
		return walletForzenClient.pay(requestDto);
	}
}
