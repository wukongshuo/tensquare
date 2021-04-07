package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
public class SpitService {

	@Autowired
	private SpitDao spitDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	/**
	 * 查询全部记录
	 * @return
	 */
	public List<Spit> findAll(){
		return spitDao.findAll();		
	}
	
	/**
	 * 根据主键查询实体
	 * @param id
	 * @return
	 */
	public Spit findById(String id){
		Spit spit = spitDao.findById(id).get();
		return spit;
	}
	
	/**
	 * 增加
	 * @param spit
	 */
	public void add(Spit spit) {
		spit.set_id(idWorker.nextId()+"" );
		spit.setPublishTime(new Date());//发布日期
		spit.setVisits(0);//浏览量
		spit.setShare(0);//分享数
		spit.setThumbup(0);//点赞数
		spit.setComment(0);//回复数
		spit.setState("1");//状态
		if(spit.getParentId()!=null && !"".equals(spit.getParentId())){//如果存在上级ID,评论
			Query query=new Query();
			query.addCriteria(Criteria.where("_id").is(spit.getParentId()));
			Update update=new Update();
			update.inc("comment",1);
			mongoTemplate.updateFirst(query,update,"spit");
		}
		spitDao.save(spit);
	}
	
	/**
	 * 修改
	 * @param spit
	 */
	public void update(Spit spit) {
		spitDao.save(spit);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		spitDao.deleteById(id);
	}

	/**
	 * 根据上级ID查询吐槽列表
	 * @param parentId
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Spit> findByParentId(String parentId, int page, int size){
		PageRequest pageRequest = PageRequest.of(page-1, size);
		return spitDao.findByParentId(parentId, pageRequest);
	}

	/**
	 * 点赞
	 * @param id
	 */
	public void updateThumbup(String id){
//		Spit spit = spitDao.findById(id).get();
//		spit.setThumbup(spit.getThumbup()+1);
//		spitDao.save(spit);
		/**
		 * 上面是查询出所有对象，然后对点赞数+1再进行整个对象的更行操作
		 * 下面实现点赞数+1的效率会更高，利用mongodb的文档型数据库特点，只取需要更新的字段（可以理解为局部更新）
		 * */
		Query query=new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		Update update=new Update();
		update.inc("thumbup",1);
		mongoTemplate.updateFirst(query,update,"spit");
	}
}