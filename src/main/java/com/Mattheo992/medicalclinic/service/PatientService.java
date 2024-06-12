package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.*;
import com.Mattheo992.medicalclinic.model.dtos.PatientDto;
import com.Mattheo992.medicalclinic.model.mappers.PatientDtoMapper;
import com.Mattheo992.medicalclinic.model.mappers.UserMapper;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import com.Mattheo992.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientDtoMapper patientDtoMapper;
    private final UserMapper userMapper;

    public List<PatientDto> getPatients(Pageable pageable) {
        List<Patient> patients = patientRepository.findAll(pageable).getContent();
        return patientDtoMapper.toDtos(patients);
    }

    public PatientDto getPatient(String email) {
        return patientRepository.findByEmail(email)
                .map(patientDtoMapper::dto)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));
    }

    public PatientDto addPatient(Patient patient) {
        checkIfEmailIsAvailable(patient.getEmail());
        Patient savedPatient = patientRepository.save(patient);
        return patientDtoMapper.dto(savedPatient);
    }

    @Transactional
    public PatientDto addPatientWithUser(PatientDto patientDto) {
        checkIfEmailIsAvailable(patientDto.getEmail());
        User user = userMapper.userDtoToUser(patientDto.getUser());
        userRepository.save(user);
        Patient patient = patientDtoMapper.dto(patientDto);
        patient.setUser(user);
        Patient savedPatient = patientRepository.save(patient);
        return patientDtoMapper.dto(savedPatient);
    }

    @Transactional
    public void deletePatient(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email does not exist."));
        User user = patient.getUser();
        if (user != null) {
            userRepository.delete(user);
        }
        patientRepository.delete(patient);
    }

    @Transactional
    public PatientDto editPatient(String email, Patient uptadetPatient) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        patient.setEmail(uptadetPatient.getEmail());
        patient.setFirstName(uptadetPatient.getFirstName());
        patient.setLastName(uptadetPatient.getLastName());
        patient.setBirthday(uptadetPatient.getBirthday());
        patient.setPhoneNumber(uptadetPatient.getPhoneNumber());
        patient.setIdCardNo(uptadetPatient.getIdCardNo());
        return patientDtoMapper.dto(patientRepository.save(patient));
    }

    private void checkIfEmailIsAvailable(String email) {
        if (patientRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Patient with given email already exists.");
        }
    }
}