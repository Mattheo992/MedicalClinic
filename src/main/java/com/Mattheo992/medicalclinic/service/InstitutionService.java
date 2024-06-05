package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.*;
import com.Mattheo992.medicalclinic.repository.DoctorRepository;
import com.Mattheo992.medicalclinic.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final DoctorRepository doctorRepository;
    private final InstitutionMapper institutionMapper;


    public InstitutionDto addInstitution(InstitutionDto institutionDto) {
        Institution institution = institutionMapper.toEntity(institutionDto);
        checkIfNameIsAvailable(institution.getInstitutionName());
        return institutionMapper.toDto(institutionRepository.save(institution));
    }

    public Page<InstitutionDto> getInstitutions (Pageable pageable) {
        Page<Institution> intitutionPage = institutionRepository.findAll(pageable);
        return intitutionPage.map(institutionMapper::toDto);
    }

    public void addDoctorToInstitution(Long institutionId, Long doctorId) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        institution.getDoctors().add(doctor);
        institutionRepository.save(institution);
    }

    private void checkIfNameIsAvailable(String name) {
        if (institutionRepository.existsByInstitutionName(name)) {
            throw new IllegalArgumentException("Institution with given name already exist!");
        }
    }
}