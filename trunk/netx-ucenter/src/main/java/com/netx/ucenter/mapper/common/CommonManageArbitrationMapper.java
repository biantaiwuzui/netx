package com.netx.ucenter.mapper.common;

import com.netx.common.vo.common.ArbitrationSelectResponseVo;
import com.netx.ucenter.model.common.CommonManageArbitration;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
public interface CommonManageArbitrationMapper extends BaseMapper<CommonManageArbitration> {
	List<ArbitrationSelectResponseVo> selectByUserIdAndStatusCode(@Param("userId") String userId, @Param("statusCode") Integer statusCode);

    List<ArbitrationSelectResponseVo>  selectByNicknameAndStatusCodes(@Param("nickname") String nickname, @Param("statusCodes") Integer[] statusCodes) throws Exception;

    List<ArbitrationSelectResponseVo>  selectByUserIdAll(@Param("userId") String userId) throws Exception;

    List<ArbitrationSelectResponseVo>  selectByNicknameAll(@Param("nickname") String nickname) throws Exception;
}