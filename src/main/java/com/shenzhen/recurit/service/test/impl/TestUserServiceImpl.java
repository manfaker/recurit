package com.shenzhen.recurit.service.test.impl;

import com.shenzhen.recurit.dao.test.TestUserMapper;
import com.shenzhen.recurit.service.test.TestUserService;
import com.shenzhen.recurit.vo.test.TestUserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TestUserServiceImpl implements TestUserService {

    @Resource
    private TestUserMapper testUserMapper;

    @Override
    public TestUserVO getUserByName(String name) {
        return testUserMapper.getUserByName(name);
       // return null;
    }
}
