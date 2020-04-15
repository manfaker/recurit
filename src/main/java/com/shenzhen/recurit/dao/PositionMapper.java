package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.PositionPojo;
import com.shenzhen.recurit.vo.PositionVO;
import org.apache.ibatis.annotations.Param;

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
    List<PositionPojo> getByCompanyCode(@Param("companyCode") String companyCode,@Param("userCode")  String userCode);

    /**
     * 根据id获取职位信息
     * @param id
     * @return
     */

    PositionPojo getByPositionId(int id);


    /**
     * 根据id删除职位信息
     * @param id
     * @return
     */
    void deleteByPositionId(int id);

    /**
     * 根据修改职位信息
     * @param position
     * @return
     */
    int updatePosition(PositionVO position);

    /**
     * 获取所有的简历信息
     * @return
     */
    List<PositionPojo> getAllPositions(PositionVO positionVO);

    /**
     * 获取热门职业
     * @return
     */
    List<PositionPojo> getPopularPositions();

    /**
     * 通过信息获取最近浏览记录
     * @param userCode
     * @return
     */
    List<PositionPojo> getRecentlyPositions(@Param("userCode") String userCode);
}
