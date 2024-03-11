package com.fil.rouge.web.dto.response;

import com.fil.rouge.domain.enums.RentStatus;
import com.fil.rouge.web.dto.PropertyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentResponseDTO {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double totalPrice;

    private boolean isPaid;

    private RentStatus status;

    private PropertyDto property;

    private String ownerFullName;

    private String ownerPhone;

    private String tenantFullName;
}
