package com.epam.esm.service.impl;

import com.epam.esm.constant.LanguagePath;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.ServiceSearchException;
import com.epam.esm.exception.ServiceValidationException;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final CertificateDao certificateDao;
    private final UserDao userDao;
    private final Validator validator;
    private final LocaleManager localeManager;
    private final UserConverter userConverter;
    private final CertificateConverter certificateConverter;
    private final OrderConverter orderConverter;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, CertificateDao certificateDao, UserDao userDao,
                            Validator validator, LocaleManager localeManager, UserConverter userConverter,
                            CertificateConverter certificateConverter, OrderConverter orderConverter) {
        this.orderDao = orderDao;
        this.certificateDao = certificateDao;
        this.userDao = userDao;
        this.validator = validator;
        this.localeManager = localeManager;
        this.userConverter = userConverter;
        this.certificateConverter = certificateConverter;
        this.orderConverter = orderConverter;
    }

    @Override
    @Transactional
    public OrderDto create(String userId, String certificateId)
            throws ServiceValidationException, ServiceSearchException {
        Order order = null;
        validator.validateId(userId);
        validator.validateId(certificateId);
        if (validator.isUserExist(Long.parseLong(userId))
                && validator.isCertificateExist(Long.parseLong(certificateId))) {
            OrderDto orderDto = new OrderDto();
            orderDto.setCreateDate(OffsetDateTime.now());
            orderDto.setUser(userConverter.convertUserToUserDto(userDao.findById(Long.parseLong(userId))));
            orderDto.setCertificate(certificateConverter.convertCertificateToCertificateDto(
                            certificateDao.findById(Long.parseLong(certificateId))));
            orderDto.setPrice(certificateDao.findById(Long.parseLong(certificateId)).getPrice());
            order = orderDao.create(orderConverter.convertOrderDtoToOrder(orderDto));
        }
        return checkOrder(order);
    }

    @Override
    public OrderDto findById(String id) throws ServiceSearchException, ServiceValidationException {
        validator.validateId(id);
        Order result = orderDao.findById(Long.parseLong(id));
        return checkOrder(result);
    }

    @Override
    public List<OrderDto> findAll(String page, String pageSize)
            throws ServiceValidationException {
        validator.validatePage(page);
        validator.validatePage(pageSize);
        List<OrderDto> orderDtoList = new ArrayList<>();
        if (Integer.parseInt(page) <= getCountOfPages(pageSize)) {
            for (Order element : orderDao.findAll(Integer.parseInt(page), Integer.parseInt(pageSize))) {
                orderDtoList.add(orderConverter.convertOrderToOrderDto(element));
            }
        }
        return orderDtoList;
    }

    @Override
    public List<OrderDto> findByUserId(String userId, String page, String pageSize)
            throws ServiceValidationException {
        validator.validateId(userId);
        validator.validatePage(page);
        validator.validatePage(pageSize);
        List<OrderDto> orderDtoList = new ArrayList<>();
        if (Integer.parseInt(page) <= getCountOfPagesOfUserOrders(userId, pageSize)) {
            List<Order> resultOrders = orderDao.findByUserId(
                    Long.parseLong(userId),
                    Integer.parseInt(page),
                    Integer.parseInt(pageSize));
            for (Order element : resultOrders) {
                orderDtoList.add(orderConverter.convertOrderToOrderDto(element));
            }
        }
        return orderDtoList;
    }

    private OrderDto checkOrder(Order order)
            throws ServiceSearchException {
        if (order != null) {
            return orderConverter.convertOrderToOrderDto(order);
        } else {
            throw new ServiceSearchException(
                    localeManager.getLocalizedMessage(LanguagePath.ERROR_NOT_FOUND));
        }
    }

    private int getCountOfPages(String pageSize) throws ServiceValidationException {
        validator.validatePage(pageSize);
        int countOfRecords = orderDao.findCountOfRecords();
        int size = Integer.parseInt(pageSize);
        int countPages = countOfRecords % size == 0 ? countOfRecords / size : countOfRecords / size + 1;
        return countPages == 0 ? 1 : countPages;
    }

    private int getCountOfPagesOfUserOrders(String id, String pageSize) {
        int countOfRecords = orderDao.findCountOfRecordsByUserId(Long.parseLong(id));
        int size = Integer.parseInt(pageSize);
        int countPages = countOfRecords % size == 0 ? countOfRecords / size : countOfRecords / size + 1;
        return countPages == 0 ? 1 : countPages;
    }
}
