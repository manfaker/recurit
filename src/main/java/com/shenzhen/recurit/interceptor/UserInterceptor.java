package com.shenzhen.recurit.interceptor;

import com.shenzhen.recurit.Interface.PermissionVerification;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.utils.EmptyUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authUser= String.valueOf(request.getAttribute(InformationConstant.AUTH_USER));

        return true;
    }

    private boolean hasPermission(Object handler,String authUser){
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod=(HandlerMethod)handler;
            // 获取方法上的注解
            PermissionVerification requiredPermission = handlerMethod.getMethod().getAnnotation(PermissionVerification.class);
            if(EmptyUtils.isNotEmpty(requiredPermission)){

            }
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
