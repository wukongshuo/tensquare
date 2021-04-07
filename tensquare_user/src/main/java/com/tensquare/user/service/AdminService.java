package com.tensquare.user.service;

import com.tensquare.user.dao.AdminDao;
import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.Admin;
import com.tensquare.user.pojo.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AdminService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    BCryptPasswordEncoder encoder;

    /**
     * @return
     */
    public List<Admin> findAll(){
        return adminDao.findAll();
    }

    /**
     * @return
     */
    public Admin findById(String id){
        return adminDao.findById(id).get();
    }

    /**
     * @param user
     */
    public void add(Admin user, String code){
        //判断验证码是否正确
        String syscode = (String)redisTemplate.opsForValue().get("smscode_" + user.getMobile()); //提取系统正确的验证码
        if(syscode==null){
            throw new RuntimeException("请点击获取短信验证码");
        }
        if(!syscode.equals(code)){
            throw new RuntimeException("验证码输入不正确");
        }

        user.setId( idWorker.nextId()+"" );
        //密码加密
        String newPassword = encoder.encode(user.getPassword());//加密后的密码
        user.setPassword(newPassword);
        user.setOnLine(0L);//在线时长
        user.setRegDate(new Date());//注册日期
        user.setUpdateDate(new Date());//更新日期
        user.setLastDate(new Date());//最后登陆日期

        adminDao.save(user);
    }

    /**
     * @param user
     */
    public void update(Admin user){
        adminDao.save(user);
    }

    /**
     * @param id
     */
    public void deleteById(String id){
        adminDao.deleteById(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Specification<Admin> createSpecification(Map searchMap){
        return new Specification<Admin>() {
            @Override
            public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
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
    public List<User> findSearch(Map searchMap){
        Specification specification= createSpecification(searchMap);
        return adminDao.findAll( specification);
    }

    /**
     * 分页条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<Admin> findSearch(Map searchMap, int page, int size){
        Specification specification= createSpecification(searchMap);
        PageRequest pageRequest=PageRequest.of(page-1,size);
        return adminDao.findAll( specification ,pageRequest);
    }

    /**
     * 根据登陆名和密码查询
     * @param loginName
     * @param password
     * @return
     */
    public Admin findByLoginNameAndPassword(String loginName, String password){
        Admin admin = adminDao.findByLoginName(loginName);
        if( admin!=null && encoder.matches(password,admin.getPassword())){
            return admin;
        }else{
            return null;
        }
    }

}