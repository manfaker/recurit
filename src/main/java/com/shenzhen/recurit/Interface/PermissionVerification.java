package com.shenzhen.recurit.Interface;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionVerification {
    String authorities() default "";
}
