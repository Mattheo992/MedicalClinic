package com.Mattheo992.medicalclinic.repository;

import com.Mattheo992.medicalclinic.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findAll(Pageable pageable);
    boolean existsByEmail (String email);
    List<Doctor> findBySpecialization(String specialization);
}
