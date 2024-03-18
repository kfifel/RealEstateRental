package com.fil.rouge.domain;

import com.fil.rouge.domain.enums.PropertyType;
import com.fil.rouge.domain.enums.RentStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private Double size;

    private Double pricePerMonth;

    private Double pricePerDay;

    private Integer numberOfRooms;

    private boolean hasBalcony;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "property", fetch = FetchType.LAZY)
    private List<PropertyImage> images;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "property", fetch = FetchType.LAZY)
    private List<Rent> rents;
    
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @ManyToOne
    private AppUser landlord;

    private String address;

    @ManyToOne
    private City city;

    // Method to calculate the number of rentals
    public int calculateNumberOfRentals() {
        return this.rents != null
                ? this.rents.stream()
                    .filter(rent -> rent.getStatus().equals(RentStatus.ONGOING) || rent.getStatus().equals(RentStatus.FINISHED))
                    .toList()
                    .size()
                : 0;
    }
}