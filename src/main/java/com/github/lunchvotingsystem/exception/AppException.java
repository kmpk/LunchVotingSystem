package com.github.lunchvotingsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public abstract class AppException extends ResponseStatusException {

    protected AppException(HttpStatus status, String message) {
        super(status, message);
    }

    @Override
    public String getMessage() {
        return getReason();
    }
}
