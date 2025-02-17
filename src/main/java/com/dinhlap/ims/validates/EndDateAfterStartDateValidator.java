package com.dinhlap.ims.validates;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, Object> {

    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(EndDateAfterStartDate constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDateField();
        this.endDateField = constraintAnnotation.endDateField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field startDateField = value.getClass().getDeclaredField(this.startDateField);
            Field endDateField = value.getClass().getDeclaredField(this.endDateField);

            startDateField.setAccessible(true);
            endDateField.setAccessible(true);

            Object startDate = startDateField.get(value);
            Object endDate = endDateField.get(value);

            if (startDate == null || endDate == null) {
                return true;
            }

            if (!(startDate instanceof LocalDate) || !(endDate instanceof LocalDate)) {
                throw new IllegalArgumentException("Fields must be of type LocalDate");
            }

            boolean isValid = ((LocalDate) endDate).isAfter((LocalDate) startDate);
            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("End date must be after start date!")
                        .addPropertyNode(this.endDateField)
                        .addConstraintViolation();
            }

            return ((LocalDate) endDate).isAfter((LocalDate) startDate);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}
