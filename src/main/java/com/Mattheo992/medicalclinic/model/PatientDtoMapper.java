package com.Mattheo992.medicalclinic.model;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface PatientDtoMapper {
   PatientDto sourceToDestination(Patient source);
   Patient sourceToDestination(PatientDto source);
    }

