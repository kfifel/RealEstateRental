package com.fil.rouge.service;


import com.fil.rouge.domain.Property;
import com.fil.rouge.web.dto.PropertyDto;

import java.util.List;
import java.util.Optional;

public interface PropertyService {

    List<Property> findAll();
    Optional<Property> findById(Long propertyId);
    Property create(Property property);
    void update(Property property);
    void delete(Long propertyId);
}
