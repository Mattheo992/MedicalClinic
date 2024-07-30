package com.Mattheo992.medicalclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    private List<Visit> visits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if  (!( o instanceof Patient))
            return false;
        Patient patient = (Patient) o;
        return id != null &&
                id.equals(patient.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id : " + id +
                ", email : '" + email + '\'' +
                ", idCard number : '" + idCardNo + '\'' +
                ", first name : '" + firstName + '\'' +
                ", last name : '" + lastName + '\'' +
                ", phone number : '" + phoneNumber + '\'' +
                ", birthday : " + birthday +
                ", user : " + user +
                '}';
    }
}