package com.tensquare.article.controller;

import com.tensquare.article.pojo.Article;
import com.tensquare.base.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文章控制层
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 查询全部列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        int s = 1/0;
        return new Result(true, StatusCode.OK,"查询成功", articleService.findAll());
    }

    /**
     *  根据ID查询文章
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功", articleService.findById(id));
    }

    /**
     *  增加文章
     * @param article
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add( @RequestBody Article article){
        articleService.add(article);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     *  修改文章
     * @param article
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.PUT)
    public Result update(@RequestBody Article article, @PathVariable String id){
        article.setId(id);
        articleService.update(article);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}" ,method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        articleService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result<List<Article>> findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功", articleService.findSearch(searchMap));
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
        Page pageList= articleService.findSearch(searchMap,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageList.getTotalElements(),pageList.getContent()  ));
    }

    /**
     * 审核
     * @param id
     * @return
     */
    @RequestMapping(value="/examine/{id}",method=RequestMethod.PUT)
    public Result examine(@PathVariable String id){
        articleService.examine(id);
        return new Result(true, StatusCode.OK, "审核成功！");
    }

    /**
     * 点赞
     * @param id
     * @return
     */
    @RequestMapping(value="/thumbup/{id}",method=RequestMethod.PUT)
    public Result updateThumbup(@PathVariable String id){
        articleService.updateThumbup(id);
        return new Result(true, StatusCode.OK,"点赞成功");
    }
}