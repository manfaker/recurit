package com.shenzhen.recurit.utils;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.pojo.DocumentPojo;
import com.shenzhen.recurit.service.DocumentService;
import com.shenzhen.recurit.service.impl.DocumentServiceImpl;
import com.shenzhen.recurit.vo.DocumentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
    private static DocumentService documentService;
    private static VaribaleUtils varibaleUtils;
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    static{
        if(EmptyUtils.isEmpty(documentService)){
            documentService = SpringUtils.getBean(DocumentServiceImpl.class);
        }
        if(EmptyUtils.isEmpty(varibaleUtils)){
            varibaleUtils = SpringUtils.getBean(VaribaleUtils.class);
        }
    }

    private static String getFilePath(){
        String url = varibaleUtils.getDocumentUrl();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(InformationConstant.SIMPLE_DATE_FORMAT);
        String currDateStr =simpleDateFormat.format(new Date());
        url = url + File.separator+currDateStr;
        return url;
    }

    public static DocumentPojo saveFile(String category, MultipartFile file){
        if(EmptyUtils.isEmpty(file)){
            return null;
        }
        DocumentVO documentVO = new DocumentVO();
        String originalName = file.getOriginalFilename();
        String[] name = originalName.split("\\.");
        String fileName = name[NumberEnum.ZERO.getValue()];
        String suffix = name[NumberEnum.ONE.getValue()];
        String url = getFilePath();
        String timeName=category+System.currentTimeMillis();
        documentVO.setOldName(fileName);
        documentVO.setDocumentName(timeName);
        documentVO.setUrl(url);
        documentVO.setCategory(category);
        documentVO.setSuffix(suffix);
        documentVO.setDocumentSize(file.getSize());
        File filePath= new File(url);
        if(!filePath.exists()){
            filePath.mkdirs();
        }
        String currPath = filePath+File.separator+timeName+OrdinaryConstant.SYMBOL_5+suffix;
        File currFile = new File(currPath);
        try {
            file.transferTo(currFile);
            return documentService.saveDocument(documentVO);
        } catch (IOException e) {
            logger.error("文件不存在，或者异常");
            return null;
        }

    }

}
