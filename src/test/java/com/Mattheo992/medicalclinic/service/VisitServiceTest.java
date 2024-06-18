package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.Visit;
import com.Mattheo992.medicalclinic.model.dtos.VisitDto;
import com.Mattheo992.medicalclinic.model.mappers.VisitMapper;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import com.Mattheo992.medicalclinic.repository.VisitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class VisitServiceTest {
    VisitService visitService;
    VisitRepository visitRepository;
    PatientRepository patientRepository;
    VisitMapper visitMapper;

    @BeforeEach
    void setup() {
        this.visitRepository = Mockito.mock(VisitRepository.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.visitMapper = Mappers.getMapper(VisitMapper.class);
        this.visitService = new VisitService(visitRepository, patientRepository, visitMapper);
    }

    @Test
    void getVisitsByPatientId_PatientExist_ReturnedVisitsList() {
        //given
        Long id = 1L;
        Patient patient = new Patient();
        patient.setId(id);
        patient.setEmail("test@test.pl");
        List<Visit> visits = new ArrayList<>();
        List<VisitDto> dtoVisits = visitMapper.ListDto(visits);
        visits.add(createVisit(1L, LocalDateTime.of(2024, 06, 17, 9, 00, 00), LocalDateTime.of(2024, 06, 17, 9, 15, 00)));
        visits.add(createVisit(2L, LocalDateTime.of(2024, 06, 18, 9, 00, 00), LocalDateTime.of(2024, 06, 18, 9, 15, 00)));
        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(visitRepository.findByPatientId(id)).thenReturn(visits);
        when(visitMapper.ListDto(visits)).thenReturn(dtoVisits);
        //when
        List<VisitDto> result = visitService.getVisitsByPatientId(id);
//then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(LocalDateTime.of(2024, 06, 17, 9, 00, 00), result.get(0).getStartDate());
        Assertions.assertEquals(LocalDateTime.of(2024, 06, 17, 9, 15, 00), result.get(0).getEndDate());
        Assertions.assertEquals(LocalDateTime.of(2024, 06, 18, 9, 00, 00), result.get(1).getStartDate());
        Assertions.assertEquals(LocalDateTime.of(2024, 06, 18, 9, 15, 00), result.get(1).getEndDate());
    }

    @Test
    void getVisitsByPatientId_PatientNotExists_ReturnedException() {
        //given
        Long patientId = 1L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());
        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> visitService.getVisitsByPatientId(patientId));
        //then
        Assertions.assertEquals("Patient not foud", exception.getMessage());
    }

    @Test
    void registerPatientForVisit_VisitExist_PatientRegisteredForVisit() {
        //given
        Long patientId = 1L;
        Long visitId = 1L;
        Visit visit = createVisit(visitId, LocalDateTime.of(2024, 06, 17, 9, 00, 00), LocalDateTime.of(2024, 06, 17, 9, 15, 00));
        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setEmail("m@m.pl");
        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(visitRepository.save(visit));
        //when
        Visit result = visitService.registerPatientForVisit(visitId, patientId);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getPatient().getId());
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("m@m.pl", result.getPatient().getEmail());
    }

    @Test
    void registerPatientForVisit_VisitNotExists_ReturnedException() {
        //given
        Long visitId = 1L;
        Long patientId = 1L;
        when(visitRepository.findById(visitId)).thenReturn(Optional.empty());
        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> visitService.registerPatientForVisit(visitId, patientId));
        //then
        Assertions.assertEquals("isit with given id does not exist.", exception.getMessage());
    }

    @Test
    void createVisit_DateIsAvailable_VisitCreated() {
        //given
        Visit visit = createVisit(1L, LocalDateTime.of(2024, 06, 17, 9, 00, 00), LocalDateTime.of(2024, 06, 17, 9, 15, 00));
        VisitDto visitDto = visitMapper.visitDto(visit);
        when(visitRepository.save(visit)).thenReturn(visit);
        //when
        Visit result = visitService.createVisit(visitDto);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1l, result.getId());
        Assertions.assertEquals(LocalDateTime.of(2024, 06, 17, 9, 00, 00), result.getStartDate());
        Assertions.assertEquals(LocalDateTime.of(2024, 06, 17, 9, 15, 00), result.getEndDate());
    }

    @Test
    void createVisit_DateIsNotAvailable_VisitedNotCreated() {
        //given
        LocalDateTime startDate = LocalDateTime.of(2024, 06, 17, 9, 00, 00);
        Visit visit = createVisit(1L, startDate, LocalDateTime.of(2024, 06, 17, 9, 15, 00));
        VisitDto visitDto = visitMapper.visitDto(visit);
        when(visitRepository.existsByStartDate(startDate)).thenReturn(true);
        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> visitService.createVisit(visitDto));
        //then
        Assertions.assertEquals("Visit with given date is already exist", exception.getMessage());
    }

    Visit createVisit(Long id, LocalDateTime startTime, LocalDateTime endTime) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setEmail("m@m.pl");
        return new Visit(id, startTime, endTime, patient);
    }
}