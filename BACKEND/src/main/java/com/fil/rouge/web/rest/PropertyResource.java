package com.fil.rouge.web.rest;


import com.fil.rouge.domain.Property;
import com.fil.rouge.service.PropertyService;
import com.fil.rouge.utils.ResponseApi;
import com.fil.rouge.web.dto.PropertyDto;
import com.fil.rouge.web.exception.ResourceNotFoundException;
import com.fil.rouge.web.mapper.PropertyDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyResource {

    private final PropertyService propertyService;
    private final PropertyDtoMapper propertyDtoMapper;

    @GetMapping
    public ResponseEntity<List<PropertyDto>> getAllProperties(
            @ParameterObject Pageable pageable,
            @Param("query") String query
    ) {
        Page<PropertyDto> properties;

        if (query != null && !query.isEmpty()) {
            properties = propertyService.search(query, pageable)
                    .map(property -> propertyDtoMapper.toDto(property, true));
        } else {
            properties = propertyService.findAll(pageable)
                    .map(property -> propertyDtoMapper.toDto(property, true));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(properties.getTotalElements()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(properties.getContent());
    }

    @GetMapping("/{propertyId}")
    public PropertyDto getPropertyById(@PathVariable Long propertyId) {
        Property property = propertyService.findById(propertyId).orElseThrow();
        return propertyDtoMapper.toDto(property, false);
    }

    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(
            @RequestBody @Valid PropertyDto propertyDto
    ) throws ResourceNotFoundException {
        Property propertyDto1 = propertyService.create(propertyDtoMapper.toEntity(propertyDto));
        URI uri = URI.create("/api/v1/properties/" + propertyDto.getId());
        return ResponseEntity.created(uri).body(propertyDtoMapper.toDto(propertyDto1, true));
    }

    @PostMapping("/{propertyId}/images")
    public ResponseEntity<PropertyDto> createPropertyImages(
            @PathVariable("propertyId")  Long id,
            @RequestParam("images") List<MultipartFile> images) {
        Property propertyDto1 = propertyService.createPropertyImages(id, images);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(propertyDtoMapper.toDto(propertyDto1, true));
    }

    @PutMapping
    public ResponseEntity<String> updateProperty(@RequestBody @Valid PropertyDto updatedPropertyDto) {
        propertyService.update(propertyDtoMapper.toEntity(updatedPropertyDto));
        return ResponseEntity.ok("Property updated successfully");
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<ResponseApi<String>> deleteProperty(@PathVariable Long propertyId) throws ResourceNotFoundException {
        propertyService.delete(propertyId);
        return ResponseEntity.ok(ResponseApi.
                <String>builder()
                        .message("Property deleted successfully")
                .build());
    }

    @GetMapping("/become-landlord")
    public ResponseEntity<Object> becomeLandlord() {
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<PropertyDto>> getAvailableProperties(
            @Param("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @Param("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @Param("city") String city,
            @ParameterObject Pageable pageable
    ) {
        Page<Property> properties = propertyService.getAvailableProperties(startDate, endDate, city, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(properties.getTotalElements()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(
                    properties.stream()
                        .map(property -> propertyDtoMapper.toDto(property, true))
                        .toList());
    }


}
