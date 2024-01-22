package com.company.acpect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j // When applied to a class, it automatically creates a static SLF4J logger instance named log.
public class PerformanceAspect {

//    Logger log = LoggerFactory.getLogger(PerformanceAspect.class);


    @Pointcut("@annotation(com.company.annotation.ExecutionTime)")
    private void anyExecutionTimeOperation(){}

    @Around("anyExecutionTimeOperation()")
    public Object anyExecutionTimeOperationAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        long beforeTime = System.currentTimeMillis(); // time = execution started.
        Object result = null;
        log.info("Execution will start");
        try {
            result = proceedingJoinPoint.proceed(); // execution time
        } catch (Throwable e) {
            e.printStackTrace();
        }
        long afterTime = System.currentTimeMillis(); // time = execution completed.
        log.info("Time taken to execute : {} ms - Method: {} - Parameters: {}",
                (afterTime-beforeTime), proceedingJoinPoint.getSignature().toShortString(),proceedingJoinPoint.getArgs());
        return result;
        /*
         * · We used the @ExecutionTime annotation in the User Controller at the method level.
         * · Output:
         * 1. Log info: Execution will start
         * 2. Log info: Time taken to execute : 12 ms - Method: UserController.getUsers() - Parameters: []
         */
    }






}
