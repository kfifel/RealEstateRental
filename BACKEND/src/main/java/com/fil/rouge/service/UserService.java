package com.fil.rouge.service;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.utils.ValidationException;
import com.fil.rouge.web.dto.RoleDto;
import com.fil.rouge.web.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    AppUser save (AppUser user);

    List<AppUser> findAll();

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByEmail(String email);

    void revokeRole(Long id, List<RoleDto> roles) throws ValidationException;

    AppUser assigneRole(Long id, List<RoleDto> roles) throws ValidationException, ResourceNotFoundException;

    List<String> getAuthorities();

    UserDetailsService userDetailsService();

    AppUser findByUsername(String username);

    List<String> getMyAuthorities();

    public AppUser getCurrentUser();

    void delete(Long id);

    void softDelete(Long id);

    void forceDelete(Long id);
}
