package com.neo.entity;
/**

 购买的服务包
 create table purchased_service(
 wechat_id varchar(300) not null,
 serviceid int not null,
 sum_count int not null,
 left_count int not null,
 purchased_time  timestamp not null default current_timestamp,
 name varchar(100) not null,
 price varchar(20) not null,
 duration varchar(20) not null,
 content varchar(300),
 kind varchar(20),
 primary key(wechat_id,serviceid),
 constraint fk_patient foreign key (wechat_id) references patient_info (wechat_id),
 constraint fk_service foreign key (serviceid) references service (id)
 )engine=INNODB default charset=utf8;
 */
public class PurchasedServiceEntity {
    private String  wechat_id;//微信id
    private int serviceid;//服务包id
    private int sum_count;//总次数
    private int left_count;//剩余次数
    private String purchased_time;//购买时间
    private String name;//名称
    private String price; //价格
    private String duration;//期限
    private String content;//内容
    private String kind;//适用人群

    public PurchasedServiceEntity() {
    }

    public PurchasedServiceEntity(String wechat_id, int serviceid, int sum_count, int left_count, String purchased_time, String name, String price, String duration, String content, String kind) {
        this.wechat_id = wechat_id;
        this.serviceid = serviceid;
        this.sum_count = sum_count;
        this.left_count = left_count;
        this.purchased_time = purchased_time;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.content = content;
        this.kind = kind;
    }

    public String getWechat_id() {
        return wechat_id;
    }

    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
    }

    public int getServiceid() {
        return serviceid;
    }

    public void setServiceid(int serviceid) {
        this.serviceid = serviceid;
    }

    public int getSum_count() {
        return sum_count;
    }

    public void setSum_count(int sum_count) {
        this.sum_count = sum_count;
    }

    public int getLeft_count() {
        return left_count;
    }

    public void setLeft_count(int left_count) {
        this.left_count = left_count;
    }

    public String getPurchased_time() {
        return purchased_time;
    }

    public void setPurchased_time(String purchased_time) {
        this.purchased_time = purchased_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
