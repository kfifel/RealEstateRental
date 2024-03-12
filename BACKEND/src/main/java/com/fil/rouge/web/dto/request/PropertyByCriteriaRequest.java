package com.fil.rouge.web.dto.request;

import com.fil.rouge.domain.enums.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyByCriteriaRequest {

    private String title;
    private String description;
    private String address;
    private Integer numberOfRooms;
    private Double minPricePerDay;
    private Double maxPricePerDay;
    private PropertyType propertyType;
    private String city;
}
