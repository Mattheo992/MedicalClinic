package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.dtos.DoctorDto;
import com.Mattheo992.medicalclinic.model.dtos.InstitutionDto;
import com.Mattheo992.medicalclinic.service.InstitutionService;
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
@RequestMapping("/institutions")
public class InstitutionController {
    private final InstitutionService institutionService;

    @Operation(summary = "Create institution", description = "Returns created institution. Ensures the institutions name is unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institution successfully created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = InstitutionDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data or institution name is already exist"
                    , content = {@Content(mediaType = "application/json")})
    })
    @PostMapping
    public InstitutionDto addInstitution(@RequestBody InstitutionDto institutionDto) {
        return institutionService.addInstitution(institutionDto);
    }

    @Operation(summary = "Get all institutions", description = "Returned paginated list of institutions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned list of institutions",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = InstitutionDto.class ))}),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content)
    })
    @GetMapping
    public List<InstitutionDto> getInstitutions(Pageable pageable) {
        return institutionService.getInstitutions(pageable);
    }

    @Operation(summary = "Add a doctor to an institution", description = "Associates a doctor with an institution.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully added doctor to institution", content = @Content),
            @ApiResponse(responseCode = "404", description = "Institution or doctor not found", content = @Content)
    })
    @PostMapping("/{institutionId}/doctors/{doctorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addDoctorToInstitution(
            @PathVariable("institutionId") Long institutionId,
            @PathVariable("doctorId") Long doctorId
    ) {
        institutionService.addDoctorToInstitution(institutionId, doctorId);
    }
}