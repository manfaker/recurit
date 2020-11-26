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
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileCommonUtils {
    private static DocumentService documentService;
    private static VaribaleUtils varibaleUtils;
    private static Logger logger = LoggerFactory.getLogger(FileCommonUtils.class);
    private static final String IMAGE = "image";

    static {
        if (EmptyUtils.isEmpty(documentService)) {
            documentService = SpringUtils.getBean(DocumentServiceImpl.class);
        }
        if (EmptyUtils.isEmpty(varibaleUtils)) {
            varibaleUtils = SpringUtils.getBean(VaribaleUtils.class);
        }
    }

    private static String getFilePath() {
        String url = varibaleUtils.getDocumentUrl();
        return url;
    }

    private static String getCurrDateStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(InformationConstant.SIMPLE_DATE_FORMAT);
        return simpleDateFormat.format(new Date());
    }

    public static String getRelativePath() {
        String relativepPath;
        try {
            relativepPath = ResourceUtils.getURL("classpath:").getPath();
            if (relativepPath.lastIndexOf(File.separator) != -1) {
                relativepPath = relativepPath + IMAGE;
            } else {
                relativepPath = relativepPath + File.separator + IMAGE;
            }
        } catch (FileNotFoundException e) {
            relativepPath = getFilePath();
            e.printStackTrace();
        }
        return relativepPath;
    }

    private static String addCurrTime(String path, String relativePath) {
        if (EmptyUtils.isEmpty(path)) {
            return OrdinaryConstant.IS_BLACK;
        }
        if (path.lastIndexOf(File.separator) != -1) {
            path = path + getCurrDateStr();
        } else {
            path = path + File.separator + relativePath;
        }
        return path;
    }

    /**
     * @param category 类型
     * @param file     文件
     * @param isImage  是否服务器文件
     * @return
     */
    public static DocumentPojo saveFile(String category, MultipartFile file, boolean isImage) {
        if (EmptyUtils.isEmpty(file)) {
            return null;
        }
        DocumentVO documentVO = new DocumentVO();
        String originalName = file.getOriginalFilename();
        String[] name = originalName.split("\\.");
        String fileName = name[NumberEnum.ZERO.getValue()];
        String suffix = name[NumberEnum.ONE.getValue()];
        String timeName = category + System.currentTimeMillis();
        documentVO.setOldName(fileName);
        documentVO.setDocumentName(timeName);
        documentVO.setCategory(category);
        documentVO.setSuffix(suffix);
        documentVO.setDocumentSize(file.getSize());
        String url = OrdinaryConstant.IS_BLACK;
        String relativePath = getCurrDateStr();
        if (!isImage) {
            url = getFilePath();
        } else {
            url = getRelativePath();
        }
        File filePath = new File(addCurrTime(url, relativePath));
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        if (!isImage) {
            documentVO.setUrl(filePath.getPath());
        } else {
            documentVO.setUrl(relativePath);
        }
        String currPath = filePath + File.separator + timeName + OrdinaryConstant.SYMBOL_5 + suffix;
        File currFile = new File(currPath);
        try {
            file.transferTo(currFile);
            return documentService.saveDocument(documentVO);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件不存在，或者异常");
            return null;
        }

    }

}
