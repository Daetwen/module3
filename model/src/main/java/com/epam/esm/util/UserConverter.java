package com.epam.esm.util;

import com.epam.esm.builder.UserBuilder;
import com.epam.esm.builder.UserDtoBuilder;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserDto convertUserToUserDto(User user) {
        UserDtoBuilder userDtoBuilder = new UserDtoBuilder();
        userDtoBuilder.setId(user.getId());
        userDtoBuilder.setName(user.getName());
        userDtoBuilder.setSurname(user.getSurname());
        return userDtoBuilder.build();
    }

    public User convertUserDtoToUser(UserDto userDto) {
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.setId(userDto.getId());
        userBuilder.setName(userDto.getName());
        userBuilder.setSurname(userDto.getSurname());
        return userBuilder.build();
    }
}
