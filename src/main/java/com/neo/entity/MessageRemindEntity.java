package com.neo.entity;
/**
 #消息模板表
 create table message_remind(
 id int not null auto_increment,
 title varchar(50) not null,
 target varchar(50) not null,
 remark varchar(50) not null,
 period varchar(50) not null,
 primary key (id)
)engine=INNODB default charset=utf8;
 */
public class MessageRemindEntity {
    private int id;//消息id
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
