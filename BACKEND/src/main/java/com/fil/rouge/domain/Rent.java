package com.fil.rouge.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double totalPrice;

    @ManyToOne
    private PropertyListing propertyListing;

    @ManyToOne
    private AppUser tenant;

}
