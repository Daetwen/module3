package com.epam.esm.service.impl;

import com.epam.esm.constant.LanguagePath;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.CertificateConverter;
import com.epam.esm.util.LocaleManager;
import com.epam.esm.util.TagConverter;
import com.epam.esm.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final Validator validator;
    private final CertificateDao certificateDao;
    private final TagDao tagDao;
    private final LocaleManager localeManager;
    private final CertificateConverter certificateConverter;
    private final TagConverter tagConverter;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao, Validator validator,
                                  LocaleManager localeManager, CertificateConverter certificateConverter,
                                  TagConverter tagConverter) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.validator = validator;
        this.localeManager = localeManager;
        this.certificateConverter = certificateConverter;
        this.tagConverter = tagConverter;
    }

    @Override
    @Transactional
    public CertificateDto create(CertificateDto certificateDto) throws ServiceSearchException {
        Certificate certificate = null;
        if (validator.isValidCertificate(certificateDto)) {
            certificateDto.setCreateDate(OffsetDateTime.now());
            certificateDto.setLastUpdateDate(OffsetDateTime.now());
            certificate = certificateDao.create(
                    certificateConverter.convertCertificateDtoToCertificate(certificateDto));
        }
        return checkCertificate(certificate);
    }

    @Override
    public CertificateDto findById(String id) throws ServiceSearchException, ServiceValidationException {
        validator.validateId(id);
        Certificate result = certificateDao.findById(Long.parseLong(id));
        return checkCertificate(result);
    }

    @Override
    public List<CertificateDto> findAll(String page, String pageSize)
            throws ServiceValidationException {
        validator.validatePage(page);
        validator.validatePage(pageSize);
        List<CertificateDto> certificateDtoList = new ArrayList<>();
        if (Integer.parseInt(page) <= getCountOfPages(pageSize)) {
            for (Certificate element : certificateDao.findAll(Integer.parseInt(page), Integer.parseInt(pageSize))) {
                certificateDtoList.add(certificateConverter.convertCertificateToCertificateDto(element));
            }
        }
        return appendTags(certificateDtoList);
    }

    @Override
    public List<CertificateDto> findByParameters(Map<String, String> parameters, String page, String pageSize)
            throws ServiceValidationException {
        validator.validatePage(page);
        validator.validatePage(pageSize);
        int currentPage = Integer.parseInt(page);
        int currentPageSize = Integer.parseInt(pageSize);
        List<CertificateDto> certificateDtoList = new ArrayList<>();
        if (currentPage <= getCountOfPages(pageSize)) {
            for (Certificate element : certificateDao.findByParameters(parameters, currentPage, currentPageSize)) {
                certificateDtoList.add(certificateConverter.convertCertificateToCertificateDto(element));
            }
        }
        return appendTags(certificateDtoList);
    }

    @Override
    @Transactional
    public CertificateDto update(CertificateDto certificateDto) throws ServiceSearchException {
        Certificate certificate = null;
        if (validator.isValidCertificateDto(certificateDto)
                && certificateDao.findById(certificateDto.getId()) != null) {
            certificate = certificateDao.findById(certificateDto.getId());
            certificate = certificateDao.update(createCertificateForUpdate(certificate, certificateDto));
        }
        return checkCertificate(certificate);
    }

    @Override
    @Transactional
    public CertificateDto deleteById(String id)
            throws ServiceValidationException, ServiceSearchException {
        validator.validateId(id);
        Certificate certificate = certificateDao.deleteById(Long.parseLong(id));
        return checkCertificate(certificate);
    }

    private int getCountOfPages(String pageSize) {
        int countOfRecords = certificateDao.findCountOfRecords();
        int size = Integer.parseInt(pageSize);
        int countPages = countOfRecords % size == 0 ? countOfRecords / size : countOfRecords / size + 1;
        return countPages == 0 ? 1 : countPages;
    }

    private CertificateDto checkCertificate(Certificate certificate)
            throws ServiceSearchException {
        if (!Objects.isNull(certificate)) {
            CertificateDto certificateDto =
                    certificateConverter.convertCertificateToCertificateDto(certificate);
            appendTagsForOne(certificateDto);
            return certificateDto;
        } else {
            throw new ServiceSearchException(
                    localeManager.getLocalizedMessage(LanguagePath.ERROR_NOT_FOUND));
        }
    }

    private List<CertificateDto> appendTags(List<CertificateDto> certificateDtoList) {
        for (CertificateDto certificateDto : certificateDtoList) {
            appendTagsForOne(certificateDto);
        }
        return certificateDtoList;
    }

    private void appendTagsForOne(CertificateDto certificateDto) {
        List<Tag> tags = tagDao.findByCertificateId(certificateDto.getId());
        List<TagDto> tagsDto = new ArrayList<>();
        for (Tag tag : tags) {
            tagsDto.add(tagConverter.convertTagToTagDto(tag));
        }
        certificateDto.setTags(tagsDto);
    }

    private Certificate createCertificateForUpdate(Certificate certificate, CertificateDto certificateDto) {
        if (validator.isValidName(certificateDto.getName())) {
            certificate.setName(certificateDto.getName());
        }
        if (validator.isValidDescription(certificateDto.getDescription())) {
            certificate.setDescription(certificateDto.getDescription());
        }
        if (certificateDto.getPrice() != null && validator.isValidPrice(certificateDto.getPrice())) {
            certificate.setPrice(certificateDto.getPrice());
        }
        if (certificateDto.getDuration() != null && validator.isValidDuration(certificateDto.getDuration())) {
            certificate.setDuration(certificateDto.getDuration());
        }
        certificate.setLastUpdateDate(OffsetDateTime.now());
        return certificate;
    }
}
