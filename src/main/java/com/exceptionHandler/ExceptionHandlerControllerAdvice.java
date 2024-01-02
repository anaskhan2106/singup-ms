package com.exceptionHandler;

import com.dto.ExceptionResponseDto;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ExceptionResponseDto handleResourceNotFound(final ResourceNotFoundException exception,
                                                final HttpServletRequest request) {

        ExceptionResponseDto error = new ExceptionResponseDto();
        error.setErrorMessage(exception.getMessage());
        error.callerURL(request.getRequestURI());
        return error;
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public @ResponseBody ExceptionResponseDto handleIllegalStateException(final Exception exception,
                                                              final HttpServletRequest request) {
        ExceptionResponseDto error = new ExceptionResponseDto();
        error.setErrorMessage(exception.getMessage());
        error.callerURL(request.getRequestURI());
        return error;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ExceptionResponseDto handleException(final Exception exception,
                                                           final HttpServletRequest request) {

        ExceptionResponseDto error = new ExceptionResponseDto();
        error.setErrorMessage(exception.getMessage());
        error.callerURL(request.getRequestURI());
        return error;
    }
}
