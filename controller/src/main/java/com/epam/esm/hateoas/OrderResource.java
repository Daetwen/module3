package com.epam.esm.hateoas;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;

import java.util.List;

public interface OrderResource {

    Resource<OrderDto> getOne(OrderDto orderDto)
            throws ServiceValidationException, ServiceSearchException;

    Resource<List<Resource<OrderDto>>> getAll(List<OrderDto> orderDtoList, String page, String pageSize)
            throws ServiceValidationException, ServiceSearchException;
}
