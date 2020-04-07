package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.vo.PositionUserRelationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PositionUserRelationMapper {

    /**
     * 查询所有的关联关系
     * @param listPositionId
     * @param userCode
     * @param relationStatus
     * @return
     */
    List<PositionUserRelationVO> getRelationByPostionAndUser(@Param("listPositionId") List<Integer> listPositionId,@Param("userCode")  String userCode,@Param("relationStatus") int relationStatus);

    /**
     * 批量保存关联关系
     * @param relations
     */
    void saveBatchRelation(@Param("relations") List<PositionUserRelationVO> relations);

    /**
     * 批量移除
     * @param listPositionId
     * @param userCode
     * @param relationStatus
     */
    int deleteBatchRelation(@Param("listPositionId") List<Integer> listPositionId,@Param("userCode")  String userCode,@Param("relationStatus") int relationStatus);
}
