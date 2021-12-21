package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.List;

/**
 * The interface User dao.
 */
public interface UserDao {

    /**
     * Find by user id.
     *
     * @param id the id for search
     * @return the result of search
     */
    User findById(Long id);

    /**
     * Find all users.
     *
     * @param page     the current page
     * @param pageSize the page size
     * @return the page of all users in the database
     */
    List<User> findAll(Integer page, Integer pageSize);

    /**
     * Find count of user records.
     *
     * @return the int number of user records
     */
    int findCountOfRecords();
}
