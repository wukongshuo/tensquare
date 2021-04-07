package com.tensquare.recruit.controller;

import com.tensquare.recruit.pojo.Enterprise;
import com.tensquare.recruit.service.EnterpriseService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 企业控制层
 */
@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    /**
     * 查询全部列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        int s = 1/0;
        return new Result(true, StatusCode.OK,"查询成功", enterpriseService.findAll());
    }

    /**
     *  根据ID查询企业
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功", enterpriseService.findById(id));
    }

    /**
     *  增加企业
     * @param enterprise
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add( @RequestBody Enterprise enterprise){
        enterpriseService.add(enterprise);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     *  修改企业
     * @param enterprise
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.PUT)
    public Result update( @RequestBody Enterprise enterprise,@PathVariable String id){
        enterprise.setId(id);
        enterpriseService.update(enterprise);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除企业
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        enterpriseService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result<List<Enterprise>> findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功", enterpriseService.findSearch(searchMap));
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
        Page pageList= enterpriseService.findSearch(searchMap,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageList.getTotalElements(),pageList.getContent()  ));
    }

    /**
     * 查询热门企业
     * @return
     */
    @RequestMapping(value="/search/hotList",method=RequestMethod.GET)
    public Result hotList(){
        return new Result(true, StatusCode.OK, "查询成功", enterpriseService.hotList());
    }
}