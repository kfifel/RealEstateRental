package com.fil.rouge.repository;

import com.fil.rouge.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    List<Role> findAllByNameIn(List<String> roleNames);
}
