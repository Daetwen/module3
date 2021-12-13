package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {

    private static final String NAME_PARAMETER = "name";
    private final EntityManager entityManager;

    @Autowired
    public TagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public int create(Tag tag) {
        int count = 0;
        if (findByName(tag.getName()) == null) {
            entityManager.persist(tag);
            entityManager.flush();
            count = 1;
        }
        return count;
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
    public int deleteById(Long id) {
        int count = 0;
        Tag tag = entityManager.find(Tag.class, id);
        if(tag != null) {
            entityManager.remove(tag);
            entityManager.flush();
            count = 1;
        }
        return count;
    }

    @Override
    public int findCountOfRecords() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        criteriaQuery.select(criteriaQuery.from(Tag.class));
        return entityManager.createQuery(criteriaQuery).getResultList().size();
    }
}
