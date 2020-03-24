package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.vo.LabelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LabelMapper {
    /**
     * 批量新增
     * @param listLabel
     * @return
     */
    List<LabelPojo> saveBatchLabel(@Param("listLabel") List<LabelVO> listLabel);
}
