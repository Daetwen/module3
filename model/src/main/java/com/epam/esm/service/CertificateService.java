package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;

import java.util.List;

/**
 * The interface Certificate service.
 */
public interface CertificateService {

    /**
     * Create one certificate.
     *
     * @param certificateDto the certificate dto to create
     * @return the int number of created elements
     */
    int create(CertificateDto certificateDto);

    /**
     * Find by id one certificate dto.
     *
     * @param id the id for search
     * @return the certificate dto
     * @throws ServiceSearchException     the service search exception
     * @throws ServiceValidationException the service validation exception
     */
    CertificateDto findById(String id) throws ServiceSearchException, ServiceValidationException;

    /**
     * Find all certificates.
     *
     * @param page     the page
     * @param pageSize the page size
     * @return the list of all certificates in the database
     * @throws ServiceValidationException the service validation exception
     */
    List<CertificateDto> findAll(String page, String pageSize) throws ServiceValidationException;
//
//    /**
//     * Find all certificates with the required parameters.
//     *
//     * @param parameters the parameters for search and sort
//     * @return the list of found certificates
//     */
//    List<CertificateDto> findByParameters(Map<String, String> parameters);
//
//    /**
//     * pdate one certificate.
//     *
//     * @param certificateDto the certificate dto from which the parameters for the update will be taken
//     * @return number of renewed certificates
//     */
//    int update(CertificateDto certificateDto);
//
//    /**
//     * Update create one certificate link with a tag.
//     *
//     * @param certificateHasTagDto the certificate has tag dto with parameters for creation
//     * @return number of links created
//     * @throws ServiceValidationException the service validation exception
//     */
//    int updateAddTagToCertificate(CertificateHasTagDto certificateHasTagDto)
//            throws ServiceValidationException;

    /**
     * Delete certificate by id.
     *
     * @param id the id
     * @return number of certificates removed
     * @throws ServiceValidationException the service validation exception
     */
    int deleteById(String id) throws ServiceValidationException;
//
//    /**
//     * Delete one certificate link with a tag.
//     *
//     * @param certificateHasTagDto the certificate has tag dto with parameters for removing
//     * @return number of removed links
//     * @throws ServiceValidationException the service validation exception
//     */
//    int deleteTagFromCertificate(CertificateHasTagDto certificateHasTagDto)
//            throws ServiceValidationException;
}
