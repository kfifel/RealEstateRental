package com.fil.rouge.service;

import com.fil.rouge.domain.AppUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    AppUser save (AppUser user);

    List<AppUser> findAll();

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByEmail(String email);
}
