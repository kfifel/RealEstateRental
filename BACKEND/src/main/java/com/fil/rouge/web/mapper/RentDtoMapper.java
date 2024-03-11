package com.fil.rouge.web.mapper;

import com.fil.rouge.domain.Rent;
import com.fil.rouge.web.dto.response.RentResponseDTO;

public class RentDtoMapper {

    private RentDtoMapper() {
    }

    public static RentResponseDTO toRentResponseDTO(Rent rent) {
        return RentResponseDTO.builder()
                .id(rent.getId())
                .startDate(rent.getStartDate())
                .endDate(rent.getEndDate())
                .totalPrice(rent.getTotalPrice())
                .isPaid(rent.isPaid())
                .status(rent.getStatus())
                .ownerFullName(rent.getProperty().getLandlord().getFullName())
                .ownerPhone(rent.getProperty().getLandlord().getPhone())
                .tenantFullName(rent.getTenant().getFullName())
                .build();
    }
}
