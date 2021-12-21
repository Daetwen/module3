package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {

    private static final String ID_PARAMETER = "id";
    private static final String JOIN_TABLE_ORDERS = "orders";
    private final EntityManager entityManager;

    @Autowired
    public OrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Order create(Order order) {
        entityManager.persist(order);
        entityManager.flush();
        return order;
    }

    @Override
    public Order findById(Long id) {
        return entityManager.find(Order.class, id);
    }

    @Override
    public List<Order> findAll(Integer page, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> rootEntry = criteriaQuery.from(Order.class);
        CriteriaQuery<Order> all = criteriaQuery.select(rootEntry);
        TypedQuery<Order> resultQuery = entityManager
                .createQuery(all)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize);
        return resultQuery.getResultList();
    }

    @Override
    public int findCountOfRecords() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        criteriaQuery.select(criteriaQuery.from(Order.class));
        return entityManager.createQuery(criteriaQuery).getResultList().size();
    }

    @Override
    public List<Order> findByUserId(Long userId, Integer page, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<User> rootEntry = criteriaQuery.from(User.class);
        Join<User, Order> orders = rootEntry.join(JOIN_TABLE_ORDERS);
        CriteriaQuery<Order> byUserId = criteriaQuery
                .select(orders)
                .where(criteriaBuilder.equal(rootEntry.get(ID_PARAMETER), userId));
        TypedQuery<Order> resultQuery = entityManager
                .createQuery(byUserId)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize);
        return resultQuery.getResultList();
    }

    @Override
    public int findCountOfRecordsByUserId(Long userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<User> rootEntry = criteriaQuery.from(User.class);
        Join<User, Order> orders = rootEntry.join(JOIN_TABLE_ORDERS);
        CriteriaQuery<Order> byUserId = criteriaQuery
                .select(orders)
                .where(criteriaBuilder.equal(rootEntry.get(ID_PARAMETER), userId));
        TypedQuery<Order> resultQuery = entityManager.createQuery(byUserId);
        return resultQuery.getResultList().size();
    }
}
