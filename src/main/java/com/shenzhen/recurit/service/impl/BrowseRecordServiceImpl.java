package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.BrowseRecordMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.PositionPojo;
import com.shenzhen.recurit.service.BrowseRecordService;
import com.shenzhen.recurit.service.PositionService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.BrowseRecordVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class BrowseRecordServiceImpl implements BrowseRecordService {
    @Resource
    private BrowseRecordMapper browseRecordMapper;
    @Resource
    private PositionService positionService;
    @Override
    public BrowseRecordVO saveBrowseRecord(BrowseRecordVO browseRecordVO) {
        if(EmptyUtils.isNotEmpty(browseRecordVO)&&browseRecordVO.getPositionId()> NumberEnum.ZERO.getValue()){
            PositionPojo positionPojo = positionService.getByPositionId(browseRecordVO.getPositionId());
            if(EmptyUtils.isNotEmpty(positionPojo)){
                browseRecordVO.setCompanyCode(positionPojo.getCompanyCode());
                setBrowseRecordInfo(browseRecordVO);
                browseRecordMapper.saveBrowseRecord(browseRecordVO);
                return browseRecordVO;
            }
        }
        return browseRecordVO;
    }

    private void setBrowseRecordInfo(BrowseRecordVO browseRecordVO){
        if(EmptyUtils.isEmpty(browseRecordVO)){
            return;
        }
        UserVO user = ThreadLocalUtils.getUser();
        browseRecordVO.setUserCode(user.getUserCode());
        browseRecordVO.setCreater(user.getUserName());
        browseRecordVO.setCreateDate(new Date());
    }
}
