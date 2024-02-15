package com.fil.rouge.web.mapper;

import com.fil.rouge.domain.City;
import com.fil.rouge.domain.Property;
import com.fil.rouge.web.dto.PropertyDto;

public class PropertyDtoMapper {


    public static Property toEntity(PropertyDto propertyDto) {
        return Property.builder()
                .id(propertyDto.getId())
                .address(propertyDto.getAddress())
                .city(City.builder()
                        .name(propertyDto.getCity())
                    .build()
                )
                .description(propertyDto.getDescription())
                .numberOfRooms(propertyDto.getNumberOfRooms())
                .pricePerDay(propertyDto.getPricePerMonth())
                .pricePerMonth(propertyDto.getPricePerMonth())
                .propertyType(propertyDto.getPropertyType())
                .size(propertyDto.getSize())
                .title(propertyDto.getTitle())
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
                .size(property.getSize())
                .ownerId(property.getLandlord().getId())
                .title(property.getTitle())
                .build();
    }
}
