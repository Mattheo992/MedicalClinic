package com.Mattheo992.medicalclinic.controller;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.model.dtos.DoctorDto;
import com.Mattheo992.medicalclinic.model.dtos.InstitutionDto;
import com.Mattheo992.medicalclinic.model.mappers.DoctorMapper;
import com.Mattheo992.medicalclinic.service.InstitutionService;
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

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InstitutionControllerTest {
    @MockBean
    InstitutionService institutionService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addInstitution_DataCorrect_InstitutionSaved() throws Exception {
        InstitutionDto institutionDto = new InstitutionDto();
        institutionDto.setInstitutionName("Barlik");
        institutionDto.setCity("Lodz");

        when(institutionService.addInstitution(institutionDto)).thenReturn(institutionDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/institutions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(institutionDto))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institutionName").value("Barlik"))
                .andExpect(jsonPath("$.city").value("Lodz"));
    }

    @Test
    void addInstitution_InstitutionNameIsNotAvailable_InstitutionNotSaved() throws Exception {
        InstitutionDto institutionDto = new InstitutionDto();
        institutionDto.setInstitutionName("Test");

        when(institutionService.addInstitution(institutionDto)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Institution with given name already exists!"));

        mockMvc.perform(MockMvcRequestBuilders.post("/institutions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(institutionDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Institution with given name already exists!"));
    }

    @Test
    void getInstitutions_InstitutionsExists_ReturnedInstitutionsList() throws Exception {
         InstitutionDto institutionDto1 = new InstitutionDto("Barlik", "CityA", "12345", "StreetA", 1L, null);
         InstitutionDto institutionDto2 = new InstitutionDto("CKD", "CityB", "67890", "StreetB", 2L, null);
                List<InstitutionDto> institutionDtos = new ArrayList<>();
                institutionDtos.add(institutionDto1);
                institutionDtos.add(institutionDto2);

        Pageable pageable = PageRequest.of(0, 10);
        when(institutionService.getInstitutions(pageable)).thenReturn(institutionDtos);

        mockMvc.perform(get("/institutions").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].institutionName").value("Barlik"))
                .andExpect(jsonPath("$[1].institutionName").value("CKD"));
    }


    @Test
    void getInstitutions_InstitutionNotExists_ReturnedEmptyList() throws Exception {
        List<InstitutionDto> institutionDtos = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10);

        when(institutionService.getInstitutions(pageable)).thenReturn(institutionDtos);

        mockMvc.perform(get("/institutions").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void addDoctorToInstitution_DoctorExistsInstitutionExists_ReturnedInstitutions() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Adam");
        doctor.setSurname("Dobry");
        doctor.setEmail("11@22.pl");
        Institution institution1 = new Institution();
        institution1.setId(1L);
        institution1.setInstitutionName("Barlik");

        doNothing().when(institutionService).addDoctorToInstitution(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/institutions/{institutionId}/doctors/{doctorId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(institutionService).addDoctorToInstitution(1L, 1L);
    }

    @Test
    void addDoctorToInstitution_DoctorNotFound_ReturnsNotFound() throws Exception {
        Long institutionId = 1L;
        Long doctorId = 100L;

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found")).when(institutionService).addDoctorToInstitution(institutionId, doctorId);

        mockMvc.perform(MockMvcRequestBuilders.post("/institutions/{institutionId}/doctors/{doctorId}", institutionId, doctorId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Doctor not found"));

        verify(institutionService, times(1)).addDoctorToInstitution(institutionId, doctorId);
    }
}