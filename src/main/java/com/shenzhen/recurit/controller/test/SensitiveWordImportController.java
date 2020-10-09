package com.shenzhen.recurit.controller.test;

import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.service.SensitiveWordService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Arrays;
import java.util.List;



@RestController
@RequestMapping(value = "sensitive")
@Api(tags = "敏感词汇导入")
public class SensitiveWordImportController {

    @javax.annotation.Resource
    private SensitiveWordService sensitiveWordService;

    public SensitiveWordImportController(SensitiveWordService sensitiveWordService) {
        this.sensitiveWordService = sensitiveWordService;
    }

    @PostMapping("word/import")
    public String importSensitiveWord() throws Exception{
        Resource resource = new ClassPathResource(OrdinaryConstant.IS_BLACK);
        String path = resource.getFile().getAbsolutePath()+File.separator+"sensitive"+File.separator+"sensitive_word.txt";
        File file = new File(path);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file)) ;
        String str = bufferedReader.readLine();
        List<String> listStr = Arrays.asList(str.split("&&"));
        sensitiveWordService.importSensitiveWord(listStr);
        System.out.println(listStr.size());
        return "导入成功";
    }

    @PostMapping("checkSensitiveWord")
    public ResultVO checkSensitiveWord(@RequestBody String jsonData){
        return sensitiveWordService.checkSensitiveWord(jsonData);
    }

    @PostMapping("excel/import")
    @ApiImplicitParam(value = "敏感词汇" , name =  "multipartFile" ,required = true)
    @ApiOperation("敏感词汇excel导入")
    public String importSensitiveWord(MultipartFile file, HttpServletRequest request) throws Exception{
        Resource resource = new ClassPathResource("");
        String path = resource.getFile().getAbsolutePath();
        File newFile = new File(path);
        if(!newFile.exists()){
            newFile.mkdirs();
        }
        String fileName = System.currentTimeMillis()+file.getOriginalFilename();
        File currFile = new File(newFile,fileName);
        FileUtils.copyInputStreamToFile(file.getInputStream(),currFile);
        InputStream inputStream = new FileInputStream(currFile);
        Workbook workbook = new XSSFWorkbook(inputStream);
        int sheetNumber = workbook.getNumberOfSheets();
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        return "导入成功";
    }
}
