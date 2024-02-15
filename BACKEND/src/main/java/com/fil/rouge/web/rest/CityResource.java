package com.fil.rouge.web.rest;

import com.fil.rouge.domain.City;
import com.fil.rouge.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/cities")
@RequiredArgsConstructor
public class CityResource {

    private final CityRepository cityRepository;

    @GetMapping
    public ResponseEntity<List<String>> finAllCities() {
        return ResponseEntity.ok(
                cityRepository.findAll().stream()
                        .map(City::getName)
                        .toList()
        );
    }
}
