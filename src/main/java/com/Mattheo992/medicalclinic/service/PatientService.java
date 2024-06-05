package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.*;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import com.Mattheo992.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientDtoMapper patientDtoMapper;
    private final UserMapper userMapper;

    public Page<PatientDto> getPatients (Pageable pageable) {
        Page<Patient> patientPage = patientRepository.findAll(pageable);
        return patientPage.map(patientDtoMapper::dto);
    }

    public PatientDto getPatient(String email) {
        return patientRepository.findByEmail(email)
                .map(patientDtoMapper::dto)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));
    }

    public Patient addPatient(Patient patient) {
        checkIfEmailIsAvailable(patient.getEmail());
        return patientRepository.save(patient);
    }
    public PatientDto addPatientWithUser(PatientDto patientDto) {
        checkIfEmailIsAvailable(patientDto.getEmail());
        User user = userMapper.userDtoToUser(patientDto.getUser());
        userRepository.save(user);
        Patient patient = patientDtoMapper.dto(patientDto);
        patient.setUser(user);
        Patient savedPatient = patientRepository.save(patient);
        return patientDtoMapper.dto(savedPatient);
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

    public PatientDto editPatient(String email, PatientDto patientDto) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException
                ("Patient with given email does not exist"));
        Patient updatedPatient = patientDtoMapper.dto(patientDto);
        updatedPatient.setId(patient.getId());
        return patientDtoMapper.dto(patientRepository.save(updatedPatient));
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