package com.aron.flightschedule.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ControllerErrorResponse handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {

        log.error("handleResourceNotFoundException(): exception=" + exception + ", request=" + request, exception);

        String errorMsg = exception.getMessage();
        return ControllerErrorResponse.builder()
                .timestamp(getTimestamp())
                .status(HttpStatus.NOT_FOUND.value())
                .exceptionMessage(errorMsg)
                .exceptionClass(getExceptionClass(exception))
                .path(getPath(request))
                .build();
    }

    // NOTE : add handling other exceptions here ...

    private static long getTimestamp() {
        return System.currentTimeMillis();
    }

    private static String getExceptionClass(Exception exception) {
        return exception.getClass().getName();
    }

    private static Optional<String> getPath(WebRequest request) {
        String desc = request.getDescription(false);
        return Optional.ofNullable(desc.substring(desc.indexOf("/")));
    }

}
