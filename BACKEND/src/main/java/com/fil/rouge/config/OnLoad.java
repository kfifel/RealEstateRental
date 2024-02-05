package com.fil.rouge.config;


import com.fil.rouge.domain.AppUser;
import com.fil.rouge.domain.Role;
import com.fil.rouge.repository.RoleRepository;
import com.fil.rouge.repository.UserRepository;
import com.fil.rouge.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OnLoad implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = createRoles();
        createUsers(roles);
    }

    private void createUsers(List<Role> roles) {
        if(userRepository.count() > 0)
            return;
        roles.forEach(role -> {
            if(role.getName().equals(AuthoritiesConstants.ROLE_ADMIN))
                userRepository.save(AppUser.builder()
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("password"))
                        .firstName("Admin")
                        .lastName("User")
                        .verifiedAt(LocalDateTime.now())
                        .roles(List.of(role))
                        .build());

            if (role.getName().equals(AuthoritiesConstants.ROLE_PROPERTY))
                userRepository.save(AppUser.builder()
                        .email("property@gmail.com")
                        .password(passwordEncoder.encode("password"))
                        .firstName("Property")
                        .lastName("User")
                        .verifiedAt(LocalDateTime.now())
                        .roles(List.of(role))
                        .build());
            if (role.getName().equals(AuthoritiesConstants.ROLE_TENANT)) {
                userRepository.save(AppUser.builder()
                        .email("tenant@gmail.com")
                        .password(passwordEncoder.encode("password"))
                        .firstName("Tenant")
                        .lastName("User")
                        .verifiedAt(LocalDateTime.now())
                        .roles(List.of(role))
                        .build());
            }
        });
    }

    private List<Role> createRoles() {
        if(roleRepository.count() > 0)
            return roleRepository.findAll();

        return roleRepository.saveAll(List.of(
                Role.builder().name(AuthoritiesConstants.ROLE_ADMIN).build(),
                Role.builder().name(AuthoritiesConstants.ROLE_PROPERTY).build(),
                Role.builder().name(AuthoritiesConstants.ROLE_TENANT).build()
        ));
    }
}
