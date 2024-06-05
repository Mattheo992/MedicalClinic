package com.Mattheo992.medicalclinic.repository;

import com.Mattheo992.medicalclinic.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Patient> findAll (Pageable pageable);
}