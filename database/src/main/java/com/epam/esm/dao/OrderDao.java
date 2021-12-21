package com.epam.esm.dao;

import com.epam.esm.entity.Order;

import java.util.List;

/**
 * The interface Order dao.
 */
public interface OrderDao {

    /**
     * Create order.
     *
     * @param order the order to create
     * @return the created order
     */
    Order create(Order order);

    /**
     * Find by id one order.
     *
     * @param id the id for search
     * @return the result of search
     */
    Order findById(Long id);

    /**
     * Find all orders.
     *
     * @param page     the page
     * @param pageSize the page size
     * @return the page of all orders in the database
     */
    List<Order> findAll(Integer page, Integer pageSize);

    /**
     * Find count of order records.
     *
     * @return the int number of order records
     */
    int findCountOfRecords();

    /**
     * Find orders by user id.
     *
     * @param userId   the user id
     * @param page     the page
     * @param pageSize the page size
     * @return the page of all orders in the database for user
     */
    List<Order> findByUserId(Long userId, Integer page, Integer pageSize);

    /**
     * Find count of order records by user id.
     *
     * @param userId the user id
     * @return the int number of order records
     */
    int findCountOfRecordsByUserId(Long userId);
}
