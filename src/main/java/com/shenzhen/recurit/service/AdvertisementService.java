package com.shenzhen.recurit.service;

import com.github.pagehelper.PageInfo;
import com.shenzhen.recurit.dao.AdvertisementMapper;
import com.shenzhen.recurit.pojo.AdvertisementPojo;
import com.shenzhen.recurit.vo.AdvertisementVO;

import javax.annotation.Resource;
import java.util.List;

public interface AdvertisementService {

    /**
     * 新增
     *
     * @param advertisementVO {@link AdvertisementVO}
     * @return
     */
    AdvertisementPojo addAdvertisement(AdvertisementVO advertisementVO);

    /**
     * 修改
     *
     * @param advertisementVO {@link AdvertisementVO}
     * @return
     */
    AdvertisementPojo updateAdvertisement(AdvertisementVO advertisementVO);

    /**
     * 批量删除
     * 
     * @param idList
     * @return
     */
    int batchDeleteAdvertisement(List<Integer> idList);

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<AdvertisementPojo> getAdvertisementsByPage(int pageNum, int pageSize);

    /**
     * 根据id 批量获取广告信息
     *
     * @param idList
     * @return
     */
    List<AdvertisementPojo> getAdvertisementByIds(List<Integer> idList);

    /**
     * 根据id 获取广告信息
     *
     * @param id
     * @return
     */
    AdvertisementPojo getAdvertisementById(Integer id);

}
