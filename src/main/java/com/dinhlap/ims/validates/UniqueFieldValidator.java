package com.dinhlap.ims.validates;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.transaction.annotation.Transactional;

public class UniqueFieldValidator implements ConstraintValidator<UniqueField, Object> {

    @PersistenceContext
    private EntityManager entityManager;

    private String fieldName;
    private Class<?> entityClass;

    @Override
    public void initialize(UniqueField constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.entityClass = constraintAnnotation.entity();
    }

    @Transactional
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String queryString = String.format("SELECT COUNT(e) FROM %s e WHERE e.%s = :value", entityClass.getName(), fieldName);
        
        Long count = (Long) entityManager.createQuery(queryString)
                .setParameter("value", value)
                .getSingleResult();

        return count == 0;
    }
}
