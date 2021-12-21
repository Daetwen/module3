package com.epam.esm.service.impl;

import com.epam.esm.constant.LanguagePath;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.LocaleManager;
import com.epam.esm.util.TagConverter;
import com.epam.esm.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagConverter tagConverter;
    private final Validator validator;
    private final TagDao tagDao;
    private final LocaleManager localeManager;

    @Autowired
    public TagServiceImpl(TagDao tagDao, Validator validator,
                          TagConverter tagConverter, LocaleManager localeManager) {
        this.tagDao = tagDao;
        this.validator = validator;
        this.tagConverter = tagConverter;
        this.localeManager = localeManager;
    }

    @Override
    @Transactional
    public TagDto create(TagDto tagDto) throws ServiceSearchException {
        Tag createdTag = null;
        if (validator.isValidTag(tagDto)) {
            createdTag = tagDao.create(tagConverter.convertTagDtoToTag(tagDto));
        }
        return checkTag(createdTag);
    }

    @Override
    public TagDto findById(String id) throws ServiceSearchException, ServiceValidationException {
        validator.validateId(id);
        Tag result = tagDao.findById(Long.parseLong(id));
        return checkTag(result);
    }

    @Override
    public TagDto findByName(String name)
            throws ServiceSearchException, ServiceValidationException {
        validator.validateName(name);
        Tag result = tagDao.findByName(name);
        return checkTag(result);
    }

    @Override
    public List<TagDto> findAll(String page, String pageSize)
            throws ServiceValidationException {
        validator.validatePage(page);
        validator.validatePage(pageSize);
        List<TagDto> tagDtoList = new ArrayList<>();
        if (Integer.parseInt(page) <= getCountOfPages(pageSize)) {
            for (Tag element : tagDao.findAll(Integer.parseInt(page), Integer.parseInt(pageSize))) {
                tagDtoList.add(tagConverter.convertTagToTagDto(element));
            }
        }
        return tagDtoList;
    }

    @Override
    public TagDto findMostPopular() throws ServiceSearchException {
        Tag result = tagDao.findMostPopular();
        return checkTag(result);
    }

    @Override
    @Transactional
    public TagDto deleteById(String id) throws ServiceValidationException, ServiceSearchException {
        validator.validateId(id);
        Tag result = tagDao.deleteById(Long.parseLong(id));
        return checkTag(result);
    }

    private TagDto checkTag(Tag tag) throws ServiceSearchException {
        if (tag != null) {
            return tagConverter.convertTagToTagDto(tag);
        } else {
            throw new ServiceSearchException(
                    localeManager.getLocalizedMessage(LanguagePath.ERROR_NOT_FOUND));
        }
    }

    private int getCountOfPages(String pageSize) {
        int countOfRecords = tagDao.findCountOfRecords();
        int size = Integer.parseInt(pageSize);
        int countPages = countOfRecords % size == 0 ? countOfRecords / size : countOfRecords / size + 1;
        return countPages == 0 ? 1 : countPages;
    }
}
