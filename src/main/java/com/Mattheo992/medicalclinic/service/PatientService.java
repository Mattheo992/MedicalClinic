package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.PatientDto;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientJpaRepository;

    public PatientService(PatientRepository patientJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
    }

    public List<PatientDto> getPatients() {
        return patientJpaRepository.findAll().stream()
                .map(patient -> new PatientDto(patient.getId(), patient.getEmail(), patient.getFirstName(), patient.getLastName(), patient.getBirthday()))
                .collect(Collectors.toList());
    }

    public PatientDto getPatient(String email) {
        Patient patient = patientJpaRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));
        return new PatientDto(patient.getId(), patient.getEmail(), patient.getFirstName(), patient.getLastName(), patient.getBirthday());
    }

    public Patient addPatient(Patient patient) {
        return patientJpaRepository.save(patient);
    }

    public void deletePatient(String email) {
        Patient patient = patientJpaRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));
        patientJpaRepository.delete(patient);
    }

    public Patient editPatient(String email, Patient newPatient) {
        Patient patient = patientJpaRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));

        patient.setEmail(newPatient.getEmail());
        patient.setPassword(newPatient.getPassword());
        patient.setIdCardNo(newPatient.getIdCardNo());
        patient.setFirstName(newPatient.getFirstName());
        patient.setLastName(newPatient.getLastName());
        patient.setPhoneNumber(newPatient.getPhoneNumber());
        patient.setBirthday(newPatient.getBirthday());

        return patientJpaRepository.save(patient);
    }

    public Patient editPassword(String email, Patient newPatient) {
        Patient patient = patientJpaRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));

        patient.setPassword(newPatient.getPassword());

        return patientJpaRepository.save(patient);
    }
}

