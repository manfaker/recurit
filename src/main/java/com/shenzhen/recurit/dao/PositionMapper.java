package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.vo.PositionVO;

import java.util.List;

public interface PositionMapper {
    
    /**
     * 保存职位信息
     * @param position
     */
    void savePosition(PositionVO position);


}
