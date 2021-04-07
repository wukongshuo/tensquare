package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
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
public class UserService {

    @Autowired
    private UserDao userDao;

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
    public List<User> findAll(){
        return userDao.findAll();
    }

    /**
     * @return
     */
    public User findById(String id){
        return userDao.findById(id).get();
    }

    /**
     * @param user
     */
    public void add(User user, String code){
        //判断验证码是否正确
        String syscode = (String)redisTemplate.opsForValue().get("smscode_" + user.getMobile()); //提取系统正确的验证码
        if(syscode==null){
            throw new RuntimeException("请点击获取短信验证码");
        }
        if(!syscode.equals(code)){
            throw new RuntimeException("验证码输入不正确");
        }

        user.setId( idWorker.nextId()+"" );
        user.setFollowCount(0);//关注数
        user.setFansCount(0);//粉丝数
        user.setOnLine(0L);//在线时长
        user.setRegDate(new Date());//注册日期
        user.setUpdateDate(new Date());//更新日期
        user.setLastDate(new Date());//最后登陆日期

        //密码加密
        String newPassword = encoder.encode(user.getPassword());//加密后的密码
        user.setPassword(newPassword);
        userDao.save(user);
    }

    /**
     * @param user
     */
    public void update(User user){
        userDao.save(user);
    }

    /**
     * @param id
     */
    public void deleteById(String id){
        userDao.deleteById(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap){
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
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
        return userDao.findAll( specification);
    }

    /**
     * 分页条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map searchMap, int page, int size){
        Specification specification= createSpecification(searchMap);
        PageRequest pageRequest=PageRequest.of(page-1,size);
        return userDao.findAll( specification ,pageRequest);
    }

    /**
     * 发送短信验证码
     * @param mobile 手机号
     */
    public void sendSms(String mobile){
        //1.生成6位短信验证码
        Random random=new Random();
        int max=999999;//最大数
        int min=100000;//最小数
        int code = random.nextInt(max);//随机生成
        if(code<min){
            code=code+min;
        }
        System.out.println(mobile+"收到验证码是："+code);
        //2.将验证码放入redis
        redisTemplate.opsForValue().set("smscode_"+mobile, code+"" ,5, TimeUnit.MINUTES );//五分钟过期

        //3.将验证码和手机号发动到rabbitMQ中
        Map<String,String> map=new HashMap();
        map.put("mobile",mobile);
        map.put("code",code+"");
        rabbitTemplate.convertAndSend("sms",map);
    }

    /**
     * 根据手机号和密码查询用户
     * @param mobile
     * @param password
     * @return
     */
    public User findByMobileAndPassword(String mobile,String password){
        User user = userDao.findByMobile(mobile);
        if(user!=null &&  encoder.matches(password,user.getPassword())){
            return user;
        }else{
            return null;
        }
    }

}