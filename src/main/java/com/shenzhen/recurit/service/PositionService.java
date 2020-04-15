package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.PositionPojo;
import com.shenzhen.recurit.vo.PositionVO;
import com.shenzhen.recurit.vo.ResultVO;

import java.util.List;

public interface PositionService {

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
    PositionVO savePosition(PositionVO position);
    /**
     * 根据id删除职位信息
     * @param id
     */
    ResultVO deletePositionById(int id);

    /**
     * 根据公司id查找职位信息
     * @param companyCode
     */
    List<PositionPojo> getByCompanyCode(String companyCode,String userCode);

    /**
     * 修改职位信息
     * @param position
     */
    ResultVO updatePosition(PositionVO position);

    /**
     * 根据id 获取职位信息
     * @param id
     * @return
     */
    PositionPojo getByPositionId(int id);

    /**
     * 获取所有的简历信息
     * @return
     */
    List<PositionPojo> getAllPositions(PositionVO positionVO);

    /**
     *
     * @return
     */
    List<PositionPojo> getPopularPositions();

    List<PositionPojo> getRecentlyPositions(String userCode);
}
