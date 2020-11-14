package com.shenzhen.recurit.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.dao.UserMapper;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.enums.SymbolEnum;
import com.shenzhen.recurit.pojo.*;
import com.shenzhen.recurit.service.*;
import com.shenzhen.recurit.utils.*;
import com.shenzhen.recurit.utils.excel.ExportUtils;
import com.shenzhen.recurit.vo.*;
import io.netty.buffer.Unpooled;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String EXPERIENCE = "EXPERIENCE";
    private static final String SALARY = "SALARY";

    @Resource
    private RedisTempleUtils redisTempleUtils;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ResumeService resumeService;
    @Resource
    private PositionService positionService;
    @Resource
    private PositionUserRelationService positionUserRelationService;
    @Resource
    private DesiredPositionService desiredPositionService;
    @Resource
    private DictionaryService dictionaryService;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Object getVerificationCode(String number) {
        if (EmptyUtils.isEmpty(number)) {
            return ResultVO.error("手机号码或者邮箱不能为空!");
        }
        ResultVO resultVO;
        boolean flag;
        if (number.contains(OrdinaryConstant.SYMBOL_1)) {
            if (!emailVerify(number)) {
                return ResultVO.error("邮箱填写有误，请重新输入!");
            }
            resultVO = EmailUtils.sendEmail(number);
            String code = (String) resultVO.getData();
            flag = redisTempleUtils.setValue(number, code, 600, TimeUnit.SECONDS);
        } else {
            if (!phoneVerify(number)) {
                return ResultVO.error("手机填写有误，请重新输入!");
            }
            resultVO = PhoneUtils.getVerifyCode(number);
            String code = (String) resultVO.getData();
            flag = redisTempleUtils.setValue(number, code, 600, TimeUnit.SECONDS);
        }
        if (!flag) {
            return ResultVO.error("服务器跑丢了，请稍后再试......");
        }
        return resultVO;
    }

    @Override
    @Transactional
    public Object addByNumber(String jsonData) {
        JSONObject jsonObject = JSON.parseObject(jsonData);
        String number = jsonObject.getString(InformationConstant.NUMBER);
        String code = jsonObject.getString(InformationConstant.CODE);
        if (EmptyUtils.isEmpty(number)) {
            return ResultVO.error("手机号码或者邮箱不能为空，请重新输入登录号码！");
        }
        if (EmptyUtils.isEmpty(code)) {
            return ResultVO.error("验证码不能为空！请输入验证码");
        }
        UserVO userVO = JSONObject.parseObject(jsonData, UserVO.class);
        if (EmptyUtils.isEmpty(userVO)) {
            userVO = new UserVO();
        }
        addUserName(userVO);
        addResume(userVO);
        if (code.equals(redisTempleUtils.getValue(number, String.class))) {
            if (number.contains(OrdinaryConstant.SYMBOL_1)) {
                userVO.setEmail(number);
            } else {
                userVO.setPhone(number);
            }
            ResultVO resultVO = addUser(userVO);
            if (resultVO.getCode() == 200) {
                String category;
                if (number.contains(OrdinaryConstant.SYMBOL_1)) {
                    category = InformationConstant.EMAIL;
                } else {
                    category = InformationConstant.PHONE;
                }
                String entryName = saveUserToRedis(userVO, category);
                userVO.setEntryCode(entryName);
                return ResultVO.success(userVO);
            } else {
                return resultVO;
            }
        }
        return ResultVO.error("验证码已过期，请重新发送验证码！");
    }

    private void addResume(UserVO userVO) {
        if (EmptyUtils.isEmpty(userVO)) {
            return;
        }
        ResumeVO resumeVO = new ResumeVO();
        resumeVO.setUserCode(userVO.getUserCode());
        resumeService.initResumenTemplate(resumeVO);
    }

    /**
     * 没有登录名自动生成登录名
     *
     * @param userVO
     */
    private void addUserName(UserVO userVO) {
        if (EmptyUtils.isEmpty(userVO)) {
            userVO = new UserVO();
        }
        /**
         * 目前未增加公司，默认绑定公司
         */
        if (EmptyUtils.isNotEmpty(userVO.getRoleNum()) && "ROLE0001".equals(userVO.getRoleNum())) {
            userVO.setCompanyCode("SH120155452");
        }
        userVO.setUserCode(getUserCodeByTime());
        if (EmptyUtils.isEmpty(userVO.getUserName())) {
            int index = NumberEnum.ZERO.getValue();
            while (index < NumberEnum.TEN.getValue()) {
                String userName = RandomUtils.randomStr(NumberEnum.SIXTEEN.getValue());
                UserVO user = getUserByName(userName);
                if (EmptyUtils.isEmpty(user)) {
                    userVO.setUserName(userName);
                    break;
                }
                index++;
            }
        }
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isEmailNO(String email) {
        Pattern p = Pattern.compile("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isUserNameNO(String userName) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_\\x7f-\\xff][a-zA-Z0-9_\\x7f-\\xff]{2,18}$");
        Matcher m = p.matcher(userName);
        return m.matches();
    }


    @Override
    @Transactional
    public ResultVO addUser(UserVO userVO) {
        ResultVO resultVO = validateUser(userVO);
        if (EmptyUtils.isNotEmpty(resultVO)) {
            return resultVO;
        }
        setBaseUser(userVO);
        userMapper.addUser(userVO);
        addResume(userVO);
        return ResultVO.success(userVO);
    }

    private ResultVO validateUser(UserVO userVO) {
        if (EmptyUtils.isEmpty(userVO)) {
            return ResultVO.error("用户信息不能为空");
        }
        String userName = userVO.getUserName();
        if (EmptyUtils.isNotEmpty(userName)) {
            UserVO user = getUserByName(userName);
            if (EmptyUtils.isNotEmpty(user)) {
                return ResultVO.error("用户名已存在，请重新输入！");
            }
        } else {
            userVO.setUserName(getRandomName());
        }
        String phone = userVO.getPhone();
        if (EmptyUtils.isNotEmpty(phone)) {
            if (!isMobileNO(phone)) {
                return ResultVO.error("手机号码不存在，请重新输入！");
            }
            UserVO user = getUserByPhone(phone);
            if (EmptyUtils.isNotEmpty(user)) {
                return ResultVO.error("手机号码已存在，请重新输入！");
            }
        }
        String email = userVO.getEmail();
        if (EmptyUtils.isNotEmpty(email)) {
            if (email.length() > NumberEnum.THIRTY_TWO.getValue()) {
                return ResultVO.error("邮箱长度不能超过32个字符！");
            }
            if (!isEmailNO(email)) {
                return ResultVO.error("邮箱格式有误，请重新输入");
            }
            UserVO user = getUserByEmail(email);
            if (EmptyUtils.isNotEmpty(user)) {
                return ResultVO.error("邮箱已存在，请重新输入！");
            }
        }
        if (EmptyUtils.isNotEmpty(userVO.getBirth())) {
            userVO.setAge(getCalculationAge(userVO.getBirth()));
        }
        return null;
    }

    private void setBaseUser(UserVO userVO) {
        if (EmptyUtils.isEmpty(userVO)) {
            userVO = new UserVO();
        }
        if (EmptyUtils.isNotEmpty(userVO.getPassword())) {
            userVO.setPassword(EncryptBase64Utils.encryptBASE64(userVO.getPassword()));
        }
        if (EmptyUtils.isNotEmpty(userVO.getRoleNum()) && "ROLE0001".equals(userVO.getRoleNum())) {
            userVO.setCompanyCode("SH120155452");
        }
        userVO.setUserCode(getUserCodeByTime());
        userVO.setCreateDate(new Date());
        userVO.setUpdateDate(new Date());
    }

    private String getUserCodeByTime() {
        return InformationConstant.UPPER_USER + new Date().getTime();
    }

    /**
     * 计算年龄
     *
     * @param birth
     * @return
     */
    private int getCalculationAge(Date birth) {
        int age = NumberEnum.ZERO.getValue();
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birth);
        Calendar newCalendar = Calendar.getInstance();
        age = newCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
        if (newCalendar.get(Calendar.MONTH) > birthCalendar.get(Calendar.MONTH)) {
            age++;
        } else if (newCalendar.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH)
                && newCalendar.get(Calendar.DAY_OF_MONTH) > birthCalendar.get(Calendar.DAY_OF_MONTH)) {
            age++;
        } else {
        }
        return age;
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
    public Object logoutUser(String jsonData) {
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        UserVO userVO;
        boolean isDelete = false;
        if (jsonObject.containsKey(InformationConstant.USERCODE)) {
            String userName = jsonObject.getString(InformationConstant.USERCODE);
            userVO = redisTempleUtils.getValue(userName, UserVO.class);
            if (EmptyUtils.isNotEmpty(userVO)) {
                if (EmptyUtils.isNotEmpty(redisTempleUtils.getValue(Md5EncryptUtils.encryptMd5(userVO.getUserName()), String.class))) {
                    isDelete = redisTempleUtils.deleteValue(Md5EncryptUtils.encryptMd5(userVO.getUserName()));
                }
                if (EmptyUtils.isNotEmpty(redisTempleUtils.getValue(Md5EncryptUtils.encryptMd5(userVO.getEmail()), String.class))) {
                    isDelete = redisTempleUtils.deleteValue(Md5EncryptUtils.encryptMd5(userVO.getEmail()));
                }
                if (EmptyUtils.isNotEmpty(redisTempleUtils.getValue(Md5EncryptUtils.encryptMd5(userVO.getPhone()), String.class))) {
                    isDelete = redisTempleUtils.deleteValue(Md5EncryptUtils.encryptMd5(userVO.getPhone()));
                }
            }
            if (isDelete) {
                return ResultVO.success("退出成功");
            }
        }
        return ResultVO.error("用户已退出");
    }

    @Override
    public UserVO getUserInfoCookie(String userCode) {
        UserVO userVO = redisTempleUtils.getValue(userCode, UserVO.class);
        if (EmptyUtils.isNotEmpty(userVO) && EmptyUtils.isNotEmpty(userVO.getUserCode())) {
            String image = redisTempleUtils.getValue(userVO.getUserCode(), String.class);
            userVO.setImage(image);
        }
        return userVO;
    }


    @Override
    public ResultVO updatePassword(String jsonData) {
        UserVO user;
        JSONObject jsonObject = JSON.parseObject(jsonData);
        String newPassword = jsonObject.getString(InformationConstant.NEW_PASSWORD);
        if (EmptyUtils.isEmpty(newPassword)) {
            return ResultVO.error("新密码不能为空");
        }
        if (jsonObject.containsKey(InformationConstant.USERNAME) && jsonObject.containsKey(InformationConstant.PASSWORD)) {
            String userName = jsonObject.getString(InformationConstant.USERNAME);
            String password = jsonObject.getString(InformationConstant.PASSWORD);
            if (EmptyUtils.isEmpty(userName)) {
                return ResultVO.error("用户名不能为空");
            }
            if (EmptyUtils.isEmpty(password)) {
                return ResultVO.error("旧密码不能为空");
            }
            user = userMapper.getUserByNameAndPass(userName, EncryptBase64Utils.encryptBASE64(password));
            if (EmptyUtils.isEmpty(user)) {
                return ResultVO.error("用户名或者密码有误，请重新填写！");
            }
        } else {
            String number = jsonObject.getString(InformationConstant.NUMBER);
            String code = jsonObject.getString(InformationConstant.CODE);
            if (EmptyUtils.isEmpty(number)) {
                return ResultVO.error("登录号码不能为空！");
            }
            //忘记密码
            //1 通过手机或者邮箱查找用户信息
            //2 通过手机或者邮箱查找code，并比较code
            if (number.contains(OrdinaryConstant.SYMBOL_1)) {
                if (number.length() > NumberEnum.THIRTY_TWO.getValue()) {
                    return ResultVO.error("邮箱长度不能超过32个字符！");
                }
                if (!isEmailNO(number)) {
                    return ResultVO.error("邮箱格式有误，请重新输入");
                }
                user = userMapper.getUserByEmail(number);
                if (EmptyUtils.isEmpty(user)) {
                    return ResultVO.error("邮箱不存在，请先注册！");
                }
            } else {
                if (!isMobileNO(number)) {
                    return ResultVO.error("手机号码填写有误，请重新输入！");
                }
                user = userMapper.getUserByPhone(number);
                if (EmptyUtils.isEmpty(user)) {
                    return ResultVO.error("该手机号码不存在，请先注册！");
                }
            }
            if (!code.equals(redisTempleUtils.getValue(number, String.class))) {
                return ResultVO.error("验证码有误！");
            }
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setPassword(EncryptBase64Utils.encryptBASE64(newPassword));
        //旧密码换新密码
        userVO.setUpdateDate(new Date());
        int result = userMapper.updateUser(userVO);
        if (result > NumberEnum.ZERO.getValue()) {
            return ResultVO.success(userVO);
        } else {
            return ResultVO.error("修改失败");
        }

    }

    @Override
    public UserVO getUserInfoByNameOrNumber(String jsonData) {
        UserVO user;
        JSONObject jsonObject = JSON.parseObject(jsonData);
        if (jsonObject.containsKey(InformationConstant.USERNAME)) {
            String userName = jsonObject.getString(InformationConstant.USERNAME);
            user = userMapper.getUserByName(userName);
        } else if (jsonObject.containsKey(InformationConstant.NUMBER)) {
            String number = jsonObject.getString(InformationConstant.NUMBER);
            if (number.contains(OrdinaryConstant.SYMBOL_1)) {
                user = userMapper.getUserByEmail(number);
            } else {
                user = userMapper.getUserByPhone(number);
            }
        } else {
            user = null;
        }
        return user;
    }

    @Override
    public ResultVO deleteUser(int userId) {
        UserVO userVO = new UserVO();
        UserVO currUser = userMapper.getUserById(userId);
        if (EmptyUtils.isEmpty(currUser)) {
            return ResultVO.error("当前用户不存在，请勿重复删除");
        }
        userVO.setId(userId);
        userVO.setStatus(NumberEnum.TWO.getValue());
        userVO.setUpdateDate(new Date());
        UserVO user = updateUser(userVO);
        return ResultVO.success("删除成功");
    }

    @Override
    public UserVO updateUser(UserVO user) {
        if (EmptyUtils.isNotEmpty(user.getBirth())) {
            user.setAge(getCalculationAge(user.getBirth()));
        }
        user.setUpdateDate(new Date());
        int result = userMapper.updateUser(user);
        UserVO userVO;
        if (result > NumberEnum.ZERO.getValue()) {
            userVO = userMapper.getUserById(user.getId());
            saveUserToRedis(userVO, null);
        } else {
            userVO = user;
        }
        return userVO;
    }

    @Override
    public Object loginUser(String jsonData) {
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        UserVO userVO;
        String category = OrdinaryConstant.IS_BLACK;
        String entryName = OrdinaryConstant.IS_BLACK;
        if (jsonObject.containsKey(InformationConstant.USERNAME) && jsonObject.containsKey(InformationConstant.PASSWORD)) {
            String userName = jsonObject.getString(InformationConstant.USERNAME);
            String password = jsonObject.getString(InformationConstant.PASSWORD);
            if (EmptyUtils.isEmpty(userName)) {
                return ResultVO.error("用户名不能为空！");
            }
            if (EmptyUtils.isEmpty(password)) {
                return ResultVO.error("密码不能为空！");
            }
            // UserVO currUser = userMapper.getUserByName(userName);
            UserVO currUser = userMapper.getUserByNameOrEmailOrPhone(userName);
            if (EmptyUtils.isEmpty(currUser)) {
                return ResultVO.error(userName + "用户不存在，请先注册");
            }
            userVO = userMapper.getUserByNameAndPass(userName, EncryptBase64Utils.encryptBASE64(password));
            if (EmptyUtils.isEmpty(userVO) || EmptyUtils.isEmpty(userVO.getUserName())) {
                return ResultVO.error("密码错误！");
            }
            category = InformationConstant.USERNAME;
        } else {
            if (jsonObject.containsKey("number")) {
                String number = jsonObject.getString("number");
                String code = jsonObject.getString("code");
                if (EmptyUtils.isEmpty(number)) {
                    return ResultVO.error("登录号码不能为空！");
                }
                if (number.contains(OrdinaryConstant.SYMBOL_1)) {
                    userVO = userMapper.getUserByEmail(number);
                    category = InformationConstant.EMAIL;
                } else {
                    userVO = userMapper.getUserByPhone(number);
                    category = InformationConstant.PHONE;
                }
                if (EmptyUtils.isEmpty(code)) {
                    return ResultVO.error("验证码不能为空！");
                }
                if (EmptyUtils.isEmpty(userVO)) {
                    return ResultVO.error("用户不存在，请先注册！");
                }
                if (!code.equals(redisTempleUtils.getValue(number, String.class))) {
                    return ResultVO.error("验证码不正确！");
                }
            } else {
                userVO = new UserVO();
            }
        }
        entryName = saveUserToRedis(userVO, category);
        userVO.setEntryCode(entryName);
        return ResultVO.success("登录成功", userVO);
    }

    private String saveUserToRedis(UserVO userVO, String category) {
        String entryName = OrdinaryConstant.IS_BLACK;
        String userInfo;
        if (EmptyUtils.isNotEmpty(userVO)) {
            if (EmptyUtils.isNotEmpty(category)) {
                userInfo = JSON.toJSONString(userVO);
                if (category.equals(InformationConstant.USERNAME) && EmptyUtils.isNotEmpty(userVO.getUserName())) {
                    entryName = Md5EncryptUtils.encryptMd5(userVO.getUserName());
                } else if (category.equals(InformationConstant.EMAIL) && EmptyUtils.isNotEmpty(userVO.getEmail())) {
                    entryName = Md5EncryptUtils.encryptMd5(userVO.getEmail());
                } else if (category.equals(InformationConstant.PHONE) && EmptyUtils.isNotEmpty(userVO.getPhone())) {
                    entryName = Md5EncryptUtils.encryptMd5(userVO.getPhone());
                }
                if (EmptyUtils.isNotEmpty(entryName)) {
                    redisTempleUtils.setValue(entryName, userInfo, 60 * 60 * 24 * 15, TimeUnit.SECONDS);
                }
            } else {
                String userStr;
                userInfo = JSON.toJSONString(userVO);
                if (EmptyUtils.isNotEmpty(userVO.getUserName())) {
                    entryName = Md5EncryptUtils.encryptMd5(userVO.getUserName());
                    userStr = redisTempleUtils.getValue(entryName, String.class);
                    if (EmptyUtils.isNotEmpty(userStr)) {
                        redisTempleUtils.setValue(entryName, userInfo, 60 * 60 * 24 * 15, TimeUnit.SECONDS);
                    }
                }
                if (EmptyUtils.isNotEmpty(userVO.getPhone())) {
                    entryName = Md5EncryptUtils.encryptMd5(userVO.getPhone());
                    userStr = redisTempleUtils.getValue(entryName, String.class);
                    if (EmptyUtils.isNotEmpty(userStr)) {
                        redisTempleUtils.setValue(entryName, userInfo, 60 * 60 * 24 * 15, TimeUnit.SECONDS);
                    }
                }
                if (EmptyUtils.isNotEmpty(userVO.getEmail())) {
                    entryName = Md5EncryptUtils.encryptMd5(userVO.getEmail());
                    userStr = redisTempleUtils.getValue(entryName, String.class);
                    if (EmptyUtils.isNotEmpty(userStr)) {
                        redisTempleUtils.setValue(entryName, userInfo, 60 * 60 * 24 * 15, TimeUnit.SECONDS);
                    }
                }
            }

        }
        return entryName;
    }

    /**
     * 邮箱正则验证
     *
     * @param email
     * @return
     */
    public static boolean emailVerify(String email) {
        email = email.trim();
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 手机号码正则验证
     *
     * @param phoneNum
     * @return
     */
    public static boolean phoneVerify(String phoneNum) {
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
     *
     * @return
     */
    private String getRandomName() {
        StringBuilder stringBuilder = new StringBuilder(OrdinaryConstant.IS_BLACK);
        for (int index = 0; index < 6; index++) {
            int num = (int) (Math.random() * 2);
            if (num == 1) {
                //小写字母
                stringBuilder.append((char) (int) (Math.random() * 26 + 97));
            } else {
                //大写字母
                stringBuilder.append((char) (int) (Math.random() * 26 + 65));
            }
        }
        stringBuilder.append(OrdinaryConstant.SYMBOL_3);
        for (int index = 0; index < 4; index++) {
            stringBuilder.append((int) (Math.random() * 10));
        }
        return stringBuilder.toString();
    }

    public ResultVO updateOrSaveImage(UserVO userVO) {
        redisTempleUtils.setValue(userVO.getUserCode(), userVO.getImage());
        return ResultVO.success();
    }

    public UserVO getUserCode(String userCode) {
        return userMapper.getUserCode(userCode);
    }

    @Override
    public Object verificateIphone(String phone, String code) {
        if (EmptyUtils.isEmpty(code)) {
            return ResultVO.error("验证码不能为空");
        }
        if (!isMobileNO(phone)) {
            return ResultVO.error("手机号码有误");
        }
        String value = redisTempleUtils.getValue(phone, String.class);
        if (code.equals(value)) {
            return ResultVO.success("验证成功", true);
        } else {
            return ResultVO.success("验证码错误", false);
        }
    }

    @Override
    public ResultVO batchUserInfo(ImportResultPojo importInfos) {
        Map<Integer, Map<String, List<String>>> checkMap = new HashMap<>();
        List<UserVO> listUser = importInfos.getListT();
        if (EmptyUtils.isNotEmpty(listUser) && listUser.size() > NumberEnum.ZERO.getValue()) {
            Map<Integer, UserVO> mapUser = new HashMap<>();
            exchangeListToMap(mapUser, listUser);
            Map<Integer, UserVO> queryData = new HashMap<>();
            filterProblemData(mapUser, checkMap, queryData);
            List<UserVO> listVO = new ArrayList<>();
            for (Map.Entry<Integer, UserVO> entry : queryData.entrySet()) {
                if (listVO.size() > NumberEnum.ONE_HUNDRED.getValue()) {
                    batchSaveUserInfo(listVO);
                    listVO.clear();
                }
                UserVO userVO = entry.getValue();
                if (EmptyUtils.isNotEmpty(userVO.getBirth())) {
                    userVO.setAge(getCalculationAge(userVO.getBirth()));
                }
                setBaseUser(userVO);
                try {
                    addResume(userVO);
                    listVO.add(userVO);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            if (listVO.size() > NumberEnum.ZERO.getValue()) {
                batchSaveUserInfo(listVO);
                UserVO currUser = ThreadLocalUtils.getUser();
                for (UserVO userVO : listUser) {
                    try {
                        // 默认获取第一个职位发布简历
                        PageInfo<PositionPojo> pageInfo = positionService.getByCompanyCode(currUser.getCompanyCode(), currUser.getUserCode(), 1, 1);
                        PositionPojo positionPojo = null;
                        if (EmptyUtils.isNotEmpty(pageInfo.getList()) && !pageInfo.getList().isEmpty()) {
                            positionPojo = pageInfo.getList().get(0);
                            PositionUserRelationVO positionUserRelationVO = new PositionUserRelationVO();
                            positionUserRelationVO.setApply(2);
                            positionUserRelationVO.setFollow(1);
                            positionUserRelationVO.setPositionId(positionPojo.getId());
                            // 修改投递状态
                            positionUserRelationService.createOrUpdateRelation(positionUserRelationVO, userVO);
                            resumeService.sendResumeEmail(userVO.getUserCode());
                        }


                    } catch (Exception e) {
                        LOGGER.error(userVO.getUserName() + "简历发送失败");
                        e.printStackTrace();
                    }
                }
            }
            //将成功或者失败的重新显示
            //File file = importInfos.getTemplateFile();
            //addExportInfomation(file,checkMap);
        }
        return ResultVO.success(checkMap);

    }

    /**
     * 导出结果文档
     */
    private void addExportInfomation(File file, Map<Integer, Map<String, List<String>>> checkMap) {
        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream = new FileInputStream(file);
            String suffix = file.getName().substring(file.getName().indexOf(OrdinaryConstant.SYMBOL_5) + NumberEnum.ONE.getValue());
            if (OrdinaryConstant.SYMBOL_7.equals(suffix)) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new HSSFWorkbook(inputStream);
            }
            if (EmptyUtils.isNotEmpty(workbook)) {
                Sheet sheet = workbook.getSheetAt(NumberEnum.ZERO.getValue());
                Row firstRow = sheet.getRow(NumberEnum.ZERO.getValue());
                if (EmptyUtils.isEmpty(firstRow)) {
                    logger.error("模板excel 首列标题信息不能为空");
                    throw new RuntimeException();
                }
                int lastCellNum = firstRow.getLastCellNum() + NumberEnum.ONE.getValue();
                Cell lastRowCell = firstRow.createCell(lastCellNum);
                lastRowCell.setCellValue("导入信息");
                int lastRowNum = sheet.getLastRowNum();
                for (int index = NumberEnum.ONE.getValue(); index <= lastRowNum; index++) {
                    Row row = sheet.getRow(index);
                    Cell cell = row.createCell(lastCellNum);
                    if (EmptyUtils.isNotEmpty(cell)) {
                        if (EmptyUtils.isNotEmpty(checkMap)
                                && checkMap.containsKey(index)
                                && checkMap.get(index).containsKey("error")
                                && EmptyUtils.isNotEmpty(checkMap.get(index).get("error"))
                                && checkMap.get(index).get("error").size() > NumberEnum.ZERO.getValue()) {
                            cell.setCellValue("导入成功");
                        } else {
                            cell.setCellValue(String.join(OrdinaryConstant.SYMBOL_4, checkMap.get(index).get("error")));
                        }
                    }
                }
                ExportUtils.buildExcelDocument(file.getName(), workbook, CommonUtils.getResponse());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (EmptyUtils.isNotEmpty(inputStream)) {
                try {
                    inputStream.close();
                    file.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 修改并导出结论
     */
    private void updateExportResult(ImportResultPojo importInfos, Map<Integer, Map<String, List<String>>> checkMap, HttpServletResponse response) {
        File file = importInfos.getTemplateFile();
        Map<Integer, ExportsPojo> exportMap = importInfos.getExportMap();

    }

    private Map<String, Integer> getExportColumnInfo(Map<Integer, ExportsPojo> exportMap) {
        Map<String, Integer> map = new HashMap<>();
        //for(Map.Entry<Integer, ExportsPojo>)
        return null;
    }

    @Override
    public void batchSaveUserInfo(List<UserVO> listUser) {
        userMapper.batchSaveUserInfo(listUser);
    }

    public List<UserPojo> getUserByNameAndPhoneAndEmail(List<String> listName, List<String> listPhone, List<String> listEmail) {
        return userMapper.getUserByNameAndPhoneAndEmail(listName, listPhone, listEmail);
    }

    /**
     * 过滤异常数据，检查重复数据
     *
     * @param mapUser
     * @param checkMap
     * @param queryData
     */
    private void filterProblemData(Map<Integer, UserVO> mapUser, Map<Integer, Map<String, List<String>>> checkMap, Map<Integer, UserVO> queryData) {
        for (Map.Entry<Integer, UserVO> entry : mapUser.entrySet()) {
            int key = entry.getKey();
            UserVO userVO = entry.getValue();
            Map<String, List<String>> mapError = null;
            if (checkMap.containsKey(key)) {
                mapError = checkMap.get(key);
            } else {
                mapError = new HashMap<>();
            }
            checkData(userVO, mapError, queryData, key);
            checkMap.put(key, mapError);
        }
        List<UserPojo> repeatUsers = getUserByNameAndPhoneAndEmail(queryData);
        List<Integer> filterData = new ArrayList<>();
        //查找重复数据
        filterRepeatData(mapUser, repeatUsers, filterData, checkMap);
        //过滤掉重复数据
        if (EmptyUtils.isNotEmpty(filterData) && filterData.size() > NumberEnum.ZERO.getValue()) {
            for (Integer f : filterData) {
                if (queryData.containsKey(f)) {
                    queryData.remove(f);
                }
            }
        }

    }

    private void filterRepeatData(Map<Integer, UserVO> mapUser, List<UserPojo> repeatUsers, List<Integer> filterData, Map<Integer, Map<String, List<String>>> checkMap) {
        if (EmptyUtils.isNotEmpty(repeatUsers) && repeatUsers.size() > NumberEnum.ZERO.getValue()) {
            for (UserPojo userPojo : repeatUsers) {
                boolean flag = false;
                for (Map.Entry<Integer, UserVO> entry : mapUser.entrySet()) {
                    if ((EmptyUtils.isNotEmpty(userPojo.getUserName()) && userPojo.getUserName().equals(entry.getValue().getUserName())) ||
                            (EmptyUtils.isNotEmpty(userPojo.getPhone()) && userPojo.getPhone().equals(entry.getValue().getPhone())) ||
                            (EmptyUtils.isNotEmpty(userPojo.getPhone()) && userPojo.getPhone().equals(entry.getValue().getPhone()))) {
                        Map<String, List<String>> mapError = checkMap.get(entry.getKey());
                        List<String> listError = mapError.get("error");
                        List<String> listField = mapError.get("field");
                        if (userPojo.getUserName().equals(entry.getValue().getUserName()) && !listField.contains("userName")) {
                            flag = true;
                            listError.add("用户名" + userPojo.getUserName() + "已存在！");
                            listField.add("userName");
                        }
                        if (userPojo.getPhone().equals(entry.getValue().getPhone()) && !listField.contains("phone")) {
                            flag = true;
                            listError.add("电话号码" + userPojo.getPhone() + "已存在！");
                            listField.add("phone");
                        }
                        if (userPojo.getEmail().equals(entry.getValue().getEmail()) && !listField.contains("email")) {
                            flag = true;
                            listError.add("邮箱" + userPojo.getEmail() + "已存在！");
                            listField.add("email");
                        }
                        if (flag) {
                            filterData.add(entry.getKey());
                            break;
                        }
                    }
                }
            }
        }
    }

    private List<UserPojo> getUserByNameAndPhoneAndEmail(Map<Integer, UserVO> queryData) {
        List<UserPojo> listUser = new ArrayList<>();
        if (queryData.size() > NumberEnum.ZERO.getValue()) {
            List<String> listName = new ArrayList<>();
            List<String> listPhone = new ArrayList<>();
            List<String> listEmail = new ArrayList<>();
            for (Map.Entry<Integer, UserVO> entry : queryData.entrySet()) {
                if (listName.size() >= NumberEnum.ONE_HUNDRED.getValue() ||
                        listPhone.size() >= NumberEnum.ONE_HUNDRED.getValue() ||
                        listEmail.size() >= NumberEnum.ONE_HUNDRED.getValue()) {
                    List<UserPojo> repeatUser = getUserByNameAndPhoneAndEmail(listName, listPhone, listEmail);
                    listUser.addAll(repeatUser);
                    listName.clear();
                    listPhone.clear();
                    listEmail.clear();
                }
                listName.add(entry.getValue().getUserName());
                listPhone.add(entry.getValue().getPhone());
                listEmail.add(entry.getValue().getEmail());
            }
            if (listName.size() >= NumberEnum.ZERO.getValue() ||
                    listPhone.size() >= NumberEnum.ZERO.getValue() ||
                    listEmail.size() >= NumberEnum.ZERO.getValue()) {
                List<UserPojo> repeatUser = getUserByNameAndPhoneAndEmail(listName, listPhone, listEmail);
                listUser.addAll(repeatUser);
            }
        }
        return listUser;
    }


    /**
     * 转换数据
     *
     * @param userVO {@link UserVO}
     */
    private void exchangData(UserVO userVO) {
        //转换数据
        exchangeSex(userVO);
        exchangeRole(userVO);
        userVO.setPassword(EncryptBase64Utils.encryptBASE64(userVO.getPassword()));
    }

    /**
     * 转换数据
     *
     * @param userPojo {@link UserPojo}
     */
    private void exchangData(UserPojo userPojo) {
        //转换数据
        exchangeSex(userPojo);
        exchangeRole(userPojo);
        exchangeJobExperience(userPojo);
        exchangeSalary(userPojo);
        String password = userPojo.getPassword();
        if(EmptyUtils.isNotEmpty(password)){
            userPojo.setPassword(EncryptBase64Utils.encryptBASE64(password));
        }
    }

    private void exchangeSalary(UserPojo userPojo){
        String salary = userPojo.getSalary();
        if(EmptyUtils.isNotEmpty(salary)){
            DictionaryVO signleByDictNumber = dictionaryService.getSignleByDictNumber(SALARY, salary);
            if(EmptyUtils.isNotEmpty(signleByDictNumber)){
                userPojo.setSalaryName(signleByDictNumber.getDictName());
            }
        }else {
            String salaryName = userPojo.getSalaryName();
            if(EmptyUtils.isNotEmpty(salaryName)){
                List<DictionaryVO> allDictByCategory = dictionaryService.getAllDictByCategory(SALARY);
                for(DictionaryVO dictionaryVO : allDictByCategory){
                    if(salaryName.equals(dictionaryVO.getDictName())){
                        userPojo.setJobExperience(dictionaryVO.getDictNum());
                        break;
                    }
                }
            }
        }
    }

    private void exchangeJobExperience(UserPojo userPojo){
        String jobExperience = userPojo.getJobExperience();
        if(EmptyUtils.isNotEmpty(jobExperience)){
            DictionaryVO signleByDictNumber = dictionaryService.getSignleByDictNumber(EXPERIENCE, jobExperience);
            if(EmptyUtils.isNotEmpty(signleByDictNumber)){
                userPojo.setJobExperienceName(signleByDictNumber.getDictName());
            }
        }else {
            String jobExperienceName = userPojo.getJobExperienceName();
            if(EmptyUtils.isNotEmpty(jobExperienceName)){
                List<DictionaryVO> allDictByCategory = dictionaryService.getAllDictByCategory(EXPERIENCE);
                for(DictionaryVO dictionaryVO : allDictByCategory){
                    if(jobExperienceName.equals(dictionaryVO.getDictName())){
                        userPojo.setJobExperience(dictionaryVO.getDictNum());
                        break;
                    }
                }
            }
        }
    }


    // 转换角色
    private void exchangeRole(UserVO userVO){
        if (EmptyUtils.isNotEmpty(userVO.getRoleName()) || EmptyUtils.isNotEmpty(userVO.getRoleNum())) {
            if (EmptyUtils.isNotEmpty(userVO.getRoleName())) {
                if (InformationConstant.ADMIN_EN.equals(userVO.getRoleName())) {
                    userVO.setRoleNum("ROLE0000");
                } else if (InformationConstant.JOBSEEKER_EN.equals(userVO.getRoleName())) {
                    userVO.setRoleNum("ROLE0002");
                } else {
                    userVO.setRoleNum("ROLE0001");
                }
            } else {
                if ("ROLE0000".equals(userVO.getRoleNum())) {
                    userVO.setRealName(InformationConstant.ADMIN_EN);
                } else if ("ROLE0002".equals(userVO.getRoleNum())) {
                    userVO.setRealName(InformationConstant.JOBSEEKER_EN);
                } else {
                    userVO.setRealName(InformationConstant.ENTERPRISE_EN);
                }
            }
        } else {
            userVO.setRealName(InformationConstant.JOBSEEKER_EN);
            userVO.setRoleNum("ROLE0002");
        }
    }

    // 转换角色
    private void exchangeRole(UserPojo userPojo){
        if (EmptyUtils.isNotEmpty(userPojo.getRoleName()) || EmptyUtils.isNotEmpty(userPojo.getRoleNum())) {
            if (EmptyUtils.isNotEmpty(userPojo.getRoleName())) {
                if (InformationConstant.ADMIN_EN.equals(userPojo.getRoleName())) {
                    userPojo.setRoleNum("ROLE0000");
                } else if (InformationConstant.JOBSEEKER_EN.equals(userPojo.getRoleName())) {
                    userPojo.setRoleNum("ROLE0002");
                } else {
                    userPojo.setRoleNum("ROLE0001");
                }
            } else {
                if ("ROLE0000".equals(userPojo.getRoleNum())) {
                    userPojo.setRealName(InformationConstant.ADMIN_EN);
                } else if ("ROLE0002".equals(userPojo.getRoleNum())) {
                    userPojo.setRealName(InformationConstant.JOBSEEKER_EN);
                } else {
                    userPojo.setRealName(InformationConstant.ENTERPRISE_EN);
                }
            }
        } else {
            userPojo.setRealName(InformationConstant.JOBSEEKER_EN);
            userPojo.setRoleNum("ROLE0002");
        }
    }

    // 转换性别
    private void exchangeSex(UserVO userVO){
        if (EmptyUtils.isNotEmpty(userVO.getSexNum()) || EmptyUtils.isNotEmpty(userVO.getSex())) {
            if (EmptyUtils.isNotEmpty(userVO.getSexNum())) {
                if (InformationConstant.MALE.equals(userVO.getSexNum())) {
                    userVO.setSex("01");
                } else if (InformationConstant.FEMALE.equals(userVO.getSexNum())) {
                    userVO.setSex("02");
                } else {
                    userVO.setSex("00");
                }
            } else {
                if ("01".equals(userVO.getSex())) {
                    userVO.setSexNum("男");
                } else if ("02".equals(userVO.getSexNum())) {
                    userVO.setSexNum("女");
                } else {
                    userVO.setSexNum("未知");
                }
            }
        } else {
            userVO.setSex("00");
            userVO.setSexNum("未知");
        }
    }

    // 转换性别
    private void exchangeSex(UserPojo userPojo){
        if (EmptyUtils.isNotEmpty(userPojo.getSexNum()) || EmptyUtils.isNotEmpty(userPojo.getSex())) {
            if (EmptyUtils.isNotEmpty(userPojo.getSexNum())) {
                if (InformationConstant.MALE.equals(userPojo.getSexNum())) {
                    userPojo.setSex("01");
                } else if (InformationConstant.FEMALE.equals(userPojo.getSexNum())) {
                    userPojo.setSex("02");
                } else {
                    userPojo.setSex("00");
                }
            } else {
                if ("01".equals(userPojo.getSex())) {
                    userPojo.setSexNum("男");
                } else if ("02".equals(userPojo.getSexNum())) {
                    userPojo.setSexNum("女");
                } else {
                    userPojo.setSexNum("未知");
                }
            }
        } else {
            userPojo.setSex("00");
            userPojo.setSexNum("未知");
        }
    }

    /**
     * 检查并转换数据数据
     *
     * @param userVO
     * @param mapError
     */
    private void checkData(UserVO userVO, Map<String, List<String>> mapError, Map<Integer, UserVO> queryData, int key) {
        boolean flag = false;
        if (EmptyUtils.isNotEmpty(userVO)) {
            exchangData(userVO);
            String phone = EmptyUtils.isNotEmpty(userVO.getPhone()) ? userVO.getPhone() : OrdinaryConstant.IS_BLACK;
            String email = EmptyUtils.isNotEmpty(userVO.getEmail()) ? userVO.getEmail() : OrdinaryConstant.IS_BLACK;
            List<String> listError = null;
            List<String> listField = null;
            if (mapError.containsKey("error")) {
                listError = mapError.get("error");
            } else {
                listError = new ArrayList<>();
            }
            if (mapError.containsKey("field")) {
                listField = mapError.get("field");
            } else {
                listField = new ArrayList<>();
            }
            if (!emailVerify(email)) {
                flag = true;
                listError.add("邮箱格式有误！");
                if (!listField.contains("email"))
                    listField.add("email");
            }
            if (!phoneVerify(phone)) {
                flag = true;
                listError.add("电话格式有误！");
                if (!listField.contains("phone"))
                    listField.add("phone");
            }
            mapError.put("error", listError);
            mapError.put("field", listField);
            if (!flag) {
                queryData.put(key, userVO);
            }
        }
    }

    private void exchangeListToMap(Map<Integer, UserVO> mapUser, List<UserVO> listUser) {
        //list 转 map
        for (int index = NumberEnum.ZERO.getValue(); index < listUser.size(); index++) {
            mapUser.put(index + NumberEnum.ONE.getValue(), listUser.get(index));
        }
    }

    /**
     * 查询所有未投递简历的求职人员
     *
     * @return
     */
    public List<UserVO> getAllIsNotPosition() {
        List<UserVO> userPojoList = userMapper.getAllIsNotPosition();
        userPojoList.forEach(userVO -> exchangData(userVO));
        return userPojoList;
    }

    @Override
    public PageInfo<UserPojo> queryPersonnel(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserPojo> allJobSeeker = userMapper.getAllJobSeeker();
        allJobSeeker.stream().forEach(userPojo -> {
            exchangData(userPojo);
        });
        PageInfo<UserPojo> pageInfo = new PageInfo<>(allJobSeeker);
        return pageInfo;
    }

    @Override
    public List<UserPojo> getAllJobSeeker() {
        List<UserPojo> jobSeekerList = userMapper.getAllJobSeeker();
        jobSeekerList.stream().forEach(userPojo -> {
            exchangData(userPojo);
        });
        return jobSeekerList;
    }

    private void setUserInfo(List<UserPojo> userPojoList){
        if(EmptyUtils.isEmpty(userPojoList) || userPojoList.isEmpty()){
            return;
        }
    }




}