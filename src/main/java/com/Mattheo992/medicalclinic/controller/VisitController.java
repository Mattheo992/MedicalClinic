package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Visit;
import com.Mattheo992.medicalclinic.model.VisitDto;
import com.Mattheo992.medicalclinic.model.VisitMapper;
import com.Mattheo992.medicalclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public VisitDto createVisit(@RequestBody VisitDto visitDto) {
        Visit createdVisit = visitService.createVisit(visitDto);
        VisitDto createdVisitDto = visitMapper.visitDto(createdVisit);
        return createdVisitDto;
    }

    @PostMapping("/{visitId}/patients/{patientId}")
    public void registerPatient(
            @PathVariable("visitId") Long visitId,
            @PathVariable("patientId") Long patientId) {
        visitService.registerPatientForVisit(visitId, patientId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Visit> getVisitsForPatient(
            @PathVariable("patientId") Long patientId) {
        return visitService.getVisitsByPatientId(patientId);
    }
}
