package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.Visit;
import com.Mattheo992.medicalclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<Visit> createVisit(@RequestBody Map<String, Object> requestBody) {
        LocalDateTime startDate = LocalDateTime.parse((CharSequence) requestBody.get("startDate"));
        LocalDateTime endDate = LocalDateTime.parse((CharSequence) requestBody.get("endDate"));
        Visit createdVisit = visitService.createVisit(startDate, endDate);
        return ResponseEntity.ok(createdVisit);
    }


    @PostMapping("/{visitId}/register/{patientId}")
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
