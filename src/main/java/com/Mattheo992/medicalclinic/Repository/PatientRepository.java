package com.Mattheo992.medicalclinic.Repository;

import com.Mattheo992.medicalclinic.Model.Patient;
import org.springframework.stereotype.Repository;

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
                .filter(patient -> email.equals(patient.getEmail()))
                .findFirst();
    }

    public Patient createPatient(Patient patient) {
        patients.add(patient);
        return patient;
    }

    public void deletePatient(String email) {
        patients.removeIf(patient -> patient.getEmail().equals(email));
    }

    public void editPatientByEmail(String email, Patient newPatient) {
        Optional<Patient> editedPatient = getPatient(email);
        if (editedPatient.isPresent()) {
            Patient patientToUpdate = editedPatient.get();
            patientToUpdate.setEmail(newPatient.getEmail());
            patientToUpdate.setPassword(newPatient.getPassword());
            patientToUpdate.setIdCardNo(newPatient.getIdCardNo());
            patientToUpdate.setFirstName(newPatient.getFirstName());
            patientToUpdate.setLastName(newPatient.getLastName());
            patientToUpdate.setPhoneNumber(newPatient.getPhoneNumber());
            patientToUpdate.setBirthday(newPatient.getBirthday());
        }
    }
}

