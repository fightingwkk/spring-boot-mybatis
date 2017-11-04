package com.neo.entity;
/*
create table risk_level (
id int auto_increment,
risk_level varchar(32) not null,
content varchar(256) not null,
primary key(id)
 )engine=INNODB default charset=utf8;
 */
public class RiskLevelEntity {
    private int id;
    private String risk_level;
    private String content;

    public RiskLevelEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRisk_level() {
        return risk_level;
    }

    public void setRisk_level(String risk_level) {
        this.risk_level = risk_level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
