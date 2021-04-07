package com.tensquare.gathering.service;

import com.tensquare.gathering.dao.GatheringDao;
import com.tensquare.gathering.pojo.Gathering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GatheringService {

    @Autowired
    private GatheringDao gatheringDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *
     * @return
     */
    public List<Gathering> findAll() {
        return gatheringDao.findAll();
    }

    /**
     *
     * @return
     */
    @Cacheable(value="gathering",key="#id")
    public Gathering findById(String id) {
        return gatheringDao.findById(id).get();
    }

    /**
     *
     * @param article
     */
    public void add(Gathering article) {
        article.setId(idWorker.nextId() + "");
        gatheringDao.save(article);
    }

    /**
     *
     * @param article
     */
    @CacheEvict(value="gathering",key="#gathering.id")
    public void update(Gathering article) {
        gatheringDao.save(article);
    }

    /**
     *
     * @param id
     */
    @CacheEvict(value="gathering",key="#id")
    public void deleteById(String id) {
        gatheringDao.deleteById(id);
    }

    /**
     * 构建查询条件
     *
     * @param searchMap
     * @return
     */
    private Specification<Gathering> createSpecification(Map searchMap) {
        return new Specification<Gathering>() {
            @Override
            public Predicate toPredicate(Root<Gathering> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(searchMap.get("labelname"))) {
                    predicateList.add(cb.like(root.get("labelname").as(String.class), "%" + (String) searchMap.get("labelname") + "%"));
                }
                if (!StringUtils.isEmpty(searchMap.get("state"))) {
                    predicateList.add(cb.equal(root.get("state").as(String.class), (String) searchMap.get("state")));
                }
                if (!StringUtils.isEmpty(searchMap.get("recommend"))) {
                    predicateList.add(cb.equal(root.get("recommend").as(String.class), (String) searchMap.get("recommend")));
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    public List<Gathering> findSearch(Map searchMap) {
        Specification specification = createSpecification(searchMap);
        return gatheringDao.findAll(specification);
    }

    /**
     * 分页条件查询
     *
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<Gathering> findSearch(Map searchMap, int page, int size) {
        Specification specification = createSpecification(searchMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return gatheringDao.findAll(specification, pageRequest);
    }

    /**
     *
     * @param id
     */
    @Transactional
    public void examine(String id) {
        gatheringDao.examine(id);
    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional
    public int updateThumbup(String id) {
        return gatheringDao.updateThumbup(id);
    }

}