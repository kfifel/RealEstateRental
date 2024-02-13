package com.fil.rouge.web.dto;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.domain.City;
import com.fil.rouge.domain.enums.PropertyType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class PropertyDto {

    private Long id;

    @NotBlank
    private String name;

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

    @NotNull
    private Double price;

    private Long ownerId;

    @NotNull
    private PropertyType propertyType;

    @NotNull
    private String city;
}
