package com.fil.rouge.service.impl;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.domain.Role;
import com.fil.rouge.repository.RoleRepository;
import com.fil.rouge.security.SecurityUtils;
import com.fil.rouge.service.RoleService;
import com.fil.rouge.utils.CustomError;
import com.fil.rouge.utils.ValidationException;
import com.fil.rouge.web.dto.RoleDto;
import com.fil.rouge.web.exception.EmailAlreadyExistException;
import com.fil.rouge.repository.UserRepository;
import com.fil.rouge.service.UserService;
import com.fil.rouge.web.exception.ResourceNotFoundException;
import com.fil.rouge.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fil.rouge.utils.AppConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @Override
    public AppUser save(AppUser user) {
        findByEmail(user.getEmail()).ifPresent(u -> {
            throw new EmailAlreadyExistException();
        });
        return userRepository.save(user);
    }

    @Override
    public Page<AppUser> findAll(Pageable pageable, String query) {
        if (query != null && !query.isEmpty())
            return userRepository.searchByQuery(query, pageable);
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<AppUser> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void handleRoles(Long id, List<String> roleNames) throws ValidationException, ResourceNotFoundException {
        if (roleNames.isEmpty()) {
            throw new ValidationException(new CustomError("roles", "Roles cannot be empty"));
        }
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", USER_NOT_FOUND));

        List<Role> roles = roleService.findAllByNameIn(roleNames);

        if (roles.size() < roleNames.size()) {
            List<String> notFoundRoles = roleNames.stream()
                    .filter(roleName -> roles.stream().noneMatch(role -> role.getName().equals(roleName)))
                    .toList();
            throw new ResourceNotFoundException("role", "Roles not found: " + String.join(", ", notFoundRoles));
        }

        if (new HashSet<>(user.getRoles()).containsAll(roles) && new HashSet<>(roles).containsAll(user.getRoles())) {
            throw new ValidationException(new CustomError("roles", "User already has the same roles"));
        }

        user.getRoles().clear();
        user.getRoles().addAll( new HashSet<>(roles).stream().toList() );
        userRepository.save(user);
    }

    @Override
    public List<String> getAuthorities() {
        return roleService.getALlRoles().stream().map(Role::getName).toList();
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public AppUser findByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public List<String> getMyAuthorities() {
        return getCurrentUser()
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    @Override
    public AppUser getCurrentUser() {
        String currentUserLogin = SecurityUtils.getCurrentUserEmail();
        if(currentUserLogin == null)
            throw new BadCredentialsException(USER_NOT_FOUND);
        return this.findByUsername(currentUserLogin);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void softDelete(Long id) {
        userRepository.softDelete(id);
    }

    @Override
    public void forceDelete(Long id) {
        userRepository.forceDelete(id);
    }

    @Override
    public void enable(Long id, boolean enable) {
        this.findById(id).ifPresentOrElse(user -> {
            user.setEnabled(enable);
            userRepository.save(user);
        }, () -> {
            throw new UserNotFoundException("User not found");
        });
    }
}
