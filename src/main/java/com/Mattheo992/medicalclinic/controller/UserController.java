package com.Mattheo992.medicalclinic.controller;

import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.model.dtos.PatientDto;
import com.Mattheo992.medicalclinic.model.dtos.UserDto;
import com.Mattheo992.medicalclinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get all users", description = "Returned paginated list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned list of users",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content)
    })
    @GetMapping
    public List<UserDto> getUsers(Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @Operation(summary = "Get a user by id", description = "Returned a user founded by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned user founded by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @Operation(summary = "Create user", description = "Returns created user. Ensures the username is unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data or username is already exist"
                    , content = {@Content(mediaType = "application/json")})
    })
    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @Operation(summary = "Edit password of user with given id", description = "Returns edited user with given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password of user with given id successfully edited",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found"
                    , content = {@Content(mediaType = "application/json")})
    })
    @PatchMapping("/{id}/password")
    public UserDto updatePassword(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updatePassword(id, updatedUser);
    }
}