package com.Mattheo992.medicalclinic.exception.exceptions;

import org.springframework.http.HttpStatus;

public class MedicalClinicException extends RuntimeException {
    private HttpStatus status;

    public MedicalClinicException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
