package com.neo.entity;
/*
#事项提醒表
create table reminders (
id int not null auto_increment,
phone varchar(31) not null,
title varchar(31) not null default '',
content varchar(255) not null default '',
datetime timestamp not null default current_timestamp,
isread int not null default 0,
delete_status int not null default 0,
primary key(id)
)engine = INNODB default charset=utf8;

 */
public class RemindersEntity {
    private int id;
    private String phone;
    private String title;
    private String content;
    private String datetime;
    private int isread;
    private int delete_status;

    public RemindersEntity() {
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

    public int getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(int delete_status) {
        this.delete_status = delete_status;
    }
}
