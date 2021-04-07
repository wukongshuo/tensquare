package com.tensquare.recruit.service;

import com.tensquare.recruit.dao.EnterpriseDao;
import com.tensquare.recruit.pojo.Enterprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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
public class EnterpriseService {

    @Autowired
    private EnterpriseDao enterpriseDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部标签
     * @return
     */
    public List<Enterprise> findAll(){
        return enterpriseDao.findAll();
    }

    /**
     * 根据ID查询标签
     * @return
     */
    public Enterprise findById(String id){
        return enterpriseDao.findById(id).get();
    }

    /**
     * 增加标签
     * @param label
     */
    public void add(Enterprise label){
        label.setId( idWorker.nextId()+"" );//设置ID
        enterpriseDao.save(label);
    }

    /**
     * 修改标签
     * @param label
     */
    public void update(Enterprise label){
        enterpriseDao.save(label);
    }

    /**
     * 删除标签
     * @param id
     */
    public void deleteById(String id){
        enterpriseDao.deleteById(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Specification<Enterprise> createSpecification(Map searchMap){
        return new Specification<Enterprise>() {
            @Override
            public Predicate toPredicate(Root<Enterprise> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicateList=new ArrayList<>();
                if(!StringUtils.isEmpty(searchMap.get("labelname"))){
                    predicateList.add(cb.like( root.get("labelname").as(String.class), "%"+(String)searchMap.get("labelname")+"%"  ) );
                }
                if(!StringUtils.isEmpty(searchMap.get("state"))){
                    predicateList.add(cb.equal( root.get("state").as(String.class), (String)searchMap.get("state") ) );
                }
                if(!StringUtils.isEmpty(searchMap.get("recommend"))){
                    predicateList.add(cb.equal( root.get("recommend").as(String.class), (String)searchMap.get("recommend") ) );
                }
                return cb.and( predicateList.toArray( new Predicate[predicateList.size()]) );
            }
        };
    }

    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    public List<Enterprise> findSearch(Map searchMap){
        Specification specification= createSpecification(searchMap);
        return enterpriseDao.findAll( specification);
    }

    /**
     * 分页条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<Enterprise> findSearch(Map searchMap, int page, int size){
        Specification specification= createSpecification(searchMap);
        PageRequest pageRequest=PageRequest.of(page-1,size);
        return enterpriseDao.findAll( specification ,pageRequest);
    }

    /**
     * 热门企业列表
     * @return
     */
    public List<Enterprise> hotList(){
        return enterpriseDao.findByIsHot("1");
    }

}