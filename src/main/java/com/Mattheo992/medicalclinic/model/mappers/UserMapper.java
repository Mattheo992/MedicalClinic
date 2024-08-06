package com.Mattheo992.medicalclinic.model.mappers;

import com.Mattheo992.medicalclinic.model.User;
import com.Mattheo992.medicalclinic.model.dtos.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.control.MappingControl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User user);
List<UserDto> toListDto (List<User> users);
    User userDtoToUser(UserDto userDto);
}