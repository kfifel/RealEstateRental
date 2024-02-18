package com.fil.rouge.web.mapper;

import com.fil.rouge.domain.City;
import com.fil.rouge.domain.Property;
import com.fil.rouge.utils.FileUtils;
import com.fil.rouge.web.dto.PropertyDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PropertyDtoMapper {

    private FileUtils fileUtils;

    public Property toEntity(PropertyDto propertyDto) {
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

    public PropertyDto toDto(Property property) {
        final List<byte[]>  images = new ArrayList<>();
        if(property.getImages() != null && !property.getImages().isEmpty())
        {
            property.getImages().forEach(image -> {
                byte[] bytes;
                try {
                    bytes = fileUtils.fileToByteArray(image.getPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                images.add(bytes);
            });
        }
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
                .images(images)
                .build();
    }
}
