package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.Visit;
import com.Mattheo992.medicalclinic.model.dtos.VisitDto;
import com.Mattheo992.medicalclinic.model.mappers.VisitMapper;
import com.Mattheo992.medicalclinic.repository.DoctorRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VisitServiceTest {
    VisitService visitService;
    VisitRepository visitRepository;
    PatientRepository patientRepository;
    VisitMapper visitMapper;
    DoctorRepository doctorRepository;

    @BeforeEach
    void setup() {
        this.visitRepository = Mockito.mock(VisitRepository.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.visitMapper = Mappers.getMapper(VisitMapper.class);
        this.visitService = new VisitService(visitRepository, patientRepository, visitMapper, doctorRepository);
    }

    @Test
    void getVisitsByPatientId_PatientExist_ReturnedVisitsList() {
        // Arrange
        Long patientId = 1L;
        Patient patient = new Patient();
        patient.setId(patientId);

        Visit visit1 = new Visit();
        visit1.setId(1L);
        visit1.setStartDate(LocalDateTime.of(2025, 7, 1, 10, 0));
        visit1.setEndDate(LocalDateTime.of(2025, 7, 1, 11, 0));
        visit1.setPatient(patient);

        Visit visit2 = new Visit();
        visit2.setId(2L);
        visit2.setStartDate(LocalDateTime.of(2025, 7, 2, 14, 0));
        visit2.setEndDate(LocalDateTime.of(2025, 7, 2, 15, 0));
        visit2.setPatient(patient);

        List<Visit> visits = new ArrayList<>();
        visits.add(visit1);
        visits.add(visit2);

        VisitDto visitDto1 = new VisitDto(visit1.getStartDate(), visit1.getEndDate());
        VisitDto visitDto2 = new VisitDto(visit2.getStartDate(), visit2.getEndDate());
        List<VisitDto> expectedVisitDtoList = List.of(visitDto1, visitDto2);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(visitRepository.findByPatientId(patientId)).thenReturn(visits);

        List<VisitDto> result = visitService.getVisitsByPatientId(patientId);

        assertEquals(expectedVisitDtoList.size(), result.size());
        assertEquals(expectedVisitDtoList.get(0).getStartDate(), result.get(0).getStartDate());
        assertEquals(expectedVisitDtoList.get(1).getStartDate(), result.get(1).getStartDate());
    }

    @Test
    void getVisitsByPatientId_PatientNotExists_ReturnedException() {
        //given
        Long patientId = 1L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> visitService.getVisitsByPatientId(patientId));
        //then
        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void registerPatientForVisit_VisitExist_PatientRegisteredForVisit() {
        // given
        Long visitId = 1L;
        Long patientId = 1L;
        LocalDateTime futureVisitDate = LocalDateTime.of(2025, 1, 1, 10, 0);
        Visit visit = new Visit();
        visit.setStartDate(futureVisitDate);
        Patient patient = new Patient();
        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        // when
        visitService.registerPatientForVisit(visitId, patientId);
        // then
        assertEquals(patient, visit.getPatient());
        verify(visitRepository, times(1)).save(visit);
    }

    @Test
    void registerPatientForVisit_VisitNotExists_ReturnedException() {
        //given
        Long visitId = 1L;
        Long patientId = 1L;
        when(visitRepository.findById(visitId)).thenReturn(Optional.empty());
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> visitService.registerPatientForVisit(visitId, patientId));
        //then
        assertEquals("Visit with given id does not exist.", exception.getMessage());
    }

    @Test
    void createVisit_DateIsAvailable_VisitCreated() {
        // given
        LocalDateTime startVisit = LocalDateTime.of(2024, 10, 17, 9, 0, 0);
        LocalDateTime endVisit = LocalDateTime.of(2024, 10, 17, 9, 15, 0);
        VisitDto visitDto = new VisitDto();
        visitDto.setStartDate(startVisit);
        visitDto.setEndDate(endVisit);
        Visit savedVisit = new Visit();
        savedVisit.setId(1L);
        savedVisit.setStartDate(startVisit);
        savedVisit.setEndDate(endVisit);
        when(visitRepository.save(any())).thenReturn(savedVisit);
        // when
        Visit createdVisit = visitService.createVisit(visitDto);
        // then
        assertNotNull(createdVisit);
        assertEquals(startVisit, createdVisit.getStartDate());
        assertEquals(endVisit, createdVisit.getEndDate());
    }

    @Test
    void createVisit_DateIsNotAvailable_VisitedNotCreated() {
        //given
        LocalDateTime startDate = LocalDateTime.of(2024, 10, 17, 9, 00, 00);
        Visit visit = createVisit(1L, startDate, LocalDateTime.of(2024, 10, 17, 9, 15, 00));
        VisitDto visitDto = visitMapper.visitDto(visit);
        when(visitRepository.existsByStartDate(startDate)).thenReturn(true);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> visitService.createVisit(visitDto));
        //then
        assertEquals("Visit with given date is already exist", exception.getMessage());
    }

    Visit createVisit(Long id, LocalDateTime startTime, LocalDateTime endTime) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setEmail("m@m.pl");
        Doctor doctor = new Doctor();
        return new Visit(id, startTime, endTime, patient, doctor);
    }
}