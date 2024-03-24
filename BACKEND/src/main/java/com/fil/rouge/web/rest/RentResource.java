package com.fil.rouge.web.rest;

import com.fil.rouge.domain.Rent;
import com.fil.rouge.domain.enums.RentStatus;
import com.fil.rouge.service.RentService;
import com.fil.rouge.web.dto.request.RentRequestDTO;
import com.fil.rouge.web.dto.response.RentResponseDTO;
import com.fil.rouge.web.dto.response.RentStatisticsResponse;
import com.fil.rouge.web.mapper.PropertyDtoMapper;
import com.fil.rouge.web.mapper.RentDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/rents")
@RequiredArgsConstructor
public class RentResource {

    private final RentService rentService;
    private final PropertyDtoMapper propertyDtoMapper;

    @PostMapping
    public ResponseEntity<RentResponseDTO> reserveProperty(
            @RequestBody @Valid RentRequestDTO reservation) {
        Rent save = rentService.save(reservation);
        RentResponseDTO rentResponseDTO = RentDtoMapper.toRentResponseDTO(save);
        rentResponseDTO.setProperty(propertyDtoMapper.toDto(save.getProperty(), true));
        return ResponseEntity.ok(rentResponseDTO);
    }

    @GetMapping("/property/{propertyId}/rent-list")
    @PreAuthorize("hasRole('ROLE_PROPERTY')")
    public ResponseEntity<List<RentResponseDTO>> fetchRentsForProperty(
            @PathVariable Long propertyId,
            @ParameterObject Pageable pageable
    ) {
        Page<Rent> rents = rentService.findAllRentByPropertyId(propertyId, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(rents.getTotalElements()));

        return ResponseEntity.ok()
            .headers(headers)
            .body(
                rents.stream()
                    .map(RentDtoMapper::toRentResponseDTO)
                    .toList()
            );
    }

    @PatchMapping("/{rentId}/status/{status}")
    @PreAuthorize("hasRole('ROLE_PROPERTY')")
    public ResponseEntity<RentResponseDTO> updateRentStatus(
            @PathVariable Long rentId,
            @PathVariable RentStatus status
    ) {
        Rent rent = rentService.updateRentStatus(rentId, status);
        RentResponseDTO rentResponseDTO = RentDtoMapper.toRentResponseDTO(rent);
        rentResponseDTO.setProperty(propertyDtoMapper.toDto(rent.getProperty(), true));
        return ResponseEntity.ok(rentResponseDTO);
    }

    @GetMapping("statistics")
    public ResponseEntity<RentStatisticsResponse> getStatistics(
            @RequestParam Long propertyId
            ) {
        return ResponseEntity.ok(rentService.getStatistics(propertyId));
    }
}
