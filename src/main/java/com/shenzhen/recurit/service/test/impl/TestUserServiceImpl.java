package com.shenzhen.recurit.service.test.impl;

import com.shenzhen.recurit.dao.test.TestUserMapper;
import com.shenzhen.recurit.service.test.TestUserService;
import com.shenzhen.recurit.vo.UserVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TestUserServiceImpl implements TestUserService {

    @Resource
    private TestUserMapper userMapper;

    @Override
    public UserVo getUserByName(String name) {
        return userMapper.getUserByName(name);
    }
}
