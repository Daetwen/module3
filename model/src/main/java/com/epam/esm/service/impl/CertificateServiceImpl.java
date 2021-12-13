package com.epam.esm.service.impl;

import com.epam.esm.constant.ParameterName;
import com.epam.esm.constant.FindRequest;
import com.epam.esm.constant.LanguagePath;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateHasTagDto;
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
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final Validator validator;
    private final CertificateDao certificateDao;
    private final TagDao tagDao;
    private final LocaleManager localeManager;
    private final CertificateConverter certificateConverter;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao, Validator validator,
                                  LocaleManager localeManager, CertificateConverter certificateConverter) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.validator = validator;
        this.localeManager = localeManager;
        this.certificateConverter = certificateConverter;
    }

    @Override
    @Transactional
    public int create(CertificateDto certificateDto) {
        int countOfCreation = 0;
        if (validator.isValidCertificate(certificateDto)) {
            certificateDto.setCreateDate(OffsetDateTime.now());
            certificateDto.setLastUpdateDate(OffsetDateTime.now());
            countOfCreation = certificateDao.create(
                    certificateConverter.convertCertificateDtoToCertificate(certificateDto));
        }
        return countOfCreation;
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
        int countOfPages = getCountOfPages(pageSize);
        List<CertificateDto> certificateDtoList = new ArrayList<>();
        if (Integer.parseInt(page) <= countOfPages) {
            for (Certificate element : certificateDao.findAll(Integer.parseInt(page), Integer.parseInt(pageSize))) {
                certificateDtoList.add(certificateConverter.convertCertificateToCertificateDto(element));
            }
        }
        return certificateDtoList;
    }

//    @Override
//    public List<CertificateDto> findByParameters(Map<String, String> parameters, String page, String pageSize)
//            throws ServiceValidationException {
//        List<CertificateDto> certificateDtoList = new ArrayList<>();
//        return appendTags(certificateDtoList, page, pageSize);
//    }
//
//    @Override
//    public int update(CertificateDto certificateDto) {
//        int resultCountOfUpdate = 0;
//        if (validator.isValidCertificateDto(certificateDto)) {
//            resultCountOfUpdate = certificateDao.update(
//                    CertificateService.super.convertCertificateDtoToCertificate(certificateDto));
//        }
//        return resultCountOfUpdate;
//    }
//
//    @Override
//    public int updateAddTagToCertificate(CertificateHasTagDto certificateHasTagDto)
//            throws ServiceValidationException {
//        int resultCountOfUpdate = 0;
//        return resultCountOfUpdate;
//    }

    @Override
    @Transactional
    public int deleteById(String id) throws ServiceValidationException {
        validator.validateId(id);
        return certificateDao.deleteById(Long.parseLong(id));
    }

//    @Override
//    public int deleteTagFromCertificate(CertificateHasTagDto certificateHasTagDto)
//            throws ServiceValidationException {
//        String certificateId = certificateHasTagDto.getCertificateId();
//        String tagId = certificateHasTagDto.getTagId();
//        int resultCountOfDeletes = 0;
//        validator.validateId(certificateId);
//        validator.validateId(tagId);
//        Long localCertificateId = Long.parseLong(certificateId);
//        Long localTagId = Long.parseLong(tagId);
//        resultCountOfDeletes =
//                certificateDao.deleteTagFromCertificate(localCertificateId, localTagId);
//        return resultCountOfDeletes;
//    }
//
//    private StringBuilder createRequest(Map<String, String> parameters) {
//        String union = "UNION ";
//        StringBuilder request = new StringBuilder();
//        request.append(getRequestWithTagName(parameters))
//                .append(getRequestWithNameOrDescription(parameters, request, union))
//                .append(getRequestWithSort(parameters));
//        return request;
//    }

    private int getCountOfPages(String pageSize) {
        int countOfRecords = certificateDao.findCountOfRecords();
        int size = Integer.parseInt(pageSize);
        int countPages = countOfRecords % size == 0 ? countOfRecords / size : countOfRecords / size + 1;
        return countPages == 0 ? 1 : countPages;
    }

    private CertificateDto checkCertificate(Certificate certificate)
            throws ServiceSearchException {
        if (certificate != null) {
            return certificateConverter.convertCertificateToCertificateDto(certificate);
        } else {
            throw new ServiceSearchException(
                    localeManager.getLocalizedMessage(LanguagePath.ERROR_NOT_FOUND));
        }
    }

//    private String getRequestWithTagName(Map<String, String> parameters) {
//        return parameters.containsKey(ParameterName.TAG_NAME.name()) ?
//                FindRequest.FIND_CERTIFICATE_BY_TAG_NAME :
//                "";
//    }
//
//    private String getRequestWithNameOrDescription(Map<String, String> parameters,
//                                                StringBuilder request,
//                                                String mergeOperation) {
//        StringBuilder localRequest = new StringBuilder();
//        if (parameters.containsKey(ParameterName.NAME_OR_DESC_PART.name())) {
//            if (request.length() == 0) {
//                localRequest.append(FindRequest.FIND_CERTIFICATE_BY_PART_OF_NAME_OR_DESCRIPTION);
//            } else {
//                localRequest.append(mergeOperation)
//                        .append(FindRequest.FIND_CERTIFICATE_BY_PART_OF_NAME_OR_DESCRIPTION);
//            }
//        }
//        return localRequest.toString();
//    }
//
//    private String getRequestWithSort(Map<String, String> parameters) {
//        return parameters.containsKey(ParameterName.SORT.name()) ?
//                FindRequest.SORT_BY_NAME + parameters.get(ParameterName.SORT.name()) :
//                "";
//    }
//
//    private Object[] createArguments(Map<String, String> parameters) {
//        List<Object> arguments = new ArrayList<>();
//        getArgumentWithTagName(parameters, arguments);
//        getArgumentWithNameOrDescription(parameters, arguments);
//        return getFinalArgumentsForSearch(arguments);
//    }
//
//    private void getArgumentWithTagName(Map<String, String> parameters, List<Object> arguments) {
//        if (parameters.containsKey(ParameterName.TAG_NAME.name())) {
//            arguments.add(parameters.get(ParameterName.TAG_NAME.name()));
//        }
//    }
//
//    private void getArgumentWithNameOrDescription(Map<String, String> parameters, List<Object> arguments) {
//        if (parameters.containsKey(ParameterName.NAME_OR_DESC_PART.name())) {
//            parameters.computeIfPresent(ParameterName.NAME_OR_DESC_PART.name(),
//                    (key, value) -> value = "%" + value + "%");
//            arguments.add(parameters.get(ParameterName.NAME_OR_DESC_PART.name()));
//            arguments.add(parameters.get(ParameterName.NAME_OR_DESC_PART.name()));
//        }
//    }
//
//    private Object[] getFinalArgumentsForSearch(List<Object> arguments) {
//        Object[] argumentsResult = new Object[arguments.size()];
//        for (int i = 0; i < arguments.size(); i++) {
//            argumentsResult[i] = arguments.get(i);
//        }
//        return argumentsResult;
//    }
//
//    private List<CertificateDto> covertListOfCertificateToCertificateDto(List<Certificate> certificates) {
//        List<CertificateDto> certificateDtoList = new ArrayList<>();
//        for (Certificate element : certificates) {
//            certificateDtoList.add(certificateConverter.convertCertificateToCertificateDto(element));
//        }
//        return certificateDtoList;
//    }
}
