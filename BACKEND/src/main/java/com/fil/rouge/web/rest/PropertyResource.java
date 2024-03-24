package com.fil.rouge.web.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fil.rouge.domain.Property;
import com.fil.rouge.service.PropertyService;
import com.fil.rouge.service.RentService;
import com.fil.rouge.utils.ResponseApi;
import com.fil.rouge.web.dto.InquiriesSourceDTO;
import com.fil.rouge.web.dto.MonthlyIncomeDTO;
import com.fil.rouge.web.dto.PropertyDto;
import com.fil.rouge.web.dto.PropertyOverviewDTO;
import com.fil.rouge.web.dto.request.PropertyByCriteriaRequest;
import com.fil.rouge.web.dto.response.RentStatisticsResponse;
import com.fil.rouge.web.exception.ResourceNotFoundException;
import com.fil.rouge.web.mapper.PropertyDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;
import java.net.URI;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyResource {

    private final PropertyService propertyService;
    private final PropertyDtoMapper propertyDtoMapper;
    private final RentService rentService;
    private final ObjectMapper objectMapper;
    private static final String X_TOTAL_COUNT = "X-Total-Count";

    @Value("${file.upload-dir}")
    private String uploadDir;

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
        headers.add(X_TOTAL_COUNT, String.valueOf(properties.getTotalElements()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(properties.getContent());
    }

    @GetMapping("my-property")
    @PreAuthorize("hasRole('ROLE_PROPERTY')")
    public ResponseEntity<List<PropertyDto>> findMyProperty(
            @ParameterObject Pageable pageable,
            @Param("query") String query) {
        Page<Property> properties = propertyService.findMyProperties(query, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_TOTAL_COUNT, String.valueOf(properties.getTotalElements()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(
                    properties.stream()
                        .map(property -> propertyDtoMapper.toDto(property, true))
                        .toList());
    }

    @GetMapping("/{propertyId}")
    public PropertyDto getPropertyById(@PathVariable Long propertyId) {
        Property property = propertyService.findById(propertyId).orElseThrow();
        return propertyDtoMapper.toDto(property, false);
    }

    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(
            @RequestParam("property") String propertyStr,
            @RequestParam("images") List<MultipartFile> images
    ) throws ResourceNotFoundException, JsonProcessingException {
        PropertyDto propertyDto = objectMapper.readValue(propertyStr, PropertyDto.class);
        Property propertyDto1 = propertyService.create(
                propertyDtoMapper.toEntity(propertyDto),
                images);
        URI uri = URI.create("/api/v1/properties/" + propertyDto.getId());
        return ResponseEntity.created(uri).body(propertyDtoMapper.toDto(propertyDto1, true));
    }

    @GetMapping("/top-4")
    public ResponseEntity<List<PropertyDto>> getTop4Properties() {
        List<Property> properties = propertyService.getTop4Properties();
        return ResponseEntity.ok(
                properties.stream()
                        .map(property -> propertyDtoMapper.toDto(property, true))
                        .toList());
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
        headers.add(X_TOTAL_COUNT, String.valueOf(properties.getTotalElements()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(
                    properties.stream()
                        .map(property -> propertyDtoMapper.toDto(property, true))
                        .toList());
    }

    @PostMapping("/search")
    public ResponseEntity<List<PropertyDto>> searchProperties(
            @RequestBody PropertyByCriteriaRequest request,
            @ParameterObject Pageable pageable
    ) {
        Page<Property> properties = propertyService.searchProperties(request, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.add(X_TOTAL_COUNT, String.valueOf(properties.getTotalElements()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(
                        properties.stream()
                                .map(property -> propertyDtoMapper.toDto(property, true))
                                .toList());
    }

    @GetMapping("overview")
    public ResponseEntity<PropertyOverviewDTO> getPropertiesOverview() {
        PropertyOverviewDTO overview = propertyService.getPropertiesOverview();
        return ResponseEntity.ok(overview);
    }

    @GetMapping("monthly-income")
    public ResponseEntity<MonthlyIncomeDTO> getMonthlyIncome() {
        MonthlyIncomeDTO monthlyIncome = propertyService.getMonthlyIncome();
        return ResponseEntity.ok(monthlyIncome);
    }

    @GetMapping("inquiries-source")
    public ResponseEntity<InquiriesSourceDTO> getInquiriesSource() {
        RentStatisticsResponse statistics = rentService.getStatistics(0L);
        Map<String, Long> sourceCounts = new HashMap<>();
        sourceCounts.put("pending", statistics.getTotalPendingRent());
        sourceCounts.put("ongoing", statistics.getTotalOngoingRent());
        sourceCounts.put("canceled", statistics.getTotalCanceledRent());
        sourceCounts.put("approved", statistics.getTotalApprovedRent());
        sourceCounts.put("rejected", statistics.getTotalRejectedRent());
        sourceCounts.put("finished", statistics.getTotalFinishedRent());

        return ResponseEntity.ok(InquiriesSourceDTO.builder()
                .sourceCounts(sourceCounts)
                .build());
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
