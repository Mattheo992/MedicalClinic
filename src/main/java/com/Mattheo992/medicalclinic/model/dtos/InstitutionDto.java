package com.Mattheo992.medicalclinic.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstitutionDto {
    private String institutionName;
    private String city;
    private String zipCode;
    private String streetName;
    private Long numberOfStreet;
    private List<SimpleDoctorDto> doctors;

    @Override
    public String toString() {
        return "InstitutionDto{" +
                "institution name : '" + institutionName + '\'' +
                ", city : '" + city + '\'' +
                ", zip code : '" + zipCode + '\'' +
                ", street name : '" + streetName + '\'' +
                ", number of street : " + numberOfStreet +
                ", doctors : " + doctors +
                '}';
    }
}