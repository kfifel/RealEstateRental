package com.fil.rouge.service;


import com.fil.rouge.domain.Rent;
import com.fil.rouge.domain.enums.RentStatus;
import com.fil.rouge.web.dto.request.RentRequestDTO;
import com.fil.rouge.web.dto.response.RentStatisticsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface RentService {

    Rent save(RentRequestDTO rent);
    Page<Rent> findAllRentByPropertyId(Long propertyId, Pageable pageable);

    RentStatisticsResponse getStatistics(Long propertyId);

    Rent updateRentStatus(Long rentId, RentStatus status);

    Double findTotalIncomeBetweenDates(LocalDate startOfMonth, LocalDate endOfMonth);
}
