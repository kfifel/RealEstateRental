package com.fil.rouge.config.seeder;


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
    private static final String PASSWORD = "password";

    @Override
    public void run(String... args) {
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
                        .password(passwordEncoder.encode(PASSWORD))
                        .firstName("Admin")
                        .lastName("User")
                        .verifiedAt(LocalDateTime.now())
                        .roles(List.of(role))
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .phone("1234567890")
                        .verifiedAt(LocalDateTime.now())
                        .build());

            if (role.getName().equals(AuthoritiesConstants.ROLE_PROPERTY))
                userRepository.save(AppUser.builder()
                        .email("property@gmail.com")
                        .password(passwordEncoder.encode(PASSWORD))
                        .firstName("Property")
                        .lastName("User")
                        .verifiedAt(LocalDateTime.now())
                        .roles(List.of(role))
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .phone("1234567890")
                        .verifiedAt(LocalDateTime.now())
                        .build());
            if (role.getName().equals(AuthoritiesConstants.ROLE_TENANT)) {
                userRepository.save(AppUser.builder()
                        .email("tenant@gmail.com")
                        .password(passwordEncoder.encode(PASSWORD))
                        .firstName("Tenant")
                        .lastName("User")
                        .verifiedAt(LocalDateTime.now())
                        .roles(List.of(role))
                        .createdAt(LocalDateTime.now())
                        .phone("1234567890")
                        .verifiedAt(LocalDateTime.now())
                        .enabled(true)
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
