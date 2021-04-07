package com.tensquare.user.controller;

import com.tensquare.user.pojo.Admin;
import com.tensquare.user.pojo.User;
import com.tensquare.user.service.AdminService;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制层
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 查询全部列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        int s = 1/0;
        return new Result(true, StatusCode.OK,"查询成功", adminService.findAll());
    }

    /**
     *  根据ID查询用户
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功", adminService.findById(id));
    }

//    /**
//     *  增加标签
//     * @param user
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.POST)
//    public Result add( @RequestBody User user){
//        userService.add(user);
//        return new Result(true,StatusCode.OK,"增加成功");
//    }

    /**
     *  修改用户
     * @param user
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.PUT)
    public Result update(@RequestBody Admin user, @PathVariable String id){
        user.setId(id);
        adminService.update(user);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        adminService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result<List<Admin>> findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功", adminService.findSearch(searchMap));
    }

    /**
     *  条件+分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value="/search/{page}/{size}",method = RequestMethod.POST)
    public Result<List> findSearch( @RequestBody Map searchMap ,@PathVariable int page,@PathVariable int size ){
        Page pageList= adminService.findSearch(searchMap,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageList.getTotalElements(),pageList.getContent()  ));
    }


    /**
     * 用户注册
     * @param user
     */
    @RequestMapping(value="/register/{code}",method=RequestMethod.POST)
    public Result register( @RequestBody Admin user  ,@PathVariable String code){
        adminService.add(user,code);
        return new Result(true,StatusCode.OK,"注册成功");
    }

    /**
     * 用户登陆
     * @param
     * @param
     * @return
     */
    @RequestMapping(value="/login",method=RequestMethod.POST)
    public Result login(@RequestBody Map<String,String> loginMap){
        Admin admin = adminService.findByLoginNameAndPassword(loginMap.get("loginName"), loginMap.get("password"));
        if(admin!=null){
            //生成token
            String token = jwtUtil.createJWT(admin.getId(), admin.getName(), "admin");
            Map map=new HashMap();
            map.put("token",token);
            map.put("name",admin.getName());//登陆名
            return new Result(true,StatusCode.OK,"登陆成功",map);
        }else{
            return new Result(false,StatusCode.LOGINERROR,"用户名或密码错误");
        }
    }
}