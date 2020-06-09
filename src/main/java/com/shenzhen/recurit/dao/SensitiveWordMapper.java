package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.vo.SensitiveWordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SensitiveWordMapper {
    void saveBatchSensitiveWord(@Param("listSensitiveWord") List<SensitiveWordVO> listSensitiveWord);

    List<SensitiveWordVO> checkSensitiveWord(@Param("listStr") List<String> listStr);
}
