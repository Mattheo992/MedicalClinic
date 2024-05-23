package com.Mattheo992.medicalclinic.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface PatientToPatientDtoMapper {
   PatientDto sourceToDestination(Patient source);
//   Patient sourceToDestination(PatientDto source);
    }

