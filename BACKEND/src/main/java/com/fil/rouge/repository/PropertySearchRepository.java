package com.fil.rouge.repository;

import com.fil.rouge.domain.City;
import com.fil.rouge.domain.Property;
import com.fil.rouge.domain.Rent;
import com.fil.rouge.domain.enums.RentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PropertySearchRepository {

    private final EntityManager entityManager;
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";

    public Page<Property> searchByCityAndDateAvailability(
            LocalDate startDate, LocalDate endDate, String city, Pageable pageable) {

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
                        cb.and(cb.lessThanOrEqualTo(rent.get(START_DATE), startDate),
                                cb.greaterThanOrEqualTo(rent.get(END_DATE), startDate)),
                        cb.and(cb.lessThanOrEqualTo(rent.get(START_DATE), endDate),
                                cb.greaterThanOrEqualTo(rent.get(END_DATE), endDate)),
                        cb.and(cb.greaterThanOrEqualTo(rent.get(START_DATE), startDate),
                                cb.lessThanOrEqualTo(rent.get(END_DATE), endDate))
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
