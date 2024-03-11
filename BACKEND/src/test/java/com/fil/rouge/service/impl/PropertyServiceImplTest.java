package com.fil.rouge.service.impl;

import com.fil.rouge.domain.Property;
import com.fil.rouge.repository.RentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceImplTest {

    @InjectMocks
    private PropertyServiceImpl propertyService;


    @Test
    public void test_valid_pageable_object() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Property> result = propertyService.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(10, result.getSize());
    }
}