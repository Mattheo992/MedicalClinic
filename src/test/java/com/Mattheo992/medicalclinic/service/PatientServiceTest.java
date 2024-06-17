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
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
        this.patientService = new PatientService(patientRepository, userRepository,patientDtoMapper,userMapper);
    }

    @Test
    void getPatients_PatientsExist_PatientsReturned() {
        //given
        List<Patient> patients = new ArrayList<>();
        patients.add(createPatient("aa@wp.pl", 1L));
        patients.add(createPatient("bb@wp.pl", 2L));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        //when
        when(patientRepository.findAll()).thenReturn(patients);
        List<PatientDto> result = patientService.getPatients(pageable);
        //then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("aa@wp.pl", result.get(0).getEmail());
        Assertions.assertEquals("bb@wp.pl", result.get(1).getEmail());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(2L, result.get(1).getId());
    }

    @Test
    void GetPatient_PatientExist_PatientReturned() {
        //given
        Patient patient1 = createPatient("cc@onet.pl", 3L);
        PatientDto patientDto = patientDtoMapper.dto(patient1);
        String email = "cc@onet.pl";
        //when
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient1));
        when(patientDtoMapper.dto(patient1)).thenReturn(patientDto);
        PatientDto result = patientService.getPatient(email);
//then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("cc@onet.pl", result.getEmail());
        Assertions.assertEquals(3L, result.getId());
    }

    @Test
    void addPatient_EmailIsAvailable_PatientCreated() {
        Patient patient = createPatient("a@a.pl", 1L);
        Patient savedPatient = createPatient("a@a.pl", 1L);
        PatientDto patientDto = patientDtoMapper.dto(savedPatient);
//when
doNothing().when(patientRepository.existsByEmail(patient.getEmail()));
when(patientRepository.save(patient)).thenReturn(savedPatient);
when(patientDtoMapper.dto(savedPatient)).thenReturn(patientDto);
PatientDto result = patientService.addPatient(patient);
//then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("a@a.pl", result.getEmail());
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void addPatientWithUser_EmailIsAvailable_PatientWithUserCreated(){
User user = new User(1L, "mateusz", "1234",null);
Patient patient = createPatient("a@a.pl", 1L);
PatientDto patientDto = patientDtoMapper.dto(patient);

    }

    @Test
    void editPatient_Patient_PatientEdited(){
        Patient patient = createPatient("a@a.pl", 1L);
        Patient editedPatient = createPatient("b@b.pl", 1L);
        Patient savedPatient = createPatient("b@b.pl", 1L);
        PatientDto patientDto = patientDtoMapper.dto(savedPatient);
        String email = "a@a.pl";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(savedPatient);
        when(patientDtoMapper.dto(savedPatient)).thenReturn(patientDto);

        PatientDto result = patientService.editPatient(email,editedPatient);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("b@b.pl", result.getEmail());
    }

    Patient createPatient(String email, Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("mateusz");
        return new Patient(id, email, "1111", "mateusz", "kowalski", "333666888",
                LocalDate.of(1992, 10, 11), user, new ArrayList<>());
    }
}
