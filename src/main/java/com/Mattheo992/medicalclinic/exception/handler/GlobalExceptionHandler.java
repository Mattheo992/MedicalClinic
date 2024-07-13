package com.Mattheo992.medicalclinic.exception.handler;

import com.Mattheo992.medicalclinic.exception.exceptions.DoctorNotFound;
import com.Mattheo992.medicalclinic.exception.exceptions.MedicalClinicException;
import com.Mattheo992.medicalclinic.exception.exceptions.PatientNotFound;
import com.Mattheo992.medicalclinic.exception.exceptions.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleIllegalArgumentException(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    }

    @ExceptionHandler(PatientNotFound.class)
    public ResponseEntity<String> handlePatientNotFound(PatientNotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

   @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFound ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
   }

   @ExceptionHandler(DoctorNotFound.class)
    public ResponseEntity<String> handleDoctorNotFound(DoctorNotFound ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
   }
}