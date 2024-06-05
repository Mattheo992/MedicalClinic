package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.DoctorDto;
import com.Mattheo992.medicalclinic.model.DoctorMapper;
import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Page<DoctorDto> getDoctors(Pageable pageable) {
        Page<Doctor> doctorsPage = doctorRepository.findAll(pageable);
        return doctorsPage.map(doctorMapper::toDto);
    }

    public Set<Institution> getInstitutionsForDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        return doctor.getInstitutions();
    }
}
