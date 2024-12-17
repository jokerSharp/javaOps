package ru.javawebinar.topjava.web.user;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.javawebinar.topjava.HasIdAndEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Optional;

@Component
public class UniqueMailValidator implements org.springframework.validation.Validator {

    public static final String EXCEPTION_DUPLICATE_EMAIL = "User with this email already exists";

    private final UserRepository repository;

    public UniqueMailValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        Optional<User> extracted = Optional.ofNullable(repository.getByEmail(user.getEmail()));
        if (extracted.isPresent()
                && user.getId() != null
                && !extracted.get().getId().equals(user.getId())) {
            errors.rejectValue("email", "", EXCEPTION_DUPLICATE_EMAIL);
        }
    }
}
