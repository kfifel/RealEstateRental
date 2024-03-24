package com.fil.rouge.web.dto.request;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class RentRequestDTO {

    @NotNull(message = "Property id is required")
    private Long propertyId;

    @Future(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @Future(message = "End date cannot be in the past")
    private LocalDate endDate;
}
