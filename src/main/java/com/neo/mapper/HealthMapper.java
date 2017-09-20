package com.neo.mapper;


import com.neo.entity.*;
import com.neo.mapper.provider.HealthProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
    @Insert("insert into blood_pressure values(#{wechat_id},#{date},#{time},#{systolic_pressure},#{diastolic_pressure},#{rhr})")
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

        /*
    * 保存心电表
    * */
    @Insert("insert into cardiogram values (#{wechat_id},#{cardiogram},#{date},#{remark})")
    void saveCardiogram(CardiogramEntity cardiogramEntity);

        /*
    * 生成风险评估报告
    * */
    @Insert("insert into risk_report values (#{wechat_id},#{count},#{time},#{prob},#{content})")
    void saveRiskReport(RiskReportEntity riskReportEntity);

        /*
    * 查找所有风险评估报告
    * */
    @Select("select * from risk_report where wechat_id = #{wechat_id}")
    List<RiskReportEntity> selectAllRiskReport(String wechat_id);

    /*
     * 查找最新的风险评估报告
     * */
    @Select("select * from risk_report where wechat_id = #{wechat_id} order by time desc limit 1 ")
    RiskReportEntity selectNewestReport(String wechat_id);
    /*
    * 查找风险评估人数
    * */
    @Select("select count(distinct wechat_id) from risk_report ")
    Integer getReportNumber();

     /*查看患者是否有血压心率记录
     * */
     @Select("select count(*) from blood_pressure where wechat_id=#{wechat_id} and time=#{time}")
    Integer getNumberOfBloodPressure(@Param("wechat_id")String wechat_id, @Param("time") String time);
}
