package com.github.lunchvotingsystem;

import com.github.lunchvotingsystem.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.lunchvotingsystem.model.Dish.DISH_DUPLICATE_NAME_EXCEPTION;
import static com.github.lunchvotingsystem.model.Dish.DISH_NAME_MENU_DATE_RESTAURANT_CONSTRAINT;
import static com.github.lunchvotingsystem.model.Restaurant.RESTAURANT_ADDRESS_CONSTRAINT;
import static com.github.lunchvotingsystem.model.Restaurant.RESTAURANT_DUPLICATE_ADDRESS_EXCEPTION;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail body = ex.updateAndGetBody(null, LocaleContextHolder.getLocale());
        Map<String, String> invalidParams = new LinkedHashMap<>();
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            invalidParams.put(error.getObjectName(), error.getDefaultMessage());
        }
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            invalidParams.put(error.getField(), error.getDefaultMessage());
        }
        body.setProperty("invalid_params", invalidParams);
        body.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        return handleExceptionInternal(ex, body, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unexpectedException(Exception ex, WebRequest request) {
        log.error("Unexpected exception: {}", ex.getMessage());
        return createProblemDetailExceptionResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appException(AppException ex, WebRequest request) {
        log.error("ApplicationException: {}", ex.getMessage());
        return createProblemDetailExceptionResponse(ex, ex.getStatusCode(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        log.error("DataIntegrityViolationException: {}", ex.getMessage());
        if (ex.getMessage().contains(RESTAURANT_ADDRESS_CONSTRAINT.toUpperCase())) {
            return createCustomDataIntegrityViolationResponse(ex, request, "address", RESTAURANT_DUPLICATE_ADDRESS_EXCEPTION);
        }
        if (ex.getMessage().contains(DISH_NAME_MENU_DATE_RESTAURANT_CONSTRAINT.toUpperCase())) {
            return createCustomDataIntegrityViolationResponse(ex, request, "name", DISH_DUPLICATE_NAME_EXCEPTION);
        }
        return createProblemDetailExceptionResponse(ex, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    private ResponseEntity<Object> createCustomDataIntegrityViolationResponse(DataIntegrityViolationException ex, WebRequest request, String field, String exceptionMessage) {
        ProblemDetail body = createProblemDetail(ex, HttpStatus.UNPROCESSABLE_ENTITY, "Invalid request content.", null, null, request);
        Map<String, String> invalidParams = new LinkedHashMap<>();
        invalidParams.put(field, exceptionMessage);
        body.setProperty("invalid_params", invalidParams);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    private ResponseEntity<?> createProblemDetailExceptionResponse(Exception ex, HttpStatusCode statusCode, WebRequest request) {
        ProblemDetail body = createProblemDetail(ex, statusCode, ex.getMessage(), null, null, request);
        return handleExceptionInternal(ex, body, new HttpHeaders(), statusCode, request);
    }
}
