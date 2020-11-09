package com.shenzhen.recurit.service;

import com.shenzhen.recurit.vo.PositionUserRelationVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;

import java.util.List;

public interface PositionUserRelationService {

    /**
     * 批量新增关注
     * @param positionUserRelationVO
     * @return
     */
    ResultVO saveBatchRelation(PositionUserRelationVO positionUserRelationVO);

    /**
     * 批量删除关注
     * @param positionUserRelationVO
     * @return
     */
    ResultVO deleteBatchRelation(PositionUserRelationVO positionUserRelationVO);


    /**
     * 修改关联信息
     * @param positionUserRelationVO
     * @return
     */
    ResultVO updateRelation(PositionUserRelationVO positionUserRelationVO);

    /**
     * 新增或者修改关联信息
     * @param positionUserRelationVO
     * @return
     */
    ResultVO createOrUpdateRelation(PositionUserRelationVO positionUserRelationVO, UserVO userVO);

    /**
     * 根据职位id获取简历投递数量
     *
     * @param positionIds
     * @return
     */
    List<PositionUserRelationVO> getCountByPosition(List<Integer> positionIds);
}
