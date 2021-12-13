package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import java.util.List;

/**
 * The interface Certificate dao.
 */
public interface CertificateDao {

    /**
     * Create one certificate.
     *
     * @param certificate the certificate to create
     * @return the int number of created elements
     */
    int create(Certificate certificate);

    /**
     * Find by id one certificate.
     *
     * @param id the id for search
     * @return the optional result as there may not be a record in the database
     */
    Certificate findById(Long id);

    /**
     * Find all certificates.
     *
     * @param page     the current page
     * @param pageSize the page size
     * @return the list of all certificates in the database
     */
    List<Certificate> findAll(Integer page, Integer pageSize);

//    /**
//     * Find all certificates with the required arguments.
//     *
//     * @param parameters the query string generated in the service
//     * @param arguments  an array of arguments by which the search will be performed
//     * @return the list of found certificates
//     */
//    List<Certificate> findByParameters(String parameters, Object... arguments);
//
//    /**
//     * Update one certificate.
//     *
//     * @param certificate the certificate from which the parameters for the update will be taken
//     * @return number of renewed certificates
//     */
//    int update(Certificate certificate);
//
//    /**
//     * Update create one certificate link with a tag.
//     *
//     * @param certificateId the certificate id
//     * @param tagId         the tag id
//     * @return number of links created
//     */
//    int updateAddTagToCertificate(Long certificateId, Long tagId);

    /**
     * Delete certificate by id.
     *
     * @param id the id of certificate for deleting
     * @return number of certificates removed
     */
    int deleteById(Long id);

//    /**
//     * Delete one certificate link with a tag.
//     *
//     * @param certificateId the certificate id
//     * @param tagId         the tag id
//     * @return number of remote links
//     */
//    int deleteTagFromCertificate(Long certificateId, Long tagId);

    /**
     * Find count of certificate records.
     *
     * @return the number of all certificate records
     */
    int findCountOfRecords();
}
