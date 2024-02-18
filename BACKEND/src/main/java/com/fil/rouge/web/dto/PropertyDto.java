package com.fil.rouge.web.dto;

import com.fil.rouge.domain.enums.PropertyType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class PropertyDto {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Integer numberOfRooms;

    @NotBlank
    private String address;

    @NotNull
    private Double size;

    @NotNull
    private Double pricePerDay;

    @NotNull
    private Double pricePerMonth;

    private Long ownerId;

    @NotNull
    private PropertyType propertyType;

    @NotNull
    private String city;

    private List<byte[]> images;
}
