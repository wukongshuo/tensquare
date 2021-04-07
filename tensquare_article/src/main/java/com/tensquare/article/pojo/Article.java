package com.tensquare.article.pojo;

import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 标签实体类
 */
@Entity
@Table(name = "tb_article")
public class Article {

    @Id
    private String id;                    
    private String columnId;// 专栏ID                         
    private String userId;// 用户ID                         
    private String title;// 文章标题                         
    private String content;// 文章内容                         
    private String image;// 文章封面                         
    private Date createTime;// 发表日期
    private Date updateTime;// 修改日期
    private String isPublic;// 是否公开   0：不公开 1：公开
    private String istop;// 是否置顶      0：不置顶 1：置顶
    private Integer visits;// 浏览量                          
    private Integer thumbup;// 点赞数                          
    private Integer comment;// 评论数                          
    private String state;// 审核状态       0：未审核  1：已审核
    private Integer channelId;// 所属频道    关联频道表ID
    private String url;// URL地址                        
    private String type;// 文章类型       0：分享 1：专栏

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getIstop() {
        return istop;
    }

    public void setIstop(String istop) {
        this.istop = istop;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Integer getThumbup() {
        return thumbup;
    }

    public void setThumbup(Integer thumbup) {
        this.thumbup = thumbup;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}