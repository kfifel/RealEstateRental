package com.fil.rouge.service;


import com.fil.rouge.domain.Property;
import com.fil.rouge.web.dto.request.PropertyByCriteriaRequest;
import com.fil.rouge.web.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PropertyService {

    Page<Property> findAll(Pageable pageable);
    Page<Property>  search(String query, Pageable pageable);
    Optional<Property> findById(Long propertyId);
    Property create(Property property, List<MultipartFile> images) throws ResourceNotFoundException;
    void update(Property property);
    void delete(Long propertyId) throws ResourceNotFoundException;
    Page<Property> getAvailableProperties(LocalDate startDate, LocalDate endDate, String city, Pageable pageable);
    Page<Property> searchProperties(PropertyByCriteriaRequest request, Pageable pageable);
    Page<Property> findMyProperties(String query, Pageable pageable);
    List<Property> getTop4Properties();
    boolean existsById(Long propertyId);
}
