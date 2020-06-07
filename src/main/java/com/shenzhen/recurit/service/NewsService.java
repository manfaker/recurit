package com.shenzhen.recurit.service;

import com.github.pagehelper.PageInfo;
import com.shenzhen.recurit.vo.NewsVO;

import java.util.List;

public interface NewsService {

    /**
     * 新增新闻信息
     * @param newsVO
     * @return
     */
    NewsVO saveNews(NewsVO newsVO);

    /**
     * 根据新闻id修改新闻信息
     * @param newsVO
     * @return
     */
    int updateNews(NewsVO newsVO);

    /**
     * 根据新闻id查询新闻信息
     * @param id
     * @return
     */
    NewsVO getNewsById(int id);


    /**
     * 获取所有的新闻信息
     * @return
     */
    PageInfo<NewsVO> getAllNews(Integer pageSize,Integer pageNo);

    /**
     * 根据id删除新闻信息
     * @param id
     * @return
     */
    int deleteNewsById(int id);
}
