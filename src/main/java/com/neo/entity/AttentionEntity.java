package com.neo.entity;
/*

 #关注表
 create table attention(
 id int not null auto_increment,
 wechat_id varchar(300),
 phone varchar(31),
 datetime timestamp default current_timestamp,
 primary key(id),
  )engine=INNODB default charset=utf8;

*/
public class AttentionEntity {
    private int id;
    private String wechat_id;
    private String phone;

    public AttentionEntity(String wechat_id, String phone) {
        this.wechat_id = wechat_id;
        this.phone = phone;
    }

    public AttentionEntity() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
