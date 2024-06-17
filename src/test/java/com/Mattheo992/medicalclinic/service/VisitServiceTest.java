package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.Visit;
import com.Mattheo992.medicalclinic.model.mappers.VisitMapper;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import com.Mattheo992.medicalclinic.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VisitServiceTest {
    VisitService visitService;
    VisitRepository visitRepository;
    PatientRepository patientRepository;
    VisitMapper visitMapper;

    @BeforeEach
    void setup(){
      this.visitRepository = Mockito.mock(VisitRepository.class);
      this.patientRepository = Mockito.mock(PatientRepository.class);
      this.visitMapper = Mappers.getMapper(VisitMapper.class);
      this.visitService = new VisitService(visitRepository, patientRepository, visitMapper);
    }

    Visit createVisit(Long id, LocalDateTime startTime, LocalDateTime endTime){
        Patient patient = new Patient();
        patient.setId(id);
        patient.setEmail("m@m.pl");
        return new Visit(id, startTime, endTime, patient);
    }

}
