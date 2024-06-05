package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.DoctorDto;
import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<DoctorDto> getDoctors (@RequestParam int page,
                                       @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorService.getDoctors(pageable);
    }

    @GetMapping("/{doctorId}/institutions")
    public Set<Institution> getInstitutionsForDoctor(@PathVariable Long doctorId) {
        return doctorService.getInstitutionsForDoctor(doctorId);
    }
}