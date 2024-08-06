package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.model.dtos.InstitutionDto;
import com.Mattheo992.medicalclinic.model.mappers.InstitutionMapper;
import com.Mattheo992.medicalclinic.repository.DoctorRepository;
import com.Mattheo992.medicalclinic.repository.InstitutionRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        // given
        Pageable pageable = PageRequest.of(0, 10);

        Institution institution1 = new Institution();
        institution1.setId(1L);
        institution1.setInstitutionName("Szpital 1");
        institution1.setCity("Łódź");
        institution1.setZipCode("12-123");
        institution1.setStreetName("Polska");
        institution1.setNumberOfStreet(10L);

        Institution institution2 = new Institution();
        institution2.setId(2L);
        institution1.setInstitutionName("Szpital 2");
        institution1.setCity("Łódź");
        institution1.setZipCode("12-333");
        institution1.setStreetName("Zielona");
        institution1.setNumberOfStreet(11L);

        List<Institution> institutions = Arrays.asList(institution1, institution2);
        Page<Institution> institutionPage = new PageImpl<>(institutions, pageable, institutions.size());
        when(institutionRepository.findAll(pageable)).thenReturn(institutionPage);

        // when
        List<InstitutionDto> result = institutionService.getInstitutions(pageable);

        // then
        assertNotNull(result);
        assertEquals(institutions.size(), result.size());
        InstitutionDto resultInstitution1 = result.get(0);
        assertEquals(institution1.getInstitutionName(), resultInstitution1.getInstitutionName());
        assertEquals(institution1.getCity(), resultInstitution1.getCity());
        assertEquals(institution1.getZipCode(), resultInstitution1.getZipCode());
        assertEquals(institution1.getStreetName(), resultInstitution1.getStreetName());
        assertEquals(institution1.getNumberOfStreet(), resultInstitution1.getNumberOfStreet());
        InstitutionDto resultInstitution2 = result.get(1);
        assertEquals(institution2.getInstitutionName(), resultInstitution2.getInstitutionName());
        assertEquals(institution2.getCity(), resultInstitution2.getCity());
        assertEquals(institution2.getZipCode(), resultInstitution2.getZipCode());
        assertEquals(institution2.getStreetName(), resultInstitution2.getStreetName());
        assertEquals(institution2.getNumberOfStreet(), resultInstitution2.getNumberOfStreet());
    }

    @Test
    void GetInstitutions_InstitutionsNotExists_ReturnedEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Institution> institutionPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(institutionRepository.findAll(pageable)).thenReturn(institutionPage);
        // when
        List<InstitutionDto> result = institutionService.getInstitutions(pageable);
        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void addInstitutions_InstitutionNameNotAvailable_InstitutionsNotCreated() {
        //given
        String institutionName = "Barlik";
        Institution institution = createInstitution(1L, "Barlik");
        InstitutionDto institutionDto = institutionMapper.toDto(institution);
        when(institutionRepository.existsByInstitutionName(institutionName)).thenReturn(true);
        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> institutionService.addInstitution(institutionDto));
        //then
        assertEquals("Institution with given name already exist!", exception.getMessage());
    }

    @Test
    void addDoctorToInstitution_InstitutionExistDoctorExist_DoctorAddedToInstitution() {
        // Given
        Long institutionId = 1L;
        Long doctorId = 1L;
        Institution institution = createInstitution(institutionId, "Barlicki");
        Doctor doctor = new Doctor(doctorId, "asd", "asd", "asd", "asd", "asd", new ArrayList<>(), new ArrayList<>());
        when(institutionRepository.findById(institutionId)).thenReturn(Optional.of(institution));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        // When
        institutionService.addDoctorToInstitution(institutionId, doctorId);
        // Then
        verify(institutionRepository, times(1)).findById(institutionId);
        verify(doctorRepository, times(1)).findById(doctorId);
        verify(institutionRepository, times(1)).save(any(Institution.class));
    }

    @Test
    void addDoctorToInstitution_InstitutionNotExist_DoctorNotAdded() {
        //give
        Long institutionId = 1L;
        Long doctorId = 1L;
        Institution institution = createInstitution(2L, "Barlicki");
        Set<Institution> institutions = new HashSet<>();
        institutions.add(institution);
        when(institutionRepository.findById(institutionId)).thenReturn(Optional.empty());
        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> institutionService.addDoctorToInstitution(institutionId, doctorId));
        //then
        assertEquals("Institution not found", exception.getMessage());
        verify(institutionRepository).findById(institutionId);
        verifyNoMoreInteractions(institutionRepository);
        verifyNoMoreInteractions(doctorRepository);
    }

    Institution createInstitution(Long id, String institutionName) {
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setEmail("lekarz@lekarz.pl");
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);
        return new Institution(id, institutionName, "Lodz", "91-013", "Narutowicza", 21L, doctors);
    }
}