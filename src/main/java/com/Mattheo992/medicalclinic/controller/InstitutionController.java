package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.InstitutionDto;
import com.Mattheo992.medicalclinic.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions")
public class InstitutionController {
    private final InstitutionService institutionService;

    @PostMapping
    public InstitutionDto addInstitution(@RequestBody InstitutionDto institutionDto) {
        return institutionService.addInstitution(institutionDto);
    }

    @GetMapping
    public Page<InstitutionDto> getInstitutions(@RequestParam int page,
                                                @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return institutionService.getInstitutions(pageable);
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
