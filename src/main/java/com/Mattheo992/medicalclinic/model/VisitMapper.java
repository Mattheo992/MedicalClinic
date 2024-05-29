package com.Mattheo992.medicalclinic.model;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface VisitMapper {
    VisitDto sourceToDestination(Visit source);
    Visit destinationToSource(VisitDto destination);
}