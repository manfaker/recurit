package com.shenzhen.recurit.service.test;

import com.shenzhen.recurit.vo.UserVo;

public interface TestUserService {
    /**
     * 根据姓名查找人物信息
     * @param name
     * @return
     */
    UserVo getUserByName(String name);
}
