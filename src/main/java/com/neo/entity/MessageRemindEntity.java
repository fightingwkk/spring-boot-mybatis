package com.neo.entity;
/**
 #消息模板表
 create table message_remind(
 id int not null auto_increment,
 phone varchar(31) not null,
 wechat_id varchar(300) not null,
 datetime timestamp not null default current_timestamp,
 title varchar(50) not null,
 target varchar(50) not null,
 remark varchar(50) not null,
 period varchar(50) not null,
 primary key (id)
)engine=INNODB default charset=utf8;
 */
public class MessageRemindEntity {
    private int id;//消息id
    private String phone;//医生电话
    private String wechat_id;//患者微信id
    private String datetime; //发送消息日期
    private String title;//标题
    private String target;//目标
    private String remark;//备注
    private int period;//周期(天)

    public MessageRemindEntity() {
    }

    public MessageRemindEntity(int id, String title, String target, String remark, int period) {
        this.id = id;
        this.title = title;
        this.target = target;
        this.remark = remark;
        this.period = period;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
