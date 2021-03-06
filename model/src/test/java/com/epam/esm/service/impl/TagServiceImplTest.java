package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.LocaleManager;
import com.epam.esm.util.TagConverter;
import com.epam.esm.util.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TagServiceImplTest {

    private TagService tagService;
    private TagConverter tagConverter;
    private Validator validator;
    private TagDao tagDao;
    private TagDto tagDtoTest1;
    private Tag tagTest1;

    @BeforeEach
    public void setUp() {
        tagDao = mock(TagDao.class);
        tagConverter = mock(TagConverter.class);
        validator = mock(Validator.class);
        LocaleManager localeManager = mock(LocaleManager.class);
        tagService = new TagServiceImpl(tagDao, validator, tagConverter, localeManager);
        tagDtoTest1 = new TagDto(5L, "Hello");
        tagTest1 = new Tag(5L, "Hello");
    }

    @Test
    public void createTestTrue() throws ServiceSearchException {
        TagDto expected = tagDtoTest1;
        when(validator.isValidTag(any(TagDto.class))).thenReturn(true);
        when(tagConverter.convertTagDtoToTag(any(TagDto.class))).thenReturn(tagTest1);
        when(tagDao.create(any(Tag.class))).thenReturn(tagTest1);
        when(tagConverter.convertTagToTagDto(any(Tag.class))).thenReturn(tagDtoTest1);
        TagDto actual = tagService.create(tagDtoTest1);
        assertEquals(expected, actual);
    }

    @Test
    public void createTestFalse1() {
        when(validator.isValidTag(any(TagDto.class))).thenReturn(false);
        assertThrows(ServiceSearchException.class,
                () -> tagService.create(tagDtoTest1));
    }

    @Test
    public void createTestFalse2() {
        when(validator.isValidTag(any(TagDto.class))).thenReturn(true);
        when(tagConverter.convertTagDtoToTag(any(TagDto.class))).thenReturn(tagTest1);
        when(tagDao.create(any(Tag.class))).thenReturn(null);
        assertThrows(ServiceSearchException.class,
                () -> tagService.create(tagDtoTest1));
    }

    @Test
    public void findByIdTestTrue() throws ServiceSearchException, ServiceValidationException {
        doNothing().when(validator).validateId(anyString());
        when(tagDao.findById(anyLong())).thenReturn(tagTest1);
        when(tagConverter.convertTagToTagDto(any(Tag.class))).thenReturn(tagDtoTest1);
        TagDto actual = tagService.findById("5");
        assertEquals(tagDtoTest1, actual);
    }

    @Test
    public void findByIdTestFalse1() throws ServiceValidationException {
        doThrow(ServiceValidationException.class).when(validator).validateId(anyString());
        assertThrows(ServiceValidationException.class,
                () -> tagService.findById("f5g"));
    }

    @Test
    public void findByIdTestFalse2() throws ServiceValidationException {
        doNothing().when(validator).validateId(anyString());
        when(tagDao.findById(anyLong())).thenReturn(null);
        assertThrows(ServiceSearchException.class,
                () -> tagService.findById("5"));
    }

    @Test
    public void findByNameTestTrue() throws ServiceSearchException, ServiceValidationException {
        doNothing().when(validator).validateName(anyString());
        when(tagDao.findByName(anyString())).thenReturn(tagTest1);
        when(tagConverter.convertTagToTagDto(any(Tag.class))).thenReturn(tagDtoTest1);
        TagDto actual = tagService.findByName("New Year");
        assertEquals(tagDtoTest1, actual);
    }

    @Test
    public void findByNameTestFalse1() throws ServiceValidationException {
        doThrow(ServiceValidationException.class).when(validator).validateName(anyString());
        assertThrows(ServiceValidationException.class,
                () -> tagService.findByName("New Year"));
    }

    @Test
    public void findByNameTestFalse2() throws ServiceValidationException {
        doNothing().when(validator).validateName(anyString());
        when(tagDao.findByName(anyString())).thenReturn(null);
        assertThrows(ServiceSearchException.class,
                () -> tagService.findByName("New Year"));
    }

    @Test
    public void findAllTestTrue() throws ServiceValidationException {
        List<TagDto> expected = new ArrayList<>();
        expected.add(tagDtoTest1);
        List<Tag> listForWork = new ArrayList<>();
        listForWork.add(tagTest1);
        when(tagDao.findAll(1, 10)).thenReturn(listForWork);
        when(tagConverter.convertTagToTagDto(any(Tag.class))).thenReturn(tagDtoTest1);
        List<TagDto> actual = tagService.findAll("1", "10");
        assertEquals(expected, actual);
    }

    @Test
    public void findAllTestFalse1() throws ServiceValidationException {
        doThrow(ServiceValidationException.class).when(validator).validatePage(anyString());
        assertThrows(ServiceValidationException.class,
                () -> tagService.findAll("1", "10"));
    }

    @Test
    public void deleteByIdTestTrue() throws ServiceValidationException, ServiceSearchException {
        TagDto expected = tagDtoTest1;
        doNothing().when(validator).validateId(anyString());
        when(tagDao.deleteById(anyLong())).thenReturn(tagTest1);
        when(tagConverter.convertTagToTagDto(any(Tag.class))).thenReturn(tagDtoTest1);
        TagDto actual = tagService.deleteById("5");
        assertEquals(expected, actual);
    }

    @Test
    public void deleteByIdTestFalse1() throws ServiceValidationException {
        doThrow(ServiceValidationException.class).when(validator).validateId(anyString());
        assertThrows(ServiceValidationException.class,
                () -> tagService.deleteById("5"));
    }

    @Test
    public void deleteByIdTestFalse2() throws ServiceValidationException {
        doNothing().when(validator).validateId(anyString());
        when(tagDao.deleteById(anyLong())).thenReturn(null);
        assertThrows(ServiceSearchException.class,
                () -> tagService.deleteById("5"));
    }
}
