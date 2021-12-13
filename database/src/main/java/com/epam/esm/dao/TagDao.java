package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;

/**
 * The interface Tag dao.
 */
public interface TagDao {

    /**
     * Create one tag.
     *
     * @param tag the tag to create
     * @return the int number of created elements
     */
    int create(Tag tag);

    /**
     * Find by id one tag.
     *
     * @param id the id for search
     * @return the result of search
     */
    Tag findById(Long id);

    /**
     * Find by name one tag.
     *
     * @param name the name for search
     * @return the result of search
     */
    Tag findByName(String name);

    /**
     * Find all tags.
     *
     * @param page     the current page
     * @param pageSize the page size
     * @return the list of all tags in the database
     */
    List<Tag> findAll(Integer page, Integer pageSize);

    /**
     * Delete tags by id.
     *
     * @param id the tag id
     * @return number of tags removed
     */
    int deleteById(Long id);


    /**
     * Find count of tag records.
     *
     * @return the int number of records
     */
    int findCountOfRecords();
}
