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
    List<LabelVO> saveBatchLabel(@Param("listLabel") List<LabelVO> listLabel);

    /**
     * 单个新增
     * @param labelVO
     */
    void saveLabel(LabelVO labelVO);
}
