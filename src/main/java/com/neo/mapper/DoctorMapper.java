package com.neo.mapper;

import java.util.List;
import java.util.Map;

import com.neo.entity.*;
import com.neo.mapper.provider.DoctorProvider;
import org.apache.ibatis.annotations.*;

import org.springframework.stereotype.Component;

@Component
public interface DoctorMapper {

    // 查找token
    @Select("select token from token where token = #{token}")
    String selectToken(String token);

    // 删除token
    @Delete("delete from token where token = #{token}")
    void deleteToken(String token);

    // 插入phone和token
    @Select("insert into token (phone,token) values (#{phone},#{token})")
    String savePhonetoken(@Param("phone") String phone, @Param("token") String token);

    // 根据phone查找token
    @Select("select token from token where phone = #{phone}")
    String selectPhoneToken(String phone);

    //更新token
    @Select("update token set token = #{newToken} where token =#{token}")
    String updateToken(@Param("newToken") String newToken, @Param("token") String token);


    // 根据电话和密码查询账号是否存在
    @Select("select * from doctor_info where phone = #{0} and password = #{1}")
    DoctorEntity selectByPhoneAndPassword(String phone, String password);

    // 存入医生二维码
    @Update("update dactor_info set QRcode_pic = #{QRcode_pic}")
    void updateQRcode(String QRcode_pic);

    // 根据电话返回所有医生数据
    @Select("select * from doctor_info where phone = #{phone} ")
    DoctorEntity selectByPhone(String phone);

    ////根据电话更新密码
    @Update("update doctor_info set password = #{password} where phone = #{phone}")
    void updatePassword(@Param("password") String password, @Param("phone") String phone);

    //新增一条医生数据
    @Insert("insert into doctor_info (phone,password,QRcode_pic) values (#{0},#{1},#{2})")
    void insertDoctor(String phone, String password ,String QRcode_pic);

    // 返回所有医生数据
    @Select("select * from doctor_info ")
    List<DoctorEntity> findAllDoctor();

    //更新医生数据
    @UpdateProvider(type = DoctorProvider.class, method = "updateDoctor")
    void updateDoctor(DoctorEntity doctorEntity);

    // 返回所有的服务包
    @Select("select * from service where status=1 and delete_status=0 order by time asc")
    List<ServiceEntity> findAllService();

    //存头像
    @Update("update doctor_info set head_pic = #{head_pic} where phone = #{phone}")
    void updateHead(@Param("head_pic") String head_pic, @Param("phone") String phone);

    //更新擅长和经验
    @Update("update doctor_info set adept = #{adept},experience = #{experience} where phone = #{phone}")
    void updateAdeptExperience(@Param("phone") String phone, @Param("adept") String adept, @Param("experience") String experience);

    //获取医生自定义标签
    @Select("select label from doctor_info where phone=#{phone}")
    @Results({
            @Result(column = "label", property = "label")
    })
    String getDoctorLabel(String phone);

    //查找关注医生的患者
    @Select("select * from attention where phone=#{phone}")
    List<AttentionEntity> findMyPatients(String phone);

    //查找患者
    @Select("select * from patient_info where wechat_id = #{wechat_id}")
    PatientEntity selectPatient(String wechat_id);

    //根据医生/标签/患者查找患者所有信息
    @Select("select label_relation.wechat_id,name,age,label_relation.phone,kind,prob,sex,head_pic,address from label_relation ,patient_info where label_relation.wechat_id=patient_info.wechat_id and label_relation.phone=#{phone} and label_relation.wechat_id=#{wechat_id} and label=#{label}")
    PatientEntity selectPatientsByLabel(@Param("phone") String phone, @Param("wechat_id") String wechat_id, @Param("label") String label);

    //根据医生和自定义标签查找患者wechat_id
    @Select("select wechat_id from label_relation where phone=#{phone} and label=#{label}")
    List<String> selectPatientByPhoneAndLabel(@Param("phone")String phone,@Param("label")String label);

    //根据医生和标签删除所有此标签下的患者
    @Delete("delete  from label_relation where phone=#{phone} and label=#{label}")
    void deleteLabelPatients(@Param("phone") String phone, @Param("label") String label);

