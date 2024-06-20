package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.Visit;
import com.Mattheo992.medicalclinic.model.dtos.VisitDto;
import com.Mattheo992.medicalclinic.model.mappers.VisitMapper;
import com.Mattheo992.medicalclinic.service.PatientService;
import com.Mattheo992.medicalclinic.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {
    @MockBean
    VisitService visitService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    VisitMapper visitMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void createVisit_DataCorrect_VisitSaved() throws Exception{
       Visit visit = new Visit();
        LocalDateTime starDate = LocalDateTime.now().plusHours(1);
        LocalDateTime endDate = starDate.plusMinutes(15);
        visit.setStartDate(starDate);
        visit.setEndDate(endDate);

        when(visitService.createVisit(visitMapper.visitDto(visit))).thenReturn(visit);

        mockMvc.perform(MockMvcRequestBuilders.post("/visits")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(visit)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void registerPatient_VisitAndPatientExists_PatientRegistered() throws Exception{
        Long visitId = 1L;
        Long patientId = 1L;

        LocalDateTime starDate = LocalDateTime.now().plusHours(1);
        LocalDateTime endDate = starDate.plusMinutes(15);
        Visit visit = new Visit();
        visit.setId(visitId);
        visit.setStartDate(starDate);
        visit.setEndDate(endDate);
        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setEmail("test");

        when(visitService.registerPatientForVisit(visitId, patientId)).thenReturn(visit);

        mockMvc.perform(MockMvcRequestBuilders.post("/visits/{visitId}/patients/{patientId}", visitId, patientId).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(visit)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void GetVisitsForPatient_VisitsAndPatientExists_VisitsListReturned() throws Exception{
        Long visitId = 1L;
        Long patientId = 1L;
        LocalDateTime starDate = LocalDateTime.now().plusHours(1);
        LocalDateTime endDate = starDate.plusMinutes(15);
        Visit visit = new Visit();
        visit.setId(visitId);
        visit.setStartDate(starDate);
        visit.setEndDate(endDate);
        Visit visit1 = new Visit();
        visit1.setId(2L);
        visit1.setStartDate(LocalDateTime.now().plusHours(2));
        visit1.setEndDate(LocalDateTime.now().plusHours(2).plusMinutes(15));
        List<Visit> visits = new ArrayList<>();
        visits.add(visit);
        visits.add(visit1);
        List<VisitDto> visitDtos = visitMapper.ListDto(visits);
        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setEmail("test");
        patient.setVisits(visits);

        when(visitService.getVisitsByPatientId(patientId)).thenReturn(visitDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/visits/patient/{patientId}", patientId).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(visitDtos)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


}
