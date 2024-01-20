package com.company.exception;
import com.company.annotation.DefaultExceptionMessage;
import com.company.dto.DefaultExceptionMessageDto;
import com.company.entity.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import java.lang.reflect.Method;
import java.util.Optional;
/*
 * 🖍️...
 * 1. Created a custom TicketingProjectException exception that extends from java Exception class.
 * 2. Created a custom @efaultExceptionMessage annotation to use on a method level.
 * 3. Created a DefaultExceptionMessageDto class and a String message field to use to store the DefaultExceptionMessage annotation custom message.
 * 4. Created and configured the GlobalExceptionHandler class, so Spring looks into this class first while handling the exception.
 * 5. Used custom @efaultExceptionMessage annotation in the controller method.
 *
 * 🖍️...
 * · @RestControllerAdvice: It allows handling exceptions across the whole application. Basically, it acts as an Exception interceptor.
 *   Rest Controller Advice’s methods (annotated with @ExceptionHandler) are shared globally across multiple @Controller components
 *   to capture exceptions and translate them to HTTP responses.
 * · @ExceptionHandler annotation indicates which type of Exception we want to handle. The exception instance and the request will be injected via method arguments.
 *
 */
@RestControllerAdvice // Spring will check this class first before throwing an exception.
public class GlobalExceptionHandler { // This is a ready configuration to implement exceptions.

    @ExceptionHandler(TicketingProjectException.class) // If an exception belongs to the TicketingProjectException, this method will run.
    public ResponseEntity<ResponseWrapper> serviceException(TicketingProjectException se){
        String message = se.getMessage();
        return new ResponseEntity<>(ResponseWrapper.builder().success(false).code(HttpStatus.CONFLICT.value()).message(message).build(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class) // If an exception belongs to the AccessDeniedException, this method will run.
    public ResponseEntity<ResponseWrapper> accessDeniedException(AccessDeniedException se){
        String message = se.getMessage();
        return new ResponseEntity<>(ResponseWrapper.builder().success(false).code(HttpStatus.FORBIDDEN.value()).message(message).build(),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class, BadCredentialsException.class}) // Otherwise, this method will run.
    public ResponseEntity<ResponseWrapper> genericException(Throwable e, HandlerMethod handlerMethod) {
        /*
         * 🖍️...
         * · We created a custom @DefaultExceptionMessage annotation to use on the method level.
         * · The below if statement condition means:
         *   If we have the @DefaultExceptionMessage annotation at the method level in the controller class, display that message.
         *   Ex: In the Controller class, we have the deleteUser method annotated with @DefaultExceptionMessage(defaultMessage = "Failed to delete user").
         */
        Optional<DefaultExceptionMessageDto> defaultMessage = getMessageFromAnnotation(handlerMethod.getMethod());
        if (defaultMessage.isPresent() && !ObjectUtils.isEmpty(defaultMessage.get().getMessage())) {
            ResponseWrapper response = ResponseWrapper
                    .builder()
                    .success(false)
                    .message(defaultMessage.get().getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(ResponseWrapper.builder().success(false).message("Action failed: An error occurred!").code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Optional<DefaultExceptionMessageDto> getMessageFromAnnotation(Method method) {
        DefaultExceptionMessage defaultExceptionMessage = method.getAnnotation(DefaultExceptionMessage.class);
        if (defaultExceptionMessage != null) {
            DefaultExceptionMessageDto defaultExceptionMessageDto = DefaultExceptionMessageDto
                    .builder()
                    .message(defaultExceptionMessage.defaultMessage()) // It is a @DefaultExceptionMessage method.
                    .build();
            return Optional.of(defaultExceptionMessageDto);
        }
        return Optional.empty();
    }




}