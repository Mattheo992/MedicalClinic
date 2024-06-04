package com.Mattheo992.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthday;

    @Override
    public String toString() {
        return "PatientDto{" +
                "id : " + id +
                ", email : '" + email + '\'' +
                ", first name : '" + firstName + '\'' +
                ", last name : '" + lastName + '\'' +
                ", birthday : " + birthday +
                '}';
    }
}
