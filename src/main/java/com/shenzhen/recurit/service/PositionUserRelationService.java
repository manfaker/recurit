package com.shenzhen.recurit.service;

import com.shenzhen.recurit.vo.ResultVO;

public interface PositionUserRelationService {

    /**
     * 批量新增关注
     * @param positionIds
     * @return
     */
    ResultVO saveBatchRelation(String positionIds,int relationStatus);

    /**
     * 批量删除关注
     * @param positionIds
     * @return
     */
    ResultVO deleteBatchRelation(String positionIds,int relationStatus);


}
