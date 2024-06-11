package com.Mattheo992.medicalclinic.model;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface PatientDtoMapper {
    PatientDto dto(Patient patient);

    List<PatientDto> toDtos(List<Patient> patients);

    Patient dto(PatientDto patientDto);
}