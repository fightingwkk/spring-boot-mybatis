package com.neo.mapper;


import com.neo.entity.*;
import com.neo.mapper.provider.HealthProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public interface HealthMapper {
    /*
     * 保存健康信息表接口
     * */
    @Insert("insert into health_info  values (#{wechat_id},#{height},#{weight},#{diabetes},#{chd},#{stroke},#{hypertension},#{other_history},#{family_history},#{smoke},#{smoking},#{drink},#{drinking})")
    void saveHealthInfo(HealthCheckEntity healthCheckEntity);

    /*
   * 查看健康信息表
   * */
    @Select("select * from health_info where wechat_id = #{wechat_id}")
    HealthCheckEntity selectHealthInfoById(String wechat_id);

    /*
    * 更改健康信息表
    * */
    @SelectProvider(type = HealthProvider.class, method = "updateHealthInfo")
    void updateHealthInfo(HealthCheckEntity healthCheckEntity);

    /*
    * 保存生化检查表
    * */
    @Insert("insert into biology_info values (#{wechat_id},#{tch},#{tch_time},#{fbg},#{fbg_time},#{tg},#{tg_time},#{hdl},#{hdl_time},#{ldl},#{ldl_time})")
    void saveBiologyInfo(BiologyCheckEntity biologyCheckEntity);
    /*
    * 查找生化检查表
    * */
    @Select("select * from biology_info where wechat_id = #{wechat_id}")
    BiologyCheckEntity selectBiologyInfoById(String wechat_id);

    /*
    * 更新生化检查表
    * */
    @Update("update biology_info set tch=#{tch},tch_time=#{tch_time},fbg=#{fbg},fbg_time=#{fbg_time},tg=#{tg},tg_time=#{tg_time},hdl=#{hdl},hdl_time=#{hdl_time},ldl=#{ldl},ldl_time=#{ldl_time} where wechat_id=#{wechat_id}")
    void updateBiologyInfo(BiologyCheckEntity biologyCheckEntity);

    /*
    * 保存血压心率表
    * */
    @Insert("insert into blood_pressure (wechat_id,date,time,systolic_pressure,diastolic_pressure,rhr) values(#{wechat_id},#{date},#{time},#{systolic_pressure},#{diastolic_pressure},#{rhr})")
    void saveBloodPressure(BloodPressureEntity bloodPressureEntity);

    /*
    * 更新血压心率表
    * */
    @Update("update blood_pressure set date=#{date},time=#{time},systolic_pressure=#{systolic_pressure},diastolic_pressure=#{diastolic_pressure},rhr=#{rhr} where wechat_id=#{wechat_id}")
    void updateBloodPressure(BloodPressureEntity bloodPressureEntity);


    /*
    * 查找血压心率记录
    * */
    @Select("select * from blood_pressure where wechat_id=#{0} and date=#{1} and time=#{2}")
    BloodPressureEntity selectBloodPressure(String wechat_id, String date, String time);

    /*
* 查找血压心率记录
* @Param  timearea:时间区间（一周，一个月）
* @Param  time:具体时间（晨起，睡前）
* */
    @Select("select * from blood_pressure where date >= date_sub(curdate(),interval #{timearea} day) and time=#{time} and wechat_id=#{wechat_id}")
    List<BloodPressureEntity> bloodPressureList(@Param("wechat_id") String wechat_id, @Param("timearea") int timearea, @Param("time") String time);

    /* 查找血压心率记录
    * @Param  timearea:时间区间（一周，一个月）
     * */
    @Select("select * from blood_pressure where date >= date_sub(curdate(),interval #{timearea} day) and wechat_id=#{wechat_id}")
    List<BloodPressureEntity> bloodPressureListByTimeArea(@Param("wechat_id") String wechat_id, @Param("timearea") int timearea);

    /* 查找最近一次的血压心率记录
 * */
    @Select("select * from blood_pressure where  wechat_id=#{wechat_id} order by date desc limit 2")
    List<BloodPressureEntity> bloodPressureListLatest(@Param("wechat_id") String wechat_id);

    /*
* 保存心电图
* */
    @Insert("insert into cardiogram (wechat_id,cardiogram,date,remark) values (#{wechat_id},#{cardiogram},#{date},#{remark})")
    void saveCardiogram(CardiogramEntity cardiogramEntity);
    /*
    * 根据患者查找心电图
     */
    @Select("select * from cardiogram where wechat_id = #{wechat_id} order by date desc")
    List<CardiogramEntity> findCardiogram(String wechat_id);
    /*
    *删除心电图
     */
    @Delete("delete from cardiogram where id=#{id}")
    void deleteCardiogramById(int id);
    /*
* 生成风险评估报告
* */
    @Insert("insert into risk_report (wechat_id, count, time, prob, risk_level_id) values (#{wechat_id},#{count},#{time},#{prob},#{risk_level_id})")
    void saveRiskReport(RiskReportEntity riskReportEntity);

    /*
* 查找所有风险评估报告
* */
    @Select("select wechat_id,count,time,prob,risk_level_id,content,risk_level from risk_report,risk_level where risk_report.risk_level_id=risk_level.id and wechat_id = #{wechat_id}")
    List<RiskReportEntity> selectAllRiskReport(String wechat_id);

    /*
     * 查找最新的风险评估报告
     * */
    @Select("select wechat_id,count,time,prob,risk_level_id,content,risk_level from risk_report,risk_level where risk_report.risk_level_id=risk_level.id and wechat_id = #{wechat_id} order by time desc limit 1 ")
    RiskReportEntity selectNewestReport(String wechat_id);

    /*
 * 根据服务次数查找风险评估报告接口
 * */
    @Select("select wechat_id,count,time,prob,risk_level_id,content,risk_level from risk_report,risk_level where risk_report.risk_level_id=risk_level.id and wechat_id = #{wechat_id}  and count = #{count}")
    RiskReportEntity selectRiskReportByCount(@Param("wechat_id") String wechat_id,@Param("count") int count);
    /*
    * 查找是否有最新的风险评估报告
    * */
    @Select("select wechat_id,count,time,prob,risk_level_id from risk_report where wechat_id = #{wechat_id} order by time desc limit 1 ")
    RiskReportEntity selectNewestReportExist(String wechat_id);

    /*
    * 查找风险评估人数
    * */
    @Select("select count(distinct wechat_id) from risk_report ")
    Integer getReportNumber();

    /*
    *查找风险等级
     */
    @Select("select * from risk_level where id=#{id}")
    RiskLevelEntity selectRiskLevelById(int id);

    /*查看患者是否有血压心率记录
    * */
    @Select("select count(*) from blood_pressure where wechat_id=#{wechat_id} and time=#{time}")
    Integer getNumberOfBloodPressure(@Param("wechat_id") String wechat_id, @Param("time") String time);

    /*
    查找生化信息
     */
    @Select("select * from biology_info where wechat_id = #{wechat_id}")
    BiologyCheckEntity selectBiologyCheckById(String wechat_id);

    /*
    *医生发送模板消息提醒
     */
    @Insert("insert into message_remind (phone,wechat_id,title,target,remark,period) values (#{phone},#{wechat_id},#{title},#{target},#{remark},#{period})")
    void saveMessageRemind(MessageRemindEntity messageRemindEntity);
    /*
    * 查找具体患者未读模板消息接口
    * */
    @Select("select a.id,a.wechat_id,a.message_id,a.isread,a.datetime,b.title,b.target,b.remark,b.period from message_list a left join message_remind b on a.message_id=b.id where a.wechat_id=#{wechat_id} and a.datetime >= date_sub(curdate(),interval 7 day) and a.isread=0   order by a.datetime desc")
    List<MessageListEntity> selectNoreadMessage(String wechat_id);

    /*
    *根据id查找模板消息
     */
    @Select("select a.id,a.wechat_id,a.message_id,a.isread,a.datetime,b.title,b.target,b.remark,b.period from message_list a left join message_remind b on a.message_id=b.id where a.id=#{id} ")
    MessageRemindEntity selectMessageRemindById(@Param("id") int id);
    /*
    *根据id查找自定义消息
     */
    @Select("select * from definition_message where id=#{id} ")
    DefinitionMessageEntity selectDefineMsgById(@Param("id") int id);

    @Delete("delete from definition_message where id=#{id}")
    void deleteDefinitionMsgById(@Param("id")int id);
    /*
    * 将具体模板消息设置为已读接口
    * */
    @Update("update message_list set isread=1 where id=#{id}")
    void updateMessageReaded( @Param("id") int id);
    /*
    * 将具体自定义消息设置为已读接口
    * */
    @Update("update definition_message set isread=1 where id=#{id}")
    void updateDefineMessageReaded( @Param("id") int id);
    /*
    *获取患者一周未读模板消息的数量
     */
    @Select("select count(*) from message_list where wechat_id=#{wechat_id} and datetime >= date_sub(curdate(),interval 7 day) and isread=0")
    int selectUnreadMessageById(String wechat_id);
    /*
    *获取患者未读自定义消息的数量
     */
    @Select("select count(*) from definition_message where wechat_id=#{wechat_id} and isread=0")
    int selectUnreadDefineMessageById(String wechat_id);
    /*
    *插入一条定时消息
     */
    @Insert("insert into message_list (wechat_id,message_id) values(#{wechat_id},#{message_id})")
    void insertMsgList(@Param("wechat_id")String wechat_id, @Param("message_id") int message_id);

    /*
    *查找需要定时发送的模板消息
     */
    @Select("select * from message_remind where curdate() <= date_add(datetime,interval period day)")
    List<MessageRemindEntity> selectTimingMsgRemind();

    /*
    *插入自定义消息
     */
    @Insert("insert into definition_message (phone,wechat_id,title,content) values (#{phone},#{wechat_id},#{title},#{content})")
    void insertDefinitionMessage(DefinitionMessageEntity definitionMessageEntity);
    /*
    *查找自定义消息
     */
    @Select("select * from definition_message where wechat_id=#{wechat_id} order by datetime desc")
    List<DefinitionMessageEntity> selectDefineMsg(String wechat_id);
}
