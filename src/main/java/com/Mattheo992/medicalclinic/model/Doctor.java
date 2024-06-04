package com.Mattheo992.medicalclinic.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String specialization;
    private String email;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor))
            return false;
        Doctor doctor = (Doctor) o;
        return id != null &&
                id.equals(doctor.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "Id : " + id +
                ", name : '" + name + '\'' +
                ", surname : '" + surname + '\'' +
                ", specialization : '" + specialization + '\'' +
                ", email : '" + email + '\'' +
                ", password : '" + password + '\'' +
                ", institutions : " + institutions +
                '}';
    }

    @ManyToMany
    @JoinTable(
            name = "doctor_institution",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "institution_id")
    )
    private Set<Institution> institutions;
}
