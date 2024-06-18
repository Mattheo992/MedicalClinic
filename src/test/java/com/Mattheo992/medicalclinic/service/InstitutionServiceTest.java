package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.model.dtos.InstitutionDto;
import com.Mattheo992.medicalclinic.model.mappers.InstitutionMapper;
import com.Mattheo992.medicalclinic.repository.DoctorRepository;
import com.Mattheo992.medicalclinic.repository.InstitutionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class InstitutionServiceTest {
    InstitutionService institutionService;
    InstitutionRepository institutionRepository;
    DoctorRepository doctorRepository;
    InstitutionMapper institutionMapper;

    @BeforeEach
    void setup() {
        this.institutionRepository = Mockito.mock(InstitutionRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.institutionMapper = Mappers.getMapper(InstitutionMapper.class);
        this.institutionService = new InstitutionService(institutionRepository, doctorRepository, institutionMapper);
    }

    @Test
    void getInstitutions_InstitutionsExists_ReturnInstitutions() {
        //given
        List<Institution> institutions = new ArrayList<>();
        institutions.add(createInstitution(1L, "Barlik"));
        institutions.add(createInstitution(2L, "CKD"));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(institutionRepository.findAll()).thenReturn(institutions);
        //when
        List<InstitutionDto> result = institutionService.getInstitutions(pageable);
        //then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Barlik", result.get(0).getInstitutionName());
        Assertions.assertEquals("CKD", result.get(1).getInstitutionName());
    }

    @Test
    void addInstitution_InstitutionNameAvailable_InstitutionCreated() {
        //given
        Institution institution = createInstitution(1L, "Barlicki");
        InstitutionDto institutionDto = institutionMapper.toDto(institution);
        when(institutionMapper.toEntity(institutionDto)).thenReturn(institution);
        doNothing().when(institutionRepository.existsByInstitutionName(institution.getInstitutionName()));
        when(institutionRepository.save(institution));
        //when
        InstitutionDto result = institutionService.addInstitution(institutionDto);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Barlicki", result.getInstitutionName());
    }

    Institution createInstitution(Long id, String institutionName) {
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setEmail("lekarz@lekarz.pl");
        Set<Doctor> doctors = new HashSet<>();
        doctors.add(doctor);
        return new Institution(id, institutionName, "Lodz", "91-013", "Narutowicza", 21L, doctors);
    }
}
