package ru.faimon.instazoo.validations;

import ru.faimon.instazoo.annotations.PasswordMatches;
import ru.faimon.instazoo.payload.request.SignUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMathesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        SignUpRequest userSignUpReq = (SignUpRequest) obj;
        return userSignUpReq.getPassword().equals(userSignUpReq.getConfirmPassword());
    }
}
