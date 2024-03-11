package com.fil.rouge.web.rest;

import com.fil.rouge.domain.Rent;
import com.fil.rouge.service.RentService;
import com.fil.rouge.web.dto.request.RentRequestDTO;
import com.fil.rouge.web.dto.response.RentResponseDTO;
import com.fil.rouge.web.mapper.PropertyDtoMapper;
import com.fil.rouge.web.mapper.RentDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
