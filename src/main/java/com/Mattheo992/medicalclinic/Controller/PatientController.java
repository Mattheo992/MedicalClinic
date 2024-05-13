package com.Mattheo992.medicalclinic.Controller;
import com.Mattheo992.medicalclinic.Model.Patient;
import com.Mattheo992.medicalclinic.Service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public List<Patient> getPatients() {
        return patientService.GetPatients();
    }

    @GetMapping("/{email}")
    public Patient getPatient(@PathVariable String email) {
        return patientService.getPatient(email);
    }

    @PostMapping
    public void addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
    }

    @DeleteMapping("/{email}")
    public void deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
    }

    @PutMapping("/{email}")
    public void editPatient(@PathVariable String email, @RequestBody Patient newPatient) {
        patientService.editPatient(email, newPatient);
    }
}
