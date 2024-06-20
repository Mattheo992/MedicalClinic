package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.model.dtos.DoctorDto;
import com.Mattheo992.medicalclinic.model.mappers.DoctorMapper;
import com.Mattheo992.medicalclinic.service.DoctorService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DoctorService doctorService;


    @Test
    void addDoctor_DataCorrect_DoctorSaved() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Adam");
        doctor.setSurname("Dobry");
        doctor.setEmail("11@22.pl");

        when(doctorService.addDoctor(doctor)).thenReturn(doctor);

        mockMvc.perform(MockMvcRequestBuilders.post("/doctors").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Adam"))
                .andExpect(jsonPath("$.surname").value("Dobry"))
                .andExpect(jsonPath("$.email").value("11@22.pl"));
    }

    @Test
    void getDoctors_DoctorsExists_ReturnDoctorDtoList() throws Exception{
        DoctorDto doctor1 = new DoctorDto("Adam", "Dobry", "Cardiology", "adam.dobry@wp.com");
        DoctorDto doctor2 = new DoctorDto("Ewa", "Kowalska", "Neurology", "ewa.kowalska@onet.com");
        List<DoctorDto> doctors = new ArrayList<>();
        doctors.add(doctor1);
        doctors.add(doctor2);
        Pageable pageable = PageRequest.of(0, 10);
        when(doctorService.getDoctors(pageable)).thenReturn(doctors);

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(doctors))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Adam"))
                .andExpect(jsonPath("$[0].surname").value("Dobry"))
                .andExpect(jsonPath("$[0].specialization").value("Cardiology"))
                .andExpect(jsonPath("$[0].email").value("adam.dobry@wp.com"))
                .andExpect(jsonPath("$[1].name").value("Ewa"))
                .andExpect(jsonPath("$[1].surname").value("Kowalska"))
                .andExpect(jsonPath("$[1].specialization").value("Neurology"))
                .andExpect(jsonPath("$[1].email").value("ewa.kowalska@onet.com"));
    }

    @Test
    void getInstitutionsForDoctor_InstitutionsAndDoctorExists_InstitutionsReturned() throws Exception {
        Institution institution1 = new Institution();
        institution1.setId(1L);
        institution1.setInstitutionName("Barlik");
        Institution institution2 = new Institution();
        institution2.setId(2L);
        institution2.setInstitutionName("CKD");

        Set<Institution> institutions = new HashSet<>(Arrays.asList(institution1, institution2));

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Adam");
        doctor.setSurname("Dobry");
        doctor.setEmail("11@22.pl");
        doctor.setInstitutions(institutions);

        when(doctorService.getInstitutionsForDoctor(1L)).thenReturn(institutions);
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/1/institutions")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(institutions))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Barlik"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("CKD"));
    }
}