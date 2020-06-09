package com.shenzhen.recurit.utils.excel;

import com.alibaba.fastjson.JSONArray;
import com.shenzhen.recurit.pojo.ExportsPojo;
import com.shenzhen.recurit.service.ExportsService;
import com.shenzhen.recurit.service.impl.ExportsServiceImpl;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.SpringUtils;
import com.shenzhen.recurit.vo.ResultVO;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class ExportUtils {

    private static ExportsService exportsService=null;

    private static void init(){
        if(EmptyUtils.isEmpty(exportsService)){
            exportsService = SpringUtils.getBean(ExportsServiceImpl.class);
        }
    }

    public static ResultVO exportExcel(JSONArray jsonArray, String instanceName){
        List<ExportsPojo> listExports = exportsService.getAllExportsByTableName(instanceName);
        return null;
    }

    public static<T> ResultVO exportExcel(List<T> listT, String instanceName){
        
        return null;
    }
}
