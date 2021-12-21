package com.epam.esm.hateoas;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;

import java.util.List;

public interface CertificateResource {

    Resource<CertificateDto> getOne(CertificateDto certificateDto)
            throws ServiceValidationException, ServiceSearchException;

    Resource<List<Resource<CertificateDto>>> getAll(List<CertificateDto> certificateDtoList,
                                                    String page,
                                                    String pageSize)
            throws ServiceValidationException, ServiceSearchException;

    Resource<List<Resource<CertificateDto>>> getByParameters(List<CertificateDto> certificateDtoList,
                                                             List<String> tagNames,
                                                             String part,
                                                             String sort,
                                                             String page,
                                                             String pageSize)
            throws ServiceValidationException, ServiceSearchException;

    Resource<CertificateDto> delete(CertificateDto certificateDto)
            throws ServiceValidationException, ServiceSearchException;
}
