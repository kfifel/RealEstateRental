package com.fil.rouge.service.impl;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.domain.Property;
import com.fil.rouge.domain.Rent;
import com.fil.rouge.domain.enums.RentStatus;
import com.fil.rouge.repository.RentRepository;
import com.fil.rouge.security.SecurityUtils;
import com.fil.rouge.service.PropertyService;
import com.fil.rouge.service.RentService;
import com.fil.rouge.web.dto.request.RentRequestDTO;
import com.fil.rouge.web.dto.response.RentStatisticsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class RentServiceImpl implements RentService {
    private final RentRepository rentRepository;
    private final PropertyService propertyService;

    public RentServiceImpl(RentRepository rentRepository, PropertyService propertyService) {
        this.rentRepository = rentRepository;
        this.propertyService = propertyService;
    }

    @Override
    public Rent save(RentRequestDTO rentRequestDTO) {
        Property property = propertyService.findById(rentRequestDTO.getPropertyId())
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));
        AppUser tenant = SecurityUtils.getCurrentUser();
        // validate the rent request and the property availability
        validateRent(rentRequestDTO, property, tenant);
        Double totalPrice = calculatePrice(rentRequestDTO.getStartDate(), rentRequestDTO.getEndDate(), property);
        return rentRepository.save(
                    Rent.builder()
                        .property(property)
                        .startDate(rentRequestDTO.getStartDate())
                        .endDate(rentRequestDTO.getEndDate())
                        .status(RentStatus.PENDING)
                        .isPaid(false)
                        .totalPrice(totalPrice)
                        .tenant(tenant)
                        .build()
        );
    }

    @Override
    public Page<Rent> findAllRentByPropertyId(Long propertyId, Pageable pageable) {
        if (!propertyService.existsById(propertyId))
            throw new IllegalArgumentException("Property not found");

        return rentRepository.findAllByPropertyId(propertyId, pageable);
    }

    @Override
    public RentStatisticsResponse getStatistics(Long propertyId) {
        if (propertyId == 0)
            rentRepository.getStatistics();

        if (!propertyService.existsById(propertyId))
            throw new IllegalArgumentException("Property not found");
        return rentRepository.getStatistics(propertyId);
    }

    @Override
    public Rent updateRentStatus(Long rentId, RentStatus status) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new IllegalArgumentException("Rent not found"));

        if (!rent.getStatus().equals(RentStatus.PENDING)) {
            throw new IllegalArgumentException("Rent status cannot be updated");
        }

        rent.setStatus(status);
        return rentRepository.save(rent);
    }

    @Override
    public Double findTotalIncomeBetweenDates(LocalDate startOfMonth, LocalDate endOfMonth) {
        return rentRepository.findTotalIncomeBetweenDates(startOfMonth, endOfMonth);
    }


    private double calculatePrice(LocalDate startDate, LocalDate endDate, Property property) {
        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        double totalPrice;

        if (numberOfDays >= 30) {
            totalPrice = property.getPricePerMonth() * (numberOfDays / 30.0);
        } else {
            totalPrice = property.getPricePerDay() * numberOfDays;
        }

        // Rounding to 2 decimal places
        return Math.round(totalPrice * 100.0) / 100.0;
    }

    private void validateRent(RentRequestDTO rent, Property property, AppUser tenant) {
        if (rent.getStartDate().isAfter(rent.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        if (rentRepository.isAvailable(property.getId(), rent.getStartDate(), rent.getEndDate())) {
            throw new IllegalArgumentException("Property is not available for the selected dates");
        }

        if (rentRepository.userHasPendingRent(tenant.getId(), property.getId())) {
            throw new IllegalArgumentException("You already have pending this rent");
        }
    }
}
