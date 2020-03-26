package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.PositionPojo;
import com.shenzhen.recurit.vo.PositionVO;

import java.util.List;

public interface PositionMapper {
    
    /**
     * 保存职位信息
     * @param position
     */
    void savePosition(PositionVO position);


    /**
     * 职位信息
     * @param companyCode
     * @return
     */
    List<PositionPojo> getByCompanyCode(String companyCode);

    /**
     * 根据id获取职位信息
     * @param id
     * @return
     */
    PositionPojo getByPositionId(int id);
}
