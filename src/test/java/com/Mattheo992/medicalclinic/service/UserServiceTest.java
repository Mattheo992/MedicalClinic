package com.Mattheo992.medicalclinic.service;

import com.Mattheo992.medicalclinic.model.Patient;
import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.model.dtos.UserDto;
import com.Mattheo992.medicalclinic.model.mappers.UserMapper;
import com.Mattheo992.medicalclinic.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.control.MappingControl;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class UserServiceTest {
    UserService userService;
    UserRepository userRepository;
    UserMapper userMapper;

    @BeforeEach
    void setup() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.userService = new UserService(userRepository, userMapper);
    }

    @Test
    void getUsers_UsersExists_UsersReturned() {
        //given
        List<User> users = new ArrayList<>();
        users.add(createUser("lubiePlacki", 1L));
        users.add(createUser("nalesniki", 2L));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        //when
        when(userRepository.findAll()).thenReturn(users);
        List<UserDto> result = userService.getUsers(pageable);
        //then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("lubiePacki", result.get(0).getUsername());
        Assertions.assertEquals("nalesniki", result.get(1).getUsername());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getUsers_UsersNotExist_ReturnEmpty() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        //when
        List<UserDto> result = userService.getUsers(pageable);
        //then
        Assertions.assertTrue(result.isEmpty(), "Expected the result list to be empty");
    }

    @Test
    void getUser_UserExist_UserReturned() {
        //given
        User user = createUser("mateusz", 1L);
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        //when
        User result = userService.getUser(id);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("mateusz", result.getUsername());
    }

    @Test
    void getUser_UserNotExist_UserNotReturned() {
        //given
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        //then
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> userService.getUser(id));
        //then
        Assertions.assertEquals("User with given id does not exist", exception.getMessage());
    }

    @Test
    void addUser_UserAvailable_UserSaved() {
        //given
        User user = createUser("mateusz", 1L);
        when(userRepository.save(user)).thenReturn(user);
        //when
        User result = userService.addUser(user);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("mateusz", result.getUsername());
    }

    @Test
    void addUser_UsernameIsNotAvailable_UserNotAdded() {
        //given
        String username = "asdasd";
        User user = createUser(username, 1L);
        when(userRepository.existsByUsername(username)).thenReturn(true);
        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, ()-> userService.addUser(user));
    //then
        Assertions.assertEquals("User with given username is already exist", exception.getMessage());
    }

    @Test
    void updatePassword_UserExist_PasswordUpdated() {
        //given
        User user = createUser("mateusz", 1L);
        String password = "nowehaslo";
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user));
        //when
        String result = userService.updatePassword(id, password);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("nowehaslo", result);
        Assertions.assertEquals(password, result);
        Assertions.assertEquals(user.getPassword(), result);
    }

    @Test
    void updatePassword_UserNotExist_PasswordNotUpdated(){
        //given
        Long id = 1L;
        String newPassword = "asdasd";
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        //when
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, ()-> userService.updatePassword(id, newPassword));
        //then
        Assertions.assertEquals("User not found", exception.getMessage());
    }


    User createUser(String username, Long id) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setFirstName("kamil");
        return new User(id, username, "haslo", patient);
    }
}
