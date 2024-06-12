package com.Mattheo992.medicalclinic.model.mappers;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.dtos.PatientDto;
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