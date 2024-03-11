package com.fil.rouge.repository;

import com.fil.rouge.domain.City;
import com.fil.rouge.domain.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    boolean existsByAddress(String address);

    @Query("SELECT p FROM Property p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.address) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.city.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Property> search(String query, Pageable pageable);

    @Query("select p from Property p WHERE p.id NOT IN (" +
            "SELECT r.property.id FROM Rent r WHERE " +
            "(((r.startDate <= :startDate AND r.endDate >= :startDate) OR " +
            "(r.startDate <= :endDate AND r.endDate >= :endDate) OR " +
            "(r.startDate >= :startDate AND r.endDate <= :endDate))) AND r.status = 'ONGOING'" +
            ")" +
            " AND p.city = :city")
    Page<Property> getAvailableProperties(LocalDate startDate,
                                          LocalDate endDate,
                                          City city,
                                          Pageable pageable);
}
