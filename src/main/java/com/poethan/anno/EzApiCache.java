package com.poethan.gear.anno;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface EzApiCache {
    /**
     * 过期时间
     */
    public int expire() default 3600;
}
