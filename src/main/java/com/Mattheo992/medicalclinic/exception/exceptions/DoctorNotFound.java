package com.Mattheo992.medicalclinic.exception.exceptions;

import org.springframework.http.HttpStatus;

import java.net.http.HttpClient;

public class DoctorNotFound extends MedicalClinicException{
    public DoctorNotFound(String message){
        super(message, HttpStatus.NOT_FOUND);
    }
}
