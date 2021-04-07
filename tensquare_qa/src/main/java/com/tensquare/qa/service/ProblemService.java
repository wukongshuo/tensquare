package com.tensquare.qa.service;

import com.tensquare.qa.dao.ProblemDao;
import com.tensquare.qa.pojo.Problem;
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
public class ProblemService {

    @Autowired
    private ProblemDao problemDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部问题
     * @return
     */
    public List<Problem> findAll(){
        return problemDao.findAll();
    }

    /**
     * 根据ID查询问题
     * @return
     */
    public Problem findById(String id){
        return problemDao.findById(id).get();
    }

    /**
     * 增加问题
     * @param label
     */
    public void add(Problem label){
        label.setId( idWorker.nextId()+"" );//设置ID
        problemDao.save(label);
    }

    /**
     * 修改问题
     * @param label
     */
    public void update(Problem label){
        problemDao.save(label);
    }

    /**
     * 删除问题
     * @param id
     */
    public void deleteById(String id){
        problemDao.deleteById(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Specification<Problem> createSpecification(Map searchMap){
        return new Specification<Problem>() {
            @Override
            public Predicate toPredicate(Root<Problem> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
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
    public List<Problem> findSearch(Map searchMap){
        Specification specification= createSpecification(searchMap);
        return problemDao.findAll( specification);
    }

    /**
     * 分页条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<Problem> findSearch(Map searchMap, int page, int size){
        Specification specification= createSpecification(searchMap);
        PageRequest pageRequest=PageRequest.of(page-1,size);
        return problemDao.findAll( specification ,pageRequest);
    }

    /**
     * 根据标签ID查询问题列表
     * @param labelId 标签ID
     * @param page 页码
     * @param size 页大小
     * @return
     */
    public Page<Problem> findNewListByLabelId(String labelId,int page, int size) {
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return  problemDao.findNewListByLabelId(labelId,pageRequest);
    }

    /**
     * 根据标签ID查询热门问题列表
     * @param labelId 标签ID
     * @param page 页码
     * @param size 页大小
     * @return
     */
    public Page<Problem> findHotListByLabelId(String labelId,int page, int size) {
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return  problemDao.findHotListByLabelId(labelId,pageRequest);
    }

    /**
     * 根据标签ID查询等待回答列表
     * @param labelId 标签ID
     * @param page 页码
     * @param size 页大小
     * @return
     */
    public Page<Problem> findWaitListByLabelId(String labelId,int page, int size) {
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return  problemDao.findWaitListByLabelId(labelId,pageRequest);
    }

}