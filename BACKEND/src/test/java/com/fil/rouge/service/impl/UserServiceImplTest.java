package com.fil.rouge.service.impl;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.domain.Role;
import com.fil.rouge.repository.UserRepository;
import com.fil.rouge.service.RoleService;
import com.fil.rouge.utils.ValidationException;
import com.fil.rouge.web.exception.ResourceNotFoundException;
import com.fil.rouge.web.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_disable_existing_user() {
        // Arrange
        Long id = 1L;
        boolean enable = false;
        AppUser user = new AppUser();
        user.setEnabled(!enable);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        userService.enable(id, enable);

        // Assert
        assertFalse(user.isEnabled());
        verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void test_enable_non_existing_user() {
        // Arrange
        Long id = 1L;
        boolean enable = true;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> userService.enable(id, enable));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(AppUser.class));
    }

    @Test
    void test_enable_existing_user() {
        // Arrange
        Long id = 1L;
        boolean enable = true;
        AppUser user = new AppUser();
        user.setEnabled(!enable);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        userService.enable(id, enable);

        // Assert
        assertTrue(user.isEnabled());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void test_user_has_no_roles_before_add_new_roles() throws ValidationException, ResourceNotFoundException {
        Long id = 1L;
        List<String> roleNames = List.of("ROLE_ADMIN", "ROLE_TENANT");

        AppUser user = AppUser.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .phone("1234567890")
                .enabled(true)
                .roles(new ArrayList<>())
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(roleService.findAllByNameIn(roleNames)).thenReturn(List.of(
                Role.builder().id(1L).name("ROLE_ADMIN").build(),
                Role.builder().id(2L).name("ROLE_TENANT").build()
        ));

        userService.handleRoles(id, roleNames);

        verify(userRepository).save(user);
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")));
        assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_TENANT")));
    }

    @Test
    void test_user_has_roles_before_add_new_roles() {
        Long id = 1L;
        List<String> roleNames = List.of("ROLE_ADMIN");

        Role roleAdmin = Role.builder().id(1L).name("ROLE_ADMIN").build();
        AppUser user = AppUser.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .phone("1234567890")
                .enabled(true)
                .roles(List.of(
                        roleAdmin
                ))
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(roleService.findAllByNameIn(roleNames)).thenReturn(List.of(
                roleAdmin
        ));

        assertThrows(ValidationException.class, () -> userService.handleRoles(id, new ArrayList<>()));
        assertThrows(ValidationException.class, () -> userService.handleRoles(id, roleNames));
        verify(userRepository, Mockito.never()).save(user);
    }
}