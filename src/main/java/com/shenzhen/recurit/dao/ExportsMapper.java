package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.ExportsPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExportsMapper {

    /**
     * 获取当前文本的导入导出所需的字段
     * @param instanceName 类型名
     * @param category  导入/导出
     * @return
     */
    List<ExportsPojo> getAllExportsByTableName(@Param("instanceName") String instanceName, @Param("category") String category);
}
