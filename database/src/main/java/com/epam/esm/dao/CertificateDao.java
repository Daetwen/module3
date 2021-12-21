package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import java.util.List;
import java.util.Map;

/**
 * The interface Certificate dao.
 */
public interface CertificateDao {

    /**
     * Create one certificate.
     *
     * @param certificate the certificate to create
     * @return the created certificate or null
     */
    Certificate create(Certificate certificate);

    /**
     * Find by id one certificate.
     *
     * @param id the id for search
     * @return the founded certificate or null
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

    /**
     * Find all certificates with the required arguments.
     *
     * @param parameters the parameters map for search
     * @param page       the current page
     * @param pageSize   the page size
     * @return the list of founded certificates
     */
    List<Certificate> findByParameters(Map<String, String> parameters, Integer page, Integer pageSize);

    /**
     * Update one certificate.
     *
     * @param certificate the certificate from which the parameters for the update will be taken
     * @return renewed certificate
     */
    Certificate update(Certificate certificate);

    /**
     * Delete certificate by id.
     *
     * @param id the id of certificate for deleting
     * @return the deleted certificate
     */
    Certificate deleteById(Long id);

    /**
     * Find count of certificate records.
     *
     * @return the number of all certificate records
     */
    int findCountOfRecords();
}
