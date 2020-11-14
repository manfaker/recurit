package com.shenzhen.recurit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.enums.ReturnEnum;
import com.shenzhen.recurit.pojo.ImportResultPojo;
import com.shenzhen.recurit.pojo.UserPojo;
import com.shenzhen.recurit.service.UserService;
import com.shenzhen.recurit.utils.EmailUtils;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.RedisTempleUtils;
import com.shenzhen.recurit.utils.StringFormatUtils;
import com.shenzhen.recurit.utils.excel.ExportUtils;
import com.shenzhen.recurit.utils.excel.ImportUtils;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
        UserVO currUser = null;
        if (EmptyUtils.isNotEmpty(user.getUserName())) {
            currUser = userService.getUserByName(user.getUserName());
            if (EmptyUtils.isNotEmpty(currUser) && user.getId() != currUser.getId()) {
                return ResultVO.error("用户名已存在，请重新填写");
            }
        }
        if (EmptyUtils.isNotEmpty(user.getPhone())) {
            currUser = userService.getUserByPhone(user.getPhone());
            if (EmptyUtils.isNotEmpty(currUser) && user.getId() != currUser.getId()) {
                return ResultVO.error("手机号已存在，请重新填写");
            }
        }
        if (EmptyUtils.isNotEmpty(user.getEmail())) {
            currUser = userService.getUserByEmail(user.getEmail());
            if (EmptyUtils.isNotEmpty(currUser) && user.getId() != currUser.getId()) {
                return ResultVO.error("邮箱已存在，请重新填写");
            }
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
    @ApiImplicitParams({
            @ApiImplicitParam(value = "导入文件", name = "file", required = true),
            @ApiImplicitParam(value = "实例名", name = "instanceName", required = true)
    })
    public Object batchUserInfo(MultipartFile file, String instanceName, HttpServletResponse response) {
        if (EmptyUtils.isEmpty(instanceName)) {
            return ResultVO.error(instanceName + "实例名对象不能为空");
        }
        ImportResultPojo importInfos = ImportUtils.getImportInfos(file, new UserVO(), instanceName);
        ResultVO resultVO = userService.batchUserInfo(importInfos);
        return JSON.toJSONString(resultVO);
    }

    @GetMapping(value = "exportUserInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "导出用户信息")
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
        return ExportUtils.exportExcel(JSON.parseArray(JSON.toJSONString(allIsNotPosition)),response,instanceName,fileName);
    }

    @GetMapping(value = "exportQueryPersonnel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "人才储备信息导出")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "导入文件", name = "fileName", required = true),
            @ApiImplicitParam(value = "实例名", name = "instanceName", required = true)
    })
    public Object exportQueryPersonnel(String fileName, String instanceName, HttpServletResponse response) {
        if (EmptyUtils.isEmpty(instanceName)) {
            return ResultVO.error(StringFormatUtils.format("%s实例名对象不能为空", instanceName));
        }
        if (EmptyUtils.isEmpty(fileName)) {
            return ResultVO.error(StringFormatUtils.format("导出文件名不能为空"));
        }
        List<UserVO> allIsNotPosition = userService.getAllIsNotPosition();
        return ExportUtils.exportExcel(JSON.parseArray(JSON.toJSONString(allIsNotPosition)),response,instanceName,fileName);
    }

    @GetMapping(value = "queryPersonnel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "人才储备信息查询")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "起始页", name = "pageNum", required = false),
            @ApiImplicitParam(value = "每页大小", name = "pageSize", required = false)
    })
    public Object queryPersonnel(Integer pageNum, Integer pageSize) {
        if(EmptyUtils.isEmpty(pageNum)||EmptyUtils.isEmpty(pageSize)||
                pageNum== NumberEnum.ZERO.getValue()||pageSize==NumberEnum.ZERO.getValue()){
            pageNum =NumberEnum.ONE.getValue();
            pageSize=NumberEnum.TWENTY.getValue();
        }
        return ResultVO.success(userService.queryPersonnel(pageNum,pageSize));
    }

}
