package com.epam.esm.controller;

import com.epam.esm.constant.ParameterName;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateHasTagDto;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;
import com.epam.esm.service.CertificateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_PAGE_SIZE = "10";
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @RequestMapping(value="/certificate_create",method = RequestMethod.POST)
    public int create(@RequestBody CertificateDto certificateDto) {
        return certificateService.create(certificateDto);
    }

    @GetMapping
    public List<CertificateDto> getAll(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) String page,
            @RequestParam(value = "pageSize", required = false, defaultValue = DEFAULT_PAGE_SIZE) String pageSize)
            throws ServiceValidationException {
        return certificateService.findAll(page, pageSize);
    }

    @GetMapping("/{id}")
    public CertificateDto getOne(@PathVariable String id)
            throws ServiceSearchException, ServiceValidationException {
        return certificateService.findById(id);
    }

//    @GetMapping("/certificate_parameters")
//    public List<CertificateDto> getSomeByParameters(
//            @RequestParam(value = "tagName", required = false) String tagName,
//            @RequestParam(value = "part", required = false) String part,
//            @RequestParam(value = "sort", required = false) String sort) {
//        Map<String, String> parameters = new HashMap<>();
//        collectParameters(parameters, ParameterName.TAG_NAME, tagName);
//        collectParameters(parameters, ParameterName.NAME_OR_DESC_PART, part);
//        collectParameters(parameters, ParameterName.SORT, sort);
//        return certificateService.findByParameters(parameters);
//    }
//
//    @RequestMapping(value="/certificate_update", method = RequestMethod.PATCH)
//    public int update(@RequestBody CertificateDto certificateDto) {
//        return certificateService.update(certificateDto);
//    }
//
//    @RequestMapping(value="/certificate_update_tag", method = RequestMethod.PUT)
//    public int updateTagFromCertificate(@RequestBody CertificateHasTagDto certificateHasTagDto)
//            throws ServiceValidationException {
//        return certificateService.updateAddTagToCertificate(certificateHasTagDto);
//    }

    @RequestMapping(value="/certificate_delete/{id}", method = RequestMethod.DELETE)
    public int delete(@PathVariable String id)
            throws ServiceValidationException {
        return certificateService.deleteById(id);
    }

//    @RequestMapping(value="/certificate_delete_tag", method = RequestMethod.DELETE)
//    public int deleteTagFromCertificate(@RequestBody CertificateHasTagDto certificateHasTagDto)
//            throws ServiceValidationException {
//        return certificateService.deleteTagFromCertificate(certificateHasTagDto);
//    }
//
//    private void collectParameters(Map<String, String> parameters,
//                                   ParameterName parameterName,
//                                   String parameter) {
//        if (StringUtils.isNotBlank(parameter)) {
//            parameters.put(parameterName.name(), parameter);
//        }
//    }
}
