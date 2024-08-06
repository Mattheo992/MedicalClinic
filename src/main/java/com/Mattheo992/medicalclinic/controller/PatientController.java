package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.dtos.InstitutionDto;
import com.Mattheo992.medicalclinic.model.dtos.PatientDto;
import com.Mattheo992.medicalclinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    @Operation(summary = "Get all patients", description = "Returned paginated list of patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned list of patients",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content)
    })
    @GetMapping
    public List<PatientDto> getPatients(Pageable pageable) {
        return patientService.getPatients(pageable);
    }

    @Operation(summary = "Get a patient by email", description = "Returned a patient founded by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned patient found by email",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content)
    })
    @GetMapping("/{email}")
    public PatientDto getPatient(@PathVariable String email) {
        return patientService.getPatient(email);
    }

    @Operation(summary = "Create patient", description = "Returns created patient. Ensures the email is unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient successfully created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data or email is already exist"
                    , content = {@Content(mediaType = "application/json")})
    })
    @PostMapping
    public PatientDto addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @Operation(summary = "Create patient with user", description = "Returns created patient with user. Ensures the email is unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient with user successfully created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data or email is already exist"
                    , content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/with-user")
    public PatientDto addPatientWithUser(@RequestBody PatientDto patientDto) {
        return patientService.addPatientWithUser(patientDto);
    }

    @Operation(summary = "Delete patient founded by email", description = "Patient with given id is deleted")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted patient with given email", content = @Content),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content)
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable("email") String email) {
        patientService.deletePatient(email);
    }

    @Operation(summary = "Edit patient with given email", description = "Returns edited patient with given email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient with given email successfully edited",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found"
                    , content = {@Content(mediaType = "application/json")})
    })
    @PutMapping("/{email}")
    public PatientDto editPatient(@PathVariable("email") String email, @RequestBody Patient patient) {
        return patientService.editPatient(email, patient);
    }
}