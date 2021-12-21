package com.epam.esm.hateoas;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ControllerException;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;

import java.util.List;

public interface TagResource {

    Resource<TagDto> getOne(TagDto tagDto)
            throws ServiceValidationException, ServiceSearchException, ControllerException;

    Resource<List<Resource<TagDto>>> getAll(List<TagDto> tagDtoList, String page, String pageSize)
            throws ServiceValidationException, ServiceSearchException, ControllerException;

    Resource<TagDto> delete(TagDto tagDto)
            throws ServiceValidationException, ServiceSearchException, ControllerException;
}
