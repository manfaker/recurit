package com.shenzhen.recurit.dao.test;

import com.shenzhen.recurit.vo.test.TestUserVO;

//@Mapper
public interface TestUserMapper {

    /**
     * 根据姓名查找人物信息
     * @param name
     * @return
     */
    TestUserVO getUserByName(String name);
}
