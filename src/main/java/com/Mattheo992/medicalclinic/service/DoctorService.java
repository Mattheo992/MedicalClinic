package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Doctor;
import com.Mattheo992.medicalclinic.model.dtos.DoctorDto;
import com.Mattheo992.medicalclinic.model.mappers.DoctorMapper;
import com.Mattheo992.medicalclinic.model.Institution;
import com.Mattheo992.medicalclinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    //Case 1: Wartości przekazane do metody są poprawne, zostaje wywołana metoda save z doctorRepository,
    // Doctor zostaje zapisany.
    //Case 2: Przekazano nieprawidłowe wartości w polach dla obiektu Doctor, metoda rzuca wyjątek
    // np. IllegalArgumentException.
    @Transactional
    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }


    //Case 1: Wywołanie metody findAll z doctorRepository zwraca pustą listę List<DoctorDto>.
    //Case 2: Wywołanie metod findAll z doctorRepository tworzy  List<Doctor> o parametrach przekazanych w Pageable.
    // Metoda przy użyciu doctorMapper mapuje <List>Doctor i zwraca List<DoctorDto>.
    public List<DoctorDto> getDoctors(Pageable pageable) {
        List<Doctor> doctors = doctorRepository.findAll(pageable).getContent();
        return doctorMapper.toDtos(doctors);
    }

    //Case 1: metoda findById z doctorRepository zwraca pustego Optional, rzucany jest wyjątek, że doktor o podanym
    // id nie istnieje.
    //Case 2: metoda findById z doctorRepository zwraca Optional<Doctor>, metoda getInstututions() wskazuje, że doktor
    // nie jest przypisany do żadnej instytucji, zwracany jest pusty Set.
    //Case 3: metoda findById z doctorRepository zwraca Optional<Doctor>, metoda getInstitutions() pobiera przypisane
    // instytucje do doktora, zwracany jest Set z przypisanymi instytucjami do doktora.
    public Set<Institution> getInstitutionsForDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        return doctor.getInstitutions();
    }
}
