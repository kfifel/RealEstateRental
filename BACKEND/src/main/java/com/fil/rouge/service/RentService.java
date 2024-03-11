package com.fil.rouge.service;


import com.fil.rouge.domain.Rent;
import com.fil.rouge.web.dto.request.RentRequestDTO;

public interface RentService {

    Rent save(RentRequestDTO rent);
}
