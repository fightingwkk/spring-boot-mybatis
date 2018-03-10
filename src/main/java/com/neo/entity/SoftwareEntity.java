package com.neo.entity;
/*
create table software (
id int auto_increment,
software_url varchar(255),
software_version varchar(31),
 datetime timestamp default current_timestamp,
 primary key(id)
 )engine=INNODB default charset=utf8;
 */
public class SoftwareEntity {
    private int id;
    private String software_url;
    private String software_version;
    private String datetime;

    public SoftwareEntity(int id, String software_url, String software_version, String datetime) {
        this.id = id;
        this.software_url = software_url;
        this.software_version = software_version;
        this.datetime = datetime;
    }

    public SoftwareEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSoftware_url() {
        return software_url;
    }

    public void setSoftware_url(String software_url) {
        this.software_url = software_url;
    }

    public String getSoftware_version() {
        return software_version;
    }

    public void setSoftware_version(String software_version) {
        this.software_version = software_version;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
