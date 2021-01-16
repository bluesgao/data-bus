package com.bluesgao.databus.core.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author bei.wu
 */
public class CheckConfigValidator implements ConstraintValidator<CheckConfig, String> {

    private boolean notBlank;
    private int max;

    @Override
    public void initialize(CheckConfig constraintAnnotation) {
        this.notBlank = constraintAnnotation.notBlank();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (this.notBlank) {

        }
        return true;
    }
}
