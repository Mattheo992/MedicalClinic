package com.Mattheo992.medicalclinic.repository;

import com.Mattheo992.medicalclinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPatientId(Long id);

    boolean existsByStartDate(LocalDateTime startDate);
}