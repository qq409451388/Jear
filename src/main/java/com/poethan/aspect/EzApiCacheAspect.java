package com.poethan.gear.aspect;

import com.poethan.gear.anno.EzApiCache;
import com.poethan.gear.module.cache.EzLocalCache;
import com.poethan.gear.utils.EncodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
@Aspect
@Order(1)
@Component
public class EzApiCacheAspect {
    @Resource
    private EzLocalCache ezCache;

    @Pointcut("@annotation(com.poethan.gear.anno.EzApiCache)")
    public void doPointCut(){}

    @Around("doPointCut()")
    public Object localLog(ProceedingJoinPoint pjp) {
        try{
            MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
            Method method = methodSignature.getMethod();
            EzApiCache ezApiCache = method.getAnnotation(EzApiCache.class);
            int expire = ezApiCache.expire();
            String className = methodSignature.getDeclaringType().getSimpleName();
            String methodName = methodSignature.getName();
            Object[] args = pjp.getArgs();
            StringBuilder keySb = new StringBuilder(className)
                    .append("::")
                    .append(methodName)
                    .append("(");
            for (Object arg : args) {
                keySb.append(EncodeUtils.dump(arg));
                keySb.append(",");
            }
            if (args.length > 0) {
                keySb.deleteCharAt(keySb.length()-1);
            }
            keySb.append(")");
            String key = keySb.toString();
            if (Objects.nonNull(ezCache.getSource(key))) {
                Object source = ezCache.getSource(key);
                log.info(key + " return "+ EncodeUtils.dump(source)+" from cache");
                return source;
            }

            Object ret = pjp.proceed();
            if (Objects.nonNull(ret)) {
                ezCache.setSourceEX(key, ret, expire);
            }

            return ret;
        } catch(Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
