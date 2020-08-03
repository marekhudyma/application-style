package com.marekhudyma.style.api.controller;


import com.marekhudyma.style.api.dto.ProblemDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(assignableTypes = {AccountController.class})
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    ProblemDto handleException(WebRequest request, MethodArgumentNotValidException cause) {
        return new ProblemDto(HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST.value(),
                cause.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    ProblemDto handleException(WebRequest request, MethodArgumentTypeMismatchException cause) {
        return new ProblemDto(HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST.value(),
                cause.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    ProblemDto handleException(WebRequest request, Exception cause) {
        return new ProblemDto(HttpStatus.INTERNAL_SERVER_ERROR.name(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                cause.getMessage());
    }

}