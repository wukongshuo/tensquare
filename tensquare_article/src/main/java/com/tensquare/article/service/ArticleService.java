package com.tensquare.base.service;

import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.concurrent.TimeUnit;

@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *
     * @return
     */
    public List<Article> findAll() {
        return articleDao.findAll();
    }

    /**
     *
     * @return
     */
    public Article findById(String id) {
        Article article = (Article) redisTemplate.opsForValue().get("article_" + id);
        // 如果缓存没有则到数据库查询并放入缓存
        if (article == null) {
            article = articleDao.findById(id).get();
            redisTemplate.opsForValue().set("article_" + id, article,1, TimeUnit.DAYS);
        }
        return article;
    }

    /**
     *
     * @param article
     */
    public void add(Article article) {
        article.setId(idWorker.nextId() + "");
        articleDao.save(article);
    }

    /**
     *
     * @param article
     */
    public void update(Article article) {
        redisTemplate.delete( "article_" + article.getId() );
        articleDao.save(article);
    }

    /**
     *
     * @param id
     */
    public void deleteById(String id) {
        redisTemplate.delete( "article_" + id );
        articleDao.deleteById(id);
    }

    /**
     * 构建查询条件
     *
     * @param searchMap
     * @return
     */
    private Specification<Article> createSpecification(Map searchMap) {
        return new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
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
    public List<Article> findSearch(Map searchMap) {
        Specification specification = createSpecification(searchMap);
        return articleDao.findAll(specification);
    }

    /**
     * 分页条件查询
     *
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<Article> findSearch(Map searchMap, int page, int size) {
        Specification specification = createSpecification(searchMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return articleDao.findAll(specification, pageRequest);
    }

    /**
     *
     * @param id
     */
    @Transactional
    public void examine(String id) {
        articleDao.examine(id);
    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional
    public int updateThumbup(String id) {
        return articleDao.updateThumbup(id);
    }

}