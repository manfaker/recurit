package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.vo.BrowseRecordVO;

public interface BrowseRecordMapper {
    /**
     * 保存浏览记录
     * @param browseRecordVO
     */
    void saveBrowseRecord(BrowseRecordVO browseRecordVO);
}
