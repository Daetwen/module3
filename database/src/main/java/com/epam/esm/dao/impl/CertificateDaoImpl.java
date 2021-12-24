package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.util.ParameterName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;

@Repository
public class CertificateDaoImpl implements CertificateDao {

    private static final String NAME_PARAMETER = "name";
    private static final String DESCRIPTION_PARAMETER = "description";
    private static final String JOIN_TABLE_TAGS = "tags";
    private static final String DELIMITER = "#";
    private final EntityManager entityManager;

    @Autowired
    public CertificateDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Certificate create(Certificate certificate) {
        entityManager.persist(certificate);
        entityManager.flush();
        return certificate;
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
    public List<Certificate> findByParameters(Map<String, String> parameters, Integer page, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> rootEntry = criteriaQuery.from(Certificate.class);
        CriteriaQuery<Certificate> allWithParameters = criteriaQuery.select(rootEntry);
        queryForFindByParameters(parameters, criteriaBuilder, allWithParameters, rootEntry);
        sortQuery(parameters, criteriaBuilder, allWithParameters, rootEntry);
        TypedQuery<Certificate> resultQuery = entityManager
                .createQuery(allWithParameters)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize);
        return resultQuery.getResultList();
    }

    @Override
    public Certificate update(Certificate certificate) {
        certificate = entityManager.merge(certificate);
        entityManager.flush();
        return certificate;
    }

    @Override
    public Certificate deleteById(Long id) {
        Certificate certificate = entityManager.find(Certificate.class, id);
        if(certificate != null) {
            entityManager.remove(certificate);
            entityManager.flush();
        }
        return certificate;
    }

    @Override
    public int findCountOfRecords() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        criteriaQuery.select(criteriaQuery.from(Certificate.class));
        return entityManager.createQuery(criteriaQuery).getResultList().size();
    }

    private void queryForFindByParameters(Map<String, String> parameters,
                                            CriteriaBuilder criteriaBuilder,
                                            CriteriaQuery<Certificate> criteriaQuery,
                                            Root<Certificate> root) {
        Predicate predicateResult = getPredicateForCertificateNameAndDescription(parameters, criteriaBuilder, root);
        predicateResult = getPredicateForTagNames(parameters, criteriaBuilder, root, predicateResult);
        criteriaQuery.where(predicateResult).distinct(true);
    }

    private Predicate getPredicateForCertificateNameAndDescription(Map<String, String> parameters,
                                                                   CriteriaBuilder criteriaBuilder,
                                                                   Root<Certificate> root) {
        Predicate predicate = getPredicateForCertificateName(parameters, criteriaBuilder, root);
        if (predicate != null) {
            predicate = criteriaBuilder.or(predicate,
                    getPredicateForCertificateDescription(parameters, criteriaBuilder, root));
        }
        return predicate;
    }

    private Predicate getPredicateForCertificateName(Map<String, String> parameters,
                                                     CriteriaBuilder criteriaBuilder,
                                                     Root<Certificate> root) {
        Predicate predicate = null;
        if (parameters.containsKey(ParameterName.NAME_OR_DESC_PART.name())) {
            predicate = criteriaBuilder.like(root.get(NAME_PARAMETER),
                    "%" + parameters.get(ParameterName.NAME_OR_DESC_PART.name()) + "%");
        }
        return predicate;
    }

    private Predicate getPredicateForCertificateDescription(Map<String, String> parameters,
                                                            CriteriaBuilder criteriaBuilder,
                                                            Root<Certificate> root) {
        Predicate predicate = null;
        if (parameters.containsKey(ParameterName.NAME_OR_DESC_PART.name())) {
            predicate = criteriaBuilder.like(root.get(DESCRIPTION_PARAMETER),
                    "%" + parameters.get(ParameterName.NAME_OR_DESC_PART.name()) + "%");
        }
        return predicate;
    }

    private Predicate getPredicateForTagNames(Map<String, String> parameters,
                                              CriteriaBuilder criteriaBuilder,
                                              Root<Certificate> root,
                                              Predicate predicate) {
        Predicate localPredicate = predicate;
        int countOfTags = 0;
        while (true) {
            if (parameters.containsKey(ParameterName.TAG_NAME.name() + DELIMITER + countOfTags)) {
                localPredicate = gerPredicateForTagName(parameters, criteriaBuilder, root, localPredicate, countOfTags);
            } else {
                break;
            }
            countOfTags++;
        }
        return localPredicate;
    }

    private Predicate gerPredicateForTagName(Map<String, String> parameters,
                                             CriteriaBuilder criteriaBuilder,
                                             Root<Certificate> root,
                                             Predicate predicate,
                                             int countOfTags) {
        Join<Certificate, Tag> tags = root.join(JOIN_TABLE_TAGS);
        CriteriaBuilder.In<String> inTags = criteriaBuilder.in(tags.get(NAME_PARAMETER));
        inTags.value(parameters.get(ParameterName.TAG_NAME.name() + DELIMITER + countOfTags));
        if (predicate != null) {
            predicate = criteriaBuilder.or(predicate, inTags);
        } else {
            predicate = inTags;
        }
        return predicate;
    }

    private void sortQuery(Map<String, String> parameters,
                           CriteriaBuilder criteriaBuilder,
                           CriteriaQuery<Certificate> criteriaQuery,
                           Root<Certificate> root) {
        if (parameters.containsKey(ParameterName.SORT.name())) {
            switch (parameters.get(ParameterName.SORT.name()).toLowerCase()) {
                case "asc": {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(NAME_PARAMETER)));
                    break;
                }
                case "desc": {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(NAME_PARAMETER)));
                    break;
                }
            }
        }
    }
}
