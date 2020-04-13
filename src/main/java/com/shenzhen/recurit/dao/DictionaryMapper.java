package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.vo.DictionaryVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DictionaryMapper {

    /**
     * 获取数据字典的最新时间
     * @return 最新时间
     */
    Date getLatestTime();

    /**
     * 获取所有的数据字典
     * @return
     */
    List<DictionaryVO> getListAll(@Param("startTime") Date startTime, @Param("endTime")  Date endTime);

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
     * 通过category 获取所有类型的数据
     * @param category
     * @return
     */
    List<DictionaryVO> getAllDictByCategory(String category);

    /**
     * dictNum 获取单个数据
     * @param dictNum
     * @return
     */
    DictionaryVO getSignleByDictNumber(String dictNum);

    /**
     * dictNum 获取
     * @param dictNum
     * @return
     */
    List<DictionaryVO> getAllChildrenByDictNum(String dictNum);

    /**
     * 根据dictNum 修改字典信息
     * @param dictionaryVO
     * @return
     */
    int updateDictionary(DictionaryVO dictionaryVO);

    /**
     * 删除数据字典根据类型
     * @param category
     */
    int deleteByCategory(String category);

    /**
     * 根据编码删除
     * @param dictNum
     */
    int deleteByDictNum(String dictNum);
}
