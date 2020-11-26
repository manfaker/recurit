package com.shenzhen.recurit.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shenzhen.recurit.dao.NoticeMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.NoticePojo;
import com.shenzhen.recurit.service.NoticeService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.NewsVO;
import com.shenzhen.recurit.vo.NoticeVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Resource
    private NoticeMapper noticeMapper;

    @Override
    public NoticePojo addNotice(NoticeVO noticeVO) {
        setNoticeInfo(noticeVO, false);
        noticeVO.setNoticeOrder(creteMaxOrder());
        noticeMapper.addNotice(noticeVO);
        return getNotice(noticeVO.getId());
    }

    private void setNoticeInfo(NoticeVO noticeVO, boolean isUpdate){
        UserVO user = ThreadLocalUtils.getUser();
        if(!isUpdate){
            noticeVO.setStatus(NumberEnum.ONE.getValue());
            noticeVO.setCreater(user.getUserName());
            noticeVO.setCreateDate(new Date());
        }
        noticeVO.setUpdater(user.getUserName());
        noticeVO.setUpdateDate(new Date());
    }

    /**
     * 获取当前最大排序值加一
     *
     * @return
     */
    private int creteMaxOrder() {
        int maxOrder = getMaxOrder();
        return maxOrder + NumberEnum.ONE.getValue();
    }

    @Override
    public int topNotice(int id) {
        NoticeVO noticeVO = new NoticeVO();
        setNoticeInfo(noticeVO, true);
        noticeVO.setNoticeOrder(creteMaxOrder());
        noticeVO.setId(id);
        return noticeMapper.updateNotice(noticeVO);
    }

    @Override
    public int batchDeleteNotice(List<Integer> idList) {
        return noticeMapper.batchDeleteNotice(idList);
    }

    @Override
    public NoticePojo updateNotice(NoticeVO noticeVO) {
        setNoticeInfo(noticeVO, true);
        noticeMapper.updateNotice(noticeVO);
        return getNotice(noticeVO.getId());
    }

    @Override
    public PageInfo<NoticePojo> getNoticeByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<NoticePojo> noticeList = noticeMapper.getAllNotice();
        PageInfo<NoticePojo> pageInfo = new PageInfo<>(noticeList);
        return pageInfo;
    }

    @Override
    public NoticePojo getNotice(int id) {
        return noticeMapper.getNotice(id);
    }

    @Override
    public Integer getMaxOrder() {
        Integer maxOrder = noticeMapper.getMaxOrder();
        return EmptyUtils.isNotEmpty(maxOrder) ? maxOrder : NumberEnum.ZERO.getValue();
    }
}
