package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.exception.exceptions.PatientNotFound;
import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.model.dtos.PatientDto;
import com.Mattheo992.medicalclinic.model.mappers.PatientDtoMapper;
import com.Mattheo992.medicalclinic.model.mappers.UserMapper;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import com.Mattheo992.medicalclinic.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PatientServiceTest {
    PatientService patientService;
    PatientRepository patientRepository;
    UserRepository userRepository;
    PatientDtoMapper patientDtoMapper;
    UserMapper userMapper;
    PatientNotFound patientNotFound;

    @BeforeEach
    void setup() {
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.patientDtoMapper = Mappers.getMapper(PatientDtoMapper.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.patientService = new PatientService(patientRepository, userRepository, patientDtoMapper, userMapper);
    }

    @Test
    void getPatients_PatientsExist_PatientsReturned() {
        //given
        List<Patient> patients = new ArrayList<>();
        patients.add(createPatient("aa@wp.pl", 1L));
        patients.add(createPatient("bb@wp.pl", 2L));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Patient> patientPage = new PageImpl<>(patients, pageable, patients.size());
        when(patientRepository.findAll(pageable)).thenReturn(patientPage);
        //when
        List<PatientDto> result = patientService.getPatients(pageable);
        //then
        assertEquals(2, result.size());
        assertEquals("aa@wp.pl", result.get(0).getEmail());
        assertEquals("bb@wp.pl", result.get(1).getEmail());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getPatients_PatientsNotExist_EmptyListReturned() {
        //given
        List<Patient> patients = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Patient> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(patientRepository.findAll(pageable)).thenReturn(emptyPage);
        //when
        List<PatientDto> result = patientService.getPatients(pageable);
        //then
        assertEquals(0, result.size());
    }

    @Test
    void GetPatient_PatientExist_PatientReturned() {
        // Given
        String patientEmail = "patient@gmail.com";
        Patient patient1 = createPatient(patientEmail, 3L);
        PatientDto patientDto = patientDtoMapper.dto(patient1);
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(patient1));
        // when
        PatientDto result = patientService.getPatient(patientEmail);
        // then
        Assertions.assertNotNull(result);
        assertEquals(patientEmail, result.getEmail());
    }

    @Test
    void GetPatient_PatientNotExist_ExceptionReturned() {
        //given
        Patient patient1 = createPatient("cc@onet.pl", 3L);
        PatientDto patientDto = patientDtoMapper.dto(patient1);
        String email = "bb@onet.pl";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when
        PatientNotFound result = assertThrows(PatientNotFound.class, () -> patientService.getPatient(email));
        //then
        assertEquals("Patient with given email does not exist.", result.getMessage());
    }

    @Test
    void addPatient_EmailIsAvailable_PatientCreated() {
        Patient patient = createPatient("test@test.pl", 1L);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        // when
        PatientDto result = patientService.addPatient(patient);
        // then
        Assertions.assertNotNull(result);
        assertEquals("test@test.pl", result.getEmail());
    }

    @Test
    void addPatient_EmailIsNotAvailable_PatientNotCreated() {
        Patient patient = createPatient("a@a.pl", 1L);
        when(patientRepository.existsByEmail(patient.getEmail())).thenReturn(true);
        //when
        IllegalArgumentException result = Assertions.assertThrows(IllegalArgumentException.class, () -> patientService.addPatient(patient));
        //then
        assertEquals("Patient with given email already exists.", result.getMessage());
    }

    @Test
    void AddPatientWithUser_EmailIsNotAvailable_PatientNotCreated(){
       //given
        String email = "asd";
        Patient patient = createPatient(email, 1L);
        PatientDto patientDto = patientDtoMapper.dto(patient);
        when(patientRepository.existsByEmail(email)).thenReturn(true);
        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, ()-> patientService.addPatientWithUser(patientDto));
        //then
        assertEquals("Patient with given email already exists.",exception.getMessage());
    }

    @Test
    void editPatient_PatientExist_PatientEdited() {
        String patientEmail = "updatepatient@gmail.com";
        Patient patient = createPatient(patientEmail, 1L);
        Patient newPatient = new Patient();
        newPatient.setEmail(patientEmail);
        newPatient.setFirstName("UpdatedFirstName");
        newPatient.setLastName("UpdatedLastName");
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        // when
        PatientDto result = patientService.editPatient(patientEmail, newPatient);
        // then
        Assertions.assertNotNull(result);
        assertEquals("UpdatedFirstName", result.getFirstName());
        assertEquals("UpdatedLastName", result.getLastName());
    }

    @Test
    void editPatient_PatientNotExists_PatientNotEdited(){
        //given
        Patient editedPatient = createPatient("b@b.pl", 1L);
        Patient savedPatient = createPatient("b@b.pl", 1L);
        PatientDto patientDto = patientDtoMapper.dto(savedPatient);
        String email = "a@a.pl";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when
        PatientNotFound exception = Assertions.assertThrows(PatientNotFound.class, ()-> patientService.editPatient(email, editedPatient));
        //then
        assertEquals("Patient with given email does not exist.", exception.getMessage());
    }

    @Test
    void deletePatient_PatientExists_PatientDeleted(){
        //given
        String email = "a@a.pl";
        Patient patient = createPatient(email, 1L);
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
        //when
        patientService.deletePatient(email);
        //then
        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    void deletePatient_PatientNotExists_PatientNotDeleted(){
        // given
        String patientEmail2 = "nonexistentpatient@gmail.com";
        when(patientRepository.findByEmail(patientEmail2)).thenReturn(Optional.empty());
        // when + then
        PatientNotFound exception = assertThrows(PatientNotFound.class, () -> patientService.deletePatient(patientEmail2));
        assertEquals("Patient with given email does not exist.", exception.getMessage());
        verify(patientRepository, times(1)).findByEmail(patientEmail2);
        verify(patientRepository, never()).delete(any(Patient.class));
    }

    Patient createPatient(String email, Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("mateusz");
        return new Patient(id, email, "1111", "mateusz", "kowalski", "333666888",
                LocalDate.of(1992, 10, 11), user, new ArrayList<>());
    }
}