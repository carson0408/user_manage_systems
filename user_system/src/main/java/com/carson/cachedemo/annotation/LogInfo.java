package com.carson.cachedemo.annotation;

import com.carson.cachedemo.aspect.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * InterfaceName LogInfo
 *
 * @author zhanghangfeng5
 * @description
 * @Version V1.0
 * @createTime
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogInfo {

    /**
     * 当前时间
     * @return
     */
    long time();

    /**
     * 日志内容
     * @return
     */
    String info() default "";

    /**
     * 日志类型
     * @return
     */
    LogType logtype() default LogType.INFO;

}
