package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.DesiredPositionPojo;
import com.shenzhen.recurit.vo.DesiredPositionVO;
import com.shenzhen.recurit.vo.ResultVO;

import java.util.List;

public interface DesiredPositionService {

    /**
     * 保存期望职位
     * @param desiredPositionVO
     * @return
     */
    ResultVO saveDesiredPosition(DesiredPositionVO desiredPositionVO);

    /**
     * 根据id删除职位信息
     * @param
     * @return
     */
    int deleteDesiredPositionById(int id);

    /**
     * 根据userCode
     * @param
     * @return
     */
    List<DesiredPositionPojo> getDesiredPositionuserCode(String userCode);

    /**
     * 修改职位信息
     * @paramdesiredPositionVO
     * @return
     */
    int updateDesiredPosition(DesiredPositionVO desiredPositionVO);

    /**
     * 获取职位信息
     * @paramdesiredPositionVO
     * @return
     */
    DesiredPositionPojo getDesiredPosition(int id);
}
