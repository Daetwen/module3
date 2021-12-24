package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class TagDaoImpl implements TagDao {

    private static final String ID_PARAMETER = "id";
    private static final String NAME_PARAMETER = "name";
    private static final String JOIN_TABLE_TAGS = "tags";

    private final EntityManager entityManager;

    @Autowired
    public TagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Tag create(Tag tag) {
        if (findByName(tag.getName()) == null) {
            entityManager.persist(tag);
            entityManager.flush();
        }
        return findByName(tag.getName());
    }

    @Override
    public Tag findById(Long id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public Tag findByName(String name) {
        Tag result = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        CriteriaQuery<Tag> tagCriteriaQuery =
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(NAME_PARAMETER), name));
        TypedQuery<Tag> typedQuery = entityManager.createQuery(tagCriteriaQuery);
        if (!typedQuery.getResultList().isEmpty()) {
            result = typedQuery.getSingleResult();
        }
        return result;
    }

    @Override
    public List<Tag> findByCertificateId(Long certificateId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Certificate> rootEntry = criteriaQuery.from(Certificate.class);
        Join<Certificate, Tag> tags = rootEntry.join(JOIN_TABLE_TAGS);
        CriteriaQuery<Tag> byCertificateId = criteriaQuery
                .select(tags)
                .where(criteriaBuilder.equal(rootEntry.get(ID_PARAMETER), certificateId));
        TypedQuery<Tag> resultQuery = entityManager.createQuery(byCertificateId);
        return resultQuery.getResultList();
    }

    @Override
    public List<Tag> findAll(Integer page, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> rootEntry = criteriaQuery.from(Tag.class);
        CriteriaQuery<Tag> all = criteriaQuery.select(rootEntry);
        TypedQuery<Tag> resultQuery = entityManager
                .createQuery(all)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize);
        return resultQuery.getResultList();
    }

    @Override
    public Tag findMostPopular() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> rootEntry = criteriaQuery.from(Tag.class);
        criteriaQuery.select(rootEntry).distinct(true);
        List<Tag> tags = entityManager.createQuery(criteriaQuery).getResultList();
        tags = selectMostPopularByCount(getTagCount(tags, criteriaBuilder));
        return selectMostPopularByPrice(getTagPrice(tags, criteriaBuilder));
    }

    @Override
    public Tag deleteById(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if(tag != null) {
            entityManager.remove(tag);
            entityManager.flush();
        }
        return tag;
    }

    @Override
    public int findCountOfRecords() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        criteriaQuery.select(criteriaQuery.from(Tag.class));
        return entityManager.createQuery(criteriaQuery).getResultList().size();
    }

    private Map<Tag, Integer> getTagCount(List<Tag> uniqueTags, CriteriaBuilder criteriaBuilder) {
        Map<Tag, Integer> countsOfUse = new HashMap<>();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> rootEntry = criteriaQuery.from(Certificate.class);
        Join<Certificate, Tag> tags = rootEntry.join(JOIN_TABLE_TAGS);
        for (Tag element : uniqueTags) {
            CriteriaBuilder.In<String> inTags = criteriaBuilder.in(tags.get(NAME_PARAMETER));
            inTags.value(element.getName());
            int count = entityManager.createQuery(criteriaQuery.select(rootEntry)
                    .where(inTags)).getResultList().size();
            countsOfUse.put(element, count);
        }
        return countsOfUse;
    }

    private List<Tag> selectMostPopularByCount(Map<Tag, Integer> countsOfUse) {
        List<Tag> resultListOfTags = new ArrayList<>();
        int maxCount = 0;
        for (Integer element : countsOfUse.values()) {
            if (element > maxCount) {
                maxCount = element;
            }
        }
        for (Tag key : countsOfUse.keySet()) {
            if (countsOfUse.get(key) == maxCount) {
                resultListOfTags.add(key);
            }
        }
        return resultListOfTags;
    }

    private Map<Tag, BigDecimal> getTagPrice(List<Tag> uniqueTags, CriteriaBuilder criteriaBuilder) {
        Map<Tag, BigDecimal> tagAllPrices = new HashMap<>();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> rootEntry = criteriaQuery.from(Certificate.class);
        Join<Certificate, Tag> tags = rootEntry.join(JOIN_TABLE_TAGS);
        for (Tag element : uniqueTags) {
            CriteriaBuilder.In<String> inTags = criteriaBuilder.in(tags.get(NAME_PARAMETER));
            inTags.value(element.getName());
            List<Certificate> certificates = entityManager.createQuery(criteriaQuery.select(rootEntry)
                    .where(inTags)).getResultList();
            BigDecimal price = new BigDecimal(0);
            for (Certificate certificate : certificates) {
                price = price.add(certificate.getPrice());
            }
            tagAllPrices.put(element, price);
        }
        return tagAllPrices;
    }

    private Tag selectMostPopularByPrice(Map<Tag, BigDecimal> tagsAndPrices) {
        Tag mostPopular = null;
        BigDecimal maxPrice = new BigDecimal(0);
        for (BigDecimal element : tagsAndPrices.values()) {
            if (element.compareTo(maxPrice) > 0) {
                maxPrice = element;
            }
        }
        for (Tag key : tagsAndPrices.keySet()) {
            if (tagsAndPrices.get(key).compareTo(maxPrice) == 0) {
                mostPopular = key;
                break;
            }
        }
        return mostPopular;
    }
}
