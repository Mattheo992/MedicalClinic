package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.PatientDto;
import com.Mattheo992.medicalclinic.model.PatientWithUserDto;
import com.Mattheo992.medicalclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public List<PatientDto> getPatients() {
        return patientService.getPatients();
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
    public Patient addPatientWithUser(@RequestBody PatientWithUserDto patientWithUserDto) {
        return patientService.addPatientWithUser(patientWithUserDto.getPatient(), patientWithUserDto.getUser());
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable("email") String email) {
        patientService.deletePatient(email);
    }

    @PutMapping("/{email}")
    public Patient editPatient(@PathVariable("email") String email, @RequestBody Patient newPatient) {
        return patientService.editPatient(email, newPatient);
    }

    @PatchMapping("/{email}/password")
    public Patient editPassword(@PathVariable String email, @RequestBody String newPassword) {
        return patientService.editPassword(email, newPassword);
    }
}
