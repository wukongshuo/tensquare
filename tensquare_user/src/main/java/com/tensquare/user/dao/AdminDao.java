package com.tensquare.user.dao;

import com.tensquare.user.pojo.Admin;
import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminDao extends JpaRepository<Admin,String>, JpaSpecificationExecutor<Admin> {

    Admin findByLoginName(String loginName);
}