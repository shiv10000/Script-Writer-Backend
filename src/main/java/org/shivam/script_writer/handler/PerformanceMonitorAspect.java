package org.shivam.script_writer.handler;


import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;



@Component
@Aspect
public class PerformanceMonitorAspect {

    public static final Logger LOGER = LoggerFactory.getLogger(PerformanceMonitorAspect.class);




    @Around("execution(* org.shivam.script_writer.service.*(..))")
    public Object monitorTime(ProceedingJoinPoint jp){


        long start = System.currentTimeMillis();

        Object object;

        try {
             object = jp.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        long end =  System.currentTimeMillis();

        LOGER.info("Time taken "+(end-start));

        return object;


    }

}
