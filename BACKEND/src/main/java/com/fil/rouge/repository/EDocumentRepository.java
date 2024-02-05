package com.fil.rouge.repository;

import com.fil.rouge.domain.EDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EDocumentRepository extends JpaRepository<EDocument, Long> {
}
