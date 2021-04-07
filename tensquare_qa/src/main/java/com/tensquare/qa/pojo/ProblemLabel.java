package com.tensquare.qa.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 问答标签中间表
 */
@Entity
@Table(name="tb_pl")
public class ProblemLabel implements Serializable {

    @Id
    private String problemId;//问题id

    @Id
    private String labelId;//标签id

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }
}