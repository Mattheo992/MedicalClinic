package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with given id does not exist"));
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }


}
