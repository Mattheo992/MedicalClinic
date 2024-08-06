package com.Mattheo992.medicalclinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.List;
import java.util.Objects;
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
    private List<Doctor> doctors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if  (!( o instanceof Institution))
            return false;
        Institution institution = (Institution) o;
        return id != null &&
                id.equals(institution.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Institution{" +
                "id : " + id +
                ", institution name : '" + institutionName + '\'' +
                ", city : '" + city + '\'' +
                ", zipCode : '" + zipCode + '\'' +
                ", street name : '" + streetName + '\'' +
                ", number of street : " + numberOfStreet +
                ", doctors : " + doctors +
                '}';
    }
}
