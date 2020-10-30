package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.pojo.FilePojo;
import com.shenzhen.recurit.service.ExcelService;
import com.shenzhen.recurit.utils.DocumentUtils;
import org.springframework.stereotype.Service;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public void downloadTemplate(FilePojo filePojo) {
        DocumentUtils.downloadFile(filePojo.getPelativePath(),filePojo.getName(), filePojo.getSuffix());
    }
}
