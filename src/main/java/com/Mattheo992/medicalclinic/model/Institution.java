package com.Mattheo992.medicalclinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String institutionName;
    private String city;
    private String zipCode;
    private String streetName;
    private Long numberOfStreet;

    @ManyToMany(mappedBy = "institutions")
    private Set<Doctor> doctors;
}