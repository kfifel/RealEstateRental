package com.fil.rouge.service.impl;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.web.exception.EmailAlreadyExistException;
import com.fil.rouge.repository.UserRepository;
import com.fil.rouge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public AppUser save(AppUser user) {
        findByEmail(user.getEmail()).ifPresent(u -> {
            throw new EmailAlreadyExistException();
        });
        return userRepository.save(user);
    }

    @Override
    public List<AppUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<AppUser> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
