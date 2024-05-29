package com.Mattheo992.medicalclinic.repository;

import com.Mattheo992.medicalclinic.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    boolean existsByInstitutionName(String name);
}
