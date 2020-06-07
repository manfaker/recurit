package com.shenzhen.recurit.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shenzhen.recurit.dao.NewsMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.PositionPojo;
import com.shenzhen.recurit.service.NewsService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.EncryptBase64Utils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.NewsVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    private NewsMapper newsMapper;

    @Override
    public NewsVO saveNews(NewsVO newsVO) {
        setNewsInfo(newsVO,true);
        newsMapper.saveNews(newsVO);
        return getNewsById(newsVO.getId());
    }

    private void setNewsInfo(NewsVO newsVO,boolean flag){
        UserVO user = ThreadLocalUtils.getUser();
        if(flag){
            newsVO.setCreateDate(new Date());
            newsVO.setCreater(user.getUserName());
        }
        newsVO.setUpdateDate(new Date());
        newsVO.setUpdater(user.getUserName());
    }

    @Override
    public int updateNews(NewsVO newsVO) {
        setNewsInfo(newsVO,false);
        return newsMapper.updateNews(newsVO);
    }

    @Override
    public NewsVO getNewsById(int id) {
        return newsMapper.getNewsById(id);
    }

    @Override
    public PageInfo<NewsVO> getAllNews(Integer pageSize,Integer pageNo) {
        if(EmptyUtils.isEmpty(pageSize)||EmptyUtils.isEmpty(pageNo)||
                pageNo== NumberEnum.ZERO.getValue()||pageSize== NumberEnum.ZERO.getValue()){
            pageNo=NumberEnum.ONE.getValue();
            pageSize=NumberEnum.TEN.getValue();
        }
        PageHelper.startPage(pageNo,pageSize);
        List<NewsVO> listNews = newsMapper.getAllNews();
        PageInfo<NewsVO> pageInfo = new PageInfo<>(listNews);
        return pageInfo;
    }

    @Override
    public int deleteNewsById(int id) {
        return newsMapper.deleteNewsById(id);
    }

    public static void main(String[] args) {
        System.out.println(EncryptBase64Utils.decryptBASE64("Uk9PVHJvb3QxMjM="));
    }
}
