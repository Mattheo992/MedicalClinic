package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.*;
import com.Mattheo992.medicalclinic.model.dtos.InstitutionDto;
import com.Mattheo992.medicalclinic.model.mappers.InstitutionMapper;
import com.Mattheo992.medicalclinic.repository.DoctorRepository;
import com.Mattheo992.medicalclinic.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final DoctorRepository doctorRepository;
    private final InstitutionMapper institutionMapper;

    @Transactional
    public InstitutionDto addInstitution(InstitutionDto institutionDto) {
        Institution institution = institutionMapper.toEntity(institutionDto);
        checkIfNameIsAvailable(institution.getInstitutionName());
        return institutionMapper.toDto(institutionRepository.save(institution));
    }

    public List<InstitutionDto> getInstitutions(Pageable pageable) {
        List<Institution> institutions = institutionRepository.findAll(pageable).getContent();
        return institutionMapper.toListDtos(institutions);
    }

    @Transactional
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