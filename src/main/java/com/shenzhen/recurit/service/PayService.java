package com.shenzhen.recurit.service;

import com.shenzhen.recurit.vo.ResultVO;

import javax.servlet.http.HttpServletRequest;

public interface PayService {

    ResultVO alipayCallBackReturn(HttpServletRequest request);

    ResultVO alipayAsyncReturn();

    ResultVO getInfoByOrderNo();
}
