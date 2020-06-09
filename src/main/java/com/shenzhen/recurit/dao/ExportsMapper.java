package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.pojo.ExportsPojo;

import java.util.List;

public interface ExportsMapper {

    List<ExportsPojo> getAllExportsByTableName(String tableName);
}
