package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.exception.exceptions.UserNotFound;
import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.model.dtos.UserDto;
import com.Mattheo992.medicalclinic.model.mappers.UserMapper;
import com.Mattheo992.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    //Case 1: Wywołanie metody findAll z userRepository zwraca pustą listę List<UserDto>.
    //Case 2: Wywołanie metod findAll z userRepository tworzy  List<User> o parametrach przekazanych w Pageable.
    // Metoda przy użyciu userMapper zwraca List<UserDto>.
    public List<UserDto> getUsers(Pageable pageable) {
        List<User> users = userRepository.findAll(pageable).getContent();
        return userMapper.toListDto(users);
    }

    //Case 1:  Wywołanie metody findById z userRepository zwraca pustego Optional i rzucany jest wyjątek,
    // że użytkownik o podanym Id nie istnieje.
    //Case 2: Wywołanie metody findById z userRepository zwraca obiekt User.
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("User with given id does not exist"));
    }

    //Case 1: Wartości przekazane do metody są poprawne, zostaje wywołana metoda save z userRepository, User zostaje zapisany.
    //Case 2: Przekazano nieprawidłowe wartości w polach dla obiektu User, metoda rzuca wyjątek np. IllegalArgumentException.
    @Transactional
    public User addUser(User user) {
        checkIsUsernameAvailable(user.getUsername());
        return userRepository.save(user);
    }

    //Case 1: Metoda findById wywołana z userRepository zwrca pustego Optionala, rzucany jest wyjątek, że taki
    // użytkownik nie został znaleziony.
    //Case 2:  Metoda findById wywołana z userRepository zwraca Optional<User>,  hasło dla podanego usera zostaje zmienione,
    // metoda save z userRepository zapisuje usera i zwracane jest nowe hasło.
    @Transactional
    public UserDto updatePassword(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("User not found"));
        user.setPassword(updatedUser.getPassword());
        return userMapper.userToUserDto(userRepository.save(user));
    }

    private void checkIsUsernameAvailable(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User with given username is already exist");
        }
    }
}