package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.PatientDto;
import com.Mattheo992.medicalclinic.model.PatientToPatientDtoMapper;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientToPatientDtoMapper patientToPatientDtoMapper;

    public PatientService(PatientRepository patientRepository, PatientToPatientDtoMapper patientToPatientDtoMapper) {
        this.patientRepository = patientRepository;
        this.patientToPatientDtoMapper = patientToPatientDtoMapper;
    }

    public List<PatientDto> getPatients() {
        return patientRepository.findAll().stream()
                .map(patientToPatientDtoMapper::sourceToDestination)
                .collect(Collectors.toList());
    }

    public PatientDto getPatient(String email) {
        return patientRepository.findByEmail(email)
                .map(patientToPatientDtoMapper::sourceToDestination)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));
    }

    public Patient addPatient(Patient patient) {
        checkIfEmailIsAvailable(patient.getEmail());
        return patientRepository.save(patient);
    }

    public void deletePatient(String email) {
        if (!patientRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Patient with given email does not exist.");
        }
        patientRepository.deleteByEmail(email);
    }

    public Patient editPatient(String email, Patient newPatient) {
        return patientRepository.editPatientByEmail(email, newPatient);
    }

    public Patient editPassword(String email, Patient newPatient) {
        return patientRepository.editPassword(email, newPatient);
    }

    private void checkIfEmailIsAvailable(String email) {
        if (patientRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Patient with given email already exists.");
        }
    }
}
