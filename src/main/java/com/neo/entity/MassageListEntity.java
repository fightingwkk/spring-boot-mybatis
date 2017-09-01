package com.neo.entity;
/**
 create table massage_list(
 wechat_id varchar(300) not null,
 message_id int not null,
 isread varchar(5) not null,
 datetime timestamp not null default current_timestamp,
 primary key(wechat_id,message_id),
 foreign key(wechat_id) references patient_info(wechat_id),
 foreign key(message_id) references message_remind(id)
 )engine=INNODB default charset=utf8;
 */
public class MassageListEntity {
    private String wechat_id;
    private int message_id;
    private String isread;
    private String datetime;

    public MassageListEntity() {
    }

    public MassageListEntity(String wechat_id, int message_id, String isread, String datetime) {
        this.wechat_id = wechat_id;
        this.message_id = message_id;
        this.isread = isread;
        this.datetime = datetime;
    }

    public String getWechat_id() {
        return wechat_id;
    }

    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
