package com.neo.entity;
/*
#医生与服务包关系表
create table doctor_service(
id int auto_increment,
doctor_phone varchar(32) not null,
service_id int not null,
added_time  varchar(36),
added_status int default 1,
delete_status int default 0,
primary key (id)
)engine=INNODB default charset=utf8;
 */
public class DoctorServiceEntity {
    private int id;
    private String doctor_phone;//医生电话
    private int service_id;//服务id
    private String name;//服务名称
    private String price;//服务价格
    private int count;//服务次数
    private String duration;//服务周期（天）
    private String kind;//服务人群
    private String added_time;//添加时间
    private int added_status;//上架状态 1上架 0下架
    private int status;//冻结状态 1未冻结 0冻结
    private int delete_status;//删除状态 1删除 0未删除

    public DoctorServiceEntity() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctor_phone() {
        return doctor_phone;
    }

    public void setDoctor_phone(String doctor_phone) {
        this.doctor_phone = doctor_phone;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getAdded_time() {
        return added_time;
    }

    public void setAdded_time(String added_time) {
        this.added_time = added_time;
    }

    public int getAdded_status() {
        return added_status;
    }

    public void setAdded_status(int added_status) {
        this.added_status = added_status;
    }

    public int getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(int delete_status) {
        this.delete_status = delete_status;
    }
}
