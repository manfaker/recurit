package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.ExportsPojo;

import java.util.List;

public interface ExportsService {

    List<ExportsPojo> getAllExportsByTableName(String tableName, String category);
}
