package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.model.dtos.PatientDto;
import com.Mattheo992.medicalclinic.model.dtos.UserDto;
import com.Mattheo992.medicalclinic.model.mappers.PatientDtoMapper;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import com.Mattheo992.medicalclinic.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {
    @MockBean
    PatientService patientService;

    @MockBean
    PatientRepository patientRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PatientDtoMapper patientDtoMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getPatients_PatientsExists_PatientsListReturned() throws Exception {
        // Given
        PatientDto patientDto1 = new PatientDto();
        patientDto1.setId(1L);
        patientDto1.setEmail("asd!@#");

        PatientDto patientDto2 = new PatientDto();
        patientDto2.setId(2L);
        patientDto2.setEmail("11@#");

        List<PatientDto> patients = new ArrayList<>();
        patients.add(patientDto1);
        patients.add(patientDto2);
        Pageable pageable = PageRequest.of(0, 10);
        when(patientService.getPatients(pageable)).thenReturn(patients);

        mockMvc.perform(MockMvcRequestBuilders.get("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0") // Simulate pagination parameters
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("asd!@#"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].email").value("11@#"));
    }

    @Test
    void getPatients_PatientsNotExists_PatientsListReturned() throws Exception {
        List<PatientDto> patients = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 10);

        when(patientService.getPatients(pageable)).thenReturn(patients);

        mockMvc.perform(MockMvcRequestBuilders.get("/patients").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patients)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getPatient_PatientExists_PatientReturned() throws Exception {
        String email = "test@test.pl";
        PatientDto patientDto = new PatientDto();
        patientDto.setEmail(email);
        patientDto.setId(1L);

        when(patientService.getPatient(email)).thenReturn(patientDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/patients/{email}", email).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@test.pl"));
    }

    @Test
    void getPatient_PatientNotExists_PatientNotReturned() throws Exception {
        String email = "testo";

        when(patientService.getPatient(email)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient with given email does not exist."));

        mockMvc.perform(MockMvcRequestBuilders.get("/patients/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patient with given email does not exist."));
    }

    @Test
    void addPatient_DataCorrect_PatientSaved() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setEmail("test");
        PatientDto patientDto = new PatientDto();
        patientDto.setId(1L);
        patientDto.setEmail("test");

        when(patientService.addPatient(patient)).thenReturn(patientDto);

        mockMvc.perform(post("/patients").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patientDto))).
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test"));
    }

    @Test
    void addPatientWithUser_DataCorrect_PatientWithUserSaved() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("mateusz");
        PatientDto patientDto = new PatientDto();
        patientDto.setId(1L);
        patientDto.setEmail("test");
        patientDto.setUser(userDto);

        when(patientService.addPatientWithUser(patientDto))
                .thenReturn(patientDto);

        mockMvc.perform(post("/patients/with-user").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patientDto))).
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test"));
    }

    @Test
    void addPatient_EmailIsNotAvailable_PatientNotSaved() throws Exception {
String email = "existing@example.com";
Patient patient = new Patient();
patient.setEmail(email);

        when(patientRepository.existsByEmail(email)).thenReturn(true);
        doThrow(new ResponseStatusException (HttpStatus.BAD_REQUEST,  "Patient with given email already exists."))
                .when(patientService).addPatient(any(Patient.class));

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addPatientWithUser_EmailIsNotAvailable_PatientNotSaved() throws Exception {
        String email = "existing@example.com";
        Patient patient = new Patient();
        patient.setEmail(email);
        User user = new User();
        user.setId(1L);
        patient.setUser(user);

        when(patientRepository.existsByEmail(email)).thenReturn(true);
        doThrow(new ResponseStatusException (HttpStatus.BAD_REQUEST,  "Patient with given email already exists."))
                .when(patientService).addPatient(any(Patient.class));

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePatient_PatientExists_PatientDeleted() throws Exception {
        String email = "test";
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setEmail(email);

        doNothing().when(patientService).deletePatient(email);

        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/{email}", email).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patient))).
                andDo(print())
                .andExpect(status().isNoContent());

        verify(patientService).deletePatient(email);
    }

    @Test
    void deletePatient_PatientNotExists_PatientNotDeleted() throws Exception {
        String email = "test";

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient with given email does not exist"))
                .when(patientService).deletePatient(email);

        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Patient with given email does not exist"));
    }

    @Test
    void editPatient_PatientExists_PatientEdited() throws Exception {
        String email = "email";
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setEmail("Mateusz");
        patient.setEmail("email");

        PatientDto patientDto = new PatientDto();
        patient.setId(1L);
        patient.setEmail("Mateusz");
        patient.setEmail("nowy");

        when(patientService.editPatient(email, patient)).thenReturn(patientDto);
        when(patientService.editPatient(email, patient)).thenReturn(patientDtoMapper.dto(patient));

        mockMvc.perform(MockMvcRequestBuilders.put("/patients/{email}", email).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patient))).
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("nowy"));
    }

    @Test
    void editPatient_PatientNotExists_PatientEdited() throws Exception {
        String email = "test";
        Patient patient = new Patient();
        patient.setId(1L);

        when(patientService.editPatient(email, patient))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/patients/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patient not found"));
    }
    }