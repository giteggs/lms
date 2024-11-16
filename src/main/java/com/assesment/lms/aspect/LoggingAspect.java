package com.assesment.lms.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.assesment.lms..*(..))")
    public void logEntry(JoinPoint joinPoint) {
        log.info("API Entry: {}() with argument[s] = {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.assesment.lms..*(..))", returning = "result")
    public void logResult(JoinPoint joinPoint, Object result) {
        log.info("API Response: {}() with response = {}", joinPoint.getSignature().getName(), result);
    }

    @Around("execution(* com.assesment.lms..*(..))")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long timeTaken = System.currentTimeMillis() - startTime;
        log.info("API Exit: {}() with time taken = {} ms", joinPoint.getSignature().getName(), timeTaken);
        return result;
    }
}
