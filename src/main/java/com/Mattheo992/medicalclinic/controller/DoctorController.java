package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.dtos.DoctorDto;
import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    @GetMapping
    public List<DoctorDto> getDoctors(Pageable pageable) {
        return doctorService.getDoctors(pageable);
    }

    @GetMapping("/{doctorId}/institutions")
    public Set<Institution> getInstitutionsForDoctor(@PathVariable Long doctorId) {
        return doctorService.getInstitutionsForDoctor(doctorId);
    }
}