package com.Mattheo992.medicalclinic.Model;

import lombok.Data;

@Data
public class Patient {
private Long id;
private String email;
    private String password;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String birthday;
}
