package com.epam.esm.builder;

import com.epam.esm.dto.UserDto;

public class UserDtoBuilder {

    private Long id;
    private String name;
    private String surname;

    public UserDto build() {
        UserDto userDto = new UserDto(id, name, surname);
        this.id = null;
        this.name = null;
        this.surname = null;
        return userDto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
