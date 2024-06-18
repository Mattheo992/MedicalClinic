package com.Mattheo992.medicalclinic.service;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PatientServiceTest {
    PatientService patientService;
    PatientRepository patientRepository;
    UserRepository userRepository;
    PatientDtoMapper patientDtoMapper;
    UserMapper userMapper;

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
        when(patientRepository.findAll()).thenReturn(patients);
        //when
        List<PatientDto> result = patientService.getPatients(pageable);
        //then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("aa@wp.pl", result.get(0).getEmail());
        Assertions.assertEquals("bb@wp.pl", result.get(1).getEmail());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getPatients_PatientsNotExist_EmptyListReturned() {
        //given
        List<Patient> patients = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(patientRepository.findAll()).thenReturn(Collections.emptyList());
        //when
        List<PatientDto> result = patientService.getPatients(pageable);
        //then
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void GetPatient_PatientExist_PatientReturned() {
        //given
        Patient patient1 = createPatient("cc@onet.pl", 3L);
        PatientDto patientDto = patientDtoMapper.dto(patient1);
        String email = "cc@onet.pl";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient1));
        when(patientDtoMapper.dto(patient1)).thenReturn(patientDto);
        //when
        PatientDto result = patientService.getPatient(email);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("cc@onet.pl", result.getEmail());
        Assertions.assertEquals(3L, result.getId());
    }

    @Test
    void GetPatient_PatientNotExist_ExceptionReturned() {
        //given
        Patient patient1 = createPatient("cc@onet.pl", 3L);
        PatientDto patientDto = patientDtoMapper.dto(patient1);
        String email = "bb@onet.pl";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> patientService.getPatient(email));
        //then
        Assertions.assertEquals("Patient with given email does not exist.", result.getMessage());
    }

    @Test
    void addPatient_EmailIsAvailable_PatientCreated() {
        Patient patient = createPatient("a@a.pl", 1L);
        Patient savedPatient = createPatient("a@a.pl", 1L);
        PatientDto patientDto = patientDtoMapper.dto(savedPatient);
        doNothing().when(patientRepository.existsByEmail(patient.getEmail()));
        when(patientRepository.save(patient)).thenReturn(savedPatient);
        when(patientDtoMapper.dto(savedPatient)).thenReturn(patientDto);
        //when
        PatientDto result = patientService.addPatient(patient);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("a@a.pl", result.getEmail());
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void addPatient_EmailIsNotAvailable_PatientNotCreated() {
        Patient patient = createPatient("a@a.pl", 1L);
        when(patientRepository.existsByEmail(patient.getEmail())).thenReturn(true);
        //when
        IllegalArgumentException result = Assertions.assertThrows(IllegalArgumentException.class, () -> patientService.addPatient(patient));
        //then
        Assertions.assertEquals("Patient with given email already exists.", result.getMessage());
    }

    @Test
    void addPatientWithUser_EmailIsAvailable_PatientWithUserCreated() {
        //given
        User user = new User(1L, "mateusz", "1234", null);
        Patient patient = createPatient("a@a.pl", 1L);
        PatientDto patientDto = patientDtoMapper.dto(patient);
        when(userRepository.save(user)).thenReturn(user);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientDtoMapper.dto(patient)).thenReturn(patientDto);
        //when
        PatientDto result = patientService.addPatientWithUser(patientDto);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getUser().getId());
        Assertions.assertEquals("mateusz", result.getUser().getUsername());
        Assertions.assertEquals("1234", result.getUser().getPassword());
        Assertions.assertEquals("a@a.pl", result.getEmail());
        Assertions.assertEquals("1L", result.getId());
    }

    @Test
    void AddPatientWithUser_EmailIsNotAvailable_PatientNotCreated(){
       //given
        String email = "asd";
        Patient patient = createPatient("a@a.pl", 1L);
        PatientDto patientDto = patientDtoMapper.dto(patient);
        when(patientRepository.existsByEmail(email)).thenReturn(true);
        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, ()-> patientService.addPatientWithUser(patientDto));
        //then
        Assertions.assertEquals("Patient with given email already exists.",exception.getMessage());
    }

    @Test
    void editPatient_PatientExist_PatientEdited() {
        //given
        Patient patient = createPatient("a@a.pl", 1L);
        Patient editedPatient = createPatient("b@b.pl", 1L);
        Patient savedPatient = createPatient("b@b.pl", 1L);
        PatientDto patientDto = patientDtoMapper.dto(savedPatient);
        String email = "a@a.pl";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(savedPatient);
        when(patientDtoMapper.dto(savedPatient)).thenReturn(patientDto);
        //when
        PatientDto result = patientService.editPatient(email, editedPatient);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("b@b.pl", result.getEmail());
    }

    @Test
    void editPatient_PatientNotExists_PatientNotEdited(){
        //given
        Patient patient = createPatient("a@a.pl", 1L);
        Patient editedPatient = createPatient("b@b.pl", 1L);
        Patient savedPatient = createPatient("b@b.pl", 1L);
        PatientDto patientDto = patientDtoMapper.dto(savedPatient);
        String email = "a@a.pl";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, ()-> patientService.editPatient(email, editedPatient));
        //then
        Assertions.assertEquals("Patient not found", exception.getMessage());
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
      //given
        String email = "a@a.pl";
        Patient patient = createPatient(email, 1L);
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()-> patientService.deletePatient(email));
        //then
        Assertions.assertEquals("Patient with given email does not exist.",exception.getMessage());
        verify(patientRepository, times(1)).findByEmail(email);
        verifyNoInteractions(patientRepository);
    }


    Patient createPatient(String email, Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("mateusz");
        return new Patient(id, email, "1111", "mateusz", "kowalski", "333666888",
                LocalDate.of(1992, 10, 11), user, new ArrayList<>());
    }
}