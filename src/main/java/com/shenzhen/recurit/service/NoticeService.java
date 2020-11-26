package com.shenzhen.recurit.service;

import com.github.pagehelper.PageInfo;
import com.shenzhen.recurit.pojo.NoticePojo;
import com.shenzhen.recurit.vo.NoticeVO;

import java.util.List;

public interface NoticeService {
    /**
     * 新增公告信息
     *
     * @param noticeVO {@Link NoticeVO}
     * @return
     */
    NoticePojo addNotice(NoticeVO noticeVO);

    /**
     * 置顶公告信息
     *
     * @param id id
     * @return
     */
    int topNotice(int id);

    /**
     * 删除公告信息
     *
     * @param idList 公告主键
     * @return
     */
    int batchDeleteNotice(List<Integer> idList);

    /**
     * 修改公告信息
     *
     * @param noticeVO
     * @return
     */
    NoticePojo updateNotice(NoticeVO noticeVO);

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<NoticePojo> getNoticeByPage(int pageNum, int pageSize);

    /**
     * 根据id获取公告信息
     *
     * @param id
     * @return
     */
    NoticePojo getNotice(int id);

    /**
     * 获取最大得排序号
     *
     * @return
     */
    Integer getMaxOrder();

}
