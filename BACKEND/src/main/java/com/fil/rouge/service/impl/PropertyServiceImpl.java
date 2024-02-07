package com.fil.rouge.service.impl;

import com.fil.rouge.domain.AppUser;
import com.fil.rouge.domain.Property;
import com.fil.rouge.repository.PropertyRepository;
import com.fil.rouge.repository.UserRepository;
import com.fil.rouge.security.SecurityUtils;
import com.fil.rouge.service.PropertyService;
import com.fil.rouge.web.exception.PropertyExistsException;
import com.fil.rouge.web.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository,
                               UserRepository userRepository) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    @Override
    public Optional<Property> findById(Long propertyId) {
        return propertyRepository.findById(propertyId);
    }

    @Override
    public Property create(Property property) {
        validateProperty(property);

        // Check if the property with the given address already exists
        if (propertyRepository.existsByAddress(property.getAddress())) {
            throw new PropertyExistsException("Property with this address already exists");
        }

        // Check if the user (landlord) exists
        AppUser owner = userRepository.findByEmail(SecurityUtils.getCurrentUserEmail())
                .orElseThrow(() -> new UserNotFoundException("Landlord not found"));

        property.setLandlord(owner);

        return propertyRepository.save(property);
    }

    private void validateProperty(Property property) {
//        // Perform basic validation checks on the PropertyDto
//        if (StringUtils.isBlank(property.getAddress())) {
//            throw new InvalidPropertyException("Address cannot be blank");
//        }
//
//        if (property.getBedrooms() <= 0) {
//            throw new InvalidPropertyException("Invalid number of bedrooms");
//        }

        // Additional validation checks can be added based on your requirements
    }

    @Override
    public void update(Property property) {

    }

    @Override
    public void delete(Long propertyId) {

    }
}
