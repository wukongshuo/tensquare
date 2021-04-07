package com.tensquare.user.dao;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

    /**
     * 根据手机号查询用户
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);
}