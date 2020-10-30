package com.shenzhen.recurit.service;

import com.shenzhen.recurit.pojo.FilePojo;

public interface ExcelService {

    /**
     * 下载模板
     * @param filePojo
     */
    void downloadTemplate(FilePojo filePojo);
}
