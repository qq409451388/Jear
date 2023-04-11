package com.poethan.jear.anno;

import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识为定时任务对象，在启动时加载到启动
 */
@Retention(RetentionPolicy.RUNTIME)
@Order(99)
@Target(ElementType.TYPE)
public @interface EzSchdulerJob {
}