package com.epam.esm.hateoas;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;

import java.util.List;

public interface UserResource {

    Resource<UserDto> getOne(UserDto userDto)
            throws ServiceValidationException, ServiceSearchException;

    Resource<List<Resource<UserDto>>> getAll(List<UserDto> userDtoList, String page, String pageSize)
            throws ServiceValidationException, ServiceSearchException;
}
