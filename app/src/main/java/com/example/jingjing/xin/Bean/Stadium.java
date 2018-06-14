package com.example.jingjing.xin.Bean;

import java.io.Serializable;

public class Stadium implements Serializable {
    private int stadiumId;
    private String stadiumname;
    private String city;
    private String stadiumtype;
    private String area;
    private String num;
    private int indoor;
    private int aircondition;
    private String mainpicture;
    private String adress;
    private String opentime;
    private String closetime;
    private Float grade;
    private int iconnum;

    public Stadium(String mainpicture, String stadiumname, String stadiumtype, String adress) {
        this.stadiumname = stadiumname;
        this.stadiumtype = stadiumtype;
        this.mainpicture = mainpicture;
        this.adress = adress;
    }

    public Stadium() {
    }

    public int getIconnum() {
        return iconnum;
    }

    public void setIconnum(int iconnum) {
        this.iconnum = iconnum;
    }

    public String getClosetime() {
        return closetime;
    }

    public void setClosetime(String closetime) {
        this.closetime = closetime;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getMainpicture() {
        return mainpicture;
    }

    public void setMainpicture(String mainpicture) {
        this.mainpicture = mainpicture;
    }

    public int getStadiumId() {
        return stadiumId;
    }

    public void setStadiumId(int stadiumId) {
        this.stadiumId = stadiumId;
    }

    public String getStadiumname() {
        return stadiumname;
    }

    public void setStadiumname(String stadiumname) {
        this.stadiumname = stadiumname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStadiumtype() {
        return stadiumtype;
    }

    public void setStadiumtype(String stadiumtype) {
        this.stadiumtype = stadiumtype;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int isIndoor() {
        return indoor;
    }

    public int getIndoor() {
        return indoor;
    }

    public int getAircondition() {
        return aircondition;
    }

    public void setIndoor(int indoor) {
        this.indoor = indoor;
    }

    public int isAircondition() {
        return aircondition;
    }

    public void setAircondition(int aircondition) {
        this.aircondition = aircondition;
    }
}
