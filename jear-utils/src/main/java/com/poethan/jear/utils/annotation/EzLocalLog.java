package com.poethan.jear.utils.annotation;

import java.lang.annotation.*;
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface EzLocalLog {
    boolean EnableConsume() default false;
}
