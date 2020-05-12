package com.shenzhen.recurit.dao;

import com.shenzhen.recurit.vo.UserVO;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    /**
     * 通过email查找用户
     * @param email
     * @return
     */
    UserVO getUserByEmail(String email);
    /**
     * 通过手机查找用户
     * @param phone
     * @return
     */
    UserVO getUserByPhone(String phone);
    /**
     * 通过用户名查找用户
     * @param userName
     * @return
     */
    UserVO getUserByName(String userName);

    /**
     * 新建用户
     * @param userVO
     */
    void addUser(UserVO userVO);

    /**
     * 通过用户名和密码查找用户
     * @param userName
     * @param password
     * @return
     */
    UserVO getUserByNameAndPass(@Param("userName") String userName,@Param("password") String password);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    int updateUser(UserVO user);

    /**
     * 根据id查找用户信息
     * @param id
     * @return
     */
    UserVO getUserById(int id);

    /**
     * 根据用户编码查找用户
     * @param userCode
     * @return
     */
    UserVO getUserCode(String userCode);

    UserVO getUserByNameOrEmailOrPhone(String userName);
}
