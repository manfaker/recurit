package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.ActivityPackagePojo;
import com.shenzhen.recurit.vo.ActivityPackageVO;

import java.util.List;

public interface ActivityPackageService {
    ActivityPackagePojo saveActivityPackage(ActivityPackageVO activityPackageVO);

    List<ActivityPackagePojo> getAllActivityPackage();

    ActivityPackagePojo getActivityPackageById(int id);

    /**
     * 根据ids获取所有套餐
     * @param ids
     * @return
     */
    List<ActivityPackagePojo> getAllActivityPackageByIds(List<Integer> ids);
}
