package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.vo.PositionUserRelationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PositionUserRelationMapper {

    /**
     * 查询所有的关联关系
     * @param listPositionId
     * @param userCode
     * @return
     */
    List<PositionUserRelationVO> getRelationByPostionAndUser(@Param("listPositionId") List<Integer> listPositionId,@Param("userCode")  String userCode);

    /**
     * 批量保存关联关系
     * @param relations
     */
    void saveBatchRelation(@Param("relations") List<PositionUserRelationVO> relations);

    /**
     * 批量移除
     * @param listPositionId
     * @param userCode
     */
    int deleteBatchRelation(@Param("listPositionId") List<Integer> listPositionId,@Param("userCode")  String userCode);

    /**
     * 根据id 修改信息
     * @param positionUserRelationVO
     * @return
     */
    int updateRelation(PositionUserRelationVO positionUserRelationVO);

    /**
     * 通过职位id和usercode获取关系信息
     * @param positionId
     * @param userCode
     * @return
     */
    PositionUserRelationVO getRelationByPositionIdAndUserCode(@Param("positionId") int positionId,@Param("userCode")  String userCode);

    /**
     * 保存关系信息
     * @param positionUserRelationVO
     */
    void saveRelation(PositionUserRelationVO positionUserRelationVO);

}
