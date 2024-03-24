package com.fil.rouge.service.impl;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.domain.Property;
import com.fil.rouge.domain.Rent;
import com.fil.rouge.domain.enums.RentStatus;
import com.fil.rouge.repository.RentRepository;
import com.fil.rouge.security.SecurityUtils;
import com.fil.rouge.service.PropertyService;
import com.fil.rouge.service.RentService;
import com.fil.rouge.utils.EmailServiceUtils;
import com.fil.rouge.web.dto.request.RentRequestDTO;
import com.fil.rouge.web.dto.response.RentStatisticsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class RentServiceImpl implements RentService {
    private final RentRepository rentRepository;
    private final PropertyService propertyService;
    private final EmailServiceUtils emailService;

    public RentServiceImpl(RentRepository rentRepository, PropertyService propertyService, EmailServiceUtils emailService) {
        this.rentRepository = rentRepository;
        this.propertyService = propertyService;
        this.emailService = emailService;
    }

    @Override
    public Rent save(RentRequestDTO rentRequestDTO) {
        Property property = propertyService.findById(rentRequestDTO.getPropertyId())
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));
        AppUser tenant = SecurityUtils.getCurrentUser();
        // validate the rent request and the property availability
        validateRent(rentRequestDTO, property, tenant);
        Double totalPrice = calculatePrice(rentRequestDTO.getStartDate(), rentRequestDTO.getEndDate(), property);
        Rent save = rentRepository.save(
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
       // create methode to send email localy
        CompletableFuture.runAsync(() -> sendEmailRentSubmitting(tenant.getEmail(), save));
        return save;
    }

    private void sendEmailRentSubmitting(String to, Rent rent) {
        String emailContent;
        try {
            emailContent = emailService.loadEmailTemplate("template/rent-request.html");
        }catch (IllegalArgumentException e) {
            log.error("Error loading email template", e);
            return;
        }
        // add api address of the server and /api/v1/properties/images/** to the image url invcase of a real server add the real server too not only localhost

        String imageUrl = !rent.getProperty().getImages().isEmpty()
                ? "http://localhost:8080/api/v1/properties/images/" + rent.getProperty().getImages().get(0)
                : "";

        String body = emailContent
                .replace("{{TENANT_NAME}}", rent.getTenant().getFullName())
                .replace("{{PROPERTY_TITLE}}", rent.getProperty().getTitle())
                .replace("{{PROPERTY_TYPE}}", rent.getProperty().getPropertyType().toString())
                .replace("{{CITY_NAME}}", rent.getProperty().getCity().getName())
                .replace("{{START_DATE}}", rent.getStartDate().toString())
                .replace("{{END_DATE}}", rent.getEndDate().toString())
                .replace("{{TOTAL_PRICE}}", rent.getTotalPrice().toString())
                .replace("{{NUMBER_OF_ROOMS}}", rent.getProperty().getNumberOfRooms().toString())
                .replace("{{HAS_BALCONY}}", rent.getProperty().isHasBalcony() ? "Yes" : "No")
                .replace("{{PROPERTY_IMAGE_URL}}", imageUrl)
                .replace("{{PROPERTY_ADDRESS}}", rent.getProperty().getAddress());
        try {
            emailService.sendEmail(to, "Rent request", body);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Rent save = rentRepository.save(rent);

        try {
            emailService.sendEmail(rent.getTenant().getEmail(), "Rent request", "Your rent request has been" + status);
        } catch (MessagingException e) {
            log.error("Error sending email", e);
        }

        return save;
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
