package com.carson.cachedemo.aspect;

import com.carson.cachedemo.annotation.Limit;
import com.carson.cachedemo.util.RequestHolder;
import com.carson.cachedemo.util.StringUtils;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

/**
 * ClassName LimitAspect
 *
 * @author zhanghangfeng5
 * @description
 * @Version V1.0
 * @createTime
 */
@Aspect
@Component
public class LimitAspect {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplates;
    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);

    public LimitAspect(RedisTemplate<Object,Object> redisTemplate) {
        this.redisTemplates = redisTemplate;
    }

    @Pointcut("@annotation(com.carson.cachedemo.annotation.Limit)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        Limit limit = signatureMethod.getAnnotation(Limit.class);
        System.out.println("测试呢"+limit.toString());
        LimitType limitType = limit.limitType();
        String key = limit.key();
        if (StringUtils.isEmpty(key)) {
            if (limitType == LimitType.IP) {
                key = StringUtils.getIp(request);
            } else {
                key = signatureMethod.getName();
            }
        }

        ImmutableList<Object> keys = ImmutableList.of(StringUtils.join(limit.prefix(), "_", key, "_", request.getRequestURI().replaceAll("/","_")));

        String luaScript = buildLuaScript();
        RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
        Number count = redisTemplates.execute(redisScript, keys, limit.count(), limit.period());
        if (null != count && count.intValue() <= limit.count()) {
            logger.info("第{}次访问key为 {}，描述为 [{}] 的接口", count, keys, limit.name());
            return joinPoint.proceed();
        } else {
            throw new Exception("访问次数受限制");
        }
    }

    /**
     * 限流脚本
     * @return
     */
    private String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }
}
