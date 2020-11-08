package com.shenzhen.recurit.utils.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.ExportsPojo;
import com.shenzhen.recurit.service.ExportsService;
import com.shenzhen.recurit.service.impl.ExportsServiceImpl;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.SpringUtils;
import com.shenzhen.recurit.utils.StringFormatUtils;
import com.shenzhen.recurit.vo.ResultVO;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mockito.internal.matchers.Or;
import org.springframework.http.MediaType;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class ExportUtils {

    private static ExportsService exportsService = null;

    private static void init() {
        if (EmptyUtils.isEmpty(exportsService)) {
            exportsService = SpringUtils.getBean(ExportsServiceImpl.class);
        }
    }

    public static ResultVO exportExcel(JSONArray jsonArray, HttpServletResponse response, String instanceName, String excelName) {
        init();
        List<ExportsPojo> listExports = exportsService.getAllExportsByTableName(instanceName, InformationConstant.EXPORT);
        if (EmptyUtils.isEmpty(listExports) || listExports.isEmpty()) {
            return ResultVO.error("导出失败");
        }
        Workbook workbook = new XSSFWorkbook();
        String sheetName = listExports.get(NumberEnum.ZERO.getValue()).getSheetName();
        Sheet sheet = null;
        if (EmptyUtils.isNotEmpty(sheetName)) {
            sheet = workbook.createSheet(sheetName);
        } else {
            sheet = workbook.createSheet();
        }
        //创建标题列
        Row titleRow = sheet.createRow(NumberEnum.ZERO.getValue());
        for (int index = NumberEnum.ZERO.getValue(); index < listExports.size(); index++) {
            ExportsPojo exportsPojo = listExports.get(index);
            Cell cell = titleRow.createCell(exportsPojo.getGradation());
            setCellType(cell);
            cell.setCellValue(exportsPojo.getColumnName());
        }
        if (EmptyUtils.isNotEmpty(jsonArray)) {
            for (int index = NumberEnum.ZERO.getValue(); index < jsonArray.size(); index++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                Row row = sheet.createRow(index + NumberEnum.ONE.getValue());
                for (int num = NumberEnum.ZERO.getValue(); num < listExports.size(); num++) {
                    String columnValue = OrdinaryConstant.IS_BLACK;
                    ExportsPojo exportsPojo = listExports.get(num);
                    String columnName = exportsPojo.getColumnField();
                    Cell cell = row.createCell(exportsPojo.getGradation());
                    setCellType(cell);
                    if (EmptyUtils.isNotEmpty(columnName) && jsonObject.containsKey(columnName)) {
                        columnValue = jsonObject.getString(columnName);
                        if (EmptyUtils.isNotEmpty(columnValue)) {
                            cell.setCellValue(columnValue);
                        }
                    }
                }
            }
        }
        buildExcelDocument(excelName, workbook, response);
        return ResultVO.success();
    }

    public static void buildExcelDocument(String fileName, Workbook wb, HttpServletResponse response) {
        try {
            if (EmptyUtils.isNotEmpty(fileName) && fileName.lastIndexOf(OrdinaryConstant.SYMBOL_7) == -1) {
                fileName = StringFormatUtils.format("%s%s%s", fileName, OrdinaryConstant.SYMBOL_5, OrdinaryConstant.SYMBOL_7);
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, InformationConstant.UTF_8));
            response.flushBuffer();
            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            close(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void close(OutputStream outputStream) {
        try {
            if (EmptyUtils.isNotEmpty(outputStream)) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void setCellType(Cell cell) {
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
    }

    public static <T> ResultVO exportExcel(List<T> listT, String instanceName) {

        return null;
    }

}
