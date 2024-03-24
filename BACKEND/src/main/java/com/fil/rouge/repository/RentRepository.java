package com.fil.rouge.repository;

import com.fil.rouge.domain.Rent;
import com.fil.rouge.web.dto.response.RentStatisticsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Rent> findAllByPropertyId(Long propertyId, Pageable pageable);

    @Query("SELECT new com.fil.rouge.web.dto.response.RentStatisticsResponse(" +
            "COUNT(r), " +
            "COUNT(CASE WHEN r.status = 'PENDING' THEN 1 END), " +
            "COUNT(CASE WHEN r.status = 'ONGOING' THEN 1 END), " +
            "COUNT(CASE WHEN r.status = 'APPROVED' THEN 1 END), " +
            "COUNT(CASE WHEN r.status = 'REJECTED' THEN 1 END), " +
            "COUNT(CASE WHEN r.status = 'CANCELLED' THEN 1 END), " +
            "COUNT(CASE WHEN r.status = 'FINISHED' THEN 1 END)" +
            ") FROM Rent r WHERE r.property.id = :propertyId")
    RentStatisticsResponse getStatistics(Long propertyId);

    @Query("SELECT new com.fil.rouge.web.dto.response.RentStatisticsResponse(" +
            "COUNT(r), " +
            "COUNT(CASE WHEN r.status = 'PENDING' THEN 1 END), " +
            "COUNT(CASE WHEN r.status = 'ONGOING' THEN 1 END), " +
            "COUNT(CASE WHEN r.status = 'APPROVED' THEN 1 END), " +
            "COUNT(CASE WHEN r.status = 'REJECTED' THEN 1 END), " +
            "COUNT(CASE WHEN r.status = 'CANCELLED' THEN 1 END), " +
            "COUNT(CASE WHEN r.status = 'FINISHED' THEN 1 END)" +
            ") FROM Rent r")
    RentStatisticsResponse getStatistics();

    @Query("SELECT SUM(r.totalPrice) FROM Rent r WHERE r.startDate >= :startDate AND r.endDate <= :endDate AND r.isPaid = true")
    Double findTotalIncomeBetweenDates(LocalDate startDate, LocalDate endDate);


}
