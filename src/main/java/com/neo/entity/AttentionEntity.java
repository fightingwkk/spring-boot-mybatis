package com.neo.entity;
/*

 关注表
 create table attention(
 wechat_id varchar(300),
 phone varchar(20),
 label varchar(300),
 primary key(wechat_id,phone),
 constraint fk_patient_doctor1 foreign key (wechat_id) references patient_info (wechat_id),
 constraint fk_patient_doctor2 foreign key (phone) references doctor_info (phone)
  )engine=INNODB default charset=utf8;

*/
public class AttentionEntity {
    private String wechat_id;
    private String phone;
    private String label;

    public AttentionEntity(String wechat_id, String phone, String label) {
        this.wechat_id = wechat_id;
        this.phone = phone;
        this.label = label;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
