package com.example.jingjing.xin.Bean;

import java.io.Serializable;

/**
 * Created by jingjing on 2018/5/28.
 */

public class Need implements Serializable {
    private int needId;
    private int userId;
    private int stadiumId;
    private String username;
    private String stadiumname;
    private String time;
    private int num;
    private String remark;
    private String sportstype;
    private int num_join;
    private String proflie;
    private String releasetime;

    public String getReleasetime() {
        return releasetime;
    }

    public void setReleasetime(String releasetime) {
        this.releasetime = releasetime;
    }

    public String getProflie() {
        return proflie;
    }

    public void setProflie(String proflie) {
        this.proflie = proflie;
    }

    public int getNeedId() {
        return needId;
    }

    public void setNeedId(int needId) {
        this.needId = needId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStadiumId() {
        return stadiumId;
    }

    public void setStadiumId(int stadiumId) {
        this.stadiumId = stadiumId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStadiumname() {
        return stadiumname;
    }

    public void setStadiumname(String stadiumname) {
        this.stadiumname = stadiumname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSportstype() {
        return sportstype;
    }

    public void setSportstype(String sportstype) {
        this.sportstype = sportstype;
    }

    public int getNum_join() {
        return num_join;
    }

    public void setNum_join(int num_join) {
        this.num_join = num_join;
    }
}
