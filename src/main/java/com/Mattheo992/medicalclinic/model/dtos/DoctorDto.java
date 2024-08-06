package com.Mattheo992.medicalclinic.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {
    private String name;
    private String surname;
    private String specialization;
    private String email;
}
