package com.Mattheo992.medicalclinic.exception.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFound extends MedicalClinicException {
    public UserNotFound(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
