package com.neo.entity;
/*
#医生标签关系表
create table label_relation(
id int auto_increment,
phone varchar(32) not null,
label varchar(16) not null,
wechat_id varchar(300) not null,
primary key(id)
)engine = INNODB default charset=utf8;
 */
public class LabelRelationEntity {
    private int id;
    private String phone;
    private String label;
    private String wechat_id;

    public LabelRelationEntity() {
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWechat_id() {
        return wechat_id;
    }

    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
    }
}
