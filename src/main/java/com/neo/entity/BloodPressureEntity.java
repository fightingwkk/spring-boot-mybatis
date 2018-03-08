package com.neo.entity;

import java.sql.Date;

/**
 #血压心率表
 create table blood_pressure(
 id int not null auto_increment,
 wechat_id varchar(300) not null,
 date date not null,
 time varchar(10) not null,
 systolic_pressure varchar(10) not null,
 diastolic_pressure varchar(10) not null,
 rhr varchar(10) not null,
 primary key(id),
 )engine=INNODB default charset=utf8;
 */

public class BloodPressureEntity {
    private int id;//自动编号
    private String wechat_id;//患者微信号id
    private String date;//日期
    private String time;//时间
    private String systolic_pressure;//收缩压
    private String diastolic_pressure;//舒张压
    private String rhr;//静息心率

    public BloodPressureEntity() {
    }


    public BloodPressureEntity(String wechat_id, String date, String time, String systolic_pressure, String diastolic_pressure, String rhr) {
        this.wechat_id = wechat_id;
        this.date = date;
        this.time = time;
        this.systolic_pressure = systolic_pressure;
        this.diastolic_pressure = diastolic_pressure;
        this.rhr = rhr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWechat_id() {
        return wechat_id;
    }

    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSystolic_pressure() {
        return systolic_pressure;
    }

    public void setSystolic_pressure(String systolic_pressure) {
        this.systolic_pressure = systolic_pressure;
    }

    public String getDiastolic_pressure() {
        return diastolic_pressure;
    }

    public void setDiastolic_pressure(String diastolic_pressure) {
        this.diastolic_pressure = diastolic_pressure;
    }

    public String getRhr() {
        return rhr;
    }

    public void setRhr(String rhr) {
        this.rhr = rhr;
    }
}
