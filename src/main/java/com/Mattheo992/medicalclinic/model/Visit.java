package com.Mattheo992.medicalclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Visit)) return false;
        Visit visit = (Visit) o;
        return id != null && id.equals(visit.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", patient=" + patient +
                ", doctor=" + doctor +
                '}';
    }
}
