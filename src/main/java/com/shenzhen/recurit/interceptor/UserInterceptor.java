package com.shenzhen.recurit.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.enums.SymbolEnum;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.Md5EncryptUtils;
import com.shenzhen.recurit.utils.RedisTempleUtils;
import com.shenzhen.recurit.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UserInterceptor.class);
    @Resource
    private RedisTempleUtils redisTempleUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authUser= String.valueOf(request.getAttribute(InformationConstant.AUTH_USER));
        //获取用户的ip地址，从而存入服务器，进行对应
        String ipAddr = UserInterceptor.getIpAddress(request);
        if(hasPermission(handler,authUser)){
            response.sendRedirect("/recurit/user/reLogin");
            return false;
        }

        return true;
    }

    //获取用户的IP地址
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddr = request.getHeader(InformationConstant.X_FORWARDED_FOR);
        logger.info(InformationConstant.X_FORWARDED_FOR+"地址是：" +ipAddr);
        if (EmptyUtils.isEmpty(ipAddr) || InformationConstant.UNKNOWN.equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader(InformationConstant.PROXY_CLIENT_IP);
            logger.info(InformationConstant.PROXY_CLIENT_IP+"地址是：" +ipAddr);
        }
        if (EmptyUtils.isEmpty(ipAddr) || InformationConstant.UNKNOWN.equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader(InformationConstant.WL_PROXY_CLIENT_IP);
            logger.info(InformationConstant.WL_PROXY_CLIENT_IP+"地址是：" +ipAddr);
        }
        if (EmptyUtils.isEmpty(ipAddr) || InformationConstant.UNKNOWN.equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getRemoteAddr();
            logger.info("remoteAddr地址是：" +ipAddr);
        }
        if (ipAddr.contains(SymbolEnum.COMMA.getValue())) {
            return ipAddr.split(SymbolEnum.COMMA.getValue())[NumberEnum.ZERO.getValue()];
        } else {
            return ipAddr;
        }
    }


    private boolean hasPermission(Object handler,String authUser){
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod=(HandlerMethod)handler;
            // 获取方法上的注解
            PermissionVerification requiredPermission = handlerMethod.getMethod().getAnnotation(PermissionVerification.class);
            if(EmptyUtils.isNotEmpty(requiredPermission)){
                String value = redisTempleUtils.getValue(Md5EncryptUtils.encryptMd5(authUser), String.class);
                UserVO userVO = JSONObject.parseObject(value,UserVO.class);
                if(EmptyUtils.isNotEmpty(userVO)){
                    return false;
                }else{
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
