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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @Operation(summary = "Register a doctor for a visit", description = "Associates a doctor with a visit if the visit is not already occupied.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully registered doctor for visit", content = @Content),
            @ApiResponse(responseCode = "400", description = "Visit is already occupied or invalid IDs provided", content = @Content),
            @ApiResponse(responseCode = "404", description = "Visit or doctor not found", content = @Content)
    })
    @PostMapping("/{visitId}/doctors/{doctorId}")
    public void registerDoctor(
            @PathVariable("visitId") Long visitId,
            @PathVariable("doctorId") Long doctorId) {
        visitService.registerDoctorForVisit(visitId, doctorId);
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

    @Operation(summary = "Get visits for a doctor", description = "Returned list of visits associated with a doctor with given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned list of visits", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VisitDto.class))),
            @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/doctor/{doctorId}")
    public List<VisitDto> getVisitsForDoctor(
            @PathVariable("doctorId") Long doctorId) {
        return visitService.getVisitsByDoctorId(doctorId);
    }

    @Operation(summary = "Get available visits by doctor specialization and date", description = "Returns a list of available visits for the chosen specialization and date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned available visits for the doctor."),
            @ApiResponse(responseCode = "404", description = "Doctor specialization not found.")
    })
    @GetMapping("/doctors/{specialization}/date/{date}")
    public List<VisitDto> getAvailableVisitsBySpecializationAndDate(
            @PathVariable("specialization") String specialization,
            @PathVariable("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return visitService.getAvailableVisitsByDoctorSpecializationAndByDate(specialization, date);
    }

    @Operation(summary = "Get available visits by doctor ID", description = "Returns a list of available visits for the chosen doctor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned available visits for the doctor."),
            @ApiResponse(responseCode = "404", description = "Doctor not found.")
    })
    @GetMapping("/doctors/{doctorId}/available-visits")
    public List<VisitDto> getAvailableVisitsByDoctorId(
            @PathVariable("doctorId") Long doctorId) {
        return visitService.getAvailableVisitsByDoctorId(doctorId);
    }

    @Operation(summary = "Get visits for a doctor with given specialization and a date range", description = "Returns a list of visits associated with a doctor in a given date range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned list of visits", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VisitDto.class))),
            @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping()
    public List<VisitDto> getVisitsByDoctorSpecializationAndStartDateBetween(
            @RequestParam("specialization") String specialization,
            @RequestParam("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return visitService.getVisitsBySpecializationAndDateRange(specialization, start, end);
    }

    @Operation(summary = "Cancel a visit", description = "Cancels a visit by its ID and returns a confirmation message.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visit successfully cancelled", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Visit not found", content = @Content)
    })
    @DeleteMapping("/{visitId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelVisit(@PathVariable Long visitId) {
     visitService.cancelVisit(visitId);}

    @GetMapping("/range")
    public List<VisitDto> getAvailableVisitsAndDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return visitService.getAvailableVisitsAndDateRange(start, end);
    }
}