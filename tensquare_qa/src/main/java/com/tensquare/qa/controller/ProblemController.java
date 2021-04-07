package com.tensquare.qa.controller;

import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 问题控制层
 */
@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    /**
     * 查询全部列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        int s = 1/0;
        return new Result(true, StatusCode.OK,"查询成功", problemService.findAll());
    }

    /**
     *  根据ID查询问题
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功", problemService.findById(id));
    }

    /**
     *  增加问题
     * @param problem
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add( @RequestBody Problem problem){
        problemService.add(problem);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     *  修改问题
     * @param problem
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.PUT)
    public Result update( @RequestBody Problem problem,@PathVariable String id){
        problem.setId(id);
        problemService.update(problem);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除问题
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        problemService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result<List<Problem>> findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功", problemService.findSearch(searchMap));
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
        Page pageList= problemService.findSearch(searchMap,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageList.getTotalElements(),pageList.getContent()  ));
    }

    /**
     * 根据标签ID查询最新问题列表
     * @param labelId
     * @return
     */
    @RequestMapping(value="/newList/{labelId}/{page}/{size}",method=RequestMethod.GET)
    public Result findNewListByLabelId(@PathVariable String labelId,@PathVariable int page,@PathVariable int size ){
        Page<Problem> pageList = problemService.findNewListByLabelId(labelId, page, size);
        PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return  new Result(true, StatusCode.OK, "查询成功",pageResult);
    }

    /**
     * 根据标签ID查询热门问题列表
     * @param labelId
     * @return
     */
    @RequestMapping(value="/hotList/{labelId}/{page}/{size}",method=RequestMethod.GET)
    public Result findHotListByLabelId(@PathVariable String labelId,@PathVariable int page,@PathVariable int size ){
        Page<Problem> pageList = problemService.findHotListByLabelId(labelId, page, size);
        PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return  new Result(true, StatusCode.OK, "查询成功",pageResult);
    }

    /**
     * 根据标签ID查询等待回答列表
     * @param labelId
     * @return
     */
    @RequestMapping(value="/waitList/{labelId}/{page}/{size}",method=RequestMethod.GET)
    public Result findWaitListByLabelId(@PathVariable String labelId,@PathVariable int page,@PathVariable int size ){
        Page<Problem> pageList = problemService.findWaitListByLabelId(labelId, page, size);
        PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return  new Result(true, StatusCode.OK, "查询成功",pageResult);
    }
}