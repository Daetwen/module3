package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ControllerException;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.ControllerValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_PAGE_SIZE = "10";
    private final TagService tagService;
    private final ControllerValidator controllerValidator;

    @Autowired
    public TagController(TagService tagService, ControllerValidator controllerValidator) {
        this.tagService = tagService;
        this.controllerValidator = controllerValidator;
    }

    @RequestMapping(value="/tag_create",method = RequestMethod.POST)
    public int create(@RequestBody TagDto tagDto) {
        return tagService.create(tagDto);
    }

    @GetMapping
    public List<TagDto> getAll(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) String page,
            @RequestParam(value = "pageSize", required = false, defaultValue = DEFAULT_PAGE_SIZE) String pageSize)
            throws ServiceValidationException {
        return tagService.findAll(page, pageSize);
    }

    @GetMapping("/tag")
    @Description("For lookups, id takes precedence over multiple passed parameters.")
    public TagDto getOne(@RequestParam(value = "id", required = false) String id,
                         @RequestParam(value = "name", required = false) String name)
            throws ControllerException, ServiceSearchException, ServiceValidationException {
        TagDto tagDto = new TagDto();
        controllerValidator.validateParameters(id, name);
        if (StringUtils.isNotBlank(id)) {
            tagDto = tagService.findById(id);
        } else if (StringUtils.isNotBlank(name)) {
            tagDto = tagService.findByName(name);
        }
        return tagDto;
    }

    @RequestMapping(value="/tag_delete/{id}",method = RequestMethod.DELETE)
    public int delete(@PathVariable String id) throws ServiceValidationException {
        return tagService.deleteById(id);
    }
}