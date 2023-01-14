package com.github.lunchvotingsystem.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalTime;

public class VoteChangeAfterDeadlineException extends AppException {
    public static final String VOTE_CHANGE_AFTER_DEADLINE_EXCEPTION = "Cannot change vote after ";

    public VoteChangeAfterDeadlineException(LocalTime deadline) {
        super(HttpStatus.BAD_REQUEST, VOTE_CHANGE_AFTER_DEADLINE_EXCEPTION + deadline);
    }
}
