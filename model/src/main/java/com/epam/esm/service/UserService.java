package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;

import java.util.List;

/**
 * The interface User service.
 */
public interface UserService {

    /**
     * Find user by id.
     *
     * @param id the id for search
     * @return the user dto
     * @throws ServiceSearchException     the service search exception
     * @throws ServiceValidationException the service validation exception
     */
    UserDto findById(String id) throws ServiceSearchException, ServiceValidationException;

    /**
     * Find all users.
     *
     * @param page     the page
     * @param pageSize the page size
     * @return the list
     * @throws ServiceValidationException the service validation exception
     */
    List<UserDto> findAll(String page, String pageSize) throws ServiceValidationException;
}
