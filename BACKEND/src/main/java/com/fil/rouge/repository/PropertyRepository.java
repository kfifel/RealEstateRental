package com.fil.rouge.repository;

import com.fil.rouge.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    boolean existsByAddress(String address);
}
