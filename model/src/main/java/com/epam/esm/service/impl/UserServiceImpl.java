package com.epam.esm.service.impl;

import com.epam.esm.constant.LanguagePath;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.LocaleManager;
import com.epam.esm.util.UserConverter;
import com.epam.esm.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final Validator validator;
    private final LocaleManager localeManager;
    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserDao userDao, Validator validator,
                           LocaleManager localeManager, UserConverter userConverter) {
        this.userDao = userDao;
        this.validator = validator;
        this.localeManager = localeManager;
        this.userConverter = userConverter;
    }

    @Override
    public UserDto findById(String id) throws ServiceSearchException, ServiceValidationException {
        validator.validateId(id);
        User result = userDao.findById(Long.parseLong(id));
        return checkUser(result);
    }

    @Override
    public List<UserDto> findAll(String page, String pageSize) throws ServiceValidationException {
        validator.validatePage(page);
        validator.validatePage(pageSize);
        List<UserDto> userDtoList = new ArrayList<>();
        if (Integer.parseInt(page) <= getCountOfPages(pageSize)) {
            for (User element : userDao.findAll(Integer.parseInt(page), Integer.parseInt(pageSize))) {
                userDtoList.add(userConverter.convertUserToUserDto(element));
            }
        }
        return userDtoList;
    }

    private UserDto checkUser(User user)
            throws ServiceSearchException {
        if (user != null) {
            return userConverter.convertUserToUserDto(user);
        } else {
            throw new ServiceSearchException(
                    localeManager.getLocalizedMessage(LanguagePath.ERROR_NOT_FOUND));
        }
    }

    private int getCountOfPages(String pageSize) {
        int countOfRecords = userDao.findCountOfRecords();
        int size = Integer.parseInt(pageSize);
        int countPages = countOfRecords % size == 0 ? countOfRecords / size : countOfRecords / size + 1;
        return countPages == 0 ? 1 : countPages;
    }
}
