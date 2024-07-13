package com.Mattheo992.medicalclinic.exception.exceptions;

import org.springframework.http.HttpStatus;

public class PatientNotFound extends MedicalClinicException {

    public PatientNotFound(String message){
        super(message, HttpStatus.NOT_FOUND);
    }
}
