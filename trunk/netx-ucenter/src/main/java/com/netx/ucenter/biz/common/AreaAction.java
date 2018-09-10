package com.netx.ucenter.biz.common;

import com.netx.common.vo.common.AreaAddRequestDto;
import com.netx.common.vo.common.AreaQueryRequestDto;
import com.netx.ucenter.model.common.CommonArea;
import com.netx.ucenter.service.common.AreaService;
import com.netx.ucenter.vo.AreaVO;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by wongloong on 17-8-29
 */
@Service
public class AreaAction{
    private Logger logger = LoggerFactory.getLogger(AreaAction.class);
    @Autowired
    private AreaService areaService;

    public boolean saveOrUpdate(AreaAddRequestDto request) {
        CommonArea wzCommonArea = new CommonArea();
        try {
            BeanUtils.copyProperties(wzCommonArea, request);
            if (StringUtils.isEmpty(wzCommonArea.getId())) {
                wzCommonArea.setCreateTime(new Date());
            } else {
                wzCommonArea.setUpdateTime(new Date());
            }
            return areaService.insertOrUpdate(wzCommonArea);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public List<CommonArea> selectByPidAndFlag(AreaQueryRequestDto request) {
        try {
            return areaService.selectByPidAndFlag(request.getFlag(),request.getPid(),request.getId());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new ArrayList<>();
    }

    public boolean deleteByIdAndValidationChildren(String id) throws Exception{
        return areaService.deleteByIdAndValidationChildren(id);
    }

    public List<AreaVO> selectAllAreas() throws Exception {
        AreaQueryRequestDto request = new AreaQueryRequestDto();
        List<CommonArea> provinceList = areaService.selectByPidAndFlag(0,"0",null);
        List<AreaVO> voList = new ArrayList<AreaVO>();
        if(provinceList != null){
            for (CommonArea province:provinceList){
                AreaVO provinceVO = areaToVO(province);
                List<CommonArea> cityList = areaService.selectByPidAndFlag(1,province.getId(),null);
                provinceVO.setChildren(toVOList(cityList));
                if(cityList != null && cityList.size() > 0){
                    for (int i = 0;i < cityList.size();i++) {
                        CommonArea city = cityList.get(i);
                        List<CommonArea> areaList = areaService.selectByPidAndFlag(2,city.getId(),null);
                        provinceVO.getChildren().get(i).setChildren(toVOList(areaList));
                    }
                }
                voList.add(provinceVO);
            }
        }
        return voList;
    }

    private AreaVO areaToVO(CommonArea area){
        AreaVO vo = new AreaVO();
        vo.setName(area.getValue());
        vo.setId(area.getId());
        vo.setFlag(area.getFlag());
        return vo;
    }

    private List<AreaVO> toVOList(List<CommonArea> list){
        List<AreaVO> voList = null;
        if(list != null){
            voList = new ArrayList<AreaVO>();
            for (CommonArea area:list) {
                voList.add(areaToVO(area));
            }
        }
        return voList;
    }
}
