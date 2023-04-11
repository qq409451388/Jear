package com.poethan.jear.aspect;

import com.poethan.jear.anno.EzLocalLog;
import com.poethan.jear.utils.JsonUtils;
import com.poethan.jear.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Order(1)
@Component
public class EzLocalLogAspect {
    @Pointcut("@annotation(com.poethan.jear.anno.EzLocalLog)")
    public void doPointCut(){}

    @Around("doPointCut()")
    public Object localLog(ProceedingJoinPoint pjp) {
        try{
            MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
            Method method = methodSignature.getMethod();
            EzLocalLog ezLocalLog = method.getAnnotation(EzLocalLog.class);
            boolean isTime = ezLocalLog.EnableConsume();
            String className = methodSignature.getDeclaringType().getSimpleName();
            String methodName = methodSignature.getName();
            if(isTime){
                SystemUtils.startTimeKeeping();
            }
            Object args = pjp.getArgs();
            String traceId = SystemUtils.getTraceId();
            log.info("\n----------------[Gear] Invoke Enter----------------\n" +
                    "traceId:{}, class:{}, method:{}, with args:{}", traceId, className, methodName, JsonUtils.encode(args));
            Object ret = pjp.proceed();
            if(isTime){
                SystemUtils.endTimeKeeping();
                log.info("\n----------------[Gear] Invoke Leave----------------\n" +
                        "traceId:{}, class:{}, method:{}, with return:{} [consume {}ms]",
                        traceId, className, methodName, JsonUtils.encode(ret), SystemUtils.consume());
            }else{
                log.info("\n----------------[Gear] Invoke Leave----------------\n" +
                        "traceId:{}, class:{}, method:{}, with return:{}",
                        className, traceId, methodName, JsonUtils.encode(ret));
            }

            return ret;
        } catch(Throwable e) {
            /*Object tryTwice = this.tryAgain(pjp);
            if(Objects.nonNull(tryTwice)){
                log.warn("[Gear] {} Has Throwable!", EzLocalLog.class.getSimpleName());
                return tryTwice;
            }*/
            e.printStackTrace();
            return null;
        } finally {
            SystemUtils.closeTimeKeeping();
        }
    }

    private Object tryAgain(ProceedingJoinPoint pjp) throws Throwable{
        return pjp.proceed();
    }
}
