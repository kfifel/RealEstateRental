package com.fil.rouge.repository;

import com.fil.rouge.domain.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface RentRepository extends JpaRepository<Rent, Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rent r WHERE r.property.id = ?1 AND" +
            " ((r.startDate <= ?2 AND r.endDate >= ?2) OR (r.startDate <= ?3 AND r.endDate >= ?3)) " +
            " AND r.status = 'ONGOING'")
    boolean isAvailable(Long id, LocalDate startDate, LocalDate endDate);


    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rent r WHERE r.tenant.id = ?1 AND r.property.id = ?2 AND r.status = 'PENDING'")
    boolean userHasPendingRent(Long userId, Long propertyId);
}
