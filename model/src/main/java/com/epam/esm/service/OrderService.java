package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;

import java.util.List;

public interface OrderService {

    int create(String userId, String certificateId) throws ServiceValidationException;

    OrderDto findById(String id) throws ServiceSearchException, ServiceValidationException;

    List<OrderDto> findAll(String page, String pageSize) throws ServiceValidationException;

    List<OrderDto> findByUserId(String userId, String page, String pageSize) throws ServiceValidationException;
}
