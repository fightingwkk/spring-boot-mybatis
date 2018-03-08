package com.neo.entity;

/**
 #建议表
 create table suggestion (
 id int auto_increment,
 wechat_id varchar(256),
 datetime timestamp default current_timestamp,
 content varchar(256) not null,
 phone varchar(32) not null,
 name varchar(32) not null,
 primary key(id)
 )engine=INNODB default charset=utf8;

 alter table suggestion add index index_wecaht_id (wechat_id);
 */
public class SuggestionEntity {
    private String wechat_id;//患者
    private String datetime;//建议时间
    private String content;//建议内容
    private String phone;//患者手机
    private String name;//患者姓名

    public SuggestionEntity() {
    }

    public SuggestionEntity(String wechat_id, String datetime, String content, String phone, String name) {
        this.wechat_id = wechat_id;
        this.datetime = datetime;
        this.content = content;
        this.phone = phone;
        this.name = name;
    }

    public String getWechat_id() {
        return wechat_id;
    }

    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
