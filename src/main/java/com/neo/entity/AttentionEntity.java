package com.neo.entity;
/*

 关注表
 create table attention(
 wechat_id varchar(256),
 phone varchar(32),
 primary key(wechat_id,phone),
  )engine=INNODB default charset=utf8;

*/
public class AttentionEntity {
    private String wechat_id;
    private String phone;

    public AttentionEntity(String wechat_id, String phone) {
        this.wechat_id = wechat_id;
        this.phone = phone;
    }

    public AttentionEntity() {
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
