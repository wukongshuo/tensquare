package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
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
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * @return
     */
    public List<Label> findAll(){
        return labelDao.findAll();
    }

    /**
     * @return
     */
    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    /**
     * @param label
     */
    public void add(Label label){
        label.setId( idWorker.nextId()+"" );//设置ID
        labelDao.save(label);
    }

    /**
     * @param label
     */
    public void update(Label label){
        labelDao.save(label);
    }

    /**
     * @param id
     */
    public void deleteById(String id){
        labelDao.deleteById(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Specification<Label> createSpecification(Map searchMap){
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
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
    public List<Label> findSearch(Map searchMap){
        Specification specification= createSpecification(searchMap);
        return labelDao.findAll( specification);
    }

    /**
     * 分页条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<Label> findSearch(Map searchMap, int page, int size){
        Specification specification= createSpecification(searchMap);
        PageRequest pageRequest=PageRequest.of(page-1,size);
        return labelDao.findAll( specification ,pageRequest);
    }

}