package com.shenzhen.recurit.utils.word;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.pojo.*;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.vo.DictionaryVO;
import com.shenzhen.recurit.vo.UserVO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public static void downloadResume(HttpServletResponse response, UserPojo userPojo){
        File resumeFiel = null;
        InputStream inputStream=null;
        OutputStream outputStream=null;
        // 设置模板目录
        try {
            String docName = InformationConstant.TEMPLATE+System.currentTimeMillis()+InformationConstant.RESUME_DOCX;
            String docPath = path+ File.separator+InformationConstant.TEMPLATE +File.separator+docName;
            createResume(docPath,userPojo);
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

    public static void createResume(String docPath,UserPojo userPojo){
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
            template = configuration.getTemplate(InformationConstant.RESUME_FTL);
            // 创建输出流
            out = new FileWriter(docPath);
            Map<String,Object> dataModel= new HashMap<>();
            setDataModel(dataModel,userPojo);
            // 生成静态页面
            template.process(dataModel, out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close(template,out);
        }
    }
    private static void setDataModel(Map<String,Object> dataModel, UserPojo userPojo){
        String userName = EmptyUtils.isNotEmpty(userPojo.getUserName())?userPojo.getUserName(): OrdinaryConstant.IS_BLACK;
        String age = userPojo.getAge()==0?OrdinaryConstant.IS_BLACK:String.valueOf(userPojo.getAge());
        String phone = OrdinaryConstant.IS_BLACK;
        String email = OrdinaryConstant.IS_BLACK;
        String city = OrdinaryConstant.IS_BLACK;
        String workingLife = OrdinaryConstant.IS_BLACK;
        String expirePosition = OrdinaryConstant.IS_BLACK;
        String salary = OrdinaryConstant.IS_BLACK;
        String jobStatus = OrdinaryConstant.IS_BLACK;
        String introduce = OrdinaryConstant.IS_BLACK;
        ResumePojo resumePojo = userPojo.getResumePojo();
        if(EmptyUtils.isNotEmpty(resumePojo)){
            phone=EmptyUtils.isNotEmpty(resumePojo.getPhone())?resumePojo.getPhone(): OrdinaryConstant.IS_BLACK;
            email=EmptyUtils.isNotEmpty(resumePojo.getEmail())?resumePojo.getEmail(): OrdinaryConstant.IS_BLACK;
            city=EmptyUtils.isNotEmpty(resumePojo.getCity())?resumePojo.getCity(): OrdinaryConstant.IS_BLACK;
            workingLife=resumePojo.getWorkingLife()==0?"0":resumePojo.getWorkingLife()%10==0?String.valueOf(resumePojo.getWorkingLife()/10):
                    String.valueOf(resumePojo.getWorkingLife()/10)+String.valueOf(resumePojo.getWorkingLife()%10);
            jobStatus=EmptyUtils.isNotEmpty(resumePojo.getJobStatus())?resumePojo.getJobStatus(): OrdinaryConstant.IS_BLACK;
            introduce=EmptyUtils.isNotEmpty(resumePojo.getIntroduce())?resumePojo.getIntroduce(): OrdinaryConstant.IS_BLACK;
        }
        List<DesiredPositionPojo> listDesiredPosition = userPojo.getListDesiredPosition();
        if(EmptyUtils.isNotEmpty(listDesiredPosition)&&listDesiredPosition.size()>0){
            DesiredPositionPojo desiredPositionPojo = listDesiredPosition.get(0);
            expirePosition=EmptyUtils.isNotEmpty(desiredPositionPojo.getDesiredPosition())?desiredPositionPojo.getDesiredPosition():OrdinaryConstant.IS_BLACK;
            DictionaryVO salaryDict = desiredPositionPojo.getSalaryDict();
            if(EmptyUtils.isNotEmpty(salaryDict)){
                salary=EmptyUtils.isNotEmpty(salaryDict.getDictName())?salaryDict.getDictName():OrdinaryConstant.IS_BLACK;
            }
        }
        dataModel.put("userName",userName);
        dataModel.put("age",age);
        dataModel.put("phone",phone);
        dataModel.put("email",email);
        dataModel.put("city",city);
        dataModel.put("workingLife",workingLife);
        dataModel.put("expirePosition",expirePosition);
        dataModel.put("salary",salary);
        dataModel.put("jobStatus",jobStatus);
        dataModel.put("introduce",introduce);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM");
        List<EducationExperiencesPojo> listEducationExperience = userPojo.getListEducationExperience();
        if(EmptyUtils.isNotEmpty(listEducationExperience)&&listEducationExperience.size()>0){
            listEducationExperience.forEach(educationExperiencesPojo -> {
                if(EmptyUtils.isNotEmpty(educationExperiencesPojo.getStartTime())&&EmptyUtils.isNotEmpty(educationExperiencesPojo.getEndTime())){
                    String stuTime = simpleDateFormat.format(educationExperiencesPojo.getStartTime())+"-"+simpleDateFormat.format(educationExperiencesPojo.getEndTime());
                    educationExperiencesPojo.setStuTime(stuTime);
                }else{
                    educationExperiencesPojo.setStuTime(OrdinaryConstant.IS_BLACK);
                }
                educationExperiencesPojo.setSchoolName(EmptyUtils.isNotEmpty(educationExperiencesPojo.getSchoolName())?educationExperiencesPojo.getSchoolName(): OrdinaryConstant.IS_BLACK);
                educationExperiencesPojo.setMajor(EmptyUtils.isNotEmpty(educationExperiencesPojo.getMajor())?educationExperiencesPojo.getMajor(): OrdinaryConstant.IS_BLACK);
            });
        }else{
            listEducationExperience= new ArrayList<>();
            EducationExperiencesPojo educationExperiencesPojo= new EducationExperiencesPojo();
            educationExperiencesPojo.setStuTime(OrdinaryConstant.IS_BLACK);
            educationExperiencesPojo.setSchoolName(OrdinaryConstant.IS_BLACK);
            educationExperiencesPojo.setMajor(OrdinaryConstant.IS_BLACK);
            listEducationExperience.add(educationExperiencesPojo);
        }
        dataModel.put("educationList",listEducationExperience);
        List<JobExperiencePojo> listJobExperiences = userPojo.getListJobExperiences();
        if(EmptyUtils.isNotEmpty(listJobExperiences)&&listJobExperiences.size()>0){
            listJobExperiences.forEach(jobExperiencePojo -> {
                if(EmptyUtils.isNotEmpty(jobExperiencePojo.getStartTime())&&EmptyUtils.isNotEmpty(jobExperiencePojo.getEndTime())){
                    String projectTime = simpleDateFormat.format(jobExperiencePojo.getStartTime())+"-"+simpleDateFormat.format(jobExperiencePojo.getEndTime());
                    jobExperiencePojo.setProjectTime(projectTime);
                }else{
                    jobExperiencePojo.setProjectTime(OrdinaryConstant.IS_BLACK);
                }
                jobExperiencePojo.setCompanyName(EmptyUtils.isNotEmpty(jobExperiencePojo.getCompanyName())?jobExperiencePojo.getCompanyName(): OrdinaryConstant.IS_BLACK);
                jobExperiencePojo.setProfessionName(EmptyUtils.isNotEmpty(jobExperiencePojo.getProfessionName())?jobExperiencePojo.getProfessionName(): OrdinaryConstant.IS_BLACK);
                jobExperiencePojo.setContent(EmptyUtils.isNotEmpty(jobExperiencePojo.getContent())?jobExperiencePojo.getContent(): OrdinaryConstant.IS_BLACK);
                jobExperiencePojo.setAchievement(EmptyUtils.isNotEmpty(jobExperiencePojo.getAchievement())?jobExperiencePojo.getAchievement(): OrdinaryConstant.IS_BLACK);
            });
        }else{
            listJobExperiences=new ArrayList<>();
            JobExperiencePojo jobExperiencePojo= new JobExperiencePojo();
            jobExperiencePojo.setProjectTime(OrdinaryConstant.IS_BLACK);
            jobExperiencePojo.setCompanyName(OrdinaryConstant.IS_BLACK);
            jobExperiencePojo.setProfessionName(OrdinaryConstant.IS_BLACK);
            jobExperiencePojo.setContent(OrdinaryConstant.IS_BLACK);
            jobExperiencePojo.setAchievement(OrdinaryConstant.IS_BLACK);
            listJobExperiences.add(jobExperiencePojo);
        }
        dataModel.put("jobList",listJobExperiences);


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
