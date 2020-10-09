package com.shenzhen.recurit.service.impl;

import com.shenzhen.recurit.dao.ExportsMapper;
import com.shenzhen.recurit.pojo.ExportsPojo;
import com.shenzhen.recurit.service.ExportsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExportsServiceImpl implements ExportsService {

    @Resource
    private ExportsMapper exportsMapper;

    @Override
    public List<ExportsPojo> getAllExportsByTableName(String instanceName) {
        return exportsMapper.getAllExportsByTableName(instanceName);
    }
}
