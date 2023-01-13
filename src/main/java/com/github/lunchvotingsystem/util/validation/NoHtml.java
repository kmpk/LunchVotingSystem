package com.github.lunchvotingsystem.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = NoHtmlValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface NoHtml {
    String EXCEPTION_NO_HTML = "no HTML is allowed";

    String message() default EXCEPTION_NO_HTML;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
