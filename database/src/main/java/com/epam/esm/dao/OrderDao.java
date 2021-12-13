package com.epam.esm.dao;

import com.epam.esm.entity.Order;

import java.util.List;

public interface OrderDao {

    int create(Order order);

    Order findById(Long id);

    List<Order> findAll(Integer page, Integer pageSize);

    int findCountOfRecords();

    List<Order> findByUserId(Long userId, Integer page, Integer pageSize);

    int findCountOfRecordsByUserId(Long userId);
}
