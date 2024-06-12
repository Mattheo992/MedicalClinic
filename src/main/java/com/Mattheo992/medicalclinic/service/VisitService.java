package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.Visit;
import com.Mattheo992.medicalclinic.model.dtos.VisitDto;
import com.Mattheo992.medicalclinic.model.mappers.VisitMapper;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import com.Mattheo992.medicalclinic.repository.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final VisitMapper visitMapper;

    public List<VisitDto> getVisitsByPatientId(Long patientId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isEmpty()) {
            throw new IllegalArgumentException("Patient not found");
        }
        List<Visit> visits = visitRepository.findByPatientId(patientId);
        return visitMapper.ListDto(visits);
    }

    @Transactional
    public Visit registerPatientForVisit(Long visitId, Long patientId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit with given id does not exist."));
        if (visit.getPatient() != null) {
            throw new IllegalArgumentException("Sorry, but visit is already occupied.");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given id does not exist."));
        visit.setPatient(patient);
        return visitRepository.save(visit);
    }

    @Transactional
    public Visit createVisit(VisitDto visitDto) {
        checkIsStartDateIsAvailable(visitDto.getStartDate());
        LocalDateTime startDate = visitDto.getStartDate();
        LocalDateTime endDate = visitDto.getEndDate();
        LocalDateTime now = LocalDateTime.now();
        if (startDate.isBefore(now)) {
            throw new IllegalArgumentException("Start date must be in the future.");
        }
        if (startDate.getMinute() % 15 != 0 || endDate.getMinute() % 15 != 0) {
            throw new IllegalArgumentException("Minutes must be multiples of 15.");
        }
        Visit visit = new Visit();
        visit.setStartDate(startDate);
        visit.setEndDate(endDate);
        return visitRepository.save(visit);
    }

    private void checkIsStartDateIsAvailable(LocalDateTime startDate) {
        if (visitRepository.existsByStartDate(startDate)) {
            throw new IllegalArgumentException("Visit with given date is already exist");
        }
    }
}