    //删除一个标签下的用户
    @Delete("delete  from label_relation where phone =#{phone} and label=#{label} and wechat_id=#{wechat_id}")
    void deleteLabelPatient(@Param("phone") String phone, @Param("label") String label, @Param("wechat_id") String wechat_id);

    //删除一个用户的标签
    @Delete("delete from label_relation where phone=#{phone} and wechat_id={wechat_id} and label=#{label}")
    void deletePatientLabel(@Param("phone") String phone, @Param("label") String label, @Param("wechat_id") String wechat_id);

    //添加一个患者进组
    @Insert("insert into label_relation (phone,wechat_id,label) values(#{phone},#{wechat_id},#{label})")
    void insertPatientLabel(@Param("phone") String phone, @Param("wechat_id") String wechat_id, @Param("label") String label);

    //获取医生未读评价个数
    @Select("select count(*) from evaluation where phone=#{phone} and isread=0 and  delete_status=0")
    int getEvaluationUnread(@Param("phone")String phone);

    //根据电话查找所有患者的评价
    @Select("select a.id, a.wechat_id,a.datetime,a.content,a.profession,a.attitude,a.speed,a.isread,a.anonymity,a.delete_status,b.head_pic,b.name,b.sex,b.age from evaluation a left join patient_info b on a.wechat_id=b.wechat_id where  a.phone=#{phone} and a.delete_status=0")
    List<EvaluationEntity> selectEvaluationByPhone(@Param("phone") String phone);

    //根据电话和患者微信号查找评价
    @Select("  select a.id, a.wechat_id,a.datetime,a.content,a.profession,a.attitude,a.speed,a.isread,a.anonymity,a.delete_status,b.head_pic,b.name,b.sex,b.age from evaluation a left join patient_info b on a.wechat_id=b.wechat_id where   a.id=#{id} and a.delete_status=0")
    EvaluationEntity selectEvaluationById(@Param("id") int id);

    //根据电话和患者微信号设置评价为已读
    @Update("update evaluation set isread=1 where id = #{id}")
    void updateEvaluationById(@Param("id") int id);

    //根据电话和患者微信号删除评价
    @Update("update  evaluation set delete_status=1 where id=#{id}")
    void deleteEvaluationById(@Param("id") int id);

    //医生群发消息
    @Insert("insert into doctor_group_sending (phone,content,group_name,type) values (#{phone},#{content},#{group_name},#{type})")
    void insertDoctorGroupSending(DoctorGroupSendingEntity doctorGroupSendingEntity);

    //查找医生群发消息历史
    @Select("select * from doctor_group_sending where phone=#{phone} and delete_status=0 order by datetime desc")
    List<DoctorGroupSendingEntity> selectDoctorGroupSending(@Param("phone")String phone);

    //删除医生群发消息历史
    @Update("update doctor_group_sending set delete_status=1 where id=#{id}")
    void deleteDoctorGroupSending(@Param("id") int id);

    //患者获得医生群发的消息
    @Insert("insert into patient_group_receiving (phone,wechat_id,content) values (#{phone},#{wechat_id},#{content})")
    void insertPatientGroupReceiving(@Param("phone")String phone,@Param("wechat_id")String wechat_id,@Param("content")String content );


    //查找医生的事项提醒
    @Select("select * from reminders where phone=#{phone} and delete_status=0")
    List<RemindersEntity> selectRemindersByPhone(@Param("phone") String phone);

    //获取医生事项提醒的详细信息
    @Select("select * from reminders where id=#{id}")
    RemindersEntity selectRemindersById(@Param("id")int id);

    //设置医生事项提醒为已读
    @Update("update reminders set isread=1 where id=#{id}")
    void updateRemindersById(int id);

    //删除医生事项提醒
    @Update("update reminders set delete_status = 1 where id =#{id}")
    void deleteRemindersById(@Param("id")int id);

    //获取医生事项提醒未读个数
    @Select("select count(*) from reminders where phone=#{phone} and isread=0 and delete_status=0")
    int getRemindersUnreadByPhone(@Param("phone")String phone);




}
