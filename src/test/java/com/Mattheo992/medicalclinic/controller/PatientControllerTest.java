package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.model.dtos.PatientDto;
import com.Mattheo992.medicalclinic.model.dtos.UserDto;
import com.Mattheo992.medicalclinic.model.mappers.PatientDtoMapper;
import com.Mattheo992.medicalclinic.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {
    @MockBean
    PatientService patientService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PatientDtoMapper patientDtoMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getPatients_PatientsExists_PatientsListReturned() throws Exception {
        PatientDto patientDto1 = new PatientDto();
        patientDto1.setId(1L);
        patientDto1.setEmail("asd!@#");
        PatientDto patientDto2 = new PatientDto();
        patientDto1.setId(2L);
        patientDto1.setEmail("11@#");
        List<PatientDto> patients = new ArrayList<>();
        patients.add(patientDto1);
        patients.add(patientDto2);
        Pageable pageable = PageRequest.of(0, 10);
        when(patientService.getPatients(pageable)).thenReturn(patients);

        mockMvc.perform(MockMvcRequestBuilders.get("/patients").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patients)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("asd!@#"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].email").value("11@#"));
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
    void addPatient_DataCorrect_PatientSaved() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setEmail("test");
       PatientDto patientDto = new PatientDto();
       patientDto.setId(1L);
       patientDto.setEmail("test");
    when(patientService.addPatient(patient)).thenReturn(patientDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/patients").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patientDto))).
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test"));
    }

    @Test
    void addPatientWithUser_DataCorrect_PatientWithUserSaved() throws Exception{
        UserDto userDto = new UserDto();
        userDto.setUsername("mateusz");
        PatientDto patientDto = new PatientDto();
        patientDto.setId(1L);
        patientDto.setEmail("test");
        patientDto.setUser(userDto);
        when(patientService.addPatientWithUser(patientDto))
                .thenReturn(patientDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/patients/with-user").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patientDto))).
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test"));
    }

    @Test
    void deletePatient_PatientExists_PatientDeleted() throws Exception{
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
    void editPatient_PatientExists_PatientEdited() throws Exception{
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
}