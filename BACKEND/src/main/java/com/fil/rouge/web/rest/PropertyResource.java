package com.fil.rouge.web.rest;


import com.fil.rouge.domain.Property;
import com.fil.rouge.service.PropertyService;
import com.fil.rouge.web.dto.PropertyDto;
import com.fil.rouge.web.exception.ResourceNotFoundException;
import com.fil.rouge.web.mapper.PropertyDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyResource {

    private final PropertyService propertyService;
    private final PropertyDtoMapper propertyDtoMapper;

    @GetMapping
    public ResponseEntity<List<PropertyDto>> getAllProperties() {
        return ResponseEntity
                .ok(propertyService
                        .findAll()
                        .stream()
                        .map(propertyDtoMapper::toDto)
                        .toList()
                );
    }

    @GetMapping("/{propertyId}")
    public PropertyDto getPropertyById(@PathVariable Long propertyId) {
        Property property = propertyService.findById(propertyId).orElseThrow();
        return propertyDtoMapper.toDto(property);
    }

    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(
            @RequestBody @Valid PropertyDto propertyDto
    ) throws ResourceNotFoundException {
        Property propertyDto1 = propertyService.create(propertyDtoMapper.toEntity(propertyDto));
        URI uri = URI.create("/api/v1/properties/" + propertyDto.getId());
        return ResponseEntity.created(uri).body(propertyDtoMapper.toDto(propertyDto1));
    }

    @PostMapping("/{propertyId}/images")
    public ResponseEntity<PropertyDto> createPropertyImages(
            @PathVariable("propertyId")  Long id,
            @RequestParam("images") List<MultipartFile> images) {
        Property propertyDto1 = propertyService.createPropertyImages(id, images);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(propertyDtoMapper.toDto(propertyDto1));
    }

    @PutMapping
    public ResponseEntity<String> updateProperty(@RequestBody @Valid PropertyDto updatedPropertyDto) {
        propertyService.update(propertyDtoMapper.toEntity(updatedPropertyDto));
        return ResponseEntity.ok("Property updated successfully");
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long propertyId) {
        propertyService.delete(propertyId);
        return ResponseEntity.ok("Property deleted successfully");
    }

    @GetMapping("/become-landlord")
    public ResponseEntity<Object> becomeLandlord() {
        return ResponseEntity.noContent()
                .build();
    }
}
