package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.model.Visit;
import com.Mattheo992.medicalclinic.model.dtos.VisitDto;
import com.Mattheo992.medicalclinic.model.mappers.VisitMapper;
import com.Mattheo992.medicalclinic.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create visit", description = "Returns created visit. Ensures the start date is available.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Visit successfully created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VisitDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data or start date is not available"
                    , content = {@Content(mediaType = "application/json")})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public VisitDto createVisit(@RequestBody VisitDto visitDto) {
        Visit createdVisit = visitService.createVisit(visitDto);
        VisitDto createdVisitDto = visitMapper.visitDto(createdVisit);
        return createdVisitDto;
    }

    @Operation(summary = "Register a patient for a visit", description = "Associates a patient with a visit if the visit is not already occupied.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully registered patient for visit", content = @Content),
            @ApiResponse(responseCode = "400", description = "Visit is already occupied or invalid IDs provided", content = @Content),
            @ApiResponse(responseCode = "404", description = "Visit or patient not found", content = @Content)
    })
    @PostMapping("/{visitId}/patients/{patientId}")
    public void registerPatient(
            @PathVariable("visitId") Long visitId,
            @PathVariable("patientId") Long patientId) {
        visitService.registerPatientForVisit(visitId, patientId);
    }

    @Operation(summary = "Get visits for a patient", description = "Returned list of visits associated with a patient with given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned list of visits", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VisitDto.class))),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/patient/{patientId}")
    public List<VisitDto> getVisitsForPatient(
            @PathVariable("patientId") Long patientId) {
        return visitService.getVisitsByPatientId(patientId);
    }
}