package com.fil.rouge.domain;

import com.fil.rouge.domain.enums.PropertyType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String propertyName;

    private String propertyDescription;

    private Double propertySize;

    private Double propertyPrice;

    @ManyToOne
    private AppUser owner;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @ManyToOne
    private City city;

}