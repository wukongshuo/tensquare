package com.tensquare.gathering.controller;

import com.tensquare.gathering.pojo.Gathering;
import com.tensquare.gathering.service.GatheringService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 活动控制层
 */
@RestController
@RequestMapping("/gathering")
public class GatheringController {

    @Autowired
    private GatheringService gatheringService;

    /**
     * 查询全部列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        int s = 1/0;
        return new Result(true, StatusCode.OK,"查询成功", gatheringService.findAll());
    }

    /**
     *  根据ID查询活动
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功", gatheringService.findById(id));
    }

    /**
     *  增加活动
     * @param gathering
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add( @RequestBody Gathering gathering){
        gatheringService.add(gathering);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     *  修改活动
     * @param gathering
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.PUT)
    public Result update(@RequestBody Gathering gathering, @PathVariable String id){
        gathering.setId(id);
        gatheringService.update(gathering);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除活动
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        gatheringService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result<List<Gathering>> findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功", gatheringService.findSearch(searchMap));
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
        Page pageList= gatheringService.findSearch(searchMap,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageList.getTotalElements(),pageList.getContent()  ));
    }

    /**
     * 审核
     * @param id
     * @return
     */
    @RequestMapping(value="/examine/{id}",method=RequestMethod.PUT)
    public Result examine(@PathVariable String id){
        gatheringService.examine(id);
        return new Result(true, StatusCode.OK, "审核成功！");
    }

    /**
     * 点赞
     * @param id
     * @return
     */
    @RequestMapping(value="/thumbup/{id}",method=RequestMethod.PUT)
    public Result updateThumbup(@PathVariable String id){
        gatheringService.updateThumbup(id);
        return new Result(true, StatusCode.OK,"点赞成功");
    }
}