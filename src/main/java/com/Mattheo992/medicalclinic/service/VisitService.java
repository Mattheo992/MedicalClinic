package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.Visit;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import com.Mattheo992.medicalclinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;

    public List<Visit> getVisitsByPatientId(Long patientId) {
        return visitRepository.findByPatientId(patientId);
    }

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

    public Visit createVisit(LocalDateTime startDate, LocalDateTime endDate) {
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
}