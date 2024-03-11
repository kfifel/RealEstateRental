package com.fil.rouge.service.impl;

import com.fil.rouge.domain.Property;
import com.fil.rouge.domain.Rent;
import com.fil.rouge.domain.enums.RentStatus;
import com.fil.rouge.repository.*;
import com.fil.rouge.web.dto.request.RentRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentServiceImplTest {

    @InjectMocks
    private PropertyServiceImpl propertyService;

    @Mock
    private RentRepository rentRepository;

    @InjectMocks
    private RentServiceImpl rentService;

    // Given a valid RentRequestDTO and available Property, save method should return a Rent object with status 'PENDING', isPaid 'false', and a totalPrice calculated based on the property's pricePerDay and pricePerMonth.
    @Test
    void test_validRentRequestDTOAndAvailableProperty() {
        // Arrange
        RentRequestDTO rentRequestDTO = new RentRequestDTO();
        rentRequestDTO.setPropertyId(1L);
        rentRequestDTO.setStartDate(LocalDate.of(2022, 1, 1));
        rentRequestDTO.setEndDate(LocalDate.of(2022, 1, 31));

        Property property = new Property();
        property.setPricePerDay(10.0);
        property.setPricePerMonth(300.0);

        when(propertyService.findById(rentRequestDTO.getPropertyId())).thenReturn(Optional.of(property));
        when(rentRepository.save(any(Rent.class))).thenReturn(new Rent());

        // Act
        Rent result = rentService.save(rentRequestDTO);

        // Assert
        assertEquals(RentStatus.PENDING, result.getStatus());
        assertFalse(result.isPaid());
        assertEquals(310.0, result.getTotalPrice(), 0.01);
    }

    // Given a valid RentRequestDTO and available Property, save method should save the Rent object to the database using the RentRepository.
    @Test
    void test_validRentRequestDTOAndAvailableProperty_saveToDatabase() {
        // Arrange
        RentRequestDTO rentRequestDTO = new RentRequestDTO();
        rentRequestDTO.setPropertyId(1L);
        rentRequestDTO.setStartDate(LocalDate.of(2022, 1, 1));
        rentRequestDTO.setEndDate(LocalDate.of(2022, 1, 31));

        Property property = new Property();
        property.setPricePerDay(10.0);
        property.setPricePerMonth(300.0);

        when(propertyService.findById(rentRequestDTO.getPropertyId())).thenReturn(Optional.of(property));
        when(rentRepository.save(any(Rent.class))).thenReturn(new Rent());

        // Act
        Rent result = rentService.save(rentRequestDTO);

        // Assert
        verify(rentRepository, times(1)).save(any(Rent.class));
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

    // Given a RentRequestDTO with a property that is not available for the selected dates, save method should throw an IllegalArgumentException with the message "Property is not available for the selected dates".
    @Test
    void test_propertyNotAvailableForSelectedDates() {
        // Arrange
        RentRequestDTO rentRequestDTO = new RentRequestDTO();
        rentRequestDTO.setPropertyId(1L);
        rentRequestDTO.setStartDate(LocalDate.of(2022, 1, 1));
        rentRequestDTO.setEndDate(LocalDate.of(2022, 1, 31));

        Property property = new Property();

        when(propertyService.findById(rentRequestDTO.getPropertyId())).thenReturn(Optional.of(property));
        when(rentRepository.isAvailable(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> rentService.save(rentRequestDTO), "Property is not available for the selected dates");
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

}