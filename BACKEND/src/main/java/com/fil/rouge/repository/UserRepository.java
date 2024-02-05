package com.fil.rouge.repository;

import com.fil.rouge.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    @Query("SELECT u FROM AppUser u WHERE u.id = ?1 AND u.deletedAt IS NULL")
    void softDelete(Long id);

    @Query("SELECT u FROM AppUser u WHERE u.id = ?1 AND u.deletedAt IS NOT NULL")
    void forceDelete(Long id);
}
