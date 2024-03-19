package com.fil.rouge.service.impl;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.domain.Property;
import com.fil.rouge.domain.Rent;
import com.fil.rouge.domain.enums.RentStatus;
import com.fil.rouge.repository.RentRepository;
import com.fil.rouge.security.SecurityUtils;
import com.fil.rouge.service.PropertyService;
import com.fil.rouge.service.RentService;
import com.fil.rouge.utils.LocalDateUtils;
import com.fil.rouge.web.dto.request.RentRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    private Double calculatePrice(LocalDate startDate, LocalDate endDate, Property property) {
        long numberOfDays = LocalDateUtils.calculateDaysBetween(startDate, endDate);

        if (numberOfDays > 29)
            return property.getPricePerDay() * numberOfDays;
        else
            return property.getPricePerMonth() * numberOfDays / 30.0;
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
