package com.carson.cachedemo.aspect;

import com.carson.cachedemo.annotation.LogInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

/**
 * ClassName LogAspect
 *
 * @author zhanghangfeng5
 * @description
 * @Version V1.0
 * @createTime
 */
@Component
@Aspect
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);


    @Pointcut("@annotation(com.carson.cachedemo.annotation.LogInfo)")
    public void pointcut(){

    }



    @Around("pointcut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String url = request.getRequestURI();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        LogInfo logInfo = signatureMethod.getDeclaredAnnotation(LogInfo.class);
        Date time = new Date(System.currentTimeMillis());
        LogType type = logInfo.logtype();
        switch (type){
            case INFO:
                logger.info("用户请求url={},time={},value={}",url,time,logInfo.info());
                break;
            case DEBUG:
                logger.debug("用户请求url={},time={},value={}",url,time,logInfo.info());
                break;
            case ERROR:
                logger.error("用户请求url={},time={},value={}",url,time,logInfo.info());
                break;
                default:
                    logger.info("用户请求url={},time={},value={}",url,time,logInfo.info());
                    break;




        }
    }
}
