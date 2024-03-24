package com.fil.rouge.service.impl;

import com.fil.rouge.domain.Property;
import com.fil.rouge.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PropertyServiceImplTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyServiceImpl propertyService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_empty_database() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        when(propertyService.findAll(pageable)).thenReturn(Page.empty());
        Page<Property> result = propertyService.findAll(pageable);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void test_invalid_pageable_object() {
        // Arrange
        Pageable pageable = null;

        // Act
        when(propertyService.findAll(pageable)).thenReturn(Page.empty());
        Page<Property> result = propertyService.findAll(pageable);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void test_correct_number_of_properties_with_specific_page_size() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 5);
        List<Property> properties = new ArrayList<>();
        properties.add(new Property());
        properties.add(new Property());
        properties.add(new Property());
        properties.add(new Property());
        properties.add(new Property());
        Page<Property> expectedPage = new PageImpl<>(properties, pageable, properties.size());

        // Act
        when(propertyRepository.findAll(pageable)).thenReturn(expectedPage);
        Page<Property> result = propertyService.findAll(pageable);

        // Assert
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
    }
}