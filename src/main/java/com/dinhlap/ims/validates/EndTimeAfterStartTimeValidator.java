package com.dinhlap.ims.validates;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalTime;

public class EndTimeAfterStartTimeValidator implements ConstraintValidator<EndTimeAfterStartTime, Object> {

    private String startTimeField;
    private String endTimeField;

    @Override
    public void initialize(EndTimeAfterStartTime constraintAnnotation) {
        this.startTimeField = constraintAnnotation.startTimeField();
        this.endTimeField = constraintAnnotation.endTimeField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field startTimeField = value.getClass().getDeclaredField(this.startTimeField);
            Field endTimeField = value.getClass().getDeclaredField(this.endTimeField);

            startTimeField.setAccessible(true);
            endTimeField.setAccessible(true);

            Object startTime = startTimeField.get(value);
            Object endTime = endTimeField.get(value);

            if (startTime == null || endTime == null) {
                return true;
            }

            if (!(startTime instanceof LocalTime) || !(endTime instanceof LocalTime)) {
                throw new IllegalArgumentException("Fields must be of type LocalTime");
            }

            boolean isValid = ((LocalTime) endTime).isAfter((LocalTime) startTime);
            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("End time must be after start time!")
                        .addPropertyNode(this.endTimeField)
                        .addConstraintViolation();
            }

            return ((LocalTime) endTime).isAfter((LocalTime) startTime);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}
