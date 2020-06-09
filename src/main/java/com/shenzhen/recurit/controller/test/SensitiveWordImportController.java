package com.shenzhen.recurit.controller.test;

import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.service.SensitiveWordService;
import com.shenzhen.recurit.vo.ResultVO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;



@RestController
@RequestMapping(value = "sensitive")
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
}
