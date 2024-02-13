package com.fil.rouge.web.mapper;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.domain.City;
import com.fil.rouge.domain.Property;
import com.fil.rouge.domain.enums.PropertyType;
import com.fil.rouge.web.dto.PropertyDto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

public class PropertyDtoMapper {


    public static Property toEntity(PropertyDto propertyDto) {
        return Property.builder()
                .id(propertyDto.getId())
                .address(propertyDto.getAddress())
                .city(City.builder()
                        .name(propertyDto.getName())
                        .build()
                )
                .description(propertyDto.getDescription())
                .numberOfRooms(propertyDto.getNumberOfRooms())
                .pricePerDay(propertyDto.getPricePerMonth())
                .pricePerMonth(propertyDto.getPricePerMonth())
                .propertyType(propertyDto.getPropertyType())
                .build();
    }

    public static PropertyDto toDto(Property property) {
        return PropertyDto.builder()
                .id(property.getId())
                .address(property.getAddress())
                .city(property.getCity().getName())
                .description(property.getDescription())
                .numberOfRooms(property.getNumberOfRooms())
                .pricePerDay(property.getPricePerMonth())
                .pricePerMonth(property.getPricePerMonth())
                .propertyType(property.getPropertyType())
                .build();
    }
}
