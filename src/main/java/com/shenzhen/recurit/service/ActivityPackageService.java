package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.ActivityPackagePojo;
import com.shenzhen.recurit.vo.ActivityPackageVO;

import java.util.List;

public interface ActivityPackageService {
    ActivityPackagePojo saveActivityPackage(ActivityPackageVO activityPackageVO);

    List<ActivityPackagePojo> getAllActivityPackage();

    ActivityPackagePojo getActivityPackageById(int id);
}
