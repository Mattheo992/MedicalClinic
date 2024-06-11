package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers(Pageable pageable) {
        Pageable sortedByUsername = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("username"));
        return userRepository.findAll(sortedByUsername).getContent();
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with given id does not exist"));
    }
@Transactional
    public User addUser(User user) {
        return userRepository.save(user);
    }
}