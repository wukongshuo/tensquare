package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制层
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 查询全部列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        int s = 1/0;
        return new Result(true, StatusCode.OK,"查询成功", userService.findAll());
    }

    /**
     *  根据ID查询用户
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功", userService.findById(id));
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
    public Result update(@RequestBody User user, @PathVariable String id){
        user.setId(id);
        userService.update(user);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        Claims claims=(Claims) request.getAttribute("admin_claims");
        if(claims==null){
            return new Result(true,StatusCode.ACCESSERROR,"无权访问");
        }
        userService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result<List<User>> findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功", userService.findSearch(searchMap));
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
        Page pageList= userService.findSearch(searchMap,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageList.getTotalElements(),pageList.getContent()  ));
    }

    /**
     * 发送短信验证码
     * @param mobile
     */
    @RequestMapping(value="/sendsms/{mobile}",method=RequestMethod.POST)
    public Result sendsms(@PathVariable String mobile ){
        userService.sendSms(mobile);
        return new Result(true,StatusCode.OK,"发送成功");
    }

    /**
     * 用户注册
     * @param user
     */
    @RequestMapping(value="/register/{code}",method=RequestMethod.POST)
    public Result register( @RequestBody User user  ,@PathVariable String code){
        userService.add(user,code);
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
        User user = userService.findByMobileAndPassword(loginMap.get("mobile"),loginMap.get("password"));
        if(user!=null){
            String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
            Map map=new HashMap();
            map.put("token",token);
            map.put("name",user.getNickname());//昵称
            map.put("avatar",user.getAvatar());//头像
            return new Result(true,StatusCode.OK,"登陆成功",map);
        }else{
            return new Result(false,StatusCode.LOGINERROR,"用户名或密码错误");
        }
    }
}