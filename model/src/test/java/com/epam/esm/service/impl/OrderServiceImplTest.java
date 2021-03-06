package com.epam.esm.service.impl;

import com.epam.esm.builder.CertificateBuilder;
import com.epam.esm.builder.CertificateDtoBuilder;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceImplTest {

    private OrderDao orderDao;
    private CertificateDao certificateDao;
    private UserDao userDao;
    private Validator validator;
    private UserConverter userConverter;
    private CertificateConverter certificateConverter;
    private OrderConverter orderConverter;
    private OrderService orderService;
    private OrderDto orderDtoTest1;
    private Order orderTest1;
    private UserDto userDto;
    private User user;
    private Certificate certificateTest1;
    private CertificateDto certificateDtoTest1;

    @BeforeAll
    public void setUp() {
        orderDao = mock(OrderDao.class);
        certificateDao = mock(CertificateDao.class);
        userDao = mock(UserDao.class);
        validator = mock(Validator.class);
        LocaleManager localeManager = mock(LocaleManager.class);
        userConverter = mock(UserConverter.class);
        certificateConverter = mock(CertificateConverter.class);
        orderConverter = mock(OrderConverter.class);
        orderService = new OrderServiceImpl(orderDao, certificateDao, userDao, validator,
                localeManager, userConverter, certificateConverter, orderConverter);

        List<Order> orderList = new ArrayList<>();
        userDto = new UserDto(5L, "Vlad", "Makarei");
        user = new User(5L, "Vlad", "Makarei", orderList);

        List<TagDto> tagDtoList = new ArrayList<>();
        List<Tag> tagList = new ArrayList<>();

        CertificateDtoBuilder certificateDtoBuilder = new CertificateDtoBuilder();
        certificateDtoBuilder.setId(2L);
        certificateDtoBuilder.setName("Name");
        certificateDtoBuilder.setDescription("Description");
        certificateDtoBuilder.setPrice(new BigDecimal(5000));
        certificateDtoBuilder.setDuration(100);
        certificateDtoBuilder.setCreateDate(OffsetDateTime.parse("2021-11-20T21:30:19+03:00"));
        certificateDtoBuilder.setLastUpdateDate(OffsetDateTime.parse("2021-11-22T21:25:37+03:00"));
        certificateDtoTest1 = certificateDtoBuilder.build();
        certificateDtoTest1.setTags(tagDtoList);

        CertificateBuilder certificateBuilder = new CertificateBuilder();
        certificateBuilder.setId(2L);
        certificateBuilder.setName("Name");
        certificateBuilder.setDescription("Description");
        certificateBuilder.setPrice(new BigDecimal(5000));
        certificateBuilder.setDuration(100);
        certificateBuilder.setCreateDate(OffsetDateTime.parse("2021-11-20T21:30:19+03:00"));
        certificateBuilder.setLastUpdateDate(OffsetDateTime.parse("2021-11-22T21:25:37+03:00"));
        certificateTest1 = certificateBuilder.build();
        certificateTest1.setTags(tagList);

        orderDtoTest1 = new OrderDto(5L, new BigDecimal(5000),
                OffsetDateTime.parse("2021-11-20T21:30:19+03:00"), userDto, certificateDtoTest1);
        orderTest1 = new Order(5L, new BigDecimal(5000),
                OffsetDateTime.parse("2021-11-20T21:30:19+03:00"), user, certificateTest1);
    }

    @Test
    public void createTestTrue() throws ServiceValidationException, ServiceSearchException {
        OrderDto expected = orderDtoTest1;
        doNothing().when(validator).validateId(anyString());
        when(validator.isUserExist(anyLong())).thenReturn(true);
        when(validator.isCertificateExist(anyLong())).thenReturn(true);
        when(userDao.findById(anyLong())).thenReturn(user);
        when(userConverter.convertUserToUserDto(any(User.class))).thenReturn(userDto);
        when(certificateDao.findById(anyLong())).thenReturn(certificateTest1);
        when(certificateConverter.convertCertificateToCertificateDto(any(Certificate.class)))
                .thenReturn(certificateDtoTest1);
        when(orderConverter.convertOrderDtoToOrder(any(OrderDto.class))).thenReturn(orderTest1);
        when(orderDao.create(any(Order.class))).thenReturn(orderTest1);
        OrderDto actual = orderService.create("5", "5");
        assertEquals(expected, actual);
    }

    @Test
    public void createTestFalse1() throws ServiceValidationException {
        doThrow(ServiceValidationException.class).when(validator).validateId(anyString());
        assertThrows(ServiceValidationException.class,
                () -> orderService.create("5gf", "5"));
    }

    @Test
    public void createTestFalse2() throws ServiceValidationException {
        doNothing().when(validator).validateId(anyString());
        when(validator.isUserExist(anyLong())).thenReturn(false);
        assertThrows(ServiceSearchException.class,
                () -> orderService.create("5", "5"));
    }

    @Test
    public void createTestFalse3() throws ServiceValidationException {
        doNothing().when(validator).validateId(anyString());
        when(validator.isUserExist(anyLong())).thenReturn(true);
        when(validator.isCertificateExist(anyLong())).thenReturn(false);
        assertThrows(ServiceSearchException.class,
                () -> orderService.create("5", "5"));
    }

    @Test
    public void findByIdTestTrue() throws ServiceValidationException, ServiceSearchException {
        doNothing().when(validator).validateId(anyString());
        when(orderDao.findById(anyLong())).thenReturn(orderTest1);
        when(orderConverter.convertOrderToOrderDto(any(Order.class))).thenReturn(orderDtoTest1);
        OrderDto actual = orderService.findById("5");
        assertEquals(orderDtoTest1, actual);
    }

    @Test
    public void findByIdTestFalse1() throws ServiceValidationException {
        doThrow(ServiceValidationException.class).when(validator).validateId(anyString());
        assertThrows(ServiceValidationException.class,
                () -> orderService.findById("f5g"));
    }

    @Test
    public void findByIdTestFalse2() throws ServiceValidationException {
        doNothing().when(validator).validateId(anyString());
        when(orderDao.findById(anyLong())).thenReturn(null);
        assertThrows(ServiceSearchException.class,
                () -> orderService.findById("6"));
    }

    @Test
    public void findAllTestTrue() throws ServiceValidationException {
        List<OrderDto> expected = new ArrayList<>();
        expected.add(orderDtoTest1);
        List<Order> listForWork = new ArrayList<>();
        listForWork.add(orderTest1);
        doNothing().when(validator).validatePage(anyString());
        when(orderDao.findAll(anyInt(), anyInt())).thenReturn(listForWork);
        when(orderConverter.convertOrderToOrderDto(any(Order.class))).thenReturn(orderDtoTest1);
        List<OrderDto> actual = orderService.findAll("1", "10");
        assertEquals(expected, actual);
    }

    @Test
    public void findAllTestFalse1() throws ServiceValidationException {
        doThrow(ServiceValidationException.class).when(validator).validatePage(anyString());
        assertThrows(ServiceValidationException.class,
                () -> orderService.findAll("1", "10"));
    }
}
