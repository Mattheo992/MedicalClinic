package com.Mattheo992.medicalclinic.Service;

import com.Mattheo992.medicalclinic.Model.Patient;
import com.Mattheo992.medicalclinic.Repository.PatientRepository;
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
        return patientRepository.getPatient(email).orElse(null);
    }

    public void addPatient(Patient patient) {
        patientRepository.createPatient(patient);
    }

    public void deletePatient(String email) {
        patientRepository.deletePatient(email);
    }

    public void editPatient(String email, Patient newPatient) {
        patientRepository.editPatientByEmail(email, newPatient);
    }
}

