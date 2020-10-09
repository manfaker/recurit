package com.shenzhen.recurit.utils.excel;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.ExportsPojo;
import com.shenzhen.recurit.pojo.ImportResultPojo;
import com.shenzhen.recurit.service.ExportsService;
import com.shenzhen.recurit.service.impl.ExportsServiceImpl;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.SpringUtils;
import com.shenzhen.recurit.utils.word.WordUtil;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImportUtils {

    private static Logger logger = LoggerFactory.getLogger(ImportUtils.class);

    private static ExportsService exportsService=null;

    static{
        if(EmptyUtils.isEmpty(exportsService)){
            exportsService= SpringUtils.getBean(ExportsServiceImpl.class);
        }
    }

    /**
     * 获取单个sheet单元的导入data信息
     * @param multipartFile
     * @param t
     * @param <T>
     * @param instanceName 实例名
     * @return
     */
    public static<T> ImportResultPojo getImportInfos(MultipartFile multipartFile,T t,String instanceName){
        ImportResultPojo<T> importPojo = new ImportResultPojo<>();
        List<T> listT = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(multipartFile)){
            File currFile = exChangeToFile(multipartFile);
            if(EmptyUtils.isNotEmpty(currFile)){
                getAllData(listT,currFile,t,false,instanceName,importPojo);
                importPojo.setListT(listT);
                importPojo.setTemplateFile(currFile);
            }
        }
        return importPojo;
    }

    private static<T> List<T> getAllImportInfos(MultipartFile multipartFile,T t,String instanceName){
        List<T> listT = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(multipartFile)){
            File currFile = exChangeToFile(multipartFile);
            if(EmptyUtils.isNotEmpty(currFile)){
                //getAllData(listT,currFile,t,false,instanceName);
            }
        }
        return listT;
    }

    /**
     *
     * @param listT  返回数据
     * @param file   导入文件
     * @param t    bean对象
     * @param isAllSheet 是否遍历所有的sheet false 只获取sheet0
     * @param <T>
     * @return
     */
    private static<T> List<T> getAllData(List<T> listT,File file,T t,boolean isAllSheet,String instanceName,ImportResultPojo importPojo){
        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream  = new FileInputStream(file);
            String suffix = file.getName().substring(file.getName().indexOf(OrdinaryConstant.SYMBOL_5)+ NumberEnum.ONE.getValue());
            if(OrdinaryConstant.SYMBOL_7.equals(suffix)){
                workbook = new XSSFWorkbook(inputStream);
            }else{
                workbook = new HSSFWorkbook(inputStream);
            }
            getAllData(listT,workbook,instanceName,t,importPojo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listT;
    }


    private  static<T> List<T> getAllData(List<T> listT,Workbook workbook,String instanceName,T t,ImportResultPojo importPojo){
        if(EmptyUtils.isNotEmpty(workbook)){
            Sheet sheet = workbook.getSheetAt(NumberEnum.ZERO.getValue());
            List<ExportsPojo> listExports = exportsService.getAllExportsByTableName(instanceName);
            if(EmptyUtils.isEmpty(listExports)){
                return listT;
            }
            Row firstRow  = sheet.getRow(NumberEnum.ZERO.getValue());
            if(EmptyUtils.isEmpty(firstRow)){
                logger.error("导入excel 首列标题信息不能为空");
                throw new RuntimeException();
            }
            int lastCellNum = firstRow.getLastCellNum();
            Map<Integer,ExportsPojo> exportMap =  new HashMap<>();
            getFirstInfo(exportMap,firstRow,lastCellNum,listExports);
            importPojo.setExportMap(exportMap);
            int lastRowNum = sheet.getLastRowNum();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OrdinaryConstant.YYYY_MM_DD_H24_MM_SS);
            for(int index=NumberEnum.ONE.getValue();index<=lastRowNum;index++){
                Row row = sheet.getRow(index);
                if(EmptyUtils.isNotEmpty(row)){
                    Class<?> clazz = t.getClass();
                    T newT;
                    try {
                        newT= (T)clazz.newInstance();
                        Field[] fields = clazz.getDeclaredFields();
                        boolean flag = false;
                        for(Field field : fields){
                            String name = field.getName();
                            for(Map.Entry<Integer,ExportsPojo> entry : exportMap.entrySet()){
                                if(name.equals(entry.getValue().getColumnField())){
                                    flag = true;
                                    Cell cell = row.getCell(entry.getKey());
                                    setNewInstanceObj(cell,field,newT,simpleDateFormat);
                                    break;
                                }
                            }
                        }
                        if(flag){
                            listT.add(newT);
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return listT;
    }

    /**
     * 将单元格中的值放入实体类中
     * @param cell
     * @param field
     * @param newT
     * @param simpleDateFormat
     * @param <T>
     */
    private static<T> void setNewInstanceObj(Cell cell,Field field,T newT,SimpleDateFormat simpleDateFormat){
        String cellValue;
        if(EmptyUtils.isNotEmpty(cell)){
            field.setAccessible(true);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            try {
                if(field.getType()==int.class || field.getType()==Integer.class){
                    cellValue=EmptyUtils.isNotEmpty(cell.getStringCellValue())?cell.getStringCellValue():OrdinaryConstant.IS_BLACK;
                    field.set(newT,Integer.valueOf(cellValue));
                }else if(field.getType()==double.class || field.getType()==Double.class){
                    cellValue=EmptyUtils.isNotEmpty(cell.getStringCellValue())?cell.getStringCellValue():OrdinaryConstant.IS_BLACK;
                    field.set(newT,Double.valueOf(cellValue));
                }else if( field.getType()== Date.class){
                    cellValue=EmptyUtils.isNotEmpty(cell.getStringCellValue())?cell.getStringCellValue():OrdinaryConstant.IS_BLACK;
                    field.set(newT,simpleDateFormat.format(cellValue));
                }else{
                    cellValue=EmptyUtils.isNotEmpty(cell.getStringCellValue())?cell.getStringCellValue():OrdinaryConstant.IS_BLACK;
                    field.set(newT,cellValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void getFirstInfo(Map<Integer,ExportsPojo> exportMap,Row firstRow,int lastCellNum,List<ExportsPojo> listExports){
        for(int i =NumberEnum.ZERO.getValue();i<lastCellNum;i++){
            Cell cell = firstRow.getCell(i);
            if(EmptyUtils.isNotEmpty(cell)){
                String value =cell.getStringCellValue();
                if(EmptyUtils.isNotEmpty(value)){
                    for(ExportsPojo exportsPojo:listExports){
                        if(value.equals(exportsPojo.getColumnName())){
                            exportMap.put(i,exportsPojo);
                            break;
                        }
                    }
                }
            }
        }
    }






    /**
     * 二进制文件转 io 文件
     * @param multipartFile
     * @return  普通文件
     */
    private static File exChangeToFile(MultipartFile multipartFile){
        if(EmptyUtils.isNotEmpty(multipartFile)){
            String originalFilename = multipartFile.getOriginalFilename();
            String fileName = originalFilename.substring(NumberEnum.ZERO.getValue(),originalFilename.indexOf(OrdinaryConstant.SYMBOL_5))+System.currentTimeMillis()+originalFilename.substring(originalFilename.indexOf(OrdinaryConstant.SYMBOL_5));
            String path = WordUtil.getResourcePath()+File.separator+ InformationConstant.IMPORT;
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            File currFile = new File(file,fileName);
            try {
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),currFile);
            } catch (IOException e) {
                logger.info("文件转换异常!!!!!");
                e.printStackTrace();
            }
            return currFile;
        }
        return null;
    }


}
