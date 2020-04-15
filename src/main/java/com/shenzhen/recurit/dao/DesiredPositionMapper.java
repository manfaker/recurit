package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.DesiredPositionPojo;
import com.shenzhen.recurit.vo.DesiredPositionVO;

import java.util.List;

public interface DesiredPositionMapper {
    void saveDesiredPosition(DesiredPositionVO desiredPositionVO);

    DesiredPositionPojo getDesiredPosition(int id);

    int deleteDesiredPositionById(int id);

    int updateDesiredPosition(DesiredPositionVO desiredPositionVO);

    List<DesiredPositionPojo> getDesiredPositionuserCode(String userCode);
}
