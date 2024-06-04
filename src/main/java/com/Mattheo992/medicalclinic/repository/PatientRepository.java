package com.Mattheo992.medicalclinic.repository;

import com.Mattheo992.medicalclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);

    boolean existsByEmail(String email);

    default Patient editPatientByEmail(String email, Patient newPatient) {
        Patient patient = findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));

        patient.setEmail(newPatient.getEmail());
        patient.setIdCardNo(newPatient.getIdCardNo());
        patient.setFirstName(newPatient.getFirstName());
        patient.setLastName(newPatient.getLastName());
        patient.setPhoneNumber(newPatient.getPhoneNumber());
        patient.setBirthday(newPatient.getBirthday());

        return save(patient);
    }
}
