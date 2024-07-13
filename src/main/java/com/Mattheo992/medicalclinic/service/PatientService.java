package com.Mattheo992.medicalclinic.service;
import com.Mattheo992.medicalclinic.exception.exceptions.PatientNotFound;
import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.model.dtos.PatientDto;
import com.Mattheo992.medicalclinic.model.mappers.PatientDtoMapper;
import com.Mattheo992.medicalclinic.model.mappers.UserMapper;
import com.Mattheo992.medicalclinic.repository.PatientRepository;
import com.Mattheo992.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientDtoMapper patientDtoMapper;
    private final UserMapper userMapper;

    //Case 1: Wywołanie metody findAll z patientRepository zwraca pustą listę List<PatientDto>.
    //Case 2: Wywołanie metod findAll z userRepository tworzy  List<Patient> o parametrach przekazanych w Pageable.
    // Metoda przy użyciu patientDtoMapper zwraca List<PatientDto>.
    public List<PatientDto> getPatients(Pageable pageable) {
        List<Patient> patients = patientRepository.findAll(pageable).getContent();
        return patientDtoMapper.toDtos(patients);
    }
//Case 1: Jeśli metoda findByEmail wywołana z patientRepository zwróci Optional empty, to getPatient powinno rzucić
    //wyjątek, że pacjent o podanym emailu nie istnieje.
    // Case 2: Jeśli metoda findByEmail wywołana z patientRepository zwróci zwróci pacjenta tj. Optional<Patient>
    // to patientDtoMapper zamienia Patient na PatientDto i zostaje zwrócony PatientDto.
public PatientDto getPatient(String email) {
    return patientRepository.findByEmail(email)
            .map(patientDtoMapper::dto)
            .orElseThrow(() -> new PatientNotFound("Patient with given email does not exist."));
}
//case1:W sytuacji gdy checkIfEmailIsAvailable zwróci true to powinien rzucić wyjątek, że taki pacjent istnieje.
    //case 2: checkIfEmailIsAvailable zwróci false to przechodzimy do wywołania patientRepository i zapisanie pacjenta,
    // a następnie zmapowania do patientDto i zapisanie go.
    public PatientDto addPatient(Patient patient) {
        checkIfEmailIsAvailable(patient.getEmail());
        Patient savedPatient = patientRepository.save(patient);
        return patientDtoMapper.dto(savedPatient);
    }
//test case1: W sytuacji gdy checkIfEmailIsAvailable zwróci true to powinien rzucić wyjątek, że taki pacjent istnieje
    // test case2: W sytuacji gdy checkIfEmailIsAvailable zwróci false to powinien przejść do userRepository.save,
    //patient repository safe a finalnie zapisanie zmapowanego pacjenta z userem
    @Transactional
    public PatientDto addPatientWithUser(PatientDto patientDto) {
        checkIfEmailIsAvailable(patientDto.getEmail());
        User user = userMapper.userDtoToUser(patientDto.getUser());
        userRepository.save(user);
        Patient patient = patientDtoMapper.dto(patientDto);
        patient.setUser(user);
        Patient savedPatient = patientRepository.save(patient);
        return patientDtoMapper.dto(savedPatient);
    }
//Case 1: W sytuacji gdy metoda findByEmail zwróci pustego Optionala, to powinniśmy otrzymać rzucony wyjątek, że pacjent o podanym
    //emailu nie istnieje.
    //Case 2: W styacji gdy metoda findByEmail zwróci Optional<Patient>, to sprawdzamy czy user dla pacjenta nie jest nullem, jeśli
    //nie to usuwamy usera przypisanego do pacjenta a następnie usuwa pacjenta.
    // Case 3: W styacji gdy metoda findByEmail zwróci true, to sprawdzamy czy user dla pacjenta nie jest nullem, jeśli tak
    //to przechodzi do usunięcia pacjenta metodą wywołaną patientRepository.delete
    @Transactional
    public void deletePatient(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFound("Patient with given email does not exist."));
        User user = patient.getUser();
        if (user != null) {
            userRepository.delete(user);
        }
        patientRepository.delete(patient);
    }
//Case 1: Sprawdzamy czy jeśli pacjent nie istnieje to zostanie rzucony wyjatek.
// Jeśli metoda findByEmail wywołana z patientRepository zwraca pustego Optionala to metoda editPatient powinna rzucić
    //wyjątek, że pacjent o podanym emailu nie został znaleziony.
    //Case 2: Sprawdzamy przypadek w którym po wywołaniu metody findByEmail zostanie znaleziony pacjent.
    //Jeśli metoda findByEmail zwraca Optional<Patient> ustawiane są wartości pól. Metoda zwraca PatientDto po zmapowaniu
// przy pomocy  patientDtoMapper i zapisany przez metodę save z patientRepository.
    @Transactional
    public PatientDto editPatient(String email, Patient uptadetPatient) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFound("Patient with given email does not exist."));
        patient.setEmail(uptadetPatient.getEmail());
        patient.setFirstName(uptadetPatient.getFirstName());
        patient.setLastName(uptadetPatient.getLastName());
        patient.setBirthday(uptadetPatient.getBirthday());
        patient.setPhoneNumber(uptadetPatient.getPhoneNumber());
        patient.setIdCardNo(uptadetPatient.getIdCardNo());
        return patientDtoMapper.dto(patientRepository.save(patient));
    }

    private void checkIfEmailIsAvailable(String email) {
        if (patientRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Patient with given email already exists.");
        }
    }
}