package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.PatientDto;
import com.Mattheo992.medicalclinic.model.PatientToPatientDtoMapper;
import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import com.Mattheo992.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientToPatientDtoMapper patientToPatientDtoMapper;
    private final UserRepository userRepository;

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
    public Patient addPatientWithUser(Patient patient, User user) {
        checkIfEmailIsAvailable(patient.getEmail());
        userRepository.save(user);
        patient.setUser(user);
        return patientRepository.save(patient);
    }

    public void deletePatient(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));
        User user = patient.getUser();
        if (user != null) {
            userRepository.delete(user);
        }
        patientRepository.delete(patient);
    }

    public Patient editPatient(String email, Patient newPatient) {
        return patientRepository.editPatientByEmail(email, newPatient);
    }
    public Patient editPassword(String email, String newPassword) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));
        User user = patient.getUser();
        user.setPassword(newPassword);
        userRepository.save(user);
        return patient;
    }

    private void checkIfEmailIsAvailable(String email) {
        if (patientRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Patient with given email already exists.");
        }
    }
}
