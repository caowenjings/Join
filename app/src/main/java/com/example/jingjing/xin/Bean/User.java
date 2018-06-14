package com.example.jingjing.xin.Bean;


import java.io.Serializable;

/**
 * Created by jingjing on 2018/5/7.
 */

public class User implements Serializable {
    private int userId;
    private String username;
    private String password;
    private String realname;
    private String sex;
    private String tel;
    private String myright;
    private String proflie;

    public String getProflie() {
        return proflie;
    }

    public void setProflie(String proflie) {
        this.proflie = proflie;
    }


    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMyright() {
        return myright;
    }

    public void setMyright(String myright) {
        this.myright = myright;
    }
}
