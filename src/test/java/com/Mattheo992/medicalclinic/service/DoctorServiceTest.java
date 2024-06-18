package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.model.dtos.DoctorDto;
import com.Mattheo992.medicalclinic.model.mappers.DoctorMapper;
import com.Mattheo992.medicalclinic.repository.DoctorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.mockito.Mockito.when;

public class DoctorServiceTest {
    DoctorService doctorService;
    DoctorRepository doctorRepository;
    DoctorMapper doctorMapper;

    @BeforeEach
    void setup() {
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
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
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("2@wp.pl", result.getEmail());
        Assertions.assertEquals(doctor, result);
    }

    @Test
    void getDoctors_DoctorsExists_ReturnedDoctors() {
        //given
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(createDoctor(1l, "1@wp.pl"));
        doctors.add(createDoctor(2L, "2@wp.pl"));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("email").ascending());
        when(doctorRepository.findAll()).thenReturn(doctors);
        //when
        List<DoctorDto> result = doctorService.getDoctors(pageable);
        //then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("1@wp.pl", result.get(0).getEmail());
        Assertions.assertEquals("2@wp.pl", result.get(1).getEmail());
    }

    @Test
    void getInstitutionsForDoctor_InstitutionAndDoctorExists_ReturnedInstitutionsForDoctor() {
        //given
        Long id = 1L;
        Doctor doctor = createDoctor(1L, "mm@wp.pl");
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        //when
        Set<Institution> result = doctorService.getInstitutionsForDoctor(id);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        result.forEach(institution -> Assertions.assertEquals(1L, institution.getId()));
    }

    @Test
    void getInstitutionsForDoctor_DoctorNotFound_ReturnedException() {
        //given
        Long id = 2L;
        Doctor doctor = createDoctor(1L, "mm@wp.pl");
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());
        //when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> doctorService.getInstitutionsForDoctor(id));
    }

    Doctor createDoctor(Long id, String email) {
        //given
        Institution institution = new Institution();
        institution.setId(id);
        institution.setInstitutionName("Barlicki");
        //when
        Set<Institution> institutions = new HashSet<>();
        //then
        return new Doctor(id, "Mateusz", "Bar", "Anestezjolog", email, "haslo", institutions);
    }
}