package com.fil.rouge.config.seeder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fil.rouge.domain.City;
import com.fil.rouge.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class CityDataLoader implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final Resource citiesJsonResource;

    public CityDataLoader(CityRepository cityRepository, @Value("classpath:/db/data/cities.json") Resource citiesJsonResource) {
        this.cityRepository = cityRepository;
        this.citiesJsonResource = citiesJsonResource;
    }

    @Override
    public void run(String... args) throws Exception {
        // if there is any cities so do not save any
        if(cityRepository.count() > 0) return;
        // Load data from cities.json and save to the database
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<City> cities = objectMapper.readValue(citiesJsonResource.getInputStream(), new TypeReference<>() {});
            cityRepository.saveAll(cities);
            log.info("Cities loaded successfully!");
        } catch (IOException e) {
            log.error("Error loading cities: " + e.getMessage());
        }
    }
}
