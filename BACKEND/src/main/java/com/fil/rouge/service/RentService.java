package com.fil.rouge.service;


import com.fil.rouge.domain.Rent;
import com.fil.rouge.web.dto.request.RentRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentService {

    Rent save(RentRequestDTO rent);
    Page<Rent> findAllRentByPropertyId(Long propertyId, Pageable pageable);
}
