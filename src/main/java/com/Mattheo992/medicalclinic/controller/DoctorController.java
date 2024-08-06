package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.dtos.DoctorDto;
import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @Operation(summary = "Create doctor", description = "Returns created doctor. Ensures the email is unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor successfully created",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Doctor.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data or emails is already exist"
                    , content = {@Content(mediaType = "application/json")})
    })
    @PostMapping
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    @Operation(summary = "Get all doctors", description = "Returned paginated list of doctors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned list of doctors",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDto.class ))}),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content)
    })
    @GetMapping
    public List<DoctorDto> getDoctors(Pageable pageable) {
        return doctorService.getDoctors(pageable);
    }

    @Operation(summary = "Get institutions assigned to the doctor", description = "Returned set of institutions assigned " +
            "to the doctor finded by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned set of institutions assigned to doctor finded by id",
                    content ={@Content(mediaType = "application/json", schema = @Schema(implementation = Institution.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content)
    })
    @GetMapping("/{doctorId}/institutions")
    public List<Institution> getInstitutionsForDoctor(@PathVariable Long doctorId) {
        return doctorService.getInstitutionsForDoctor(doctorId);
    }
}