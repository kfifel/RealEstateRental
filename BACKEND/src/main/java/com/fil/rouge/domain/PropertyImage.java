package com.fil.rouge.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;


    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "property_id")
    private Property property;
}
