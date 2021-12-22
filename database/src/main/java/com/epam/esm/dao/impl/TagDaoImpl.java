package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {

    private static final String ID_PARAMETER = "id";
    private static final String NAME_PARAMETER = "name";
    private static final String JOIN_TABLE_TAGS = "tags";

    private static final String FIND_MOST_POPULAR_TAGS =
            "SELECT tag_id, tags.name FROM tags " +
            "JOIN gift_certificates_has_tags ON tags_id = tag_id " +
            "JOIN gift_certificates ON gift_certificates_id = certificate_id " +
            "GROUP BY tag_id " +
            "ORDER BY COUNT(tags_id) DESC, SUM(price) DESC " +
            "LIMIT 1";
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
        return (Tag) entityManager.createNativeQuery(FIND_MOST_POPULAR_TAGS, Tag.class).getSingleResult();
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
}
