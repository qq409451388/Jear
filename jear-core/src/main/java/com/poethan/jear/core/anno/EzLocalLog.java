package com.poethan.jear.core.anno;

import java.lang.annotation.*;
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface EzLocalLog {
    boolean EnableConsume() default false;
}
