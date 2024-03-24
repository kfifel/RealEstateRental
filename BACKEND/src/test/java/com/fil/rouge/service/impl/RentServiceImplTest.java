package com.fil.rouge.service.impl;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.domain.Property;
import com.fil.rouge.repository.*;
import com.fil.rouge.security.SecurityUtils;
import com.fil.rouge.web.dto.request.RentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentServiceImplTest {

    @Mock
    private PropertyServiceImpl propertyService;


    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private SecurityUtils securityUtils;

    @InjectMocks
    private RentServiceImpl rentService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    // Given a RentRequestDTO with a start date after the end date, save method should throw an IllegalArgumentException with the message "Start date cannot be after end date".
    @Test
    void test_startDateAfterEndDate() {
        // Arrange
        RentRequestDTO rentRequestDTO = new RentRequestDTO();
        rentRequestDTO.setPropertyId(1L);
        rentRequestDTO.setStartDate(LocalDate.of(2022, 2, 1));
        rentRequestDTO.setEndDate(LocalDate.of(2022, 1, 31));

        Property property = new Property();

        when(propertyService.findById(rentRequestDTO.getPropertyId())).thenReturn(Optional.of(property));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rentService.save(rentRequestDTO), "Start date cannot be after end date");
    }


    // Given a RentRequestDTO with a non-existent property id, save method should throw an IllegalArgumentException with the message "Property not found".
    @Test
    void test_nonExistentPropertyId() {
        // Arrange
        RentRequestDTO rentRequestDTO = new RentRequestDTO();
        rentRequestDTO.setPropertyId(1L);
        rentRequestDTO.setStartDate(LocalDate.of(2022, 1, 1));
        rentRequestDTO.setEndDate(LocalDate.of(2022, 1, 31));

        when(propertyService.findById(rentRequestDTO.getPropertyId())).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rentService.save(rentRequestDTO), "Property not found");
    }
    @Test
    void testYourMethod() {
        // Create a mock Authentication object
        Authentication authentication = mock(Authentication.class);
        // Set up the mock SecurityContext with the mock Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        // Set the mock SecurityContext in the SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);

        // Now you can proceed with your test
        // Mock behavior of SecurityUtils.getCurrentuser()
        AppUser mockUser = new AppUser();
        mockUser.setEmail("test@example.com");
        when(securityUtils.getCurrentUser()).thenReturn(mockUser);

        // Call the method under test
        AppUser currentUser = securityUtils.getCurrentUser();

        // Verify that the method returned the expected user
        assertEquals("test@example.com", currentUser.getEmail());
    }


    @Test
    void test_null_property_id() {
        // Mock dependencies
        RentRequestDTO rentRequestDTO = new RentRequestDTO();
        rentRequestDTO.setPropertyId(null);

        // Invoke the method
        try {
            rentService.save(rentRequestDTO);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Verify the exception message
            assertEquals("Property not found", e.getMessage());
        }
    }
}