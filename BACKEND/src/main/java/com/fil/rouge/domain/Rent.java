package com.fil.rouge.domain;

import com.fil.rouge.domain.enums.RentStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double totalPrice;

    private boolean isPaid;

    @Enumerated(EnumType.STRING)
    private RentStatus status;

    @ManyToOne
    private Property property;

    @ManyToOne
    private AppUser tenant;

}
