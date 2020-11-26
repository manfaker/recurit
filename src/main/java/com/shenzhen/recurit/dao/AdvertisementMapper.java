package com.shenzhen.recurit.dao;

import com.github.pagehelper.PageInfo;
import com.shenzhen.recurit.pojo.AdvertisementPojo;
import com.shenzhen.recurit.vo.AdvertisementVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdvertisementMapper {

    /**
     * 新增
     *
     * @param advertisementVO {@link AdvertisementVO}
     * @return
     */
    void addAdvertisement(AdvertisementVO advertisementVO);

    /**
     * 修改
     *
     * @param advertisementVO {@link AdvertisementVO}
     * @return
     */
    int updateAdvertisement(AdvertisementVO advertisementVO);

    /**
     * 批量删除
     *
     * @param idList
     * @return
     */
    int batchDeleteAdvertisement(@Param("idList") List<Integer> idList);

    /**
     * 查询所有
     *
     * @return
     */
    List<AdvertisementPojo> getAllAdvertisements();

    /**
     * 根据id 批量获取广告信息
     *
     * @param idList
     * @return
     */
    List<AdvertisementPojo> getAdvertisementByIds(@Param("idList") List<Integer> idList);

    /**
     * 根据id 获取广告信息
     *
     * @param id
     * @return
     */
    AdvertisementPojo getAdvertisementById(Integer id);

}
