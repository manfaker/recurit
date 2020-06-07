package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.NewsMapper;
import com.shenzhen.recurit.service.NewsService;
import com.shenzhen.recurit.utils.EncryptBase64Utils;
import com.shenzhen.recurit.vo.NewsVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    private NewsMapper newsMapper;

    @Override
    public NewsVO saveNews(NewsVO newsVO) {
        newsMapper.saveNews(newsVO);
        return getNewsById(newsVO.getId());
    }

    @Override
    public int updateNews(NewsVO newsVO) {
        return newsMapper.updateNews(newsVO);
    }

    @Override
    public NewsVO getNewsById(int id) {
        return newsMapper.getNewsById(id);
    }

    @Override
    public List<NewsVO> getAllNews() {
        return newsMapper.getAllNews();
    }

    @Override
    public int deleteNewsById(int id) {
        return newsMapper.deleteNewsById(id);
    }

    public static void main(String[] args) {
        System.out.println(EncryptBase64Utils.decryptBASE64("Uk9PVHJvb3QxMjM="));
    }
}
