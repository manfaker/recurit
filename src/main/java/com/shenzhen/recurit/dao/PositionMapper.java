package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.vo.PositionVO;

import java.util.List;

public interface PositionMapper {

    /**
     * 获取所有的职位
     * @return
     */
    List<PositionVO> getAllPosition();

    /**
     * 根据条件筛选所有信息
     */
    List<PositionVO> getAllPositionByCondition(PositionVO position);

    /**
     * 保存职位信息
     * @param position
     */
    void savePosition(PositionVO position);

    /**
     * 根据Id查找公司信息
     * @param companyId
     */
    List<PositionVO> getByCompanyId(String companyId);

    /**
     * 修改职位信息
     * @param position
     */
    int updatePosition(PositionVO position);
}
