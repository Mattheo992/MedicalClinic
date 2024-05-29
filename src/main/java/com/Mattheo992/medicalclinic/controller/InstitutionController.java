package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions")
public class InstitutionController {
    private final InstitutionService institutionService;

    @PostMapping
    public Institution addInstitution(@RequestBody Institution institution) {
        return institutionService.addInstitution(institution);
    }

    @GetMapping
    public List<Institution> getInstitutions() {
        return institutionService.getInstitutions();
    }

    @PostMapping("/{institutionId}/doctors/{doctorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addDoctorToInstitution(
            @PathVariable("institutionId") Long institutionId,
            @PathVariable("doctorId") Long doctorId
    ) {
        institutionService.addDoctorToInstitution(institutionId, doctorId);
    }
}