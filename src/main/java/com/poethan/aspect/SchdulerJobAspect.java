package com.poethan.gear.aspect;

import com.poethan.gear.module.schduler.BaseSchduler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Aspect
public class SchdulerJobAspect {
    @Autowired
    private List<BaseSchduler> schdulerList;

    @Pointcut(value =  "@annotation(com.poethan.gear.anno.EzSchdulerJob)")
    public void jobPointCut(){
    }

    @Around(value = "jobPointCut()")
    public Object jobPointCutRun(ProceedingJoinPoint pjp) {
        log.error("SchdulerJob({}) Register Fail!", pjp.getClass().getSimpleName());
        try{
            Object ret = pjp.proceed();
            return ret;
        } catch(Throwable e) {
            log.error("SchdulerJob({}) Register Fail!", pjp.getClass().getSimpleName());
            return null;
        }
    }
}
