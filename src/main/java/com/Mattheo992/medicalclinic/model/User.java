package com.Mattheo992.medicalclinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Patient patient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User))
            return false;
        User user = (User) o;
        return id != null &&
                id.equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id : " + id +
                ", username : '" + username + '\'' +
                ", password : '" + password + '\'' +
                ", patient : " + patient +
                '}';
    }
}
