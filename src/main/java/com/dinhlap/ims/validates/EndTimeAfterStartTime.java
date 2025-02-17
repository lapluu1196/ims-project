package com.dinhlap.ims.validates;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EndTimeAfterStartTimeValidator.class)
@Target({ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface EndTimeAfterStartTime {

    String message() default "End time must be after start time!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String startTimeField();

    String endTimeField();
}
