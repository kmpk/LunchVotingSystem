package com.github.lunchvotingsystem.exception;

import org.springframework.http.HttpStatus;

public class IllegalVoteDayException extends AppException {
    public static final String ILLEGAL_VOTE_DAY_EXCEPTION = "Only today voting is allowed";

    public IllegalVoteDayException() {
        super(HttpStatus.BAD_REQUEST, ILLEGAL_VOTE_DAY_EXCEPTION);
    }
}
