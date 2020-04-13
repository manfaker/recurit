package com.shenzhen.recurit.utils.word;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.utils.EmptyUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class WordUtil {

    public static String path=null;

    static{
        if(EmptyUtils.isEmpty(path)){
            try {
                path =ResourceUtils.getURL("classpath:").getPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public static void downloadResume(HttpServletResponse response){
        File resumeFiel = null;
        InputStream inputStream=null;
        OutputStream outputStream=null;
        // 设置模板目录
        try {
            String docName = InformationConstant.TEMPLATE+System.currentTimeMillis()+InformationConstant.RESUME_DOCX;
            String docPath = path+ File.separator+InformationConstant.TEMPLATE +File.separator+docName;
            createResume(docPath);
            resumeFiel  = new File(docPath);
            inputStream = new BufferedInputStream(new FileInputStream(resumeFiel));
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="+resumeFiel.getName());
            response.addHeader("Content-Length", "" + resumeFiel.length());
            outputStream= new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close( inputStream, outputStream);
            resumeFiel.delete();
        }
        // 设置字符集
    }

    public static void createResume(String docPath){
        Template template =null;
        Writer out = null;
        try {
            // 创建模板配置对象Configuration1
            Configuration configuration = new Configuration(Configuration.getVersion());
            String templatePath = path+ File.separator+ InformationConstant.TEMPLATE +File.separator;

            // 设置模板目录
            configuration
                    .setDirectoryForTemplateLoading(new File(templatePath));
            // 设置字符集
            configuration.setDefaultEncoding(InformationConstant.UTF_8);
            template = configuration.getTemplate("test.ftl");
            // 创建输出流
            out = new FileWriter(docPath);
            // 生成静态页面
            template.process(null, out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close(template,out);
        }
    }

    private static void close(Template template, Writer out){
        try {
            if(EmptyUtils.isNotEmpty(template)){
                template.getAutoFlush();
            }
            if(EmptyUtils.isNotEmpty(out)){
                out.flush();
                out.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void close(InputStream inputStream,OutputStream outputStream){
        try {
            if(EmptyUtils.isNotEmpty(outputStream)){
                outputStream.flush();
                outputStream.close();
            }
            if(EmptyUtils.isNotEmpty(inputStream)){
                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
