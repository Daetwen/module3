package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;
import com.epam.esm.hateoas.Resource;
import com.epam.esm.hateoas.UserResource;
import com.epam.esm.service.UserService;
import static com.epam.esm.constant.PaginationParameter.DEFAULT_PAGE;
import static com.epam.esm.constant.PaginationParameter.DEFAULT_PAGE_SIZE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserResource userResource;

    @Autowired
    public UserController(UserService userService, UserResource userResource) {
        this.userService = userService;
        this.userResource = userResource;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Resource<List<Resource<UserDto>>> getAll(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) String page,
            @RequestParam(value = "pageSize", required = false, defaultValue = DEFAULT_PAGE_SIZE) String pageSize)
            throws ServiceValidationException, ServiceSearchException {
        return userResource.getAll(userService.findAll(page, pageSize), page, pageSize);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<UserDto> getOne(@PathVariable final String id)
            throws ServiceSearchException, ServiceValidationException {
        return userResource.getOne(userService.findById(id));
    }
}
