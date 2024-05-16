package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public List<Patient> GetPatients() {
        return patientRepository.getPatients();
    }

    public Patient getPatient(String email) {

        return patientRepository.getPatient(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));
    }

    public Patient addPatient(Patient patient) {
        checkIfEmailIsAvailable(patient.getEmail());
        patientRepository.createPatient(patient);
        return patient;
    }

    public void deletePatient(String email) {
        patientRepository.deletePatient(email);
    }

    public Patient editPatient(String email, Patient newPatient) {
       return patientRepository.editPatientByEmail(email, newPatient);
    }

    public Patient editPassword(String email, Patient newPatient) {
        return patientRepository.editPassword(email, newPatient);
    }

    private void checkIfEmailIsAvailable(String email) {
        if (patientRepository.getPatient(email).isPresent()) {
            throw new IllegalArgumentException("Patient with given email already exists");
        }
    }
}

