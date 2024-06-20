package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.service.PatientService;
import com.Mattheo992.medicalclinic.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {
    @MockBean
    VisitService visitService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;
}
