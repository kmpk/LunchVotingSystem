package com.github.lunchvotingsystem.exception;

import org.springframework.http.HttpStatus;

public class ModificationRestrictionException extends AppException {

    public static final String MODIFICATION_RESTRICTION_EXCEPTION = "User %d modification is restricted";

    public ModificationRestrictionException(int userId) {
        super(HttpStatus.BAD_REQUEST, String.format(MODIFICATION_RESTRICTION_EXCEPTION, userId));
    }
}
