package com.Mattheo992.medicalclinic.repository;

import com.Mattheo992.medicalclinic.model.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepository {
    private List<Patient> patients = new ArrayList<>();

    public List<Patient> getPatients() {
        return new ArrayList<>(patients);
    }

    public Optional<Patient> getPatient(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public Patient createPatient(Patient patient) {
        patients.add(patient);
        return patient;
    }

    public void deletePatient(String email) {
        Patient patient = getPatient(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist!"));
        patients.remove(patient);
    }

    public Patient editPatientByEmail(String email, Patient newPatient) {
        Patient patient = getPatient(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist!"));

            patient.setId(newPatient.getId());
            patient.setEmail(newPatient.getEmail());
            patient.setPassword(newPatient.getPassword());
            patient.setIdCardNo(newPatient.getIdCardNo());
            patient.setFirstName(newPatient.getFirstName());
            patient.setLastName(newPatient.getLastName());
            patient.setPhoneNumber(newPatient.getPhoneNumber());
            patient.setBirthday(newPatient.getBirthday());
            return patient;
    }

    public Patient editPassword(String email, Patient newPatient) {
        Patient patient = getPatient(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist!"));

        patient.setPassword(newPatient.getPassword());
        return patient;
    }
}

