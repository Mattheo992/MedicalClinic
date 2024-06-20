package com.Mattheo992.medicalclinic.controller;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void addInstitution_DataCorrect_InstitutionSaved() throws Exception{
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
    void getInstitutions_InstitutionsExists_ReturnedInstitutionsList() throws Exception{
        InstitutionDto institutionDto1 = new InstitutionDto();
        institutionDto1.setInstitutionName("Barlik");
        InstitutionDto institutionDto2 = new InstitutionDto();
        institutionDto2.setInstitutionName("CKD");
        List<InstitutionDto> institutionsDtos = new ArrayList<>();
        institutionsDtos.add(institutionDto1);
        institutionsDtos.add(institutionDto2);
        Pageable pageable = PageRequest.of(0, 10);

        when(institutionService.getInstitutions(pageable)).thenReturn(institutionsDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/institutions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(institutionsDtos))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Barlik"))
                .andExpect(jsonPath("$[1].name").value("CKD"));
    }

    @Test
    void addDoctorToInstitution_DoctorExistsInstitutionExists_ReturnedInstitutions() throws Exception{
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Adam");
        doctor.setSurname("Dobry");
        doctor.setEmail("11@22.pl");
        Institution institution1= new Institution();
        institution1.setId(1L);
        institution1.setInstitutionName("Barlik");
        doNothing().when(institutionService).addDoctorToInstitution(1L, 1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/institutions/{institutionId}/doctors/{doctorId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(institutionService).addDoctorToInstitution(1L, 1L);
    }
}
