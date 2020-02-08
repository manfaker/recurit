package com.shenzhen.recurit.dao.test;

import com.shenzhen.recurit.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

//@Mapper
public interface TestUserMapper {

    /**
     * 根据姓名查找人物信息
     * @param name
     * @return
     */
    UserVo getUserByName(String name);
}
