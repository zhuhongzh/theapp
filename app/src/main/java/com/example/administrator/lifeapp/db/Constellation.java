package com.example.administrator.lifeapp.db;

import org.litepal.crud.DataSupport;

public class Constellation extends DataSupport {
    private String name;
    private String allnumber;
    private String health;
    private String love;
    private String money;
    private String work;
    private int number;
    private String color;
    private String QFriend;
    private String summary;
    private String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAll() {
        return allnumber;
    }

    public void setAll(String all) {
        this.allnumber = all;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getQFriend() {
        return QFriend;
    }

    public void setQFriend(String qFriend) {
        this.QFriend = qFriend;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
