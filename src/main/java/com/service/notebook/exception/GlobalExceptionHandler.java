package com.service.notebook.exception;

import com.service.notebook.model.ExceptionResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class, IncorrectDateException.class,
            ServletRequestBindingException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseData handle(Exception exception) {
        return ExceptionResponseData.builder().errorCode("incorrectRequestData").errorMessage(exception.getMessage()).build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponseData handleUserNotFoundException(EntityNotFoundException exception) {
        return ExceptionResponseData.builder().errorCode("entityNotFoundException").errorMessage(exception.getMessage()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseData handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        return ExceptionResponseData.builder().errorCode("incorrectRequestData")
                .errorMessage(exception.getFieldErrors().stream()
                        .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                        .collect(Collectors.joining(", ")))
                .build();
    }
}
