package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_PAGE_SIZE = "10";
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value="/order_create",method = RequestMethod.POST)
    public int create(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "certificateId") String certificateId)
            throws ServiceValidationException {
        return orderService.create(userId, certificateId);
    }

    @GetMapping
    public List<OrderDto> getAll(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) String page,
            @RequestParam(value = "pageSize", required = false, defaultValue = DEFAULT_PAGE_SIZE) String pageSize)
            throws ServiceValidationException {
        return orderService.findAll(page, pageSize);
    }

    @GetMapping("/{id}")
    public OrderDto getOne(@PathVariable String id)
            throws ServiceSearchException, ServiceValidationException {
        return orderService.findById(id);
    }

    @GetMapping("/user/{id}")
    public List<OrderDto> getByUserId (
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) String page,
            @RequestParam(value = "pageSize", required = false, defaultValue = DEFAULT_PAGE_SIZE) String pageSize,
            @PathVariable String id)
            throws ServiceValidationException {
        return orderService.findByUserId(id, page, pageSize);
    }
}
