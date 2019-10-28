package com.hrms.test.bean;

import com.hrms.data.annotation.ImportConfig;

/**
 * 测试 bean，excel数据封装成bean
 *
 * @author ys
 */
public class TestBean {

    @ImportConfig("职位类型")
    private String id;

    @ImportConfig("职位名称")
    private String name;

    @ImportConfig("岗位职责")
    private String msg;
    private int updateTime;
    private double score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
