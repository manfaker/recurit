package com.shenzhen.recurit.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.UserMapper;
import com.shenzhen.recurit.service.UserService;
import com.shenzhen.recurit.utils.*;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RedisTempleUtils redisTempleUtils;
    @Resource
    private UserMapper userMapper;

    @Override
    public Object getVerificationCode(String number) {
        if(EmptyUtils.isEmpty(number)){
            return ResultVO.error("手机号码或者邮箱不能为空!");
        }
        ResultVO resultVO;
        boolean flag;
        if(number.contains(OrdinaryConstant.SYMBOL_1)){
            if(!emailVerify(number)){
                return ResultVO.error("邮箱填写有误，请重新输入!");
            }
            resultVO = EmailUtils.sendEmail(number);
            String code = (String) resultVO.getData();
            flag =redisTempleUtils.setValue(number,code,300000, TimeUnit.SECONDS);
        }else{
            if(!phoneVerify(number)){
                return ResultVO.error("手机填写有误，请重新输入!");
            }
            resultVO =PhoneUtils.getVerifyCode(number);
            String code = (String) resultVO.getData();
            flag=redisTempleUtils.setValue(number,code,60000, TimeUnit.SECONDS);
        }
        if(!flag){
            return ResultVO.error("服务器跑丢了，请稍后再试......");
        }
        return resultVO;
    }

    @Override
    public Object addByNumber(String jsonData) {
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        String number = jsonObject.getString(InformationConstant.NUMBER);
        String code = jsonObject.getString(InformationConstant.CODE);
        if(EmptyUtils.isEmpty(number)){
            return ResultVO.error("手机号码或者邮箱不能为空，请重新输入登录号码！");
        }
        if(EmptyUtils.isEmpty(code)){
            return ResultVO.error("验证码错误，请重新输入验证码");
        }
        UserVO userVO=new UserVO();;
        if(code.equals(redisTempleUtils.getValue(number,String.class))){
            if(number.contains(OrdinaryConstant.SYMBOL_1)){
                userVO.setEmail(number);
            }else{
                userVO.setPhone(number);
            }
            return addUser(userVO);
        }
        return ResultVO.error("验证码不正确，请重新输入验证码！");
    }

    @Override
    public Object addUser(UserVO userVO) {
        if(EmptyUtils.isEmpty(userVO)){
            return ResultVO.error("用户信息不能为空");
        }
        String userNameZh = userVO.getUserNameZh();
        if(EmptyUtils.isNotEmpty(userNameZh)){
            UserVO user = getUserByName(userNameZh);
            if(EmptyUtils.isNotEmpty(user)){
                return ResultVO.error("用户名已存在，请重新输入！");
            }
        }else{
            userVO.setUserNameZh(getRandomName());
        }
        String phone = userVO.getPhone();
        if(EmptyUtils.isNotEmpty(phone)){
            UserVO user = getUserByPhone(phone);
            if(EmptyUtils.isNotEmpty(user)){
                return ResultVO.error("手机号码已存在，请重新输入！");
            }
        }
        String email = userVO.getEmail();
        if(EmptyUtils.isNotEmpty(email)){
            UserVO user = getUserByEmail(email);
            if(EmptyUtils.isNotEmpty(user)){
                return ResultVO.error("手机号码已存在，请重新输入！");
            }
        }
        userVO.setCreateDate(new Date());
        userVO.setUpdateDate(new Date());
        userMapper.addUser(userVO);
        return ResultVO.success(userVO);
    }

    @Override
    public UserVO getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }

    @Override
    public UserVO getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }

    @Override
    public UserVO getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    public Object loginUser(String jsonData) {
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        UserVO userVO;
        String category = OrdinaryConstant.IS_BLACK;
        String entryName = OrdinaryConstant.IS_BLACK;
        if(jsonObject.containsKey(InformationConstant.USERNAME)&&jsonObject.containsKey(InformationConstant.PASSWORD)){
            String userName = jsonObject.getString(InformationConstant.USERNAME);
            String password = jsonObject.getString(InformationConstant.PASSWORD);
            if(EmptyUtils.isEmpty(userName)){
                return ResultVO.error("用户名不能为空！");
            }
            if(EmptyUtils.isEmpty(password)){
                return ResultVO.error("密码不能为空！");
            }
            userVO = userMapper.getUserByNameAndPass(userName,EncryptBase64Utils.encryptBASE64(password));
            if(EmptyUtils.isEmpty(userVO)||EmptyUtils.isEmpty(userVO.getUserNameZh())){
                return ResultVO.error("用户名或者密码错误！");
            }
            category = InformationConstant.USERNAME;

        }else{
            if(jsonObject.containsKey("number")&&jsonObject.containsKey("code")){
                String number = jsonObject.getString("number");
                String code = jsonObject.getString("code");
                if(EmptyUtils.isEmpty(number)){
                    return ResultVO.error("手机号码不能为空！");
                }
                if(number.contains(OrdinaryConstant.SYMBOL_1)){
                    userVO = userMapper.getUserByEmail(number);
                    category=InformationConstant.EMAIL;
                }else{
                    userVO = userMapper.getUserByPhone(number);
                    category=InformationConstant.PHONE;
                }
                if(EmptyUtils.isEmpty(userVO)||EmptyUtils.isEmpty(userVO.getUserNameZh())){
                    return ResultVO.error("用户不存在，请先注册！");
                }
                if(EmptyUtils.isEmpty(code)){
                    return ResultVO.error("验证码不能为空！");
                }
                if(!code.equals(redisTempleUtils.getValue(number,String.class))){
                    return ResultVO.error("验证码不正确！");
                }
            }else{
                userVO = null;
            }
        }
        entryName = saveUserToRedis(userVO,category);
        return ResultVO.success("成功",entryName);
    }

    private String saveUserToRedis(UserVO userVO,String category){
        String entryName=OrdinaryConstant.IS_BLACK;
        if(EmptyUtils.isNotEmpty(userVO)){
                String userInfo = JSON.toJSONString(userVO);
                if(category.equals(InformationConstant.USERNAME)&&EmptyUtils.isNotEmpty(userVO.getUserNameZh())){
                    entryName = Md5EncryptUtils.encryptMd5(userVO.getUserNameZh());
                }else if (category.equals(InformationConstant.EMAIL)&&EmptyUtils.isNotEmpty(userVO.getEmail())){
                    entryName = Md5EncryptUtils.encryptMd5(userVO.getEmail());
                }else if (category.equals(InformationConstant.PHONE)&&EmptyUtils.isNotEmpty(userVO.getPhone())){
                    entryName=Md5EncryptUtils.encryptMd5(userVO.getEmail());
                }
                if(EmptyUtils.isNotEmpty(entryName)){
                    redisTempleUtils.setValue(entryName,userInfo,60*60*24*15,TimeUnit.MINUTES);
                }
        }
        return entryName;
    }

    /**
     * 邮箱正则验证
     * @param email
     * @return
     */
    public static boolean emailVerify(String email){
        email = email.trim();
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 手机号码正则验证
     * @param phoneNum
     * @return
     */
    public static boolean phoneVerify(String phoneNum){
        phoneNum = phoneNum.trim();
        String regex = "^(1[3-9]\\d{9}$)";
        if (phoneNum.length() == 11) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phoneNum);
            if (m.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取随机的11位名字
     * @return
     */
    private String getRandomName(){
        StringBuilder stringBuilder = new StringBuilder(OrdinaryConstant.IS_BLACK);
        for(int index=0;index<6;index++){
            int num = (int)(Math.random()*2);
            if(num==1){
                //小写字母
                stringBuilder.append((char)(int)(Math.random()*26+97));
            }else{
                //大写字母
                stringBuilder.append((char)(int)(Math.random()*26+65));
            }
        }
        stringBuilder.append(OrdinaryConstant.SYMBOL_3);
        for(int index=0;index<4;index++){
            stringBuilder.append((int)(Math.random()*10));
        }
        return stringBuilder.toString();
    }



}
