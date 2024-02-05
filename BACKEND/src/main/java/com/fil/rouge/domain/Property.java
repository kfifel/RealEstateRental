package com.fil.rouge.domain;

import com.fil.rouge.domain.enums.PropertyType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Double size;

    private Double pricePerMonth;

    private Double pricePerDay;

    private Integer numberOfRooms;

    private boolean hasBalcony;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @ManyToOne
    private AppUser owner;

    private String address;

    @ManyToOne
    private City city;

}