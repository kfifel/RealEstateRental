package com.fil.rouge.service;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.utils.ValidationException;
import com.fil.rouge.web.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    AppUser save (AppUser user);

    Page<AppUser> findAll(Pageable pageable, String query);

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByEmail(String email);

    List<String> getAuthorities();

    UserDetailsService userDetailsService();

    AppUser findByUsername(String username);

    List<String> getMyAuthorities();

    public AppUser getCurrentUser();

    void delete(Long id);

    void softDelete(Long id);

    void forceDelete(Long id);

    void enable(Long id, boolean enable);

    void handleRoles(Long id, List<String> roles) throws ValidationException, ResourceNotFoundException;
}
