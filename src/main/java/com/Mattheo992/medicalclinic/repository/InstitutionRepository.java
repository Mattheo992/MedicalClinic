package com.Mattheo992.medicalclinic.repository;

import com.Mattheo992.medicalclinic.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    boolean existsByInstitutionName(String name);
    Page<Institution> findAll(Pageable pageable);
}
