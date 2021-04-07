package com.tensquare.gathering.pojo;

import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 活动实体类
 */
@Entity
@Table(name = "tb_gathering")
public class Gathering {

    @Id
    private String id;                    

    private String title;// 活动标题
    private String content;// 活动内容
    private Date createTime;// 发表日期
    private Date updateTime;// 修改日期
    private String isPublic;// 是否公开   0：不公开 1：公开
    private String isTop;// 是否置顶      0：不置顶 1：置顶
    private Integer visits;// 浏览量                          
    private String state;// 审核状态       0：未审核  1：已审核
    private Integer channelId;// 所属频道    关联频道表ID
    private String url;// URL地址                        
    private String type;// 活动类型       0：分享 1：专栏

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
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