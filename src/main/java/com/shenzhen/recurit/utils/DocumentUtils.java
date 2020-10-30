package com.shenzhen.recurit.utils;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件工具类
 */
public class DocumentUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentUtils.class);
    private static String BASE_PATH;

    static {
        try {
            BASE_PATH = ResourceUtils.getURL("classpath:").getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 仅限于非图片视频类的文件下载
     *
     * @param filePath
     * @param fileName
     * @param suffix
     */
    public static void downloadFile(String filePath, String fileName, String suffix) {
        HttpServletResponse response = CommonUtils.getResponse();
        String documentName = fileName + OrdinaryConstant.SYMBOL_5 + suffix;
        resetResponse(response, documentName);
        String documentPath = BASE_PATH + File.separator + filePath;
        File documentFile = new File(documentPath, documentName);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(documentFile);
            outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024 * 1024 * 5];
            int len;
            while ((len = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
        } catch (FileNotFoundException e) {
            LOGGER.error("路径为{}的{}文件不存在", documentPath, documentName);
        } catch (IOException e) {
            LOGGER.error("{}文件异常", documentName);
        } finally {
            close(outputStream);
            close(inputStream);
        }

    }

    /**
     * 关闭输入流
     *
     * @param inputStream
     */
    private static void close(InputStream inputStream) {
        try {
            if (EmptyUtils.isNotEmpty(inputStream)) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输出流
     *
     * @param outputStream
     */
    private static void close(OutputStream outputStream) {
        try {
            if (EmptyUtils.isNotEmpty(outputStream)) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void resetResponse(HttpServletResponse response, String fileName) {
        try {
            response.reset();//重置 响应头
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-download");//告知浏览器下载文件，而不是直接打开，浏览器默认为打开
            response.setHeader("content-type", "text/html;charset=UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(fileName, InformationConstant.UTF_8) + "\"");//下载文件的名称
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("{}文件名转码失败。", fileName);
        }
    }

}
