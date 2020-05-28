package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.ActivityPackageMapper;
import com.shenzhen.recurit.pojo.ActivityPackagePojo;
import com.shenzhen.recurit.service.ActivityPackageService;
import com.shenzhen.recurit.utils.ThreadLocalUtils;
import com.shenzhen.recurit.vo.ActivityPackageVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ActivityPackageServiceImpl implements ActivityPackageService {

    @Resource
    private ActivityPackageMapper activityPackageMapper;

    @Override
    public ActivityPackagePojo saveActivityPackage(ActivityPackageVO activityPackageVO) {
        setActivityPackageVO(activityPackageVO,true);
        activityPackageMapper.saveActivityPackage(activityPackageVO);
        return getActivityPackageById(activityPackageVO.getId());
    }

    private void setActivityPackageVO(ActivityPackageVO activityPackageVO,boolean flag){
        UserVO user = ThreadLocalUtils.getUser();
        if(flag){
            activityPackageVO.setCreateDate(new Date());
            activityPackageVO.setCreater(user.getUserName());
        }
        activityPackageVO.setUpdateDate(new Date());
        activityPackageVO.setUpdater(user.getUserName());
    }

    @Override
    public List<ActivityPackagePojo> getAllActivityPackage() {
        return activityPackageMapper.getAllActivityPackage();
    }

    @Override
    public ActivityPackagePojo getActivityPackageById(int id) {
        return activityPackageMapper.getActivityPackageById(id);
    }
}
