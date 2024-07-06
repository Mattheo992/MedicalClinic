package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.model.dtos.DoctorDto;
import com.Mattheo992.medicalclinic.model.mappers.DoctorMapper;
import com.Mattheo992.medicalclinic.model.mappers.DoctorMapperImpl;
import com.Mattheo992.medicalclinic.repository.DoctorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DoctorServiceTest {
    DoctorService doctorService;
    DoctorRepository doctorRepository;
    DoctorMapper doctorMapper;

    @BeforeEach
    void setup() {
        this.doctorRepository = mock(DoctorRepository.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.doctorService = new DoctorService(doctorRepository, doctorMapper);
    }

    @Test
    void addDoctor_DoctorCreated() {
        //given
        Doctor doctor = createDoctor(1L, "2@wp.pl");
        when(doctorRepository.save(doctor)).thenReturn(doctor);
//when
        Doctor result = doctorService.addDoctor(doctor);
//then
        Assertions.assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("2@wp.pl", result.getEmail());
        assertEquals(doctor, result);
    }

    @Test
    void addDoctor_EmailIsAvailable_DoctorNotAdded() {
        //given
        String email = "asdasd";
        Doctor doctor = createDoctor(1L, email);
        when(doctorRepository.existsByEmail(email)).thenReturn(true);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> doctorService.addDoctor(doctor));
        //then
        assertEquals("Doctor with given email is already exist", exception.getMessage());
    }

    @Test
    void getDoctors_DoctorsExists_ReturnedDoctors() {
        // given
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(createDoctor(1L, "1@wp.pl"));
        doctors.add(createDoctor(2L, "2@wp.pl"));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("email").ascending());
        Page<Doctor> doctorPage = new PageImpl<>(doctors, pageable, doctors.size());
        when(doctorRepository.findAll(pageable)).thenReturn(doctorPage);

        // when
        List<DoctorDto> result = doctorService.getDoctors(pageable);

        // then
        assertEquals(2, result.size());
        assertEquals("1@wp.pl", result.get(0).getEmail());
        assertEquals("2@wp.pl", result.get(1).getEmail());
    }

    @Test
    void getDoctors_DoctorsNotExists_ReturnedEmpty() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("email").ascending());
        Page<Doctor> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        DoctorRepository doctorRepository = mock(DoctorRepository.class);
        when(doctorRepository.findAll(pageable)).thenReturn(emptyPage);
        DoctorMapper doctorMapper = new DoctorMapperImpl();
        DoctorService doctorService = new DoctorService(doctorRepository, doctorMapper);
        // when
        List<DoctorDto> result = doctorService.getDoctors(pageable);
        // then
        Assertions.assertTrue(result.isEmpty(), "Expected the result list to be empty");
    }
    @Test
    void getInstitutionsForDoctor_InstitutionAndDoctorExists_ReturnedInstitutionsForDoctor() {
        // given
        Long id = 1L;
        Doctor doctor = createDoctor(id, "mm@wp.pl");
        Institution institution = createInstitution(1L, "Hospital A");
        doctor.setInstitutions(Collections.singletonList(institution)); // ustawienie instytucji dla lekarza
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));

        // when
        List<Institution> result = doctorService.getInstitutionsForDoctor(id);

        // then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getInstitutionsForDoctor_DoctorNotFound_ReturnedException() {
        //given
        Long id = 2L;
        Doctor doctor = createDoctor(1L, "mm@wp.pl");
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> doctorService.getInstitutionsForDoctor(id));
        //then
        assertEquals("Doctor not found", exception.getMessage());
    }

    private Doctor createDoctor(Long id, String email) {
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setEmail(email);

        return doctor;
    }

    private Institution createInstitution(Long id, String name) {
        Institution institution = new Institution();
        institution.setId(id);
        institution.setInstitutionName(name);
        return institution;
    }
}