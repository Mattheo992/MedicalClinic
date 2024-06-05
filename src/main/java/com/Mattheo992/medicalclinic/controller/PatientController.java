package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.PatientDto;
import com.Mattheo992.medicalclinic.model.UserDto;
import com.Mattheo992.medicalclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public Page<PatientDto> getPatients(@RequestParam int page,
                                        @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return patientService.getPatients(pageable);
    }

    @GetMapping("/{email}")
    public PatientDto getPatient(@PathVariable String email) {
        return patientService.getPatient(email);
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @PostMapping("/with-user")
    public PatientDto addPatientWithUser(@RequestBody PatientDto patientDto) {
        return patientService.addPatientWithUser(patientDto);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable("email") String email) {
        patientService.deletePatient(email);
    }

    @PutMapping("/{email}")
    public PatientDto editPatient(@PathVariable("email") String email, @RequestBody PatientDto patientDto) {
        return patientService.editPatient(email, patientDto);
    }

    @PatchMapping("/{email}/password")
    public Patient editPassword(@PathVariable String email, @RequestBody String newPassword) {
        return patientService.editPassword(email, newPassword);
    }
}
