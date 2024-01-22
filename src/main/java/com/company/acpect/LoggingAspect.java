package com.company.acpect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
@Configuration
@Slf4j // When applied to a class, it automatically creates a static SLF4J logger instance named log.
public class LoggingAspect {

    //    Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    private String getUserName(){ // Getting an authorized user.
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount details = (SimpleKeycloakAccount) authentication.getDetails();
        return  details.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }




    @Pointcut("execution(* com.company.controller.ProjectController.*(..)) || execution(* com.company.controller.TaskController.*(..))")
    private void anyControllerOperation(){}

    @Before("anyControllerOperation()")
    public void anyBeforeControllerOperationAdvice(JoinPoint joinPoint) {
        String username = getUserName();
        log.info("Before () -> User : {} - Method: {} - Parameters: {}", username, joinPoint.getSignature().toShortString(), joinPoint.getArgs());
        // Log info: Before () -> User : mike - Method: ProjectController.getProjectByCode(..) - Parameters: [1]
    }

    @AfterReturning(pointcut = "anyControllerOperation()", returning = "results") // If there is an exception, it will return IllegalArgumentException
    public void anyAfterControllerOperationAdvice(JoinPoint joinPoint, Object results) {
        String username = getUserName();
        log.info("AfterReturning  -> User : {} - Method : {} - Results: {}", username, joinPoint.getSignature().toShortString(), results.toString());
        // Log info: AfterReturning  -> User : mike - Method : ProjectController.getProjects() - Results: <200 OK OK,com.company.entity.ResponseWrapper@75bfbfdb,[]>

    }

    @AfterThrowing(pointcut = "anyControllerOperation()", throwing = "exception")
    public void anyAfterThrowingControllerOperationAdvice(JoinPoint joinPoint, RuntimeException exception) {
        String username = getUserName();
        log.info("AfterThrowing  -> User : {} - Method : {} - Exception: {}", username, joinPoint.getSignature().toShortString(), exception.getMessage());
        // Log info: AfterThrowing  -> User : mike - Method : ProjectController.getProjectByCode(..) - Exception: source cannot be null
    }








}
