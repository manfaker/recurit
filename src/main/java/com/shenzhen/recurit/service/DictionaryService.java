package com.shenzhen.recurit.service;

import com.shenzhen.recurit.vo.DictionaryVO;

import java.util.Date;
import java.util.List;

public interface DictionaryService {

    /**
     * 获取数据字典的最新时间
     * @return 最新时间
     */
    Date getLatestTime();

    /**
     * 获取所有的数据字典
     * @return
     */
    List<DictionaryVO> getListAll(Date startTime, Date endTime);

    /**
     * 保存数据
     * @param dictionary
     */
    void saveDictionary(DictionaryVO dictionary);

    /**
     * 通过字典编号获取字典信息
     * @param dictNum
     * @return
     */
    DictionaryVO getDictByNum(String dictNum);

    /**
     * 获取所有相同类型的数据
     * @param category
     * @return
     */
    List<DictionaryVO> getAllDictByCategory(String category);

    /**
     * 获取根据种类和字典编码获取单个字典信息
     * @param category
     *  @param dictNum
     * @return
     */
    DictionaryVO getSignleByDictNumber(String category,String dictNum);

    /**
     * 获取根据字典编码获取子集信息
     *  @param dictNum
     * @return
     */
    List<DictionaryVO> getAllChildrenByDictNum(String dictNum);

    /**
     * 修改编码信息
     *  @param dictionaryVO
     * @return
     */
    DictionaryVO updateDictionary(DictionaryVO dictionaryVO);

    /**
     * 刷新所有数据字典
     * @return
     */
    void refreshAllDict();

    /**
     * 批量保存数据字典
     * @param listDict
     */
    void saveBatch(List<DictionaryVO> listDict);

    void removeDict(String category, String dictNum);
}
