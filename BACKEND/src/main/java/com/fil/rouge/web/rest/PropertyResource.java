package com.fil.rouge.web.rest;


import com.fil.rouge.domain.Property;
import com.fil.rouge.service.PropertyService;
import com.fil.rouge.web.dto.PropertyDto;
import com.fil.rouge.web.mapper.PropertyDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyResource {

    private final PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<PropertyDto>> getAllProperties() {
        return ResponseEntity
                .ok(propertyService
                        .findAll()
                        .stream()
                        .map(PropertyDtoMapper::toDto)
                        .toList()
                );
    }

    @GetMapping("/{propertyId}")
    public PropertyDto getPropertyById(@PathVariable Long propertyId) {
        Property property = propertyService.findById(propertyId).orElseThrow();
        return PropertyDtoMapper.toDto(property);
    }

    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(@RequestBody @Valid PropertyDto propertyDto) {
        Property propertyDto1 = propertyService.create(PropertyDtoMapper.toEntity(propertyDto));
        URI uri = URI.create("/api/v1/properties/" + propertyDto.getId());
        return ResponseEntity.created(uri).body(PropertyDtoMapper.toDto(propertyDto1));
    }

    @PutMapping
    public ResponseEntity<String> updateProperty(@RequestBody @Valid PropertyDto updatedPropertyDto) {
        propertyService.update(PropertyDtoMapper.toEntity(updatedPropertyDto));
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
