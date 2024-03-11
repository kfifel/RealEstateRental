package com.fil.rouge.service.impl;

import com.fil.rouge.domain.*;
import com.fil.rouge.domain.enums.RentStatus;
import com.fil.rouge.repository.CityRepository;
import com.fil.rouge.repository.PropertyImageRepository;
import com.fil.rouge.repository.PropertyRepository;
import com.fil.rouge.repository.UserRepository;
import com.fil.rouge.security.SecurityUtils;
import com.fil.rouge.service.PropertyService;
import com.fil.rouge.utils.FileUtils;
import com.fil.rouge.web.exception.PropertyExistsException;
import com.fil.rouge.web.exception.ResourceNotFoundException;
import com.fil.rouge.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final FileUtils fileUtils;
    private final EntityManager entityManager;

    @Override
    public Page<Property> findAll(Pageable pageable) {
        return propertyRepository.findAll(pageable);
    }

    @Override
    public Page<Property> search(String query, Pageable pageable) {
        return propertyRepository.search(query, pageable);
    }

    @Override
    public Optional<Property> findById(Long propertyId) {
        return propertyRepository.findById(propertyId);
    }


    @Override
    @Transactional
    public Property create(Property property) throws ResourceNotFoundException {

        // Check if the property with the given address already exists
        if (propertyRepository.existsByAddress(property.getAddress())) {
            throw new PropertyExistsException("Property with this address already exists");
        }

        // Check if the user (landlord) exists
        AppUser owner = userRepository.findByEmail(SecurityUtils.getCurrentUserEmail())
                .orElseThrow(() -> new UserNotFoundException("Landlord not found"));
        City city = cityRepository.findByNameIgnoreCase(property.getCity().getName())
                .orElseThrow(() -> new ResourceNotFoundException("city", "City not found with name: " + property.getCity().getName()));
        property.setCity(city);
        property.setLandlord(owner);

        return propertyRepository.save(property);
    }

    private void uploadPropertyImages(Property property, List<MultipartFile> images) {
        List<PropertyImage> propertyImages = new ArrayList<>();

        for (MultipartFile image : images) {
            try {
                String fileName = fileUtils.storeFile(image);
                PropertyImage propertyImage = PropertyImage.builder()
                        .path(fileName)
                        .property(property)
                        .build();
                propertyImages.add(propertyImage);
            } catch (IOException e) {
                log.error("Error to store image name:" + image.getName(), e);
            }
        }

        // Save all property images
        propertyImageRepository.saveAll(propertyImages);
    }

    @Override
    public void update(Property property) {

    }

    @Override
    @Transactional
    public void delete(Long propertyId) throws ResourceNotFoundException {
        Property property = findById(propertyId).orElseThrow(
                () -> new ResourceNotFoundException("Property", "Property with id: " + propertyId)
        );
        property.getImages().forEach(image -> {
            try {
                fileUtils.deleteFile(image.getPath());
            } catch (IOException e) {
                log.error("Error to delete image with path: " + image.getPath(), e);
            }
        });
        propertyRepository.delete(property);
    }

    @Override
    public Property createPropertyImages(Long id, List<MultipartFile> images) {
        Property property = this.findById(id).orElseThrow();
        uploadPropertyImages(property, images);
        return property;
    }

    @Override
    public Page<Property> getAvailableProperties(LocalDate startDate, LocalDate endDate, String city, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> cq = cb.createQuery(Property.class);
        Root<Property> property = cq.from(Property.class);
        List<Predicate> predicates = new ArrayList<>();

        // Subquery to find properties that are not rented during the given period
        Subquery<Long> subquery = cq.subquery(Long.class);
        Root<Rent> rent = subquery.from(Rent.class);
        subquery.select(rent.get("property").get("id")).where(
                cb.equal(rent.get("status"), RentStatus.ONGOING),
                cb.or(
                        cb.and(cb.lessThanOrEqualTo(rent.get("startDate"), startDate),
                                cb.greaterThanOrEqualTo(rent.get("endDate"), startDate)),
                        cb.and(cb.lessThanOrEqualTo(rent.get("startDate"), endDate),
                                cb.greaterThanOrEqualTo(rent.get("endDate"), endDate)),
                        cb.and(cb.greaterThanOrEqualTo(rent.get("startDate"), startDate),
                                cb.lessThanOrEqualTo(rent.get("endDate"), endDate))
                )
        );

        predicates.add(cb.not(property.get("id").in(subquery)));

        // Additional condition for the city if it's provided
        if (city != null && !city.isEmpty()) {
            // Assuming there's a many-to-one relation between Property and City
            Join<Property, City> propertyCity = property.join("city");
            predicates.add(cb.equal(cb.lower(propertyCity.get("name")), city.toLowerCase()));
        }

        cq.select(property).where(predicates.toArray(new Predicate[0]));

        TypedQuery<Property> query = entityManager.createQuery(cq);

        // Apply pagination
        int totalRows = query.getResultList().size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Return a new PageImpl object
        return new PageImpl<>(query.getResultList(), pageable, totalRows);
    }
}
