package com.shenzhen.recurit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.enums.ReturnEnum;
import com.shenzhen.recurit.pojo.ImportResultPojo;
import com.shenzhen.recurit.pojo.UserPojo;
import com.shenzhen.recurit.service.DesiredPositionService;
import com.shenzhen.recurit.service.EducationExperinceService;
import com.shenzhen.recurit.service.UserService;
import com.shenzhen.recurit.utils.*;
import com.shenzhen.recurit.utils.excel.ExportUtils;
import com.shenzhen.recurit.utils.excel.ImportUtils;
import com.shenzhen.recurit.vo.DesiredPositionVO;
import com.shenzhen.recurit.vo.EducationExperienceVO;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import io.swagger.annotations.*;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "user")
@Api(tags = {"用户信息"})
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "reLogin", method = RequestMethod.GET)
    public Object reLogin() {
        return ResultVO.error(ReturnEnum.DEFAULT_301.getCode(), "跳转到登录页面");
    }

    @RequestMapping(value = "noPrivilege", method = RequestMethod.GET)
    public Object noPrivilege() {
        return ResultVO.error(ReturnEnum.DEFAULT_401.getCode(), "无权限页面");
    }

    @RequestMapping(value = "getVerificationCode", method = RequestMethod.GET)
    public Object getVerificationCode(String number) {
        Object verificationCode = userService.getVerificationCode(number);
        return verificationCode;
    }

    @RequestMapping(value = "addByNumber", method = RequestMethod.POST)
    public Object addByNumber(@RequestBody String jsonData) {
        return userService.addByNumber(jsonData);
    }

    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    public Object addUser(@RequestBody String jsonData) {
        UserVO userVO = JSONObject.parseObject(jsonData, UserVO.class);
        return userService.addUser(userVO);
    }

    @RequestMapping(value = "updatePassword", method = RequestMethod.PUT)
    public Object updatePassword(@RequestBody String jsonData) {
        return userService.updatePassword(jsonData);
    }


    @RequestMapping(value = "loginUser", method = RequestMethod.POST)
    public Object loginUser(@RequestBody String jsonData) {
        return userService.loginUser(jsonData);
    }

    @RequestMapping(value = "deleteUser", method = RequestMethod.DELETE)
    public Object deleteUser(int userId) {
        return userService.deleteUser(userId);
    }

    @RequestMapping(value = "exitUser", method = RequestMethod.POST)
    public Object exitUser(@RequestBody String jsonData) {
        return userService.logoutUser(jsonData);
    }

    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    public Object getUserInfoCookie(String userCode) {
        UserVO userVO = userService.getUserInfoCookie(userCode);
        return ResultVO.success(userVO);
    }

    @RequestMapping(value = "getUserInfoByNameOrNumber", method = RequestMethod.POST)
    public Object getUserInfoByNameOrNumber(@RequestBody String jsonData) {
        UserVO user = userService.getUserInfoByNameOrNumber(jsonData);
        if (EmptyUtils.isNotEmpty(user)) {
            return ResultVO.error("用户已存在");
        } else {
            return ResultVO.success("验证通过");
        }
    }

    @RequestMapping(value = "updateUser", method = RequestMethod.PUT)
    public Object updateUser(@RequestBody String jsonData) {
        UserVO user = JSONObject.parseObject(jsonData, UserVO.class);
        if(EmptyUtils.isEmpty(user.getUserCode()) && user.getId() > NumberEnum.ZERO.getValue()){
            UserVO userInfo = userService.getUserById(user.getId());
            if(EmptyUtils.isNotEmpty(userInfo)){
                user.setUserCode(userInfo.getUserCode());
            }
        }
        ResultVO resultVO = userService.validateUserInfoIsExist(user);
        if(EmptyUtils.isNotEmpty(resultVO)){
            return resultVO;
        }
        UserVO userVO = userService.updateUser(user);
        return ResultVO.success(userVO);
    }

    @PostMapping(value = "updateOrSaveImage")
    @ApiOperation(value = "保存或者修改图片")
    public Object updateOrSaveImage(@RequestBody @ApiParam UserVO userVO) {
        return userService.updateOrSaveImage(userVO);
    }


    @ApiOperation(value = "发送简历")
    @GetMapping(value = "sendResume")
    public void sendResume() {
        EmailUtils.sendResume("954118485@qq.com", null);
    }

    @ApiOperation(value = "验证")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机", name = "phone", required = true),
            @ApiImplicitParam(value = "验证码", name = "code", required = true)
    })
    @GetMapping(value = "verificateIphone")
    public Object verificateIphone(String phone, String code) {
        return userService.verificateIphone(phone, code);
    }

    @PostMapping(value = "batchUserInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "批量导入用户信息")
    @PermissionVerification
    @ApiImplicitParams({
            @ApiImplicitParam(value = "导入文件", name = "file", required = true),
            @ApiImplicitParam(value = "实例名", name = "instanceName", required = true)
    })
    public Object batchUserInfo(MultipartFile file, String instanceName, HttpServletResponse response) {
        if (EmptyUtils.isEmpty(instanceName)) {
            return ResultVO.error(instanceName + "实例名对象不能为空");
        }
        ImportResultPojo importInfos = ImportUtils.getImportInfos(file, new UserVO(), instanceName);
        ResultVO resultVO = userService.batchUserInfo(importInfos, false);
        return JSON.toJSONString(resultVO);
    }

    @GetMapping(value = "exportUserInfo")
    @ApiOperation(value = "导出用户信息")
    @PermissionVerification
    @ApiImplicitParams({
            @ApiImplicitParam(value = "导入文件", name = "fileName", required = true),
            @ApiImplicitParam(value = "实例名", name = "instanceName", required = true)
    })
    public Object exportUserInfo(String fileName, String instanceName, HttpServletResponse response) {
        if (EmptyUtils.isEmpty(instanceName)) {
            return ResultVO.error(StringFormatUtils.format("%s实例名对象不能为空", instanceName));
        }
        if (EmptyUtils.isEmpty(fileName)) {
            return ResultVO.error(StringFormatUtils.format("导出文件名不能为空"));
        }
        List<UserVO> allIsNotPosition = userService.getAllIsNotPosition();
        return ExportUtils.exportExcel(JSON.parseArray(JSON.toJSONString(allIsNotPosition)), response, instanceName, fileName);
    }

    @PostMapping(value = "exportQueryPersonnel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "人才储备信息导出")
    @PermissionVerification
    @ApiImplicitParams({
            @ApiImplicitParam(value = "导入文件", name = "fileName", required = true),
            @ApiImplicitParam(value = "实例名", name = "instanceName", required = true),
            @ApiImplicitParam(value = "用户编码", name = "userCodeList", required = true),
    })
    public Object exportQueryPersonnel(String fileName, String instanceName, HttpServletResponse response,
                                        @RequestBody @ApiParam List<String> userCodeList) {
        if (EmptyUtils.isEmpty(instanceName)) {
            return ResultVO.error(StringFormatUtils.format("%s实例名对象不能为空", instanceName));
        }
        if (EmptyUtils.isEmpty(fileName)) {
            return ResultVO.error(StringFormatUtils.format("导出文件名不能为空"));
        }
        List<UserPojo> allQueryPersonnel = userService.getAllJobSeeker(userCodeList);
        return ExportUtils.exportExcel(JSON.parseArray(JSON.toJSONString(allQueryPersonnel)), response, instanceName, fileName);
    }

    @GetMapping(value = "queryPersonnel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "人才储备信息查询")
    @PermissionVerification
    @ApiImplicitParams({
            @ApiImplicitParam(value = "起始页", name = "pageNum", required = false),
            @ApiImplicitParam(value = "每页大小", name = "pageSize", required = false)
    })
    public Object queryPersonnel(Integer pageNum, Integer pageSize) {
        if (EmptyUtils.isEmpty(pageNum) || EmptyUtils.isEmpty(pageSize) ||
                pageNum == NumberEnum.ZERO.getValue() || pageSize == NumberEnum.ZERO.getValue()) {
            pageNum = NumberEnum.ONE.getValue();
            pageSize = NumberEnum.TWENTY.getValue();
        }
        return ResultVO.success(userService.queryPersonnel(pageNum, pageSize));
    }

    @PostMapping(value = "batchImportPersonnel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "批量导入人才信息")
    @PermissionVerification
    @ApiImplicitParams({
            @ApiImplicitParam(value = "导入文件", name = "file", required = true),
            @ApiImplicitParam(value = "实例名", name = "instanceName", required = true)
    })
    public Object batchImportPersonnel(MultipartFile file, String instanceName) {
        if (EmptyUtils.isEmpty(instanceName)) {
            return ResultVO.error(instanceName + "实例名对象不能为空");
        }
        ImportResultPojo importInfos = ImportUtils.getImportInfos(file, new UserPojo(), instanceName);
        ResultVO resultVO = userService.batchImportPersonnel(importInfos);
        return JSON.toJSONString(resultVO);
    }

    @DeleteMapping(value = "batchDeleteByCode")
    @ApiOperation(value = "批量删除用户")
    @PermissionVerification
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户编码", name = "userCodeList", required = true),
    })
    public Object batchDeleteByCode(@RequestBody @ApiParam List<String> userCodeList) {
        if(EmptyUtils.isEmpty(userCodeList) || userCodeList.isEmpty()){
            return ResultVO.error("用户编码不能为空");
        }
        ResultVO resultVO = userService.batchDeleteByCode(userCodeList);
        return resultVO;
    }

    @PutMapping(value = "updatePersonnelByUserCode")
    @ApiOperation(value = "根据用户编码修改用户信息")
    @PermissionVerification
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户对象", name = "userPojo", required = true),
    })
    public Object updatePersonnelByUserCode(@RequestBody @ApiParam UserPojo userPojo) {
        ResultVO resultVO = userService.updatePersonnelByUserCode(userPojo);
        return resultVO;
    }

    @PostMapping(value = "addPersonnelByUserCode")
    @ApiOperation(value = "新增人才信息")
    @PermissionVerification
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户对象", name = "userPojo", required = true),
    })
    public Object addPersonnelByUserCode(@RequestBody @ApiParam UserPojo userPojo) {
        ResultVO resultVO = userService.addPersonnelByUserCode(userPojo);
        return resultVO;
    }



}
