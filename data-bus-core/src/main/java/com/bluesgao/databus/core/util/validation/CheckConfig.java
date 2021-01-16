package com.bluesgao.databus.core.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author bei.wu
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CheckConfigValidator.class)
public @interface CheckConfig {

    String message() default "events包含update事件时,fields必须有值";

    int max() default 20;

    boolean notBlank() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
