package com.neo.entity;

/**
 create table risk_report(
 wechat_id varchar(300) not null,
 count int  not null,
 time date not null,
 prob float not null,
 content varchar(300) not null,
 primary key(wechat_id)
 )engine=INNODB default charset=utf8;
 */
public class RiskReportEntity {
    private String wechat_id;//患者微信号id
    private int count;//评估次数
    private String time;//评估时间
    private float prob;//患病概率
    private String content;//详细内容

    public RiskReportEntity() {
    }

    public RiskReportEntity(String wechat_id, int count, String time, float prob, String content) {
        this.wechat_id = wechat_id;
        this.count = count;
        this.time = time;
        this.prob = prob;
        this.content = content;
    }

    public String getWechat_id() {
        return wechat_id;
    }

    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getProb() {
        return prob;
    }

    public void setProb(float prob) {
        this.prob = prob;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
