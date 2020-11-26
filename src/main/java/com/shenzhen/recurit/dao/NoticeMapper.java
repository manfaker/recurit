package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.NoticePojo;
import com.shenzhen.recurit.vo.NoticeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeMapper {
    /**
     * 新增公告信息
     *
     * @param noticeVO {@Link NoticeVO}
     * @return
     */
    void addNotice(NoticeVO noticeVO);

    /**
     * 删除公告信息
     *
     * @param idList 公告主键
     * @return
     */
    int batchDeleteNotice(@Param("idList") List<Integer> idList);

    /**
     * 修改公告信息
     *
     * @param noticeVO
     * @return
     */
    int updateNotice(NoticeVO noticeVO);

    /**
     * 获取所有得公告
     *
     * @return
     */
    List<NoticePojo> getAllNotice();

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
