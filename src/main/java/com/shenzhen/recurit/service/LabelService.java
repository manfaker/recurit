package com.shenzhen.recurit.service;

import com.shenzhen.recurit.vo.LabelVO;

import java.util.List;

public interface LabelService {

    /**
     * 批量新增
     * @param listLabel
     */
     List<LabelVO> saveBatchLabel(List<LabelVO> listLabel);

    /**
     * 单个新增
     * @param labelVO
     */
    LabelVO saveLabel(LabelVO labelVO);

    /**
     * 单个删除
     * @param id
     * @return
     */
    int deleteLabelById(int id);

    /**
     * 批量删除
     * @param category     类型
     * @param relationId   关联id
     * @return
     */
    int deleteLabelByRelationId(String category,int relationId);

    /**
     * 修改标签
     * @param labelVO
     * @return
     */
    int updateLabel(LabelVO labelVO);

    /**
     * 根据id查询
     * @param id
     * @param category
     * @param relationId
     * @return
     */
    LabelVO queryById(int id,String category,int relationId);

    /**
     * 根据id查询
     * @param category
     * @param relationId
     * @return
     */
    List<LabelVO>  queryByRelationId(String category,int relationId);





}
