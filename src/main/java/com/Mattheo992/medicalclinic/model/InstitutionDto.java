package com.Mattheo992.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InstitutionDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String institutionName;
    private String city;
    private String zipCode;
    private String streetName;
    private Long numberOfStreet;

    @Transient
    private Set<SimpleDoctorDto> doctors;

    @Override
    public String toString() {
        return "InstitutionDto{" +
                "id : " + id +
                ", institution name :'" + institutionName + '\'' +
                ", city : '" + city + '\'' +
                ", zip code : '" + zipCode + '\'' +
                ", street name : '" + streetName + '\'' +
                ", number of street : " + numberOfStreet +
                ", doctors : " + doctors +
                '}';
    }
}
