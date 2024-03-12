package com.fil.rouge.repository;

import com.fil.rouge.domain.Property;
import com.fil.rouge.web.dto.request.PropertyByCriteriaRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
public interface PropertySearchRepository {

    Page<Property> searchByCityAndDateAvailability(
            LocalDate startDate, LocalDate endDate, String city, Pageable pageable) ;

    Page<Property> searchProperties(PropertyByCriteriaRequest request, Pageable pageable);

}
