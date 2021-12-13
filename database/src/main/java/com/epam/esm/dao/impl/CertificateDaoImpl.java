package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CertificateDaoImpl implements CertificateDao {

    private final EntityManager entityManager;

    @Autowired
    public CertificateDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public int create(Certificate certificate) {
        entityManager.persist(certificate);
        entityManager.flush();
        return 1;
    }

    @Override
    public Certificate findById(Long id) {
        return entityManager.find(Certificate.class, id);
    }

    @Override
    public List<Certificate> findAll(Integer page, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> rootEntry = criteriaQuery.from(Certificate.class);
        CriteriaQuery<Certificate> all = criteriaQuery.select(rootEntry);
        TypedQuery<Certificate> resultQuery = entityManager
                .createQuery(all)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize);
        return resultQuery.getResultList();
    }

    @Override
    public int deleteById(Long id) {
        int count = 0;
        Certificate certificate = entityManager.find(Certificate.class, id);
        if(certificate != null) {
            entityManager.remove(certificate);
            entityManager.flush();
            count = 1;
        }
        return count;
    }

    @Override
    public int findCountOfRecords() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        criteriaQuery.select(criteriaQuery.from(Certificate.class));
        return entityManager.createQuery(criteriaQuery).getResultList().size();
    }
}
