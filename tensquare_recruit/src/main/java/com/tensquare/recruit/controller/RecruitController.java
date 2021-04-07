package com.tensquare.recruit.controller;

import com.tensquare.recruit.pojo.Enterprise;
import com.tensquare.recruit.pojo.Recruit;
import com.tensquare.recruit.service.RecruitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 招聘控制层
 */
@RestController
@RequestMapping("/recruit")
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    /**
     * 查询全部列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        int s = 1/0;
        return new Result(true, StatusCode.OK,"查询成功", recruitService.findAll());
    }

    /**
     *  根据ID查询职位
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功", recruitService.findById(id));
    }

    /**
     *  增加职位
     * @param recruit
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add( @RequestBody Recruit recruit){
        recruitService.add(recruit);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     *  修改职位
     * @param recruit
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.PUT)
    public Result update( @RequestBody Recruit recruit,@PathVariable String id){
        recruit.setId(id);
        recruitService.update(recruit);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除标签
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        recruitService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result<List<Enterprise>> findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功", recruitService.findSearch(searchMap));
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
        Page pageList= recruitService.findSearch(searchMap,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageList.getTotalElements(),pageList.getContent()  ));
    }

    @RequestMapping(value="/search/recommend",method= RequestMethod.GET)
    public Result recommend(){
        List<Recruit> list = recruitService.findTop4ByStateOrderByCreateTimeDesc("2");
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    /**
     * 最新职位列表
     * @return
     */
    @RequestMapping(value="/search/newList",method= RequestMethod.GET)
    public Result newList(){
        return new Result(true,StatusCode.OK,"查询成功",recruitService.newList());
    }
}