package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.*;
import com.Mattheo992.medicalclinic.model.dtos.InstitutionDto;
import com.Mattheo992.medicalclinic.model.mappers.InstitutionMapper;
import com.Mattheo992.medicalclinic.repository.DoctorRepository;
import com.Mattheo992.medicalclinic.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final DoctorRepository doctorRepository;
    private final InstitutionMapper institutionMapper;

    //Case 1: Metoda checkIfEmailIsAvailable sprawdzając czy nazwa instytucji jest dostępna zwraca true, metoda rzuca
    // wyjątek, że instytucja o podanej nazwie istnieje.
    //Case 2: : Metoda checkIfEmailIsAvailable sprawdzając czy nazwa instytucji jest dostępna zwraca false,
    //stworzony obiekt Institution poprzez institutionMapper mapowany jest na obiekt Dto a następnie przez metodę
    // save z institutionRepository jest zapisywany.
    @Transactional
    public InstitutionDto addInstitution(InstitutionDto institutionDto) {
        Institution institution = institutionMapper.toEntity(institutionDto);
        checkIfNameIsAvailable(institution.getInstitutionName());
        return institutionMapper.toDto(institutionRepository.save(institution));
    }

    //Case 1: Wywołanie metody findAll z institutionRepository zwraca pustą listę List<InstitutionDto>.
    //Case 2: Wywołanie metod findAll z institutionRepository tworzy  List<Institution> o parametrach przekazanych
    // w Pageable. Metoda przy użyciu institutionMapper zwraca List<InstitutionDto>.
    public List<InstitutionDto> getInstitutions(Pageable pageable) {
        List<Institution> institutions = institutionRepository.findAll(pageable).stream()
                .toList();
        return institutionMapper.toListDtos(institutions);
    }

    //Case 1: Metoda findById z institutionsRepository zwraca pustego Optional, rzuca wyjątkiem, ze instytucja o podanym
    // id nie istnieje.
    //Case 2: Metoda findById z institutionsRepository zwraca  Optional<Institution>, metoda findById z doctorRepository
    // zwraca pustego Optional i rzuca wyjątkiem, że doktor o podanym id nie istnieje.
    //Case 3: Metoda findById z institutionsRepository zwraca  Optional<Institution>, metoda findById z doctorRepository
    // zwraca Optional<Doctor>, doktor zostaje dodany do listy doktorów w podanej instytucji, metoda save z institutionRepository zapisuje instytucję.
    @Transactional
    public void addDoctorToInstitution(Long institutionId, Long doctorId) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        institution.getDoctors().add(doctor);
        institutionRepository.save(institution);
    }

    private void checkIfNameIsAvailable(String name) {
        if (institutionRepository.existsByInstitutionName(name)) {
            throw new IllegalArgumentException("Institution with given name already exist!");
        }
    }
}