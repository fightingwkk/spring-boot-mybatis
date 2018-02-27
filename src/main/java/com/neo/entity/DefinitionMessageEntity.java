package com.neo.entity;
/*
#自定义消息表
create table definition_message(
id int auto_increment,
phone varchar(32) not null,
wechat_id varchar(256) not null,
title varchar(32) not null default '',
content varchar(256) not null default '',
isread int not null default 0,
datetime timestamp not null default current_timestamp,
primary key(id)
)engine=INNODB default charset=utf8;
 */
public class DefinitionMessageEntity {
    private int id;
    private String phone;
    private String wechat_id;
    private String title;
    private String content;
    private String datetime;
    private int isread;

    public DefinitionMessageEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat_id() {
        return wechat_id;
    }

    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }
}
