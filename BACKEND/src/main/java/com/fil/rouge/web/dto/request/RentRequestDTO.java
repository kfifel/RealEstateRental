package com.fil.rouge.web.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RentRequestDTO {

    private Long propertyId;
    private LocalDate startDate;
    private LocalDate endDate;
}
