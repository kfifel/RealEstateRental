package com.fil.rouge.security.validation;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.repository.UserRepository;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private UserRepository repository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        AppUser user = repository.findByEmail(username).orElse(null);
        return username != null && user == null;
    }
}
