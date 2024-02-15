package com.fil.rouge.service;


import com.fil.rouge.domain.Property;
import com.fil.rouge.web.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PropertyService {

    List<Property> findAll();
    Optional<Property> findById(Long propertyId);
    Property create(Property property) throws ResourceNotFoundException;
    void update(Property property);
    void delete(Long propertyId);

    Property createPropertyImages(Long id, List<MultipartFile> images);
}
