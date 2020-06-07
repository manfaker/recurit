package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.vo.NewsVO;

import java.util.List;

public interface NewsMapper {
    void saveNews(NewsVO newsVO);

    int updateNews(NewsVO newsVO);

    NewsVO getNewsById(int id);

    List<NewsVO> getAllNews();

    int deleteNewsById(int id);
}
