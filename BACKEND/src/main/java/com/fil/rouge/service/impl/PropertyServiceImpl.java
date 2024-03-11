package com.fil.rouge.service.impl;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.domain.City;
import com.fil.rouge.domain.Property;
import com.fil.rouge.domain.PropertyImage;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        City city1 = cityRepository.findByNameIgnoreCase(city)
                .orElse(null);

        return propertyRepository.getAvailableProperties(startDate, endDate, city1, pageable);
    }
}
