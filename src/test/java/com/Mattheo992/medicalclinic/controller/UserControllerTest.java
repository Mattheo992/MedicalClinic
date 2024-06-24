package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.model.dtos.UserDto;
import com.Mattheo992.medicalclinic.service.DoctorService;
import com.Mattheo992.medicalclinic.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    void getUsers_UsersExists_ReturnedUsers() throws Exception {
        UserDto userDto = new UserDto(1L, "mat", "haslo");
        UserDto userDto1 = new UserDto(2L, "pat", "password");
        Pageable pageable = PageRequest.of(0, 10);
        List<UserDto> users = new ArrayList<>();
        users.add(userDto);
        users.add(userDto1);
        when(userService.getUsers(pageable)).thenReturn(users);
        mockMvc.perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(users)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("mat"))
                .andExpect(jsonPath("$[0].password").value("haslo"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].username").value("pat"))
                .andExpect(jsonPath("$[1].password").value("password"));
    }

    @Test
    void getUsers_UsersNotExists_ReturnedEmptyList() throws Exception {
        List<UserDto> users = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 10);
        when(userService.getUsers(pageable)).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(users)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].length").value(0));
    }

    @Test
    void getUser_UserExists_ReturnedUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("mat");
        when(userService.getUser(1L)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("mat"));
    }

    @Test
    void getUser_UserNotExists_UserNotReturned() throws Exception {
        Long userId = 1L;
        when(userService.getUser(userId)).thenThrow(new IllegalArgumentException("User with given id does not exist"));

        mockMvc.perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with given id does not exist."));
    }

    @Test
    void addUser_DataCorrect_UserSaved() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("mat");
        when(userService.addUser(user)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("mat"));
    }

    @Test
    void addUser_UsernameNotAvailable_UserNotSaved() throws Exception {
        User user = new User();
        user.setUsername("mat");
when(userService.addUser(user)).thenThrow(new IllegalArgumentException("User with given username is already exist"));

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User with given username is already exist."));
    }

    @Test
    void updatePassword_UserExists_PasswordUpdated() throws Exception {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("Mateusz");
        user.setPassword("nowehaslo");

        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setUsername("Mateusz");
        userDto.setPassword("nowehaslo");

        when(userService.updatePassword(id, user)).thenReturn(userDto);

        mockMvc.perform(patch("/users/{id}/password", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value("nowehaslo"))
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    void updatePassword_UserNotExists_PasswordNotUpdated() throws Exception{
        Long id = 1L;
        User user = new User();
        user.setId(id);
        when(userService.updatePassword(id, user)).thenThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}/password", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().string("User not found"));
    }
}