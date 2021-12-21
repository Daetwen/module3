package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.LocaleManager;
import com.epam.esm.util.UserConverter;
import com.epam.esm.util.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private UserDao userDao;
    private Validator validator;
    private UserConverter userConverter;
    private UserService userService;
    private UserDto userDtoTest1;
    private User userTest1;

    @BeforeEach
    public void setUp() {
        userDao = mock(UserDao.class);
        validator = mock(Validator.class);
        LocaleManager localeManager = mock(LocaleManager.class);
        userConverter = mock(UserConverter.class);
        userService = new UserServiceImpl(userDao, validator, localeManager, userConverter);
        List<Order> orderList = new ArrayList<>();
        userDtoTest1 = new UserDto(5L, "Vlad", "Makarei");
        userTest1 = new User(5L, "Vlad", "Makarei", orderList);
    }

    @Test
    public void findByIdTestTrue() throws ServiceValidationException, ServiceSearchException {
        doNothing().when(validator).validateId(anyString());
        when(userDao.findById(anyLong())).thenReturn(userTest1);
        when(userConverter.convertUserToUserDto(any(User.class))).thenReturn(userDtoTest1);
        UserDto actual = userService.findById("5");
        assertEquals(userDtoTest1, actual);
    }

    @Test
    public void findByIdTestFalse1() throws ServiceValidationException {
        doThrow(ServiceValidationException.class).when(validator).validateId(anyString());
        assertThrows(ServiceValidationException.class,
                () -> userService.findById("f5g"));
    }

    @Test
    public void findByIdTestFalse2() throws ServiceValidationException {
        doNothing().when(validator).validateId(anyString());
        when(userDao.findById(anyLong())).thenReturn(null);
        assertThrows(ServiceSearchException.class,
                () -> userService.findById("6"));
    }

    @Test
    public void findAllTestTrue() throws ServiceValidationException {
        List<UserDto> expected = new ArrayList<>();
        expected.add(userDtoTest1);
        List<User> listForWork = new ArrayList<>();
        listForWork.add(userTest1);
        doNothing().when(validator).validatePage(anyString());
        when(userDao.findAll(anyInt(), anyInt())).thenReturn(listForWork);
        when(userConverter.convertUserToUserDto(any(User.class))).thenReturn(userDtoTest1);
        List<UserDto> actual = userService.findAll("1", "10");
        assertEquals(expected, actual);
    }

    @Test
    public void findAllTestFalse1() throws ServiceValidationException {
        doThrow(ServiceValidationException.class).when(validator).validatePage(anyString());
        assertThrows(ServiceValidationException.class,
                () -> userService.findAll("1", "10"));
    }
}
