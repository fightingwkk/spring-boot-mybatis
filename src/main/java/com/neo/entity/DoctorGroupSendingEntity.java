package com.neo.entity;
/*
#医生群发消息表
create table doctor_group_sending(
id int  not null auto_increment,
phone varchar(31) not null,
content varchar(255) not null,
datetime timestamp not null default current_timestamp,
group_name varchar(31) not null default '',
delete_status int not null default 0,
primary key(id)
)engine=INNODB default charset = utf8;
 */


public class DoctorGroupSendingEntity {
    private int id;
    private String phone;//医生电话
    private String content;//内容
    private String datetime;//发送时间
    private String group_name;//发送群组名
    private int delete_status;//删除状态
    private int type;//类型0表示kind  1表示label

    public DoctorGroupSendingEntity() {
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

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(int delete_status) {
        this.delete_status = delete_status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
