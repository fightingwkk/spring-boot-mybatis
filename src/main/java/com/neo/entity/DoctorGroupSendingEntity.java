package com.neo.entity;
/*
#医生群发消息表
create table doctor_group_sending(
id int  not null auto_increment,
phone varchar(31) not null,
content varchar(255) not null,
datetime timestamp not null default current_timestamp,
group_names varchar(255),
kind_names varchar(511),
patient_names varchar(511),
delete_status int not null default 0,
primary key(id)
)engine=INNODB default charset = utf8;
 */


public class DoctorGroupSendingEntity {
    private int id;
    private String phone;//医生电话
    private String content;//内容
    private String datetime;//发送时间
    private String group_names;//发送自定义群组名
    private String kind_names;//发送等级类型患者
    private String patient_names;//发送患者名
    private int delete_status;//删除状态

    public DoctorGroupSendingEntity() {
    }

    public String getKind_names() {
        return kind_names;
    }

    public void setKind_names(String kind_names) {
        this.kind_names = kind_names;
    }

    public String getPatient_names() {
        return patient_names;
    }

    public void setPatient_names(String patient_names) {
        this.patient_names = patient_names;
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

    public String getGroup_names() {
        return group_names;
    }

    public void setGroup_names(String group_names) {
        this.group_names = group_names;
    }

    public int getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(int delete_status) {
        this.delete_status = delete_status;
    }

}
