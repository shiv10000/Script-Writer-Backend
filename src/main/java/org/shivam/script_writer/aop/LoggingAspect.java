package org.shivam.script_writer.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class LoggingAspect {

    public static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    // return type, class-name.method-name(args)

    @Before("execution(* org.shivam.script_writer.service.UserCategoryService.getAllUserCategory(..)) || execution(* org.shivam.script_writer.service.UserCategoryService.deleteUserCategory(..))")
    public void logMethodCall(JoinPoint jp){
        LOGGER.info("Method call " + jp.getSignature().getName());
    }

}
