package ru.javawebinar.topjava.web.user.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userRepository.getByEmail(email) == null;
    }
}
