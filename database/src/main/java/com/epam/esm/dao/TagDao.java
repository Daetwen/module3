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
     * @return the created tag
     */
    Tag create(Tag tag);

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
     * Find tags by certificate id.
     *
     * @param certificateId the certificate id
     * @return the page of all tags for certificate
     */
    List<Tag> findByCertificateId(Long certificateId);

    /**
     * Find all tags.
     *
     * @param page     the current page
     * @param pageSize the page size
     * @return the page of all tags in the database
     */
    List<Tag> findAll(Integer page, Integer pageSize);

    /**
     * Find most popular tag by count of use and price of certificates.
     *
     * @return the most popular tag
     */
    Tag findMostPopular();

    /**
     * Delete tag by id.
     *
     * @param id the tag id
     * @return the deleted tag
     */
    Tag deleteById(Long id);


    /**
     * Find count of tag records.
     *
     * @return the int number of tag records
     */
    int findCountOfRecords();
}
