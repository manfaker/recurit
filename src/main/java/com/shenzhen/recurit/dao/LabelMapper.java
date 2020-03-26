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

    /**
     * 根据id查找当前标签
     * @param id
     * @return
     */
    LabelVO getLabelById(int id);

    /**
     * 根据类型和关系id查询标签
     * @param category
     * @param relationId
     */
    void getLabelByCategory(@Param("category") String category,@Param("relationId") int relationId);

    /**
     * 根据id删除标签
     * @param id
     * @return
     */
    int deleteLabelById(int id);

    /**
     * 根据类型和关系id删除标签
     * @param category
     * @param relationId
     * @return
     */
    int deleteLabelByCategory(@Param("category") String category,@Param("relationId") int relationId);

    /**
     * 根据id修改标签信息
     * @param labelVO
     * @return
     */
    int updateLabel(LabelVO labelVO);
}
