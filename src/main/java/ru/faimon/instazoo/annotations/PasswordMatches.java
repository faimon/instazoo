package ru.faimon.instazoo.annotations;

import ru.faimon.instazoo.validations.PasswordMathesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMathesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Password cannot be empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
