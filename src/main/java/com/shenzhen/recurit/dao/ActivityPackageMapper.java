package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.ActivityPackagePojo;
import com.shenzhen.recurit.vo.ActivityPackageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityPackageMapper {

    void saveActivityPackage(ActivityPackageVO activityPackageVO);

    List<ActivityPackagePojo> getAllActivityPackage();

    ActivityPackagePojo getActivityPackageById(int id);

    List<ActivityPackagePojo> getAllActivityPackageByIds(@Param("ids") List<Integer> ids);

    int updateActivityPackage(ActivityPackageVO activityPackageVO);
}
