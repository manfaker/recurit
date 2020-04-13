package com.shenzhen.recurit.service;

import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;

public interface UserService {

    /**
     * 根据手机号或者邮箱号获取动态验证码
     * @param number
     * @return
     */
    Object getVerificationCode(String number);

    /**
     * 根据邮箱或者电话号码创建用户
     * @param jsonData
     * @return
     */
    Object addByNumber(String jsonData);

    /**
     * 添加用户
     * @param userVO
     * @return
     */
    Object addUser(UserVO userVO);

    /**
     * 通过用户名进行查找
     * @param userName
     * @return
     */
    UserVO getUserByName(String userName);

    /**
     * 通过手机进行查找
     * @param phone
     * @return
     */
    UserVO getUserByPhone(String phone);

    /**
     * 通过用户名进行查找
     * @param email
     * @return
     */
    UserVO getUserByEmail(String email);

    /**
     * 登录用户
     * @param jsonData
     * @return
     */
    Object loginUser(String jsonData);
    /**
     *退出用户
     * @param jsonData
     * @return
     */
    Object logoutUser(String jsonData);

    UserVO getUserInfoCookie(String userCode);

    UserVO updateUser(UserVO user);

    /**
     * 修改密码
     * @param  jsonData
     */
    ResultVO updatePassword(String jsonData);

    UserVO getUserInfoByNameOrNumber(String jsonData);

    /**
     * 删除用户
     * @param userId 用户id
     * @return
     */
    ResultVO deleteUser(int userId);

    /**
     * 保存或者修改用户头像信息
     * @param userVO
     * @return
     */
    ResultVO updateOrSaveImage(UserVO userVO);

    /**
     * 根据用户编码查询用户信息
     * @param userCode
     * @return
     */
    UserVO getUserCode(String userCode);
}
